package com.cruder.app.facade.validator;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface RequestValidator {
	Triple<Boolean, String, Mono<ServerResponse>> validate(String payload);
}
