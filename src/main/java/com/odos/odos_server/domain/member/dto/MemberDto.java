package com.odos.odos_server.domain.member.dto;

import com.odos.odos_server.domain.member.entity.Member;

public record MemberDto() {
  public static MemberDto from(Member hostMember) {
    return new MemberDto();
  }
}
