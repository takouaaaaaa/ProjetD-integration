package com.projinteg.GesEvents.security;

import com.projinteg.GesEvents.service.CustomUserDetailsService;
import com.projinteg.GesEvents.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract JWT token from the Authorization header
            String jwt = parseJwt(request);

            // Validate and process the token
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Extract username and role from the token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                Claims claims = jwtUtils.getClaimsFromJwtToken(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load user details from the database
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Extract role from claims
                    String role = claims.get("role", String.class); // The role is being fetched as a string, e.g., "ADMIN" or "ORGANIZATION"
                    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                    // Create an authentication object with the role
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            List.of(authority)  // Assigning the role as the authority
                    );

                    // Set the SecurityContext with the authentication object
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    // Extract JWT from Authorization header if it starts with "Bearer ".
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            logger.debug("JWT Token extracted: {}");
            return headerAuth.substring(7);
        }
        logger.debug("No JWT Token found in request headers.");
        return null;
    }
}
