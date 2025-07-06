package com.odos.odos_server.domain.common.dto;

import com.odos.odos_server.domain.member.entity.Member;

public record MemberCoreInfoDto(Long id, String nickname, ImgDto profileUrl) {
  public static MemberCoreInfoDto from(Member member) {
    return new MemberCoreInfoDto(
        member.getId(), member.getNickname(), ImgDto.from(member.getProfileImageKey()));
  }
}
