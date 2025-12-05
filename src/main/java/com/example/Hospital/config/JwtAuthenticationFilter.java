package com.example.Hospital.config;

import com.example.Hospital.entity.UserSessions;
import com.example.Hospital.entity.Users;
import com.example.Hospital.repository.UserSessionsRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Autowired
    private UserSessionsRepository userSessionsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        // Skip filter for public endpoints
        String requestPath = request.getRequestURI();
        if (requestPath.startsWith("/api/auth/") || requestPath.startsWith("/actuator/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token provided - let Spring Security handle it (will return 403)
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        
        try {
            // Validate token by checking UserSessions
            Optional<UserSessions> sessionOpt = userSessionsRepository.findByAccessToken(token);
            
            if (sessionOpt.isEmpty()) {
                // Token not found - clear context and continue (Spring Security will reject)
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            UserSessions session = sessionOpt.get();
            
            // Check if session is revoked
            if (Boolean.TRUE.equals(session.getIsRevoked())) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }
            
            // Check if session is expired
            if (session.getExpiresAt() != null && session.getExpiresAt().isBefore(LocalDateTime.now())) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }
            
            Users user = session.getUser();
            if (user == null) {
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
            
        } catch (Exception e) {
            // Clear context on any error
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }
}

