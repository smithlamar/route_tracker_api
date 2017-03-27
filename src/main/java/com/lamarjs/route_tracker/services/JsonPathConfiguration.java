package com.lamarjs.route_tracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

@org.springframework.context.annotation.Configuration
public class JsonPathConfiguration {

	@Bean
	@Autowired
	public Configuration configuration(ObjectMapper objectMapper) {
		Configuration config = Configuration.defaultConfiguration().jsonProvider(new JacksonJsonProvider(objectMapper))
				.mappingProvider(new JacksonMappingProvider(objectMapper));
		return config;

	}

}
