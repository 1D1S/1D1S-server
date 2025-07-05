package com.odos.odos_server.domain.common.dto;

import com.odos.odos_server.domain.challenge.entity.ChallengeImage;
import com.odos.odos_server.domain.diary.entity.DiaryImage;

public record ImgDto(String url) {
  public static ImgDto from(ChallengeImage image) {
    return new ImgDto(image.getUrl());
  }

  // 수정 필요
  public static ImgDto from(String key) {
    String bucket = "1d1s-image-bucket";
    String region = "ap-northeast-2";
    String url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    return new ImgDto(url);
  }

  public static ImgDto from(DiaryImage image) {
    return new ImgDto(image.getUrl());
  }

}
