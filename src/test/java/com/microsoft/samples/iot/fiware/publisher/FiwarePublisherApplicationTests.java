package com.microsoft.samples.iot.fiware.publisher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class FiwarePublisherApplicationTests {

	@Autowired
	private WebClient webClient;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(this.webClient);
	}

}
