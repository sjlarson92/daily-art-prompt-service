package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.factory.AWSObjectsFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final AWSObjectsFactory awsObjectsFactory;

    @Value("${s3.bucket}")
    String s3Bucket;

    @Value("${s3.access-key}")
    String accessKey;

    @Value("${s3.secret-key}")
    String secretKey;

    public RedirectView getImageContent(UUID id) {
        GetObjectRequest getObjectRequest = awsObjectsFactory.createGetObjectRequest(
                s3Bucket, "dap/images/" + id
        );

        // Create a GetObjectPresignRequest to specify the signature duration
        GetObjectPresignRequest getObjectPresignRequest = awsObjectsFactory.createGetObjectPresignRequest(
                Duration.ofMinutes(10),
                getObjectRequest
        );

        AwsCredentialsProvider awsCredentialsProvider = awsObjectsFactory.createAwsCredentialsProvider(
                accessKey, secretKey);

        S3Presigner s3Presigner = awsObjectsFactory.createS3Presigner(awsCredentialsProvider, Region.US_EAST_2);

        // Generate the presigned request
        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner
                .presignGetObject(getObjectPresignRequest);

        s3Presigner.close();
        return new RedirectView(presignedGetObjectRequest.url().toString());
    }

    public void saveImageToS3(UUID imageId, MultipartFile file) throws IOException {

        S3Client s3Client = awsObjectsFactory.createS3Client(Region.US_EAST_2);

        PutObjectRequest putObjectRequest = awsObjectsFactory
                .createPutObjectRequest(s3Bucket,"dap/images/" + imageId, MediaType.IMAGE_PNG_VALUE);

        RequestBody requestBody = awsObjectsFactory.createRequestBody(file);

        PutObjectResponse response = awsObjectsFactory.saveImage(s3Client, putObjectRequest, requestBody);

        log.info("Response is: " + response.sdkHttpResponse().statusCode() + response.sdkHttpResponse().statusText());
    }
}
