package com.odos.odos_server.member.dto;

import com.odos.odos_server.member.enums.Gender;
import com.odos.odos_server.member.enums.Job;
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
