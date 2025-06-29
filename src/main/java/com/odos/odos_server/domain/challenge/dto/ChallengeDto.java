package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.common.dto.LikesDto;
import com.odos.odos_server.domain.member.dto.MemberDto;
import java.util.Collections;
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
  public static ChallengeDto from(Challenge challenge) {
    return new ChallengeDto(
        challenge.getId(),
        MemberDto.from(challenge.getHostMember()),
        challenge.getMemberChallenges() != null
            ? challenge.getMemberChallenges().stream()
                .filter(
                    mc ->
                        mc.getMemberChallengeRole() == MemberChallengeRole.APPLICANT
                            || mc.getMemberChallengeRole() == MemberChallengeRole.HOST)
                .map(ApplicantsDto::from)
                .toList()
            : Collections.emptyList(),
        challenge.getTitle(),
        challenge.getDescription(),
        LikesDto.from(challenge.getLikes()),
        challenge.getImages() != null
            ? challenge.getImages().stream().map(ImgDto::from).toList()
            : Collections.emptyList(),
        ChallengeInfoDto.from(challenge));
  }
}
