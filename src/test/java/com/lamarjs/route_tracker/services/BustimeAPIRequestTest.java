package com.lamarjs.route_tracker.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import com.jayway.jsonpath.Configuration;
import com.lamarjs.route_tracker.TestUtils;
import com.lamarjs.route_tracker.exceptions.BusTimeErrorReceivedException;
import com.lamarjs.route_tracker.models.BusLine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BustimeAPIRequestTest extends junit.framework.TestSuite {

	static HashMap<String, HashMap<String, String>> sampleFiles;
	BusLine referenceBusLine;

	@Autowired
	Configuration jsonPathConfig;
	@Autowired
	BustimeAPIRequest request;

	@BeforeClass
	public static void onlyOnce() throws IOException {
		sampleFiles = TestUtils.loadSampleFiles();
	}

	@Before
	public void setUp() {
		request.setKey("test");
		referenceBusLine = new BusLine("1", "Bronzeville/Union Station", "#336633");
	}

	// TODO: Add more tests! Especially in the sections that have none.

	///////////////////////////////////
	// Send Request Method Tests
	///////////////////////////////////
	@Test
	public void send_method_populates_response_body_when_request_url_is_set()
			throws MalformedURLException, BusTimeErrorReceivedException {

		request.setRequestURL(new URL(sampleFiles.get("urls").get("routes")));
		request.send();
		assertNotNull(request.getResponseBody());
	}

	@Test
	public void request_routes_populates_response_body_from_real_key()
			throws RestClientException, MalformedURLException, URISyntaxException {

		request.setKey(System.getenv("BTRK"));

		try {
			request.requestRoutes();
		} catch (BusTimeErrorReceivedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(request.getResponseBody());
	}

	///////////////////////////////////
	// Build Request URL Method Tests
	///////////////////////////////////

	@Test
	public void build_routes_request_url_has_correct_format() throws MalformedURLException {
		request.buildRoutesRequestURL();

		assertEquals(sampleFiles.get("urls").get("routes"), request.getRequestURL().toString());
	}

	//////////////////////////////////
	// Request routes Response Tests
	//////////////////////////////////

	@Test
	public void request_routes_fills_array_of_buslines()
			throws RestClientException, MalformedURLException, BusTimeErrorReceivedException, URISyntaxException {
		request.setKey(System.getenv("BTRK"));
		log.debug("[request_routes_fills_array_of_buslines()] - key: {}", request.getKey());
		int actual = 0;

		List<BusLine> busLines = request.requestRoutes();
		actual = busLines.size();
		log.debug("[request_routes_fills_array_buslines()] - parsed buslines list size: {}", actual);
		assertTrue(actual > 0);
	}

	@Test
	public void request_routes_throws_bustime_error_received_on_invalid_key()
			throws RestClientException, MalformedURLException, URISyntaxException {
		String actual = "";
		String expected = "Invalid API access key supplied";
		try {
			request.requestRoutes(request.buildRoutesRequestURL().getRequestURL());
		} catch (BusTimeErrorReceivedException e) {
			actual = e.getMessage();
		}

		assertTrue(actual.equals(expected));
	}

	@Test
	public void get_bustime_error_returns_null_given_json_with_no_error()
			throws RestClientException, MalformedURLException, URISyntaxException {
		String actual = null;
		String expected = null;
		request.setResponseBody(sampleFiles.get("json").get("routes"));
		actual = request.getBustimeError(request.getResponseBody());

		assertTrue(actual == expected);
	}

	@Test
	public void get_bustime_error_returns_error_string_when_given_parsed_json_with_error()
			throws RestClientException, MalformedURLException, URISyntaxException {
		String actual = null;
		String expected = "Invalid API access key supplied";
		String sampleRawResponseEntity = sampleFiles.get("json").get("errorInvalidKey");
		Object parsedResponseBody = jsonPathConfig.jsonProvider().parse(sampleRawResponseEntity);
		actual = request.getBustimeError(parsedResponseBody);

		assertTrue(actual.equals(expected));
	}
}
