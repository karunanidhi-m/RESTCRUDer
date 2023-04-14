package com.cruder.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.cruder.service.repo.CruderRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class CruderServiceImpl implements CruderService {
	static final Logger logger = LogManager.getLogger(CruderServiceImpl.class);
	static final String DATA_NOT_FOUND_MSG = "{\"msg\" : \"Data not found for %s : %s\"}";
	@Autowired
	private CruderRepo cruderRepo;
	
	@Override
	public Mono<ServerResponse> create(String payload) {
		logger.debug("Received Payload->" + payload);
		boolean status = cruderRepo.create(payload);
		if (status) {
			return ServerResponse.ok().
					header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue("{\"msg\":\"Created.\"}");
		}
		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue("{\"msg\":\"Could not create record.\"}");
	}

	@Override
	public Mono<ServerResponse> retrieve(String payload) {
		ObjectMapper om = new ObjectMapper();
		try {
			JsonNode jn = om.readTree(payload);
			String fieldName = jn.get("field").asText();
			String value = jn.get("value").asText();
			String jsonData = cruderRepo.retrieve(fieldName, value);

			if (StringUtils.isEmpty(jsonData)) {
				return ServerResponse.status(HttpStatus.OK).
						header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(String.format(DATA_NOT_FOUND_MSG, fieldName, value));
			}
			return ServerResponse.ok().
					header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(jsonData);
		} catch (JsonProcessingException excep) {
			return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).
					header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue("{\"msg\":\"Could not create record.\"}");
		}
	}

}
