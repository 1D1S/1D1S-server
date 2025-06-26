package com.odos.odos_server.domain.member.service;

import com.odos.odos_server.domain.member.dto.SignupInfoRequest;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
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

    member.completeProfile(
        request.getMemberNickname(),
        request.getMemberProfileImageUrl(),
        request.getMemberJob(),
        request.getMemberBirth(),
        request.getMemberGender(),
        request.getMemberPublic());

    memberRepository.save(member);
  }
}
