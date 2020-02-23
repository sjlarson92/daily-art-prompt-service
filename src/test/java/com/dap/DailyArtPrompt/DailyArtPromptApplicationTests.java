package com.dap.DailyArtPrompt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase // this replaces the database with the embedded/light/testable version
class DailyArtPromptApplicationTests {

	@Test
	void contextLoads() {
	}

}
