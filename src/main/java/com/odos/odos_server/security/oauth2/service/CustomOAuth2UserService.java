package com.odos.odos_server.security.oauth2.service;

import com.odos.odos_server.member.entity.Member;
import com.odos.odos_server.member.enums.MemberRole;
import com.odos.odos_server.member.enums.Provider;
import com.odos.odos_server.member.repository.MemberRepository;
import com.odos.odos_server.security.oauth2.CustomOAuth2User;
import com.odos.odos_server.security.oauth2.info.OAuth2UserInfo;
import com.odos.odos_server.security.oauth2.info.OAuth2UserInfoFactory;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    Provider provider =
        Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
    Map<String, Object> attributes = oAuth2User.getAttributes();

    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.get(provider, attributes);
    String email = userInfo.getEmail();
    if (email == null || email.isBlank()) {
      throw new OAuth2AuthenticationException(
          new OAuth2Error("invalid_email"), "이메일을 제공하지 않는 소셜 로그인 공급자입니다.");
    }

    Member member =
        memberRepository
            .findByEmail(email)
            .orElseGet(
                () -> {
                  Member newMember =
                      Member.builder()
                          .email(email)
                          .provider(provider)
                          .socialId(userInfo.getId())
                          .role(MemberRole.USER)
                          .build();
                  return memberRepository.save(newMember);
                });

    log.debug(">>> attributes raw: {}", attributes);

    return new CustomOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
        attributes,
        userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName(),
        member.getEmail(),
        member.getRole());
  }

  public Member createMember(String email, Provider provider, String socialId) {
    Member newMember =
        Member.builder()
            .email(email)
            .provider(provider)
            .socialId(socialId)
            .role(MemberRole.USER)
            .build();
    return memberRepository.save(newMember);
  }
}
