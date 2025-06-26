package com.odos.odos_server.member.entity;

import com.odos.odos_server.member.enums.Gender;
import com.odos.odos_server.member.enums.Job;
import com.odos.odos_server.member.enums.MemberRole;
import com.odos.odos_server.member.enums.SignupRoute;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "MEMBER")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  private SignupRoute signupRoute;

  private String socialId;
  private String refreshToken;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  private String memberNickname;
  private String memberProfileImageUrl;

  @Enumerated(EnumType.STRING)
  private Job memberJob;

  private LocalDate memberBirth;

  @Enumerated(EnumType.STRING)
  private Gender memberGender;

  private Boolean memberPublic;

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }

  public void completeProfile(
      String nickname,
      String profileImageUrl,
      Job job,
      LocalDate birth,
      Gender gender,
      Boolean isPublic) {
    this.memberNickname = nickname;
    this.memberProfileImageUrl = profileImageUrl;
    this.memberJob = job;
    this.memberBirth = birth;
    this.memberGender = gender;
    this.memberPublic = isPublic;
    this.role = MemberRole.USER;
  }
}
