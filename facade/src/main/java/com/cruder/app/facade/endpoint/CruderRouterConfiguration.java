package com.cruder.app.facade.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.cruder.app.facade.service.handler.CruderHandler;

@Configuration
public class CruderRouterConfiguration {
	
	@Bean
	public RouterFunction<ServerResponse> routes(CruderHandler cruderHandler) {
		return RouterFunctions.route()
				.POST("cruder/create", RequestPredicates.accept(MediaType.APPLICATION_JSON), cruderHandler::create)
				.POST("cruder/retrieve", RequestPredicates.accept(MediaType.APPLICATION_JSON), cruderHandler::retrieve)
				.build();
	}
}
