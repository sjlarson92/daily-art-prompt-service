package com.dap.DailyArtPrompt.controller;

import com.dap.DailyArtPrompt.entity.Image;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class UserControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @ActiveProfiles("test")
    @AutoConfigureEmbeddedDatabase
    @Sql(scripts = "/scripts/insert-images.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class getUserImages {
        @Test
        public void returnsAListOfImages() {
            UUID userId = UUID.fromString("64A90ED2-F125-4003-9920-09D14344AA4A");
            System.out.println("integration test userId: " + userId);
            Image[] images = testRestTemplate.getForObject(
                    "/users/" + userId + "/images",
                    Image[].class
            );

            assertThat(images).hasSize(4);
        }
    }
}
