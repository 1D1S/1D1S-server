package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.common.dto.LikesDto;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryGoal;
import com.odos.odos_server.domain.diary.entity.DiaryImage;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import com.odos.odos_server.domain.member.dto.MemberDto;
import com.odos.odos_server.domain.member.entity.Member;
import java.util.List;

public record DiaryResponseDTO(
    Long id,
    ChallengeDto challenge,
    MemberDto author,
    String title,
    String content,
    LikesDto likes,
    List<ImgDto> img,
    List<DiaryGoalDTO> goals) {

  /** Convert Diary entity to DiaryResponseDTO. */
  public static DiaryResponseDTO from(
      Diary entity,
      DiaryGoal goal,
      DiaryImage img,
      DiaryLike like,
      Challenge challenge,
      Member member) {
    return null;
  }
  //    return new DiaryResponseDTO(
  //        entity.getId(),
  //        entity.getChallenge() == null ? null : ChallengeDto.from(entity.getChallenge()),
  //        MemberDto.from(entity.getMember()),
  //        entity.getTitle(),
  //        entity.getContent(),
  //        entity.getDiaryImages() == null
  //            ? List.of()
  //            : entity.getDiaryImages().stream().map(ImgDto::from).toList());
  //  }
}
