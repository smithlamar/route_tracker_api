package com.lamarjs.route_tracker.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamarjs.route_tracker.models.BusLine;
import com.lamarjs.route_tracker.models.BusLine.Direction;
import com.lamarjs.route_tracker.models.BusLine.Stop;

//TODO: Add tests for all url building methods and json parsing methods.

/**
 *
 * @author Lamar J. Smith
 */
public class BustimeAPIRequest {

	// Base request components

	/**
	 * This is the base component of the CTA API's request URL. The key and any
	 * parameters that follow it are appended to the end of this string.
	 */
	public static final String BUSTIME_REQUEST_BASE = "http://ctabustracker.com/bustime/api/v2/";

	// Request Parameters

	// TODO: Implement most of these constants as an inner enum class.
	public enum Parameter {
		KEY("?key="), ROUTE("&rt="), DIRECTION("&dir="), STOPID("&stpid="), LIMIT("&top=");

		private final String paramFormat;

		private Parameter(String param) {
			this.paramFormat = param;
		}

		public String format() {
			return paramFormat;
		}
	}

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
	 * The direction parameter indicates a specific direction traveled by a
	 * specified route.
	 */
	public static final String DIR = "&dir=";

	/**
	 * The stop id parameter indicates a single stop along a specific direction
	 * traveled by a specified route.
	 */
	public static final String STPID = "&stpid=";

	/**
	 * The top parameter indicates the total number of predictions to be
	 * returned by a "getpredictions" request.
	 */
	public static final String TOP = "&top=";

	// Direction Names
	public static final String NORTH = "Northbound";
	public static final String SOUTH = "Southbound";
	public static final String EAST = "Eastbound";
	public static final String WEST = "Westbound";

	// Request types
	public enum RequestType {
		ROUTES("getroutes"), DIRECTIONS("getdirections"), STOPS("getstops"), PREDICTIONS("getpredictions");

		private final String format;

		private RequestType(String type) {
			this.format = type;
		}

		public String format() {
			return format;
		}
	}

	/**
	 * Returns a list of BusLines available.
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

	// Constructors

	/**
	 * Creates a BusTimeAPIRequest object. The supplied builder methods
	 * construct legal API request URLs for the various end-points that the CTA
	 * has made available. Once a requestURL has been created, the
	 * {@link send()} method can be called to send the requestURL to the CTA
	 * API.
	 */
	public BustimeAPIRequest() {
		// The key to be inserted into the request URL key parameter should be
		// set as an environment variable.
		key = System.getenv("BTRK");
	}

	/**
	 * Convenience method primarily for testing. Allows a key to be specified as
	 * an argument instead of as an environment variable.
	 * 
	 * @param key
	 *            Required to make CTA API calls. Can optionally be provided as
	 *            an environment variable using the no-arg constructor
	 *            {@link BustimeAPIRequest()}
	 * @see BustimeAPIRequest()
	 */
	public BustimeAPIRequest(String key) {
		this.key = key;
	}

	/**
	 * Builds a well formated requestURL for the CTA API that returns a list of
	 * all available bus routes.
	 * 
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildGetRoutesRequest() throws MalformedURLException {
		// Build the request
		requestURL = new URL(
				BUSTIME_REQUEST_BASE + RequestType.ROUTES.format + Parameter.KEY.paramFormat + key + F_JSON);
		return this;
	}

	/**
	 * Builds a request url for the CTA API. The constants in this class provide
	 * the proper format for the parameters and other elements that form the
	 * final URL. All that is required is to add the value after each constant.
	 * Ex: APIRequest(GET_BUS_STOPS, RT, "x9", DIR, BusLine.SOUTH) will return a
	 * request URL for a list of stops along the South Bound X9 - Express
	 * Ashland bus.
	 * 
	 * @param requestType
	 * @param urlParameters
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildRequestURL(String requestType, String... urlParameters) throws MalformedURLException {
		requestURL = new URL(
				(BUSTIME_REQUEST_BASE + requestType + API_KEY + key + Arrays.toString(urlParameters) + F_JSON)
						.replaceAll("\\[", "").replaceAll("]", "").replaceAll(",", "").replaceAll(", ", ""));
		return this;
	}

	// TODO: This version of the buildRequestURL() method should accept a map of
	// enum objects that represent the possible parameters and use only the ones
	// that have values assigned.

	/**
	 * Builds a well formated request url for the CTA API. The constants in this
	 * class provide the proper format for the parameters and other elements
	 * that form the final URL. All that is required is to add the value after
	 * each constant. Ex: APIRequest(GET_BUS_STOPS, RT, "x9", DIR,
	 * BusLine.SOUTH) will return a request URL for a list of stops along the
	 * South Bound X9 - Express Ashland bus.
	 * 
	 * @param requestType
	 * @param urlParameters
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildRequestURL(RequestType requestType, LinkedHashMap<Parameter, String> urlParameters,
			Boolean returnJson) throws MalformedURLException {

		String json = returnJson ? F_JSON : "";
		StringBuilder paramsBuilder = new StringBuilder(json);

		for (Parameter param : urlParameters.keySet()) {
			paramsBuilder.append(param.paramFormat).append(urlParameters.get(param));
		}
		requestURL = new URL(BUSTIME_REQUEST_BASE + requestType.format + paramsBuilder.toString());

		return this;
	}

	/**
	 * Sends the last built requestURL. {@link buildRequestURL()},
	 * {@link buildGetRoutesRequest()}, or {@link setRequestURL()} should be
	 * called first to create a valid request.
	 * 
	 * @throws java.io.IOException
	 */
	public BustimeAPIRequest send() throws IOException {
		responseBody = IOUtils.toString(requestURL, "UTF-8");
		return this;
	}

