package com.cruder.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.cruder.service.impl.CruderService;

import reactor.core.publisher.Mono;

@Component
public class ServiceHandler {
	@Autowired
	public CruderService cruderService;
	
	public Mono<ServerResponse> create(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(String.class)
							.flatMap(payload -> cruderService.create(payload));				
	}
	
	public Mono<ServerResponse> retrieve(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(String.class)
							.flatMap(payload -> cruderService.retrieve(payload));				
	}
}
