package com.odos.odos_server.domain.member.dto;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.common.Enum.MemberRole;
import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.member.entity.Member;
import java.util.List;

public record MemberDto(
    Long id,
    MemberPublicDto isPublic,
    ImgDto profileImageUrl,
    MemberRole role,
    String email,
    String nickname,
    MemberInfoDto info,
    // StreakDto streak,
    List<ChallengeDto> challenge
    // List<DiaryDto> diary
    ) {
  public static MemberDto from(Member member) {
    return new MemberDto(
        member.getId(),
        MemberPublicDto.from(member.getIsPublic()),
        ImgDto.from(member.getProfileImageUrl()),
        MemberRole.valueOf(member.getRole().name()), // 도메인 Role → DTO Role
        member.getEmail(),
        member.getNickname(),
        MemberInfoDto.from(member),
        // StreakDto.from(member.getStreak()),
        member.getChallenges().stream().map(ChallengeDto::from).toList()
        // member.getDiary().stream()
        //        .map(DiaryDTO::from)
        //        .toList()
        );
  }
}
