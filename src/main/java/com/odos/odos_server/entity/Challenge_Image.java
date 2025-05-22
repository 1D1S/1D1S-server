package com.odos.odos_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Challenge_Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long challenge_image_id;

  @Column private String challenge_image_url;

  @ManyToOne
  @JoinColumn(name = "challenge_id")
  private Challenge challenge;
}
