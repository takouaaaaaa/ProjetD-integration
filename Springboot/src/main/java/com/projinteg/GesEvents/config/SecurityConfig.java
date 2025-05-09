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
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
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

                        .requestMatchers("/api/auth/signin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/companies/register").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/events/addEvent").hasRole("ORGANIZATION")
                        .requestMatchers(HttpMethod.GET, "/api/events/companies/{companyId}/events").hasRole("ORGANIZATION")
                        .requestMatchers(HttpMethod.GET, "/api/events/{id}/etat").hasRole("ORGANIZATION")
                        .requestMatchers(HttpMethod.PUT, "/api/events/updateEvent/{id}").hasRole("ORGANIZATION")

                        .requestMatchers(HttpMethod.GET, "/api/events/getById/{id}").hasRole("ORGANIZATION")

                        .requestMatchers(HttpMethod.GET, "/api/companies/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/companies/getById/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/companies/getUnconfirmedCompanies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/companies/getConfirmedCompanies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/companies/confirmCompany/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/companies/unconfirmCompany/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/companies/deleteCompany/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/events/getAll").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}/accepter").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/events/{id}/rejeter").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}