package com.odos.odos_server.domain.member.entity;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import com.odos.odos_server.domain.common.Enum.Gender;
import com.odos.odos_server.domain.common.Enum.Job;
import com.odos.odos_server.domain.common.Enum.MemberRole;
import com.odos.odos_server.domain.common.Enum.SignupRoute;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import com.odos.odos_server.domain.diary.entity.DiaryReport;
import com.odos.odos_server.domain.friend.Friend;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
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

  private String nickname;
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  private Job job;

  private LocalDate birth;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private Boolean isPublic;

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
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
    this.job = job;
    this.birth = birth;
    this.gender = gender;
    this.isPublic = isPublic;
    this.role = MemberRole.USER;
  }

  @OneToMany(mappedBy = "hostMember", cascade = CascadeType.ALL)
  private List<Challenge> challenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<MemberChallenge> memberChallenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<ChallengeLike> challengeLikes;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Friend> senders;

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<Friend> receivers;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<MemberInterest> memberInterests;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryLike> diaryLikes;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryReport> diaryReports;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary> diaries;
}
