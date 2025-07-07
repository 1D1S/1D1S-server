package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.common.dto.LikesDto;
import com.odos.odos_server.domain.common.dto.MemberCoreInfoDto;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import java.util.List;
import java.util.Optional;

public record DiaryDto(
    Long id,
    ChallengeDto challenge,
    MemberCoreInfoDto author,
    String title,
    String content,
    LikesDto like,
    List<ImgDto> img,
    DiaryPublicDto isPublic,
    DiaryInfoDto diaryInfo) {

  public static DiaryDto from(Diary diary, List<DiaryLike> likes) {

    // s3로 사진 저장 로직으로 변경해야함
    List<ImgDto> images =
        Optional.ofNullable(diary.getDiaryImages()).orElse(List.of()).stream()
            .map(i -> ImgDto.from(i.getUrl()))
            .toList();

    // 이 다이어리에 좋아요 누른 사람의 정보를 가져오고 다이어리의 총 좋아요 개수 가져오는거 맞죠??
    // LikesDto likesDto = LikesDto.fromDiary(likes == null ? List.of() : likes);

    return new DiaryDto(
        diary.getId(),
        diary.getChallenge() == null ? null : ChallengeDto.from(diary.getChallenge()),
        MemberCoreInfoDto.from(diary.getMember()),
        diary.getTitle(),
        diary.getContent(),
        LikesDto.fromDiary(likes),
        images,
        DiaryPublicDto.from(diary.getIsPublic()),
        DiaryInfoDto.from(diary));
  }
}
