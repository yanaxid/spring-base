package com.base.basicjwt.service;

import com.base.basicjwt.dto.LoginResponse;
import com.base.basicjwt.dto.UserDto;
import com.base.basicjwt.jwt.JwtUtil;
import com.base.basicjwt.model.BlacklistToken;
import com.base.basicjwt.model.User;
import com.base.basicjwt.repository.BlacklistTokenRepository;
import com.base.basicjwt.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final PasswordEncoder encoder;
    private final BlacklistTokenRepository blacklistTokenRepository;

    public ResponseEntity<String> signIn(UserDto userDto) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getUsername(),
                            userDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername());

            String role = user.getRole().name();

            LoginResponse response = new LoginResponse();
            response.setJwtToken(jwtUtils.generateToken(userDetails));
            response.setUsername(userDetails.getUsername());
            response.setRole(role);
            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    public String signUp(User user) {
        try {

            if (userRepository.existsByUsername(user.getUsername())) {
                return "Error: Username is already taken! ";
            }

            User newUser = User.builder()
                    .fullname(user.getFullname())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(encoder.encode(user.getPassword()))
                    .role(user.getRole())
                    .build();

            userRepository.save(newUser);
            return "User registered successfully!";
        } catch (Exception e) {
            return e.getMessage();

        }
    }

    public ResponseEntity<String> logOut(HttpServletRequest request) {
        String token = jwtUtils.parseJwt(request);

        if (token == null && !jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.badRequest().body("Invalid or missing token.");
        }

        if (!blacklistTokenRepository.existsByToken(token)) {
            blacklistTokenRepository.save(new BlacklistToken(token));
            log.info("JWT added to blacklist: {}", token);
        }

        return ResponseEntity.ok("Logout successful, token added to blacklist.");
    }


}
