package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.factory.AWSObjectsFactory;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = ImageController.class,
        properties = {
                "S3_BUCKET=some-mock-s3-bucket",
                "ACCESS_KEY=some key",
                "SECRET_KEY=super secret key"
        }
)
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    String s3Bucket = "some-mock-s3-bucket"; // same as properties above
    String accessKey = "some key";
    String secretKey = "super secret key";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ImageRepository imageRepository;

    @MockBean
    AWSObjectsFactory awsObjectsFactory;

    @Nested
    @DisplayName("/images")
    class getImage {

        @Nested
        class whenImageWithIdExists {

            @Test
            public void shouldReturnImage() throws Exception {
                UUID imageId = UUID.randomUUID();
                Image image = new Image(imageId, 1234, "some name", "src", false, null);
                when(imageRepository.findById(imageId)).thenReturn(Optional.of(image));
                mockMvc.perform(
                        get("/images/" + imageId)
                ).andExpect(content().string(objectMapper.writeValueAsString(image)));
            }
        }

        @Nested
        class whenImageWithIdDoesNotExists {

            @Test
            public void shouldReturn404WithCorrectErrorMessage() throws Exception {
                UUID imageId = UUID.randomUUID();
                when(imageRepository.findById(imageId)).thenReturn(Optional.empty());
                String message = Objects.requireNonNull(
                        mockMvc.perform(get("/images/" + imageId))
                                .andExpect(status().isNotFound())
                                .andReturn().getResolvedException()).getMessage();

                assertThat(message).contains("No event found by given id: " + imageId);
            }
        }
    }

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
        public void createsAGetObjectRequestWithTheCorrectParams() throws Exception {
            mockMvc.perform(get("/images/" + id + "/content"));
            verify(awsObjectsFactory).createGetObjectRequest(
                    s3Bucket,
                    "dap/images/" + id
            );
        }

        @Test
        public void createsAGetObjectPresignRequestWithCorrectParams() throws Exception {
            mockMvc.perform(get("/images/" + id + "/content"));
            verify(awsObjectsFactory).createGetObjectPresignRequest(
                    Duration.ofMinutes(10),
                    getObjectRequest
            );
        }

        @Test
        public void createAwsCredentialProviderWithCorrectParams() throws Exception {
            mockMvc.perform(get("/images/" + id + "/content"));
            verify(awsObjectsFactory).createAwsCredentialsProvider(
                    accessKey,
                    secretKey
            );
        }

        @Test
        public void createS3PresignerWithCorrectParams() throws Exception {
            mockMvc.perform(get("/images/" + id + "/content"));
            verify(awsObjectsFactory).createS3Presigner(
                    awsCredentialsProvider,
                    Region.US_EAST_2
            );
        }
    }
}
