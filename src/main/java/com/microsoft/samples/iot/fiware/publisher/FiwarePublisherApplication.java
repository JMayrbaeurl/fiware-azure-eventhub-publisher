package com.microsoft.samples.iot.fiware.publisher;

import java.util.Collections;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class FiwarePublisherApplication {

	@Value("${fiware.broker.url:http://localhost:1026/v2/}")
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

	@Bean
	public Publisher createPublisher(EventHubProducerClient producer) {

		EventHubPublisher aPublisher = new EventHubPublisher();
		aPublisher.setEventHubClient(producer);

		return aPublisher;
	}

	@Bean
	public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
}
