package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.common.dto.LikesDto;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import java.util.Collections;
import java.util.List;

public record DiaryResponseDTO(
    Long id,
    // ChallengeDto challenge,
    // MemberDto author,
    String title,
    String content,
    LikesDto likes,
    List<ImgDto> img,
    Boolean isPublic,
    DiaryInfoDTO diaryInfo) {

  /** Convert Diary entity to DiaryResponseDTO. */
  public static DiaryResponseDTO from(
      Diary entity, List<DiaryLike> likes /* + Member, Challenge 넣기 */) {
    List<ImgDto> images =
        entity.getDiaryImages() == null
            ? Collections.emptyList()
            : entity.getDiaryImages().stream().map(ImgDto::from).toList();
    LikesDto likesDto =
        likes == null ? new LikesDto(Collections.emptyList(), 0) : LikesDto.fromDiary(likes);

    return new DiaryResponseDTO(
        entity.getId(),
        // entity.getChallenge() == null ? null : ChallengeDto.from(entity.getChallenge()),
        // MemberDto.from(entity.getMember()),
        entity.getTitle(),
        entity.getContent(),
        likesDto,
        images,
        Boolean.TRUE.equals(entity.getIsPublic()),
        DiaryInfoDTO.from(entity));
  }

  public static List<DiaryResponseDTO> from(List<Diary> diaries, List<DiaryLike> likes) {
    return diaries.stream().map(d -> from(d, likes)).toList();
  }
}
