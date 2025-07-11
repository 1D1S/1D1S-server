package com.odos.odos_server.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtTokenExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      filterChain.doFilter(request, response);
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
      log.warn("token expired : {}", e.getMessage());
      setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED");
    } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
      log.warn("wrong JWT : {}", e.getMessage());
      setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "INVALID_JWT");
    }
  }

  private void setErrorResponse(HttpServletResponse response, int status, String message)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json;charset=UTF-8");
  }
}
