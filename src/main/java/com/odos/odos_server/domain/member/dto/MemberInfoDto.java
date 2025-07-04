package com.odos.odos_server.domain.member.dto;

import com.odos.odos_server.domain.member.entity.Member;
import java.util.List;

public record MemberInfoDto(String job, List<String> category, String birthday, String gender) {
  public static MemberInfoDto from(Member member) {
    return new MemberInfoDto(
        member.getJob().name(),
        member.getMemberInterests().stream().map(mi -> mi.getCategory().name()).toList(),
        member.getBirth().toString(),
        member.getGender().name());
  }
}
