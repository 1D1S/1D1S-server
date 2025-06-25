package com.odos.odos_server.security.jwt;

import com.odos.odos_server.member.entity.Member;
import com.odos.odos_server.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Component
@Getter
@Slf4j
public class JwtTokenProvider {

  private final MemberRepository memberRepository;

  @Value("${jwt.secret-key}")
  private String secretKey;

  @Value("${jwt.access-token-exp-time}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh-token-exp-time}")
  private Long refreshTokenExpirationPeriod;

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secretKey는 Base64 인코딩된 문자열이어야 함
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // AccessToken 생성
  public String createAccessToken(Member member) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + accessTokenExpirationPeriod);

    return Jwts.builder()
        .setSubject("AccessToken")
        .claim("id", member.getId())
        .claim("email", member.getEmail())
        .claim("role", member.getRole().name())
        .claim("provider", member.getProvider().name())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  // RefreshToken 생성 : claim 넣을 필요 x
  public String createRefreshToken() {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + refreshTokenExpirationPeriod);

    return Jwts.builder()
        .setSubject("RefreshToken")
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public void sendAccessAndRefreshToken(
      HttpServletResponse response, String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    response.setHeader("Authorization-Refresh", "Bearer " + refreshToken);
  }

  public boolean isValidToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public boolean isExpired(String token) {
    try {
      return parseToken(token).getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return true;
    }
  }

  private Claims parseToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public Optional<String> extractMemberId(String accessToken) {
    try {
      Object id = parseToken(accessToken).get("id");
      return Optional.ofNullable(id).map(Object::toString);
    } catch (JwtException | IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
  }

  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return extractTokenFromHeader(request.getHeader("Authorization-Refresh"));
  }

  public Optional<String> extractTokenFromHeader(String bearerToken) {
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return Optional.of(bearerToken.substring(7));
    }
    return Optional.empty();
  }

  public void updateRefreshToken(String email, String refreshToken) {
    memberRepository
        .findByEmail(email)
        .ifPresentOrElse(
            member -> {
              member.updateRefreshToken(refreshToken);
              memberRepository.save(member);
            },
            () -> {
              throw new UsernameNotFoundException("User not found with email: " + email);
            });
  }
}

// 클라이언트 요청에서 jwt token, id 추출하는 부분
  // JWT 토큰의 헤더(Authorization의 형식 = Authorization : <type> <credentials> -> Bearer는 여기서 type에 해당하고,
  // 토큰 값이 credentials에 해당)
