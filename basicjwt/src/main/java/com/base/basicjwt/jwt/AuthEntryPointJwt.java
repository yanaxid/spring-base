package com.base.basicjwt.jwt;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

   @Override
   public void commence(
         HttpServletRequest request,
         HttpServletResponse response,
         AuthenticationException authException) throws IOException {

      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      String jsonResponse = "{ \"status\": %d, \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\" }".formatted(
              HttpServletResponse.SC_UNAUTHORIZED,
              authException.getMessage(),
              request.getServletPath());

      response.getWriter().write(jsonResponse);
      response.getWriter().flush();
   }
}
