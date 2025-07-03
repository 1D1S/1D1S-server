package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.common.dto.LikesDto;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import java.util.Collections;
import java.util.List;

public record DiaryResponseDto(
    Long id,
    // ChallengeDto challenge,
    // MemberDto author,
    String title,
    String content,
    LikesDto likes,
    List<ImgDto> img,
    Boolean isPublic,
    DiaryInfoDto diaryInfo) {

  public static DiaryResponseDto from(
      Diary entity, List<DiaryLike> likes /* + Member, Challenge 넣기 */) {

    // s3로 사진 저장 로직으로 변경해야함
    List<ImgDto> images =
        entity.getDiaryImages() == null
            ? Collections.emptyList()
            : entity.getDiaryImages().stream().map(ImgDto::from).toList();

    // 이 다이어리에 좋아요 누른 사람의 정보를 가져오고 다이어리의 총 좋아요 개수 가져오는거 맞죠??
    LikesDto likesDto = LikesDto.fromDiary(likes);

    return new DiaryResponseDto(
        entity.getId(),
        // entity.getChallenge() == null ? null : ChallengeDto.from(entity.getChallenge()),
        // MemberDto.from(entity.getMember()),
        entity.getTitle(),
        entity.getContent(),
        likesDto,
        images,
        Boolean.TRUE.equals(entity.getIsPublic()),
        DiaryInfoDto.from(entity));
  }
}
