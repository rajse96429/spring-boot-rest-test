package com.spring.boot.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringBootRestTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestTestApplication.class, args);
	}
	
	@Bean
	public RestTemplate createRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public AsyncRestTemplate createAsyncRestTemplate() {
		return new AsyncRestTemplate();
	}
}
