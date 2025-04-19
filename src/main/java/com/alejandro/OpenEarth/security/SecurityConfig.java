package com.alejandro.OpenEarth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("jwtAuthFilter")
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/picture/**",
                                "/api/auth/**",
                                "/api/geo/**",
                                "/api/user/details",
                                "/api/house",
                                "/api/house/categories",
                                "/api/house/details",
                                "/api/house/countries",
                                "/api/house/status",
                                "/api/house/nearTo"
                                ).permitAll()

                        .requestMatchers(
                                "/api/rent/myRents",
                                "/api/rent/cancel",
                                "/api/rent/create",
                                "/api/review/create"
                        ).hasRole("GUEST")

                        .requestMatchers(
                                "/api/house/delete",
                                "/api/house/update",
                                "/api/house/create",
                                "/api/rent/house",
                                "/api/rent/houses"
                        ).hasRole("HOSTESS")

                        .requestMatchers(
                                "/api/user/update",
                                "/api/report/create"
                        ).hasAnyRole("GUEST", "HOSTESS")

                        .requestMatchers(
                                "/api/user/activate",
                                "/api/user/deactivate",
                                "/api/user/delete",
                                "/api/user",
                                "/api/report",
                                "/api/report/delete",
                                "/api/report/get"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/index")) // Redirect to /index if 403 error
                .formLogin(form -> form.disable()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .logout(logout -> logout.disable());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L); // 1 hour for preflight cache

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
