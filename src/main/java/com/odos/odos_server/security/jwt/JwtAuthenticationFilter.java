package com.odos.odos_server.security.jwt;

import com.odos.odos_server.member.entity.Member;
import com.odos.odos_server.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/oauth2/") || path.startsWith("/auth/") || path.equals("/login");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    Optional<String> accessOpt =
        jwtTokenProvider.extractAccessToken(request).filter(jwtTokenProvider::isValidToken);

    if (accessOpt.isPresent() && !jwtTokenProvider.isExpired(accessOpt.get())) {
      authenticateWithAccessToken(accessOpt.get());
      filterChain.doFilter(request, response);
      return;
    }

    Optional<String> refreshOpt =
        jwtTokenProvider.extractRefreshToken(request).filter(jwtTokenProvider::isValidToken);

    if (refreshOpt.isPresent()) {
      boolean refreshed = handleRefreshFlow(response, refreshOpt.get());
      if (refreshed) {
        return;
      }
    }

    accessOpt.ifPresent(this::authenticateWithAccessToken);
    filterChain.doFilter(request, response);
  }

  private boolean handleRefreshFlow(HttpServletResponse response, String refreshToken) {
    return memberRepository
        .findByRefreshToken(refreshToken)
        .map(
            member -> {
              String newRefreshToken = rotateRefreshToken(member);
              String newAccessToken = jwtTokenProvider.createAccessToken(member);
              jwtTokenProvider.sendAccessAndRefreshToken(response, newAccessToken, newRefreshToken);
              return true;
            })
        .orElse(false);
  }

  private String rotateRefreshToken(Member member) {
    String newRefreshToken = jwtTokenProvider.createRefreshToken();
    jwtTokenProvider.updateRefreshToken(member.getEmail(), newRefreshToken); // 내부에서 DB save 처리
    return newRefreshToken;
  }

  private void authenticateWithAccessToken(String token) {
    jwtTokenProvider
        .extractMemberId(token)
        .map(Long::parseLong)
        .flatMap(memberRepository::findById)
        .ifPresent(this::setAuthentication);
  }

  private void setAuthentication(Member member) {
    MemberPrincipal principal = new MemberPrincipal(member.getEmail(), member.getRole().name());

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
