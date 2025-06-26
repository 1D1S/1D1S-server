package com.odos.odos_server.domain.common.Enum;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN"),
  GUEST("ROLE_GUEST");

  private final String key;
}
