package com.odos.odos_server.domain.member.dto;

public record MemberPublicDto(Boolean isPublic) {
  public static MemberPublicDto from(Boolean isPublic) {
    return new MemberPublicDto(isPublic);
  }
}
