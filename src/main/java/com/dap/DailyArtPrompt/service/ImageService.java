package com.dap.DailyArtPrompt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class ImageService {

    @Value("${s3.bucket}")
    String s3Bucket;

    public void saveImageToS3(UUID imageId, MultipartFile file) throws IOException {

        S3Client s3Client = S3Client.builder().region(Region.US_EAST_2).build();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key("dap/images/" + imageId)
                .contentType(MediaType.IMAGE_PNG_VALUE)
                .build();

        RequestBody requestBody = RequestBody.fromBytes(file.getBytes());

        PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);

        System.out.println("Response is: " + response.sdkHttpResponse().statusCode() + response.sdkHttpResponse().statusText());
    }
}
