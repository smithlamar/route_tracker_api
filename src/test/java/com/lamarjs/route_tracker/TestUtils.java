package com.lamarjs.route_tracker;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class TestUtils {

	/**
	 * 
	 * @return A map of sample files from test resources directory.
	 * @throws IOException
	 */
	public static HashMap<String, HashMap<String, String>> loadSampleFiles() throws IOException {

		HashMap<String, HashMap<String, String>> sampleFiles = new HashMap<>();
		ClassLoader classLoader = TestUtils.class.getClassLoader();

		// Sample response bodies
		String routesJson = IOUtils.toString(classLoader.getResourceAsStream("sample_json_responses/routes.json"),
				Charsets.toCharset("UTF-8"));
		String stopsJson = IOUtils.toString(classLoader.getResourceAsStream("sample_json_responses/stops.json"),
				Charsets.toCharset("UTF-8"));
		String directionsJson = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/directions.json"), Charsets.toCharset("UTF-8"));
		String predictionsJson = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/predictions.json"), Charsets.toCharset("UTF-8"));
		String errorBadParamJson = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/error_bad_param.json"),
				Charsets.toCharset("UTF-8"));
		String errorInvalidKeyJson = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/error_invalid_key.json"),
				Charsets.toCharset("UTF-8"));
		String errorMissingParamJson = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/error_missing_param.json"),
				Charsets.toCharset("UTF-8"));
		String unwrappedRoutesJson = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/unwrapped_routes.json"),
				Charsets.toCharset("UTF-8"));

		// Sample requestURLs
		String routesUrl = IOUtils.toString(classLoader.getResourceAsStream("sample_request_urls/routes.txt"),
				Charsets.toCharset("UTF-8"));
		String stopsUrl = IOUtils.toString(classLoader.getResourceAsStream("sample_request_urls/routes.txt"),
				Charsets.toCharset("UTF-8"));
		String directionsUrl = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/directions.json"), Charsets.toCharset("UTF-8"));
		String predictionsUrl = IOUtils.toString(
				classLoader.getResourceAsStream("sample_json_responses/predictions.json"), Charsets.toCharset("UTF-8"));

		// Organize the sample files into the map
		sampleFiles.put("json", new HashMap<>());

		sampleFiles.get("json").put("routes", routesJson);
		sampleFiles.get("json").put("stops", stopsJson);
		sampleFiles.get("json").put("directions", directionsJson);
		sampleFiles.get("json").put("predictions", predictionsJson);
		sampleFiles.get("json").put("errorBadParam", errorBadParamJson);
		sampleFiles.get("json").put("errorMissingParam", errorMissingParamJson);
		sampleFiles.get("json").put("errorInvalidKey", errorInvalidKeyJson);
		sampleFiles.get("json").put("unwrappedRoutes", unwrappedRoutesJson);

		sampleFiles.put("urls", new HashMap<>());
		sampleFiles.get("urls").put("routes", routesUrl);
		sampleFiles.get("urls").put("stops", stopsUrl);
		sampleFiles.get("urls").put("directions", directionsUrl);
		sampleFiles.get("urls").put("predictions", predictionsUrl);

		return sampleFiles;
	}

}
