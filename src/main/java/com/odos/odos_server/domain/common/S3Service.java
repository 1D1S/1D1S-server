package com.odos.odos_server.domain.common;

import com.odos.odos_server.domain.common.dto.S3Dto;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

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

  public S3Dto generatePresignedUrl(String fileName) {
    S3Presigner presigner =
        S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .build();

    String uuid = UUID.randomUUID().toString();
    String key = uuid + "_" + fileName;

    PutObjectRequest objectRequest =
        PutObjectRequest.builder().bucket(bucket).key(key).contentType("image/jpeg").build();

    PresignedPutObjectRequest presignedRequest =
        presigner.presignPutObject(
            r -> r.signatureDuration(Duration.ofMinutes(10)).putObjectRequest(objectRequest));

    return new S3Dto(key, presignedRequest.url().toString());
  }

  public String getImageUrl(String fileName) {
    return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;
  }
}
