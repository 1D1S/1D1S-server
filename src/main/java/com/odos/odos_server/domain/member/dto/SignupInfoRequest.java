package com.odos.odos_server.domain.member.dto;

import com.odos.odos_server.domain.common.Enum.Gender;
import com.odos.odos_server.domain.common.Enum.Job;
import java.time.LocalDate;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class SignupInfoRequest {
  @NotNull private String memberNickname;
  private String memberProfileImageUrl;
  @NotNull private Job memberJob;
  @NotNull private LocalDate memberBirth;
  @NotNull private Gender memberGender;
  @NotNull private Boolean memberPublic;
}
