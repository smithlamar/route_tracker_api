package com.lamarjs.route_tracker.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;



/**
 *
 * @author Lamar J. Smith
 */
public class BustimeAPIRequest {

	// Base request components and parameter structure

	/**
	 * This is the base component of the CTA API's request URL. The key and any
	 * parameters that follow it are appended to the end of this string.
	 */
	public static final String BUSTIME_REQUEST_BASE = "http://ctabustracker.com/bustime/api/v2/";

	/**
	 * The API key, which can be stored as an environment variable on the
	 * server.
	 */
	public static final String API_KEY = "?key=";

	/**
	 * This is a parameter that can be added to the request to get back a JSON
	 * response instead of XML. This utility class has methods for handling the
	 * parsing of JSON responses into equivalent objects. XML parsing has not
	 * been implemented.
	 */
	public static final String F_JSON = "&format=json";

	/**
	 * The route parameter indicates a specific route for those requests that
	 * require one as input.
	 */
	public static final String RT = "&rt=";

	/**
	 *
	 */
	public static final String DIR = "&dir=";

	public static final String STPID = "&stpid=";

	public static final String TOP = "&top="; // Number of predictions to be
												// returned from
												// GET_BUS_PREDICTIONS.

	// Request types
	/**
	 * Returns a list of busroutes available.
	 */
	public static final String GET_BUS_ROUTES = "getroutes";

	/**
	 * Returns a list of directions for the specified route.
	 */
	public static final String GET_BUS_DIRECTIONS = "getdirections";

	/**
	 * Returns a list of stops for the specified route and direction
	 * combination.
	 */
	public static final String GET_BUS_STOPS = "getstops";

	/**
	 * Returns a set of ETAs for a specific stop along a bus route.
	 */
	public static final String GET_BUS_PREDICTIONS = "getpredictions";


	// Properties
	private URL requestURL; // The request URL.
	private String responseBody; // The response returned by the CTA API.
	private String key;

	// Constructor
	public BustimeAPIRequest() {
		// The key to be inserted into the request URL key parameter should be
		// set as an environment variable.
		key = System.getenv("BTRK");
		requestURL = null;
		responseBody = null;
	}

	
	/**
	 * Builds a well formated request url for the CTA API that returns a list of
	 * all available bus routes.
	 * 
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildGetRoutesRequest() throws MalformedURLException {
		// Build the request.
		requestURL = new URL(BUSTIME_REQUEST_BASE + GET_BUS_ROUTES + API_KEY + key + F_JSON);
		return this;
	}

	/**
	 * Builds a well formated request url for the CTA API. The constants in this
	 * class provide the proper format for each parameter. All that is required
	 * is to add the value after each constant. Ex: APIRequest(GET_BUS_STOPS,
	 * RT, X9, DIR, BusLine.SOUTH) will return a request URL for a list of stops along
	 * the South Bound X9 - Express Ashland bus.
	 * 
	 * @param requestType
	 * @param urlParameters
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildRequestURL(String requestType, String... urlParameters) throws MalformedURLException {
		requestURL = new URL((BUSTIME_REQUEST_BASE + requestType + API_KEY + key + Arrays.toString(urlParameters) + F_JSON)
				.replaceAll("\\[", "").replaceAll("]", "").replaceAll(",", "").replaceAll(" ", "%20"));
		return this;
	}

	/**
	 * Sends the last built requestURL. A buildRequestURL method should be called first to create a valid request. 
	 * @throws java.io.IOException
	 */
	public BustimeAPIRequest send() throws IOException {
		responseBody = IOUtils.toString(requestURL, "UTF-8");
		return this;
	}


	/**
	 * @return The last received response body.
	 */
	public String getResponse() {
		return responseBody;
	}
	/**
	 * @return The last request URL built.
	 */
	public URL getLastRequest() {
		return requestURL;
	}
}