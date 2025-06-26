package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.common.dto.LikesDto;
import com.odos.odos_server.domain.member.dto.MemberDto;
import java.util.List;

public record ChallengeDto(
    Long id,
    MemberDto hostMember,
    List<ApplicantsDto> applicants,
    String title,
    String description,
    LikesDto like,
    List<ImgDto> img,
    ChallengeInfoDto challengeInfo) {
  public static ChallengeDto from(Challenge entity) {
    return new ChallengeDto(
        entity.getId(),
        MemberDto.from(entity.getHostMember()),
        null,
        entity.getTitle(),
        entity.getDescription(),
        LikesDto.from(entity.getLikes()),
        entity.getImages().stream().map(ImgDto::from).toList(),
        ChallengeInfoDto.from(entity));
  }
}
