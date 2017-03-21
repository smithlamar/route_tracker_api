package com.lamarjs.route_tracker.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lamarjs.route_tracker.exceptions.BusTimeErrorReceivedException;
import com.lamarjs.route_tracker.models.BusLine;

public class BustimeAPIRequestTest extends junit.framework.TestSuite {
	Logger logger;
	BustimeAPIRequest request;
	static HashMap<String, HashMap<String, String>> sampleFiles;
	BusLine referenceBusLine;

	@BeforeClass
	public static void onlyOnce() throws IOException {
		LoggerFactory.getLogger(BustimeAPIRequestTest.class);
		sampleFiles = getSampleFiles();
	}

	@Before
	public void setUp() throws IOException {
		request = new BustimeAPIRequest();
		request.setKey("test");
		referenceBusLine = new BusLine("1", "Bronzeville/Union Station", "#336633");
	}

	// TODO: Add real tests! (Especially now that the classes have appropriate
	// seams.)

	///////////////////////////////////
	// Send Request Method Tests
	///////////////////////////////////
	@Test
	public void send_method_populates_response_body_when_request_url_is_set() throws IOException {
		request.setRequestURL(new URL(sampleFiles.get("urls").get("routes")));
		request.send();
		assertNotNull(request.getResponseBody());
	}

	@Test
	public void get_routes_request_populates_response_body_from_real_key()
			throws IOException, BusTimeErrorReceivedException {
		request.setKey(System.getenv("BTRK"));
		request.requestRoutes();
		assertNotNull(request.getResponseBody());
	}

	///////////////////////////////////
	// Build Request URL Method Tests
	///////////////////////////////////

	///////////////////////////////
	// Build Routes Request Tests
	///////////////////////////////

	@Test
	public void build_routes_request_url_has_correct_format() throws MalformedURLException {
		request.buildRoutesRequestURL();

		assertEquals(sampleFiles.get("urls").get("routes"), request.getRequestURL().toString());
	}

	///////////////////////////////
	// Parse Routes Response Tests
	///////////////////////////////

	// @Test
	// public void get_request_routes_iterator_returns_correct_element()
	// throws JsonProcessingException, IOException,
	// BusTimeErrorReceivedException {
	// String expected = "Bronzeville/Union Station";
	// String actual;
	//
	// Iterator<JsonNode> iterator =
	// request.getRequestRoutesIterator(sampleFiles.get("json").get("routes"));
	// actual = iterator.next().get("rtnm").asText();
	//
	// assertEquals(expected, actual);
	// }

	@Test
	public void parse_request_routes_response_returns_busline_with_correct_route_code()
			throws JsonProcessingException, IOException, BusTimeErrorReceivedException {
		BusLine expected = referenceBusLine;
		BusLine actual = request.parseRequestRoutesResponse(sampleFiles.get("json").get("routes")).get(0);
		assertEquals(expected.getRouteCode(), actual.getRouteCode());
	}

	@Test
	public void parse_request_routes_response_returns_busline_with_correct_route_name()
			throws JsonProcessingException, IOException, BusTimeErrorReceivedException {
		BusLine expected = referenceBusLine;
		BusLine actual = request.parseRequestRoutesResponse(sampleFiles.get("json").get("routes")).get(0);

		assertEquals(expected.getRouteName(), actual.getRouteName());
	}

	@Test
	public void parse_request_routes_response_returns_busline_with_correct_route_color()
			throws JsonProcessingException, IOException, BusTimeErrorReceivedException {
		BusLine expected = referenceBusLine;
		BusLine actual = request.parseRequestRoutesResponse(sampleFiles.get("json").get("routes")).get(0);

		logger.debug("parse_request_routes_response_returns_busline_with_correct_route_color() actual: "
				+ actual.getRouteColor());
		assertEquals(expected.getRouteColor(), actual.getRouteColor());
	}

	@Test
	public void parse_request_routes_response_returns_busline_with_null_directions_property()
			throws JsonProcessingException, IOException, BusTimeErrorReceivedException {
		BusLine expected = referenceBusLine;
		BusLine actual = request.parseRequestRoutesResponse(sampleFiles.get("json").get("routes")).get(0);

		logger.debug("parse_request_routes_response_returns_busline_with_null_directions_property() actual: " + actual);
		assertEquals(expected.getDirections(), actual.getDirections());
	}

	/**
	 * 
	 * @return A map of sample files from test resources directory.
	 * @throws IOException
	 */
	private static HashMap<String, HashMap<String, String>> getSampleFiles() throws IOException {
		HashMap<String, HashMap<String, String>> sampleFiles = new HashMap<>();
		ClassLoader classLoader = BustimeAPIRequestTest.class.getClassLoader();
		String routesJson = IOUtils.toString(classLoader.getResourceAsStream("sample_json_responses/routes.json"),
				Charsets.toCharset("UTF-8"));
		String stopsJson = IOUtils.toString(classLoader.getResourceAsStream("sample_json_responses/stops.json"),
				Charsets.toCharset("UTF-8"));
		String routesUrl = IOUtils.toString(classLoader.getResourceAsStream("sample_request_urls/routes.txt"),
				Charsets.toCharset("UTF-8"));
		String stopsUrl = IOUtils.toString(classLoader.getResourceAsStream("sample_request_urls/routes.txt"),
				Charsets.toCharset("UTF-8"));

		sampleFiles.put("json", new HashMap<>());
		sampleFiles.get("json").put("routes", routesJson);
		sampleFiles.get("json").put("stops", stopsJson);

		sampleFiles.put("urls", new HashMap<>());
		sampleFiles.get("urls").put("routes", routesUrl);
		sampleFiles.get("urls").put("stops", stopsUrl);

		return sampleFiles;

	}
}
