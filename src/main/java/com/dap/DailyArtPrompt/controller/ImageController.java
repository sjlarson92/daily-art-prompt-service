package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import com.dap.DailyArtPrompt.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageRepository imageRepository;

    @GetMapping("/images/{id}")
    public Image getImage(@PathVariable UUID id) {
        log.info("Fetching image with id " + id);
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No event found by given id: " + id));
    }

//    @GetMapping("/images/{id}/content")
//    public RedirectView getImageFromS3Bucket(@PathVariable UUID id) {
//        log.info("Fetching image with name: " + id);
//
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket("daily-art-prompt-vmkugdgwiz")
//                .key("dap/images/" + id)
//                .build();
//
//        // Create a GetObjectPresignRequest to specify the signature duration
//        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//        ProfileCredentialsProvider pcp = ProfileCredentialsProvider.builder()
//                .build();
//
//        S3Presigner s3Presigner = S3Presigner.builder()
//                .credentialsProvider(pcp)
//                .region(Region.US_EAST_2)
//                .build();
//
//        // Generate the presigned request
//        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
//
//        s3Presigner.close();
//
//        String url = presignedGetObjectRequest.url().toString();
//        return new RedirectView(url);
//    }
}
