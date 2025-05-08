package com.projinteg.GesEvents.config;

import com.projinteg.GesEvents.security.AuthTokenFilter;
import com.projinteg.GesEvents.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Ensure this is imported
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final AuthTokenFilter authTokenFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, AuthTokenFilter authTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "0.0.0.0"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // --- PUBLIC ENDPOINTS ---
                        .requestMatchers("/api/auth/signin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/companies/register").permitAll() // For Company registration

                        // --- ORGANIZATION ROLE SPECIFIC ENDPOINTS ---
                        // Event related actions for ORGANIZATION
                        .requestMatchers(HttpMethod.POST, "/api/events/addEvent").hasRole("ORGANIZATION")
                        .requestMatchers(HttpMethod.GET, "/api/events/companies/{companyId}/events").hasRole("ORGANIZATION") // Get own events
                        .requestMatchers(HttpMethod.GET, "/api/events/{id}/etat").hasRole("ORGANIZATION") // Get own event's status
                        .requestMatchers(HttpMethod.PUT, "/api/events/updateEvent/{id}").hasRole("ORGANIZATION") // Update own event

                        // --- ADMIN ROLE SPECIFIC ENDPOINTS ---
                        // Company management by ADMIN (all other company endpoints)
                        .requestMatchers(HttpMethod.GET, "/api/companies/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/companies/getById/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/companies/getUnconfirmedCompanies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/companies/getConfirmedCompanies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/companies/confirmCompany/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/companies/unconfirmCompany/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/companies/deleteCompany/{id}").hasRole("ADMIN") // Corrected typo if you fixed it in controller
                        // Or .requestMatchers(HttpMethod.DELETE, "/api/companies/deleteComapny/{id}").hasRole("ADMIN") if typo remains

                        // Event management by ADMIN (all other event endpoints)
                        .requestMatchers(HttpMethod.GET, "/api/events/getAll").hasRole("ADMIN") // Admin gets all events
                        .requestMatchers(HttpMethod.GET, "/api/events/getById/{id}").hasRole("ADMIN") // Admin gets any event by ID
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}/accepter").hasRole("ADMIN") // Admin accepts event
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}/rejeter").hasRole("ADMIN") // Admin rejects event
                        // Note: The general PUT /api/events/updateEvent/{id} is already defined for ORGANIZATION.
                        // If ADMINs also need to update events via this specific path using different logic or permissions,
                        // you might need hasAnyRole or a different path/controller method for admin updates.
                        // If ADMIN update uses the same path and logic, this rule is sufficient as hasRole("ORGANIZATION")
                        // won't allow ADMIN unless they also have ORGANIZATION role.
                        // If ADMINs should have blanket update access to *any* event, this current setup is fine.

                        // --- DEFAULT RULE ---
                        // Any other request not matched above must be authenticated.
                        // This is a good safeguard.
                        .anyRequest().authenticated()
                );

        // Add the JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}