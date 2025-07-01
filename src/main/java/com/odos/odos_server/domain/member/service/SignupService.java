package com.odos.odos_server.domain.member.service;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.member.dto.SignupInfoRequest;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

  private final MemberRepository memberRepository;

  @Transactional
  public void completeSignupInfo(Long memberId, SignupInfoRequest request) {
    Member member =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

    List<ChallengeCategory> list = request.getCategories();
    if (list == null || list.isEmpty()) {
      throw new IllegalArgumentException("최소 하나의 관심 카테고리를 선택해야 합니다.");
    }
    if (list.size() > 3) {
      throw new IllegalArgumentException("관심 카테고리는 최대 3개까지 선택 가능합니다.");
    }

    String regex = "^[가-힣a-zA-Z]{1,8}$";
    if (!request.getNickname().matches(regex)) {
      throw new IllegalArgumentException("닉네임은 한글과 영어만 가능하며, 특수문자 없이 8자 이내여야 합니다.");
    }

    member.completeProfile(
        request.getNickname(),
        request.getProfileImageUrl(),
        request.getJob(),
        request.getBirth(),
        request.getGender(),
        request.getIsPublic());
    member.updateCategories(list);

    memberRepository.save(member);
  }
}
