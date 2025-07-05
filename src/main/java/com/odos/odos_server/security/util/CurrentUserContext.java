package com.odos.odos_server.security.util;

import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import com.odos.odos_server.security.jwt.MemberPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserContext {

  private CurrentUserContext() {}

  public static MemberPrincipal getCurrentMemberPrincipal() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println(">>>> principal class = " + auth.getPrincipal().getClass());
    if (auth == null) {
      throw new CustomException(ErrorCode.UNAUTHORIZED);
    }
    if (!(auth.getPrincipal() instanceof MemberPrincipal principal)) {
      throw new CustomException(ErrorCode.INVALID_AUTH_PRINCIPAL);
    }
    return principal;
  }

  public static Long getCurrentMemberId() {
    return getCurrentMemberPrincipal().getId();
  }
}
