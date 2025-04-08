package com.base.basicjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.base.basicjwt.jwt.AuthEntryPointJwt;
import com.base.basicjwt.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

   private final AuthEntryPointJwt unauthorizedHandler;
   private final AuthTokenFilter authTokenFilter;

   @Bean
   AuthenticationManager authenticationManager(
           AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
   }

   @Bean
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }


   @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .csrf(csrf -> csrf.disable())
              .cors(cors -> cors.disable())
              .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authorizeHttpRequests(authz -> authz
                      .requestMatchers("/api/v1/auth/**", "/api/v1/all").permitAll()
                      .requestMatchers("/api/v1/user").hasAnyAuthority("ADMIN", "USER")
                      .requestMatchers("/api/v1/admin").hasAuthority("ADMIN")
                      .anyRequest().authenticated()
              );

      http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
      return http.build();
   }
}
