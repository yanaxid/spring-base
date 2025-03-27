package com.base.basicsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;





@Configuration
public class SecurityConfig {

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/v1/auth/**").permitAll()
                  .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                  .requestMatchers("/api/v1/member/**").hasAnyRole("ADMIN", "USER")
                  .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .build();
   }

   @Bean
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

  

}
