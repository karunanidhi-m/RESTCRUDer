package com.cruder.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class CruderServiceImpl implements CruderService {
	static final Logger logger = LogManager.getLogger(CruderServiceImpl.class);
	
	@Override
	public Mono<ServerResponse> create(String payload) {
		logger.info("Received Payload->" + payload);
		return ServerResponse.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue("{\"msg\":\"ok\"}");
	}

}
