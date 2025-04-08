package com.base.basicjwt.service;

import com.base.basicjwt.dto.ErrorDto;
import com.base.basicjwt.dto.MessageResponse;
import com.base.basicjwt.util.ResponseUtil;
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

import com.base.basicjwt.dto.LoginResponse;
import com.base.basicjwt.dto.UserDto;
import com.base.basicjwt.jwt.JwtUtil;
import com.base.basicjwt.model.BlacklistToken;
import com.base.basicjwt.model.User;
import com.base.basicjwt.repository.BlacklistTokenRepository;
import com.base.basicjwt.repository.UserRepository;

import java.util.List;

import static com.base.basicjwt.enums.MessageKey.*;

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

    public ResponseEntity<MessageResponse> signIn(UserDto userDto) {
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
            return responseUtil.successWithData(SUCCESS_SIGNIN, response);
        } catch (Exception e) {
            return responseUtil.internalServerError(ERROR_SERVER);
        }
    }

    public ResponseEntity<MessageResponse> signUp(User user) {
        try {

            if (userRepository.existsByUsername(user.getUsername())) {
                return responseUtil.badRequest(FAILED_TO_PROCESS, List.of(
                        new ErrorDto(user.getUsername(), "Username already taken")
                ));
            }

            User newUser = User.builder()
                    .fullname(user.getFullname())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(encoder.encode(user.getPassword()))
                    .role(user.getRole())
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
            }else{
                return responseUtil.success("Token has been blacklisted");
            }

            return responseUtil.success(SUCCESS_LOGOUT);
        } catch (Exception e) {
            return responseUtil.internalServerError(ERROR_SERVER);
        }

    }


}
