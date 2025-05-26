package com.odos.odos_server.security.oauth2;

import com.odos.odos_server.member.enums.MemberRole;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
  private String email;
  private MemberRole role;

  public CustomOAuth2User(
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes,
      String nameAttributeKey,
      String email,
      MemberRole role) {
    super(authorities, attributes, nameAttributeKey);
    this.email = email;
    this.role = role;
  }
}
