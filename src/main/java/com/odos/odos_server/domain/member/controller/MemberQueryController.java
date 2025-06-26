package com.odos.odos_server.domain.member.controller;

import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberQueryController {

  private final MemberService memberService;

  //    @QueryMapping
  //    public Member getMember2(@Argument Long id) {
  //        return memberService.getMemberById(id);
  //    }

  @QueryMapping
  public Member getMember(@Argument String nickname) {
    return memberService.getMemberByNickname(nickname);
  }
}
