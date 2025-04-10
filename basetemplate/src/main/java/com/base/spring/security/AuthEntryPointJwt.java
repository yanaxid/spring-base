package com.base.spring.security;


import com.base.spring.dto.MessageResponse;
import static com.base.spring.enums.MessageKey.*;
import com.base.spring.util.ErrorDtoFactory;
import com.base.spring.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

   private final ResponseUtil responseUtil;
   private final ObjectMapper objectMapper;
   private final ErrorDtoFactory error;

   @Override
   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      MessageResponse body = responseUtil.unauthorized(UNAUTHORIZED).getBody();

      String jsonResponse = objectMapper.writeValueAsString(body);

      response.getWriter().write(jsonResponse);
      response.getWriter().flush();
   }
}
