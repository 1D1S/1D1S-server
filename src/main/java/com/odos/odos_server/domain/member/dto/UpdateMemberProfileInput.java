package com.odos.odos_server.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.odos.odos_server.domain.common.Enum.ChallengeCategory;
import com.odos.odos_server.domain.common.Enum.Job;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateMemberProfileInput {
  private final String nickname;
  private final String profileImageUrl;
  private final Job job;
  private final Boolean isPublic;
  private final List<ChallengeCategory> category;

  @JsonCreator
  public UpdateMemberProfileInput(
      @JsonProperty("nickname") String nickname,
      @JsonProperty("profileImageUrl") String profileImageUrl,
      @JsonProperty("job") Job job,
      @JsonProperty("category") List<ChallengeCategory> category,
      @JsonProperty("public") Boolean isPublic) {
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
    this.job = job;
    this.category = category;
    this.isPublic = isPublic;
  }
}
