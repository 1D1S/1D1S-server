package com.odos.odos_server.member.entity;

import com.nimbusds.openid.connect.sdk.claims.Gender;
import com.odos.odos_server.member.enums.Job;
import com.odos.odos_server.member.enums.MemberRole;
import com.odos.odos_server.member.enums.Provider;
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
  private Provider provider;

  private String socialId;
  private String refreshToken;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  private String memberNickname;
  private String memberProfileImageUrl;
  private Job memberJob;
  private LocalDate memberBirth;
  private Gender memberGender;
  private Boolean memberPublic;

  public void authorizeUser() {
    this.role = MemberRole.USER;
  }

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }
}
