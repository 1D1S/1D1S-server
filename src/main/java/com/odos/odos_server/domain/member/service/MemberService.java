package com.odos.odos_server.domain.member.service;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.member.dto.UpdateMemberProfileInput;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public Member getMemberById(Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));
  }

  @Transactional(readOnly = true)
  public List<Member> getAllMembers() {
    return memberRepository.findAll();
  }

  @Transactional
  public Member updateMemberProfile(Long memberId, UpdateMemberProfileInput in) {
    Member m =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

    if (in.getNickname() != null) {
      validateNicknameSpelling(in.getNickname());
      validateNicknameTime(m);
      m.updateNickname(in.getNickname());
    }
    if (in.getProfileImageUrl() != null) m.updateProfileImageUrl(in.getProfileImageUrl());
    if (in.getJob() != null) m.updateJob(in.getJob());
    if (in.getIsPublic() != null) m.updateIsPublic(in.getIsPublic());

    List<ChallengeCategory> list = in.getCategories();
    if (list != null) {
      if (list.isEmpty()) {
        throw new IllegalArgumentException("관심 카테고리를 최소 하나 선택해야 합니다.");
      }
      if (list.size() > 3) {
        throw new IllegalArgumentException("관심 카테고리는 최대 3개까지 선택 가능합니다.");
      }
      m.updateCategories(list);
    }
    return m;
  }

  private void validateNicknameTime(Member member) {
    LocalDateTime lastModified = member.getNicknameLastModifiedAt();
    if (lastModified != null && lastModified.plusMonths(1).isAfter(LocalDateTime.now())) {
      throw new IllegalStateException("닉네임은 한 달에 한 번만 변경 가능합니다.");
    }
  }

  public void validateNicknameSpelling(String nickname) {
    String regex = "^[가-힣a-zA-Z]{1,8}$";
    if (!nickname.matches(regex)) {
      throw new IllegalArgumentException("닉네임은 한글과 영어만 가능하며, 특수문자 없이 8자 이내여야 합니다.");
    }
  }

  @Transactional
  public boolean deleteById(Long memberId) {
    if (!memberRepository.existsById(memberId)) return false;
    memberRepository.deleteById(memberId);
    return true;
  }
}
