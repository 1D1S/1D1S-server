package com.odos.odos_server.domain.member.service;

import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.repository.MemberRepository;
import com.odos.odos_server.global.error.code.ErrorCode;
import com.odos.odos_server.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public Member getMemberById(Long id) {
    return memberRepository
        .findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
  }

  public Member getMemberByNickname(String nickname) {
    return memberRepository
        .findMemberByNickname(nickname)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
  }
}
