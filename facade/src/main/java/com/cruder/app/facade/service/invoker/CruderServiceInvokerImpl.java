package com.cruder.app.facade.service.invoker;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Service
public class CruderServiceInvokerImpl implements CruderServiceInvoker {

	@Value("${cruder.services.backendService.port}")
	public String port;

	@Value("${cruder.services.backendService.url}")
	public String url;

	public static final String SERVICE_URL = "http://localhost:%s/%s";

	static final String INTERNAL_SERVER_ERROR = "{\"msg\" : \"Internal Server Error\"}";
	static final Logger logger = LogManager.getLogger(CruderServiceInvokerImpl.class);
	WebClient client;

	@PostConstruct
	public void init() {
		client = WebClient.builder().baseUrl(String.format(SERVICE_URL, port, url))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Override
	public Mono<ServerResponse> create(String payload) {
		logger.trace("Calling backend service..");
		return client.post()
					 .uri("createService")
					 .bodyValue(payload)
					 .retrieve()
					 .bodyToMono(String.class)
					 .flatMap(response -> ServerResponse.ok()
							 							.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
							 							.bodyValue((response)));
	}

	@Override
	public Mono<ServerResponse> retrieve(String payload) {
		logger.trace("Calling backend service..");
		return client.post()
				.uri("retrieveService")
				.bodyValue(payload)
				.retrieve()
				.bodyToMono(String.class)
				.flatMap(response -> ServerResponse.ok()
													.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
													.bodyValue((response)));

	}

}
