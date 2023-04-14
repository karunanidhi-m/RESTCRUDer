package com.cruder.app.facade.service.invoker;

import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface CruderServiceInvoker {
	Mono<ServerResponse> create(String payload);
	Mono<ServerResponse> retrieve(String payload);
}
