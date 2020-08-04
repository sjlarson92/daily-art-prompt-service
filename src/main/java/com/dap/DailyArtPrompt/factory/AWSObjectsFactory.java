package com.dap.DailyArtPrompt.factory;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Component
public class AWSObjectsFactory {

    public GetObjectRequest createGetObjectRequest(String bucket, String key) {
        return GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
    }

    public GetObjectPresignRequest createGetObjectPresignRequest(
            Duration duration,
            GetObjectRequest getObjectRequest
    ) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getObjectRequest)
                .build();
    }

    public AwsCredentialsProvider createAwsCredentialsProvider(
            String accessKey, String secretKey
    ) {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return StaticCredentialsProvider.create(awsCredentials);
    }

    public S3Presigner createS3Presigner(
            AwsCredentialsProvider awsCredentialsProvider,
            Region region
    ) {
        return S3Presigner.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(region)
                .build();
    }
}
