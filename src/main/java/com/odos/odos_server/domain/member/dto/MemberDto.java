package com.odos.odos_server.domain.member.dto;

import com.odos.odos_server.domain.challenge.dto.ChallengeDto;
import com.odos.odos_server.domain.common.Enum.MemberRole;
import com.odos.odos_server.domain.common.dto.ImgDto;
import com.odos.odos_server.domain.diary.dto.DiaryDto;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import com.odos.odos_server.domain.member.entity.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record MemberDto(
    Long id,
    MemberPublicDto isPublic,
    ImgDto profileImageUrl,
    MemberRole role,
    String email,
    String nickname,
    MemberInfoDto info,
    // StreakDto streak,
    List<ChallengeDto> challenge,
    List<DiaryDto> diary) {
  public static MemberDto from(Member member) {
    List<DiaryLike> diaryLikes = member.getDiaryLikes();
    List<Diary> diaries = member.getDiaries();

    Map<Long, List<DiaryLike>> likesGroupedByDiaryId =
        diaryLikes.stream().collect(Collectors.groupingBy(like -> like.getDiary().getId()));

    // 다이어리 리스트를 DiaryDto로 변환
    List<DiaryDto> diaryDtos =
        diaries.stream()
            .map(
                diary -> {
                  List<DiaryLike> likes =
                      likesGroupedByDiaryId.getOrDefault(diary.getId(), List.of());
                  return DiaryDto.from(diary, likes);
                })
            .toList();

    // 챌린지 dto 리스트로
    List<ChallengeDto> challengeDtos =
        member.getChallenges() != null
            ? member.getChallenges().stream().map(ChallengeDto::from).toList()
            : List.of();

    return new MemberDto(
        member.getId(),
        MemberPublicDto.from(member.getIsPublic()),
        ImgDto.from(member.getProfileImageKey()),
        MemberRole.valueOf(member.getRole().name()), // 도메인 Role → DTO Role
        member.getEmail(),
        member.getNickname(),
        MemberInfoDto.from(member),
        challengeDtos,
        // StreakDto.from(member.getStreak()),
        diaryDtos);
  }
}
