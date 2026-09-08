package dev.sorokin.eventmanager.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomAccessDeniedHandler customAccessDeniedHandler,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handler ->
                        handler
                                .accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/users",
                                        "/users/auth"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/users/*"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/locations",
                                        "/locations/*"
                                ).hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/locations"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/locations/*"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/locations/*"
                                ).hasAuthority("ADMIN")
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/events"
                                ).hasAuthority("USER")
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/events/search"
                                ).hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/events/*"
                                ).hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/events/my"
                                ).hasAuthority("USER")
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/events",
                                        "/events/*"
                                ).hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/events/*"
                                ).hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/events/registrations/*"
                                ).hasAuthority("USER")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/events/registrations/cancel/*"
                                ).hasAuthority("USER")
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/events/registrations/my"
                                ).hasAuthority("USER")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
