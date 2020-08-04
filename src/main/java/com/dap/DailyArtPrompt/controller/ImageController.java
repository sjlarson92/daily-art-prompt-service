package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.factory.AWSObjectsFactory;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageRepository imageRepository;

    private final AWSObjectsFactory awsObjectsFactory;

    @Value("${s3.bucket}")
    String s3Bucket;

    @Value("${s3.access-key}")
    String accessKey;

    @Value("${s3.secret-key}")
    String secretKey;

    @GetMapping("/images/{id}")
    public Image getImage(@PathVariable UUID id) {
        log.info("Fetching image with id " + id);
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No event found by given id: " + id));
    }

    @GetMapping("/images/{id}/content")
    public RedirectView getImageFromS3Bucket(@PathVariable UUID id) {
        log.info("Fetching image with id: " + id);

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
}
