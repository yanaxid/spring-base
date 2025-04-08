package com.base.basicjwt.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.base.basicjwt.model.User;
import com.base.basicjwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   
  private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByUsername(username);

      if (user == null) throw new UsernameNotFoundException("User Not Found with username: " + username);

      List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
      
      return new org.springframework.security.core.userdetails.User(
              user.getUsername(),
              user.getPassword(),
              authorities) {
      };
   }
}
