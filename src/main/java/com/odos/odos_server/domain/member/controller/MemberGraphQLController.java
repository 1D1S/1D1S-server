package com.odos.odos_server.domain.member.controller;

import com.odos.odos_server.domain.member.dto.UpdateMemberProfileInput;
import com.odos.odos_server.domain.member.entity.Member;
import com.odos.odos_server.domain.member.service.MemberService;
import com.odos.odos_server.security.util.CurrentUserContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberGraphQLController {

  private final MemberService memberService;

  @QueryMapping(name = "memberMe")
  public Member memberMe() {
    Long id = CurrentUserContext.getCurrentMemberId();
    return memberService.getMemberById(id);
  }

  @QueryMapping(name = "memberById")
  public Member memberById(@Argument Long id) {
    return memberService.getMemberById(id);
  }

  @QueryMapping(name = "allMembers")
  public List<Member> allMembers() {
    return memberService.getAllMembers();
  }

  @MutationMapping(name = "updateMemberProfile")
  public Member updateMemberProfile(@Argument UpdateMemberProfileInput input) {
    Long id = CurrentUserContext.getCurrentMemberId();
    return memberService.updateMemberProfile(id, input);
  }

  @MutationMapping(name = "deleteMember")
  public Boolean deleteMember(@Argument Long memberId) {
    return memberService.deleteById(memberId);
  }

  @MutationMapping(name = "deleteMemberMe")
  public Boolean deleteMemberMe() {
    Long id = CurrentUserContext.getCurrentMemberId();
    return memberService.deleteById(id);
  }
}
