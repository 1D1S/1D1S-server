package com.odos.odos_server.domain.common.dto;

import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import com.odos.odos_server.domain.member.dto.MemberDto;
import java.util.List;

public record LikesDto(List<MemberDto> members, int count) {
  public static LikesDto from(List<ChallengeLike> likes) {
    return new LikesDto(
        likes.stream().map(cl -> MemberDto.from(cl.getMember())).toList(), likes.size());
  }

  public static LikesDto from2(List<DiaryLike> diaryLikes) {
    return null;
  }
}
