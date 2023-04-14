package com.cruder.service.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.cruder.service.handler.ServiceHandler;

@Configuration
public class ServiceEndpointConfiguration {
	@Autowired
	ServiceHandler serviceHandlerConfiguration;
	
	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route()
				.POST("cruder/services/v1/createService", RequestPredicates.accept(MediaType.APPLICATION_JSON), serviceHandlerConfiguration::create)
				.POST("cruder/services/v1/retrieveService", RequestPredicates.accept(MediaType.APPLICATION_JSON), serviceHandlerConfiguration::retrieve)
				.build();
	}
}
