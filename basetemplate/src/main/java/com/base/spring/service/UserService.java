package com.base.spring.service;

import com.base.spring.dto.*;
import com.base.spring.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.spring.jwt.JwtUtil;
import com.base.spring.model.BlacklistToken;
import com.base.spring.model.User;
import com.base.spring.repository.BlacklistTokenRepository;
import com.base.spring.repository.UserRepository;

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

    public ResponseEntity<MessageResponse> signIn(UserLoginDto userLoginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getUsername(),
                            userLoginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername());

            String role = user.getRole().name();

            LoginResponse response = LoginResponse.builder()
                    .jwtToken(jwtUtils.generateToken(userDetails))
                    .username(userDetails.getUsername())
                    .role(role)
                    .build();

            return responseUtil.successWithData(SUCCESS_SIGNIN, response);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            return responseUtil.internalServerError(ERROR_SERVER);
        }
    }

    public ResponseEntity<MessageResponse> signUp(UserSignUpDto userSignUpDto) {
        try {

            if (userRepository.existsByUsername(userSignUpDto.getUsername())) {
                return responseUtil.badRequest(FAILED_TO_PROCESS, List.of(
                        new ErrorDto(userSignUpDto.getUsername(), "Username already taken")
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
        } catch (Exception e) {
            return responseUtil.internalServerError(ERROR_SERVER);
        }
    }

    public ResponseEntity<MessageResponse> logOut(HttpServletRequest request) {

        try {
            String token = jwtUtils.parseJwt(request);

            if (token.isEmpty() && !jwtUtils.validateJwtToken(token)) {
                return responseUtil.badRequest(FAILED_TO_PROCESS, List.of(
                        new ErrorDto(token, "Invalid token")
                ));
            }

            if (!blacklistTokenRepository.existsByToken(token)) {
                blacklistTokenRepository.save(new BlacklistToken(token));
                log.info("JWT added to blacklist: {}", token);
            } else {
                return responseUtil.success(TOKEN_BLOCKED);
            }

            return responseUtil.success(SUCCESS_LOGOUT);
        } catch (Exception e) {
            return responseUtil.internalServerError(ERROR_SERVER);
        }
    }
}
