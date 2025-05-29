package com.odos.odos_server.challenge;

import com.odos.odos_server.Enum.Challenge_Type;
import com.odos.odos_server.member.Member;
import com.odos.odos_server.member.Member_Challenge;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challenge_id;

  @Column private String challenge_title;

  @Column private String challenge_category; // enum으로 안함?

  @Column private LocalDateTime challenge_start_date;

  @Column private LocalDateTime challenge_end_date;

  @Column private Long challenge_participants_cnt; // 수 표현 위해 cnt 붙임

  @Column
  @Enumerated(EnumType.STRING)
  private Challenge_Type challenge_type;

  @Column private String challenge_description;

  @ManyToOne
  @JoinColumn(name = "challenge_host")
  private Member member; // 챌린지가 없어진다고 해서 멤버가 없어지는건 아니니까.. 고민,, 주회자 아이디 의도하긴함

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<Member_Challenge> memberChallenges;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<Challenge_Like> challengeLikes;

  @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
  private List<Challenge_Image> challengeImages;
}
