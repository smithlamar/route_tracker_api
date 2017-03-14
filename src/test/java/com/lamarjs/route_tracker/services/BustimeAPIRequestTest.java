package com.lamarjs.route_tracker.services;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lamarjs.route_tracker.services.BustimeAPIRequest.RequestType;

public class BustimeAPIRequestTest extends junit.framework.TestSuite {

	BustimeAPIRequest request;
	static HashMap<String, HashMap<String, String>> sampleFiles;

	@BeforeClass
	public static void onlyOnce() throws IOException {
		sampleFiles = getSampleFiles();
	}

	@Before
	public void setUp() throws IOException {
		request = new BustimeAPIRequest();
	}

	// TODO: Add real tests! (Especially now that the classes have appropriate
	// seams.)

	// Build request URL

	@Test
	public void routes_build_request_url_using_enum_has_correct_format() {
		request.setKey("test");

		try {
			request.buildRequestURL(RequestType.ROUTES.format());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println(request.getRequestURL());
		assertEquals(sampleFiles.get("urls").get("routes"), request.getRequestURL().toString());
	}

	/**
	 * 
	 * @return A map of sample files from test resources directory.
	 * @throws IOException
	 */
	public static HashMap<String, HashMap<String, String>> getSampleFiles() throws IOException {
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
