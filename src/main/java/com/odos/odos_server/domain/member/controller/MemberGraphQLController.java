package com.odos.odos_server.domain.member.controller;

import com.odos.odos_server.domain.member.dto.MemberDto;
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
  public MemberDto memberMe() {
    Long id = CurrentUserContext.getCurrentMemberId();
    Member member = memberService.getMemberById(id);
    return MemberDto.from(member);
  }

  @QueryMapping(name = "memberById")
  public MemberDto memberById(@Argument Long id) {
    Member member = memberService.getMemberById(id);
    return MemberDto.from(member);
  }

  @QueryMapping(name = "allMembers")
  public List<MemberDto> allMembers() {
    return memberService.getAllMembers().stream().map(MemberDto::from).toList();
  }

  @MutationMapping(name = "updateMemberProfile")
  public MemberDto updateMemberProfile(@Argument UpdateMemberProfileInput input) {
    Long id = CurrentUserContext.getCurrentMemberId();
    Member updated = memberService.updateMemberProfile(id, input);
    return MemberDto.from(updated);
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

  @MutationMapping
  public String updateMemberProfileImg(String fileName){
    return memberService.updateMemberProfileImg(fileName);
  }
}
