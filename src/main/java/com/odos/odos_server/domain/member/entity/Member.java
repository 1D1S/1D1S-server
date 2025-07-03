package com.odos.odos_server.domain.member.entity;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import com.odos.odos_server.domain.common.Enum.*;
import com.odos.odos_server.domain.diary.entity.Diary;
import com.odos.odos_server.domain.diary.entity.DiaryLike;
import com.odos.odos_server.domain.diary.entity.DiaryReport;
import com.odos.odos_server.domain.friend.Friend;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "MEMBER")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "memberId")
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

  private LocalDateTime nicknameLastModifiedAt;

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
    this.nicknameLastModifiedAt = LocalDateTime.now();
  }

  @OneToMany(mappedBy = "hostMember", cascade = CascadeType.ALL)
  private List<Challenge> challenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberChallenge> memberChallenges;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<ChallengeLike> challengeLikes;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private List<Friend> senders;

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private List<Friend> receivers;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberInterest> memberInterests = new ArrayList<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryLike> diaryLikes;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<DiaryReport> diaryReports;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Diary> diaries;

  public void updateNickname(String nickname) {
    this.nickname = nickname;
    this.nicknameLastModifiedAt = LocalDateTime.now();
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void updateCategories(List<ChallengeCategory> categories) {
    this.memberInterests.clear();

    for (ChallengeCategory category : categories) {
      this.memberInterests.add(new MemberInterest(this, category));
    }
  }

  public void updateJob(Job job) {
    this.job = job;
  }

  public void updateIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }
}
