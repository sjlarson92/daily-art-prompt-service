package com.dap.DailyArtPrompt.service;

import com.dap.DailyArtPrompt.factory.AWSObjectsFactory;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
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
class ImageContentServiceTest {

    String s3Bucket = "some-mock-s3-bucket"; // same as properties above
    String accessKey = "some key";
    String secretKey = "super secret key";

    @MockBean
    AWSObjectsFactory awsObjectsFactory;

    @Autowired
    ImageContentService imageContentService;

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
            imageContentService.getImageContent(id);
            verify(awsObjectsFactory).createGetObjectRequest(
                    s3Bucket,
                    "dap/images/" + id
            );
        }

        @Test
        public void createsAGetObjectPresignRequestWithCorrectParams() {
            imageContentService.getImageContent(id);
            verify(awsObjectsFactory).createGetObjectPresignRequest(
                    Duration.ofMinutes(10),
                    getObjectRequest
            );
        }

        @Test
        public void createAwsCredentialProviderWithCorrectParams() {
            imageContentService.getImageContent(id);
            verify(awsObjectsFactory).createAwsCredentialsProvider(
                    accessKey,
                    secretKey
            );
        }

        @Test
        public void createS3PresignerWithCorrectParams() {
            imageContentService.getImageContent(id);
            verify(awsObjectsFactory).createS3Presigner(
                    awsCredentialsProvider,
                    Region.US_EAST_2
            );
        }
    }

    @Nested
    class saveImageToS3 {
        UUID imageId = UUID.randomUUID();
        Region region = Region.US_EAST_2;
        String key = "dap/images/" + imageId;
        String contentType = MediaType.IMAGE_PNG_VALUE;
        MultipartFile file = new MockMultipartFile("some file name", "fake content".getBytes());
        S3Client s3Client = S3Client.builder().region(region).build();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key(key)
                .contentType(contentType)
                .build();
        RequestBody requestBody = RequestBody.fromBytes(file.getBytes());
        SdkHttpResponse sdkHttpResponse = SdkHttpResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .statusText("all good")
                .build();
        PutObjectResponse response = (PutObjectResponse) PutObjectResponse.builder()
                .sdkHttpResponse(sdkHttpResponse)
                .build();
        AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey));

        saveImageToS3() throws IOException {
        }

        @BeforeEach
        public void setMocks() throws IOException {
            reset(awsObjectsFactory);
            when(awsObjectsFactory.createAwsCredentialsProvider(
                    accessKey,
                    secretKey
            )).thenReturn(awsCredentialsProvider);
            when(awsObjectsFactory.createS3Client(region, awsCredentialsProvider)).thenReturn(s3Client);
            when(awsObjectsFactory.createPutObjectRequest(
                    s3Bucket,
                    key,
                    contentType)).thenReturn(putObjectRequest);
            when(awsObjectsFactory.createRequestBody(file)).thenReturn(requestBody);
            when(awsObjectsFactory.saveImage(s3Client, putObjectRequest, requestBody)).thenReturn(response);
        }

        @Test
        public void createS3ClientIsCalledWithCorrectParam() throws IOException {
            imageContentService.saveImageToS3(imageId, file);
            verify(awsObjectsFactory).createS3Client(region, awsCredentialsProvider);
        }

        @Test
        public void createPutObjectRequestIsCalledWithCorrectParams() throws IOException {
            imageContentService.saveImageToS3(imageId, file);
            verify(awsObjectsFactory).createPutObjectRequest(s3Bucket, key, contentType);
        }

        @Test
        public void createRequestBodyIsCalledWithCorrectParams() throws IOException {
            imageContentService.saveImageToS3(imageId, file);
            verify(awsObjectsFactory).createRequestBody(file);
        }

        @Test
        public void saveImageIsCalledWithCorrectParams() throws IOException {
            imageContentService.saveImageToS3(imageId, file);
            verify(awsObjectsFactory).saveImage(s3Client, putObjectRequest, requestBody);
        }

    }

}
