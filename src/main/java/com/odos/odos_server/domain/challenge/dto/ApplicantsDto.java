package com.odos.odos_server.domain.challenge.dto;

import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import com.odos.odos_server.domain.common.Enum.MemberChallengeRole;
import com.odos.odos_server.domain.common.dto.GoalDto;
import com.odos.odos_server.domain.member.entity.Member;
import java.util.Collections;
import java.util.List;

public record ApplicantsDto(Member member, MemberChallengeRole status, List<GoalDto> goals) {
  public static ApplicantsDto from(MemberChallenge memberChallenge) {
    return new ApplicantsDto(
        memberChallenge.getMember(),
        memberChallenge.getMemberChallengeRole(),
        memberChallenge.getChallengeGoals() != null
            ? memberChallenge.getChallengeGoals().stream().map(GoalDto::from).toList()
            : Collections.emptyList());
  }
}
