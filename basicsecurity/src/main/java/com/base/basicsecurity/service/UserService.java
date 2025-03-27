package com.base.basicsecurity.service;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.basicsecurity.dto.UserDto;
import com.base.basicsecurity.enums.Role;
import com.base.basicsecurity.model.User;
import com.base.basicsecurity.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

   private final UserRepository userRepository;
   private final PasswordEncoder encoder;

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("user with email %s not found", email)));
   }

   @Transactional
   public ResponseEntity<String> register(UserDto userDto) {

      try {

         boolean userExist = userRepository.findByEmail(userDto.getEmail()).isPresent();

         if (userExist) {
            return ResponseEntity.badRequest().body("user has already exist");
         }

         User user = new User();
         user.setFullname(userDto.getFullname());
         user.setEmail(userDto.getEmail());
         user.setPassword(encoder.encode(userDto.getPassword()));
         user.setRole(Role.valueOf(userDto.getRole().toUpperCase()));

         userRepository.save(user);

         return ResponseEntity.ok("register success : " + user);
      } catch (IllegalArgumentException e) {
         return ResponseEntity.badRequest().body("register failed");
      }

   }

   @Bean
   public AuthenticationManager authenticationManager() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(this);
      authProvider.setPasswordEncoder(encoder);
      return new ProviderManager(authProvider);
   }

}
