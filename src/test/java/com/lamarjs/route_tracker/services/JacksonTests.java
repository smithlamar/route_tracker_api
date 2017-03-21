package com.lamarjs.route_tracker.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.lamarjs.route_tracker.TestUtils;
import com.lamarjs.route_tracker.models.BusLine;

public class JacksonTests {

	static Logger logger;
	static HashMap<String, HashMap<String, String>> sampleFiles;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		logger = LoggerFactory.getLogger(JacksonTests.class);
		sampleFiles = TestUtils.getSampleFiles();
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void given_unwrapped_routes_json_string_mapper_parses_correct_busline_objects()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<BusLine> busLines;
		String sampleJson = sampleFiles.get("json").get("unwrappedRoutes");
		logger.debug("[given_unwrapped_routes_json_string_mapper_parses_correct_busline_objects()] - sample json: "
				+ sampleJson);

		CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, BusLine.class);
		busLines = mapper.readValue(sampleJson, type);
		// TODO: Find out why BusLine fields aren't being initialized even though the objects are being created.
		logger.debug(
				"[given_unwrapped_routes_json_string_mapper_parses_correct_busline_objects()] - resulting BusLine at index 0: "
						+ busLines.get(0).toString());
		logger.debug(
				"[given_unwrapped_routes_json_string_mapper_parses_correct_busline_objects()] - busLines list size: "
						+ busLines.size());
	}

}
