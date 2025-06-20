package com.odos.odos_server.challenge;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "ChallengeImage")
public class ChallengeImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challengeImageId;

  @Column private String challengeImageUrl;

  @ManyToOne
  @JoinColumn(name = "challengeId")
  private Challenge challenge;
}
