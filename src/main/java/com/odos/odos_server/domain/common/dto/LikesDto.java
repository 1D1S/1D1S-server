package com.odos.odos_server.domain.common.dto;

import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import java.util.Collections;
import java.util.List;

public record LikesDto(List<MemberCoreInfoDto> members, int count) {
  public static LikesDto from(List<ChallengeLike> likes) {
    if (likes == null) {
      return new LikesDto(Collections.emptyList(), 0);
    }
    return new LikesDto(
        likes.stream().map(cl -> MemberCoreInfoDto.from(cl.getMember())).toList(), likes.size());
  }

  public static LikesDto fromDiary(List<DiaryLike> likes) {
    if (likes == null) {
      return new LikesDto(Collections.emptyList(), 0); // 기본값 처리
    }
    return new LikesDto(
        likes.stream().map(dl -> MemberCoreInfoDto.from(dl.getMember())).toList(), likes.size());
  }
}
