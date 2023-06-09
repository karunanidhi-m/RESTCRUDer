package com.cruder.app.facade.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.cruder.app.facade.service.invoker.CruderServiceInvoker;
import com.cruder.app.facade.validator.RequestValidator;

import reactor.core.publisher.Mono;

@Component
public class CruderHandler {

	@Autowired
	RequestValidator requestValidator;
	
	@Autowired
	CruderServiceInvoker cruderServiceInvoker;
	
	public Mono<ServerResponse> create(ServerRequest serverRequest) {
		
		return serverRequest.bodyToMono(String.class)
							.flatMap(payload -> Mono.just(requestValidator.validate(payload)))
							.flatMap(validationResult -> validationResult.getLeft() ? 
									cruderServiceInvoker.create(validationResult.getMiddle()) : validationResult.getRight());
	}
	
	public Mono<ServerResponse> retrieve(ServerRequest serverRequest) {
		
		return serverRequest.bodyToMono(String.class)
							.flatMap(payload -> Mono.just(requestValidator.validate(payload)))
							.flatMap(validationResult -> validationResult.getLeft() ? 
									cruderServiceInvoker.retrieve(validationResult.getMiddle()) : validationResult.getRight());
	}
}
