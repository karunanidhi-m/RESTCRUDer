package com.cruder.service.impl;

import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface CruderService {
	Mono<ServerResponse> create(String payload);
}
