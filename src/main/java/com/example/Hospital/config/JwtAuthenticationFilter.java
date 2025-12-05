package com.example.Hospital.config;

import com.example.Hospital.entity.UserSessions;
import com.example.Hospital.entity.Users;
import com.example.Hospital.repository.UserSessionsRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * JWT Authentication Filter - validates JWT tokens and sets authentication context
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private UserSessionsRepository userSessionsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String requestPath = request.getRequestURI();
        
        // Skip filter for public endpoints
        if (requestPath.startsWith("/api/auth/") || requestPath.startsWith("/actuator/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No JWT token found in request for protected endpoint: {}", requestPath);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        // Extract and trim token to handle any whitespace issues
        String token = authHeader.substring(7).trim();
        
        if (token.isEmpty()) {
            log.warn("Empty token after extraction for endpoint: {}", requestPath);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // Validate token by checking UserSessions
            log.debug("Looking up token in database for endpoint: {} (token length: {})", requestPath, token.length());
            Optional<UserSessions> sessionOpt = userSessionsRepository.findByAccessToken(token);
            
            // If not found, try with trimmed query (in case token has whitespace in database)
            if (sessionOpt.isEmpty()) {
                log.debug("Token not found with exact match, trying trimmed query...");
                sessionOpt = userSessionsRepository.findByAccessTokenTrimmed(token);
            }
            
            if (sessionOpt.isEmpty()) {
                log.warn("Token not found in database for endpoint: {} (token preview: {}...)", 
                        requestPath, token.length() > 20 ? token.substring(0, 20) : token);
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            UserSessions session = sessionOpt.get();
            
            // Check if session is revoked
            if (Boolean.TRUE.equals(session.getIsRevoked())) {
                log.warn("Revoked JWT session for endpoint: {}", requestPath);
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }
            
            // Check if session is expired
            if (session.getExpiresAt() != null && session.getExpiresAt().isBefore(LocalDateTime.now())) {
                log.warn("Expired JWT session for endpoint: {}", requestPath);
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }
            
            Users user = session.getUser();
            if (user == null) {
                log.warn("User not found for valid JWT session for endpoint: {}", requestPath);
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }
            
            // Create authentication token
            String role = user.getRole() != null ? user.getRole().getTenRole() : "benhnhan";
            // Remove "ROLE_" prefix if present, then add it back
            if (role.startsWith("ROLE_")) {
                role = role.substring(5);
            }
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
            
            // Use username as principal name so Principal.getName() works correctly
            String principalName = user.getUsername() != null ? user.getUsername() : String.valueOf(user.getUserId());
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principalName,
                    null,
                    Collections.singletonList(authority)
            );
            
            // Store user object in details for services to access
            authentication.setDetails(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            log.debug("Authentication set successfully for user: {} with role: {} on endpoint: {}", 
                    principalName, role, requestPath);
            
        } catch (Exception e) {
            log.error("Error during JWT authentication for endpoint: {} - {}", requestPath, e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }
}