	/**
	 * Requests a list of all operating bus lines.
	 * 
	 * @return A list of BusLine objects that represents all routes serviced by
	 *         the CTA.
	 * @throws IOException
	 * 
	 * @throws MalformedURLException
	 */
	public ArrayList<BusLine> requestBusLines() throws MalformedURLException, IOException {

		// Build the get routes request
		buildRequestURL(RequestType.ROUTES.format);
		send();

		// TODO: Add tests to confirm that request is properly formatted, sent,
		// and raw responsebody looks like it should.

		// Parse the response into a directions list.
		ArrayList<BusLine> busLines = new ArrayList<>();

		Iterator<JsonNode> busLinesIterator = requestBusLinesJsonIterator(responseBody);

		while (busLinesIterator.hasNext()) {
			JsonNode node = busLinesIterator.next();
			busLines.add(new BusLine(node.get("rt").asText(), node.get("rtnm").asText(), node.get("rtclr").asText()));

		}

		return busLines;
	}

	private Iterator<JsonNode> requestBusLinesJsonIterator(String busLinesJsonString)
			throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode busLinesNode = mapper.readTree(busLinesJsonString).get("bustime-response").get("routes");
		return busLinesNode.elements(); // FIXME: Getting null pointer here on
										// live request of getBusLines. Likely
										// due to error in json parsing with the
										// mapper or some kind or
	}

	/**
	 * Requests a list of directions along the from the CTA API for the given
	 * BusLine
	 * 
	 * @param busline
	 *            The BusLine object that the directions request should be
	 *            handled for
	 * @return A list of Direction objects i.e. ["Northbound", "Southbound"]
	 *         that the provided BusLine object services.
	 * @throws IOException
	 * 
	 * @throws MalformedURLException
	 */
	public ArrayList<Direction> requestDirections(String routeCode) throws MalformedURLException, IOException {

		// Build the directions request
		buildRequestURL(GET_BUS_DIRECTIONS, RT + routeCode);
		send();

		// Parse the response into a directions list.
		ArrayList<Direction> directions = new ArrayList<>();

		Iterator<JsonNode> directionsIterator = requestDirectionsJsonIterator(responseBody);
		Direction temp = new BusLine().new Direction();
		while (directionsIterator.hasNext()) {
			temp.setDir(directionsIterator.next().get("dir").asText());
			directions.add(temp);
		}

		return directions;
	}

	public Iterator<JsonNode> requestDirectionsJsonIterator(String directionsJsonString)
			throws JsonProcessingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode directionsNode = mapper.readTree(directionsJsonString).get("bustime-response").get("directions");
		return directionsNode.elements();
	}

	/**
	 * Requests a list of stops along the given route code and Direction object
	 * from the CTA API.
	 *
	 * @param rt
	 *            The route code for the route that the direction is associated
	 *            with
	 * @param direction
	 *            The direction along the route that stops should be requested
	 *            for.
	 *
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public ArrayList<Stop> requestStops(String rt, String direction) throws MalformedURLException, IOException {

		// Build the stops request
		buildRequestURL(GET_BUS_STOPS, RT + rt, BustimeAPIRequest.DIR + direction);
		send();

		// Parse the response into a stops list.
		ArrayList<Stop> stops = new ArrayList<Stop>();

		Iterator<JsonNode> stopsIterator = requestStopsJsonIterator(responseBody);
		while (stopsIterator.hasNext()) {
			JsonNode element = stopsIterator.next();

			Stop temp = new BusLine().new Stop(element.get("stpid").asInt(), element.get("stpnm").asText(),
					element.get("lat").asDouble(), element.get("lon").asDouble());

			stops.add(temp);
		}

		return stops;
	}

	public Iterator<JsonNode> requestStopsJsonIterator(String stopsJsonString)
			throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		JsonNode stopsNode = mapper.readTree(stopsJsonString).get("bustime-response").get("stops");
		return stopsNode.elements();
	}

	/**
	 * @return The last received response body.
	 */
	public String getResponseBody() {
		return responseBody;
	}

	/**
	 * @return The last request URL built.
	 */
	public URL getRequestURL() {
		return requestURL;
	}

	/**
	 * 
	 * @param requestURL
	 *            The URL request that can be sent by this object's
	 *            {@link send()} method.
	 * @see send()
	 */
	public BustimeAPIRequest setRequestURL(URL requestURL) {
		this.requestURL = requestURL;
		return this;
	}
}