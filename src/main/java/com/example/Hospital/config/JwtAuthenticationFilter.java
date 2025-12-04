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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        
        try {
            // Validate token by checking UserSessions
            Optional<UserSessions> sessionOpt = userSessionsRepository.findByAccessToken(token);
            
            if (sessionOpt.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            UserSessions session = sessionOpt.get();
            
            // Check if session is revoked
            if (Boolean.TRUE.equals(session.getIsRevoked())) {
                filterChain.doFilter(request, response);
                return;
            }
            
            // Check if session is expired
            if (session.getExpiresAt() != null && session.getExpiresAt().isBefore(LocalDateTime.now())) {
                filterChain.doFilter(request, response);
                return;
            }
            
            Users user = session.getUser();
            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }
            
            // Create authentication token
            String role = user.getRole() != null ? user.getRole().getTenRole() : "ROLE_USER";
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
            
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.singletonList(authority)
            );
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
        } catch (Exception e) {
            // If any error occurs, continue without authentication
            // The endpoint will handle authorization checks
        }
        
        filterChain.doFilter(request, response);
    }
}

