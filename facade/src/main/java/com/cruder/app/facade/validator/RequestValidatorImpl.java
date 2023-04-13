package com.cruder.app.facade.validator;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Mono;

@Component
public class RequestValidatorImpl implements RequestValidator {
	static final String INTERNAL_SERVER_ERROR = "{\"msg\" : \"Internal Server Error\"}";
	static final Logger logger = LogManager.getLogger(RequestValidatorImpl.class);
	
	@Override
	public Triple<Boolean, String, Mono<ServerResponse>> validate(String payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
				try {
					logger.debug("Validating request payload", payload);
					objectMapper.readTree(payload);
					logger.info("Validation completed.");
					return Triple.of(Boolean.TRUE, payload, Mono.empty());
				}
				catch(JsonProcessingException jpe) {
					return Triple.of(Boolean.FALSE, payload, ServerResponse.status(HttpStatus.BAD_REQUEST)
							.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
							.bodyValue(ValidatorErrorMapper.invalidJson()));
				}
		}
		catch(Exception excep) {
			return Triple.of(Boolean.FALSE, payload, ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(INTERNAL_SERVER_ERROR));
		}
	}

	class ValidatorErrorMapper {
		static final String INVALID_JSON = "Invalid request Json";
		static String invalidJson() throws JsonProcessingException {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode msgNode = objectMapper.createObjectNode();
			return objectMapper.writeValueAsString(msgNode.set("msg", objectMapper.getNodeFactory().textNode(INVALID_JSON)));
		}
	}
}
