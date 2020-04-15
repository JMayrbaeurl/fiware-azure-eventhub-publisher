package com.microsoft.samples.iot.fiware.publisher;

import java.util.Collections;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FiwarePublisherApplication {

	@Value("${fiware.broker.url:'http://localhost:1026/v2/'}")
	private String fiwareBrokerURL;

	@Value("${fiware.eh.connectionString}")
	private String eventHubConnectionString;

	@Value("${fiware.eh.hubname:fiware-notifications}")
	private String eventHubName;

	public static void main(String[] args) {
		SpringApplication.run(FiwarePublisherApplication.class, args);
	}

	@Bean
	public WebClient createWebClient() {

		WebClient client = WebClient.builder().baseUrl(this.fiwareBrokerURL)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultUriVariables(Collections.singletonMap("url", this.fiwareBrokerURL)).build();

		return client;
	}

	@Bean
	public EventHubProducerClient createEventHubClient() {
		
		// create a producer using the namespace connection string and event hub name
        EventHubProducerClient producer = new EventHubClientBuilder()
            .connectionString(this.eventHubConnectionString, this.eventHubName)
			.buildProducerClient();
			
		return producer;
	}
}
