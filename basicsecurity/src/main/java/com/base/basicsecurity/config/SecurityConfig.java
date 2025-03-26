package com.base.basicsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.base.basicsecurity.enums.Role;
import com.base.basicsecurity.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

   private final UserService appUserService;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
   public AuthenticationManager authenticationManager() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(appUserService);
      authProvider.setPasswordEncoder(bCryptPasswordEncoder);
      return new ProviderManager(authProvider);
   }

}
