package com.odos.odos_server.domain.member.dto;

import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.Enum.Gender;
import com.odos.odos_server.domain.common.Enum.Job;
import com.odos.odos_server.domain.common.Enum.MemberPublic;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SignupInfoRequest {

  @NotNull private String nickname;

  private String profileImageUrl;

  @NotNull private Job job;

  @NotNull private LocalDate birth;

  @NotNull private Gender gender;

  @NotNull private MemberPublic isPublic;

  @NotNull private List<ChallengeCategory> categories;
}
