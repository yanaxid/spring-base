package com.base.spring.service;

import com.base.spring.dto.*;
import com.base.spring.exception.DuplicateResourceException;
import com.base.spring.exception.NotFoundException;
import com.base.spring.security.CustomUserDetails;
import com.base.spring.util.ErrorDtoFactory;
import com.base.spring.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.spring.security.JwtUtil;
import com.base.spring.model.BlacklistToken;
import com.base.spring.model.User;
import com.base.spring.repository.BlacklistTokenRepository;
import com.base.spring.repository.UserRepository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.base.spring.enums.MessageKey.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final PasswordEncoder encoder;
    private final BlacklistTokenRepository blacklistTokenRepository;
    private final ResponseUtil responseUtil;
    private final ErrorDtoFactory error;

    public ResponseEntity<MessageResponse> signIn(UserLoginDto userLoginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String role = userDetails.getRole();

        LoginResponse response = LoginResponse.builder()
                .jwtToken(jwtUtils.generateToken(userDetails))
                .username(userDetails.getUsername())
                .role(role)
                .build();

        return responseUtil.successWithData(SUCCESS_SIGNIN, response);
    }

    public ResponseEntity<MessageResponse> signUp(UserSignUpDto userSignUpDto) {

        if (userRepository.existsByUsername(userSignUpDto.getUsername())) {
            throw new DuplicateResourceException(List.of(
                    error.from("username", "Username already taken")
            ));
        }

        User newUser = User.builder()
                .fullname(userSignUpDto.getFullname())
                .username(userSignUpDto.getUsername())
                .email(userSignUpDto.getEmail())
                .password(encoder.encode(userSignUpDto.getPassword()))
                .role(userSignUpDto.getRole())
                .build();

        userRepository.save(newUser);
        return responseUtil.success(SUCCESS_SIGNUP);

    }

    public ResponseEntity<MessageResponse> logOut(HttpServletRequest request) {
        String token = jwtUtils.parseJwt(request);

        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("Token is missing in the request");
        }

        if (!blacklistTokenRepository.existsByToken(token)) {
            blacklistTokenRepository.save(new BlacklistToken(token));
            log.info("JWT added to blacklist: {}", token);
        } else {
            return responseUtil.success(TOKEN_BLOCKED);
        }

        return responseUtil.success(SUCCESS_LOGOUT);
    }


    private User getUserOrThrowByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(List.of(
                        error.from("username", "Authenticated user not found")
                )));
    }
}
