package com.odos.odos_server.domain.common.dto;

import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import com.odos.odos_server.domain.member.dto.MemberDto;
import java.util.Collections;
import java.util.List;

public record LikesDto(List<MemberDto> members, int count) {
  public static LikesDto from(List<ChallengeLike> likes) {
    if (likes == null) {
      return new LikesDto(Collections.emptyList(), 0);
    }
    return new LikesDto(
        likes.stream().map(cl -> MemberDto.from(cl.getMember())).toList(), likes.size());
  }
}
