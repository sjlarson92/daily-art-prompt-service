package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.factory.AWSObjectsFactory;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "S3_BUCKET=some-mock-s3-bucket",
                "ACCESS_KEY=some key",
                "SECRET_KEY=super secret key"
        }
)
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
class ImageServiceTest {

    String s3Bucket = "some-mock-s3-bucket"; // same as properties above
    String accessKey = "some key";
    String secretKey = "super secret key";

    @MockBean
    AWSObjectsFactory awsObjectsFactory;

    @Autowired
    ImageService imageService;

    @Nested
    class getImageFromS3Bucket {

        private final UUID id = UUID.randomUUID();
        private final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("randomBucket")
                .key("some key")
                .build();
        private final GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();
        private final AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey));
        private final S3Presigner s3Presigner = S3Presigner.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.US_EAST_2)
                .build();

        @BeforeEach
        public void setMocks() {
            reset(awsObjectsFactory);
            when(awsObjectsFactory.createGetObjectRequest(
                    s3Bucket,
                    "dap/images/" + id
                    )
            ).thenReturn(getObjectRequest);

            when(awsObjectsFactory.createGetObjectPresignRequest(
                    Duration.ofMinutes(10),
                    getObjectRequest
            )).thenReturn(getObjectPresignRequest);

            when(awsObjectsFactory.createAwsCredentialsProvider(
                    accessKey,
                    secretKey
            )).thenReturn(awsCredentialsProvider);

            when(awsObjectsFactory.createS3Presigner(
                    awsCredentialsProvider,
                    Region.US_EAST_2
            )).thenReturn(s3Presigner);
        }

        @Test
        public void createsAGetObjectRequestWithTheCorrectParams() {
            imageService.getImageContent(id);
            verify(awsObjectsFactory).createGetObjectRequest(
                    s3Bucket,
                    "dap/images/" + id
            );
        }

        @Test
        public void createsAGetObjectPresignRequestWithCorrectParams() {
            imageService.getImageContent(id);
            verify(awsObjectsFactory).createGetObjectPresignRequest(
                    Duration.ofMinutes(10),
                    getObjectRequest
            );
        }

        @Test
        public void createAwsCredentialProviderWithCorrectParams() {
            imageService.getImageContent(id);
            verify(awsObjectsFactory).createAwsCredentialsProvider(
                    accessKey,
                    secretKey
            );
        }

        @Test
        public void createS3PresignerWithCorrectParams() {
            imageService.getImageContent(id);
            verify(awsObjectsFactory).createS3Presigner(
                    awsCredentialsProvider,
                    Region.US_EAST_2
            );
        }
    }

}
