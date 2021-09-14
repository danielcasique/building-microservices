package com.dcasique.reservationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@RestController
public class MessageRestController {

	@Value("${message:}")
	private String msg;

	@RequestMapping("/message")
	String message() {
		return this.msg;
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
