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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;

import com.lamarjs.route_tracker.TestUtils;
import com.lamarjs.route_tracker.exceptions.BusTimeErrorReceivedException;
import com.lamarjs.route_tracker.models.BusLine;

public class BustimeAPIRequestTest extends junit.framework.TestSuite {
	static Logger logger;
	BustimeAPIRequest request;
	static HashMap<String, HashMap<String, String>> sampleFiles;
	BusLine referenceBusLine;

	@BeforeClass
	public static void onlyOnce() throws IOException {
		logger = LoggerFactory.getLogger(BustimeAPIRequestTest.class);
		sampleFiles = TestUtils.getSampleFiles();
	}

	@Before
	public void setUp() {
		request = new BustimeAPIRequest();
		request.setKey("test");
		request.setTemplateBuilder(new RestTemplateBuilder());
		referenceBusLine = new BusLine("1", "Bronzeville/Union Station", "#336633");
	}

	// TODO: Add more tests! Especially in the sections that have non.

	///////////////////////////////////
	// Send Request Method Tests
	///////////////////////////////////
	@Test
	public void send_method_populates_response_body_when_request_url_is_set() throws MalformedURLException {
		request.setRequestURL(new URL(sampleFiles.get("urls").get("routes")));
		try {
			request.send();
		} catch (BusTimeErrorReceivedException e) {

			logger.debug("[send_method_populates_response_body_when_request_url_is_set()] - bustime error msg: "
					+ e.getMessage());
		}
		assertNotNull(request.getResponseBody());
	}

	@Test
	public void request_routes_populates_response_body_from_real_key()
			throws RestClientException, MalformedURLException, BusTimeErrorReceivedException, URISyntaxException {
		request.setKey(System.getenv("BTRK"));
		request.requestRoutes();
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
		int actual = 0;
		List<BusLine> busLines = request.requestRoutes(request.buildRoutesRequestURL().getRequestURL());
		actual = busLines.size(); // TODO getting null pointer here.
		logger.debug("[request_routes_fills_array_buslines()] - parsed buslines list size: " + actual);
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
}
