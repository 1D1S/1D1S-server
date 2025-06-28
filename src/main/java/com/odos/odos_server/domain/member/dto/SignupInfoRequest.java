package com.odos.odos_server.domain.member.dto;

import com.odos.odos_server.domain.common.Enum.Gender;
import com.odos.odos_server.domain.common.Enum.Job;
import java.time.LocalDate;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class SignupInfoRequest {
  @NotNull private String nickname;
  private String profileImageUrl;
  @NotNull private Job job;
  @NotNull private LocalDate birth;
  @NotNull private Gender gender;
  @NotNull private Boolean isPublic;
}
