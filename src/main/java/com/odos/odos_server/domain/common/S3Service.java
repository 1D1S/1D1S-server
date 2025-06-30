package com.odos.odos_server.domain.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URI;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

  @Value("${cloud.aws.credentials.access-key}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secret-key}")
  private String secretKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String generatePresignedUrl(String fileName) {
    S3Presigner presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)))
            .build();

    String key = fileName;

    PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType("image/jpeg") // 필요 시 변경
            .build();

    PresignedPutObjectRequest presignedRequest =
            presigner.presignPutObject(r -> r.signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest));

    return presignedRequest.url().toString();
  }
}
