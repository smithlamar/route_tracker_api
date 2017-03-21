package com.lamarjs.route_tracker.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamarjs.route_tracker.exceptions.BusTimeErrorReceivedException;
import com.lamarjs.route_tracker.models.BusLine;
import com.lamarjs.route_tracker.models.BusLine.Direction;
import com.lamarjs.route_tracker.models.BusLine.Stop;

/**
 * This class represents a request to (and response from) the CTA Bustime
 * (BusTracker) API. It also provides the methods needed to build then send the
 * requests as well as parse the json response's returned by them into the
 * associated objets when provided. The enums {@link RequestType} and
 * {@link Parameter} represent the legal components of any valid request to the
 * Bustime API.
 * 
 * @author Lamar J. Smith
 */
@Service
public class BustimeAPIRequest {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	// Base request components
	/**
	 * This is the base component of the CTA API's request URL. The key and any
	 * parameters that follow it are appended to the end of this string.
	 */
	public static final String BUSTIME_REQUEST_BASE = "http://ctabustracker.com/bustime/api/v2/";

	/**
	 * The API key, which can be stored as an environment variable on the
	 * server, or set explicitly.
	 */
	public static final String API_KEY = "?key=";

	/**
	 * This is a parameter that can be added to the request to get back a JSON
	 * response instead of XML. This utility class has methods for handling the
	 * parsing of JSON responses into equivalent objects. This class does not
	 * implement any helper methods for parsing the XML version of the responses
	 * so it is generally a good idea to include this parameter when the option
	 * is given.
	 */
	public static final String F_JSON = "&format=json";

	// RequestURL Parameter Formats
	public enum Parameter {
		/**
		 * The route parameter indicates a specific route for those requests
		 * that require one as input.
		 */
		ROUTE("&rt="),

		/**
		 * The direction parameter indicates a specific direction traveled by a
		 * specified route.
		 */
		DIRECTION("&dir="),

		/**
		 * The stop id parameter indicates a single stop along a specific
		 * direction traveled by a specified route.
		 */
		STOPID("&stpid="),

		/**
		 * The limit parameter indicates the total number of predictions to be
		 * returned by a "predictions" request.
		 */
		LIMIT("&top=");

		private final String Format;

		private Parameter(String param) {
			this.Format = param;
		}

		public String format() {
			return Format;
		}
	}

	// Request types
	public enum RequestType {
		/**
		 * Returns a list of BusLines available.
		 */
		ROUTES("getroutes"),

		/**
		 * Returns a list of directions for the specified route.
		 */
		DIRECTIONS("getdirections"),

		/**
		 * Returns a list of stops for the specified route and direction
		 * combination.
		 */
		STOPS("getstops"),

		/**
		 * Returns a set of ETAs for a specific stop along a bus route.
		 */
		PREDICTIONS("getpredictions");

		private final String format;

		private RequestType(String type) {
			this.format = type;
		}

		public String format() {
			return format;
		}
	}

	// Direction Names
	public static final String NORTH = "Northbound";
	public static final String SOUTH = "Southbound";
	public static final String EAST = "Eastbound";
	public static final String WEST = "Westbound";

	// Properties
	private URL requestURL; // The request URL.
	private String responseBody; // The response returned by the CTA API.
	private String key; // The API key component of a request that can be set as
						// an environment variable or explicitly set.

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
	 */
	public BustimeAPIRequest(String key) {
		this.key = key;
	}

	/**
	 * Convenience method primarily for testing. Allows a requestURL to be
	 * specified as an argument instead of building it with the helper methods
	 * after creation of the instance.
	 * 
	 * @param requestURL
	 *            The request url for the CTA API. The send() method has to be
	 *            called on this instance to actually pass the request and
	 *            retrieve the data for the responseBody.
	 *
	 */
	public BustimeAPIRequest(URL requestURL) {
		this.requestURL = requestURL;
	}

	/**
	 * Builds a well formated request url for the CTA API. The enums:
	 * {@link Parameter} and {@link RequestType} in this class provide the
	 * proper request name and format for the parameters that make up the final
	 * URL.
	 * 
	 * @param requestType
	 *            {@link Parameter} representing the request type to made.
	 * @param urlParameters
	 *            A map of <{@link Parameter}, {@linkString}> pairs that each
	 *            represent a parameter and value to be included in the
	 *            requestURL.
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildRequestURL(RequestType requestType, Map<Parameter, String> urlParameters,
			Boolean returnJson) throws MalformedURLException {

		StringBuilder paramsBuilder = new StringBuilder();
		for (Parameter param : urlParameters.keySet()) {
			paramsBuilder.append(param.Format).append(urlParameters.get(param));
		}
		paramsBuilder.append(returnJson ? F_JSON : "");

		StringBuilder requestBuilder = new StringBuilder(BUSTIME_REQUEST_BASE).append(requestType.format)
				.append(API_KEY).append(key).append(paramsBuilder.toString());
		requestURL = new URL(requestBuilder.toString());
		return this;
	}

	/**
	 * Builds a well formated request url for the CTA API. The enums:
	 * {@link Parameter} and {@link RequestType} in this class provide the
	 * proper request name and format for the parameters that make up the final
	 * URL. <br>
	 * <br>
	 * <b>Ex:</b> buildRequestURL(Request.STOPS, Parameter.ROUTE + "x9" +
	 * Parameter.DIRECTION + BusLine.SOUTH) will return a request URL for a list
	 * of stops along the South Bound X9 - Express Ashland bus.
	 * 
	 * @param requestType
	 * @param urlParameters
	 * @param returnJson
	 *            Requests response as json
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildRequestURL(RequestType requestType, String urlParameters, Boolean returnJson)
			throws MalformedURLException {

		StringBuilder requestBuilder = new StringBuilder(BUSTIME_REQUEST_BASE).append(requestType.format)
				.append(API_KEY).append(key).append(urlParameters).append(returnJson ? F_JSON : "");
		requestURL = new URL(requestBuilder.toString());
		return this;
	}

	/**
	 * Convenience method similar to
	 * {@link BustimeAPIRequest#buildRequestURL(RequestType, String, Boolean)}
	 * but with Json return type enabled by default.
	 * 
	 * @see BustimeAPIRequest#buildRequestURL(RequestType, String, Boolean)
	 * @param requestType
	 * @param urlParameters
	 * @return
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildRequestURL(RequestType requestType, String urlParameters)
			throws MalformedURLException {
		return this.buildRequestURL(requestType, urlParameters, true);
	}

	/**
	 * Convenience method to build a valid Routes ("getRoutes") requestURL
	 * 
	 * @param requestType
	 * @param urlParameters
	 * @throws MalformedURLException
	 */
	public BustimeAPIRequest buildRoutesRequestURL() throws MalformedURLException {
		return buildRequestURL(RequestType.ROUTES, "", true);
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
	 * Requests a list of all operating bus lines (routes).
	 * 
	 * @return A list of BusLine objects that represents all routes serviced by
	 *         the CTA. Further initialization is required for each BusLine
	 *         object by calling each instances initialize() method. method.
	 * @throws IOException
	 * 
	 * @throws MalformedURLException
	 * @throws BusTimeErrorReceivedException
	 *             if the response from the CTA includes an error message.
	 */
	public List<BusLine> requestRoutes() throws MalformedURLException, IOException, BusTimeErrorReceivedException {

		// Send the get routes request
		buildRoutesRequestURL().send();

		// Parse the response into a Map of BusLine objects using their route
		// codes as keys.
		List<BusLine> busLines = parseRequestRoutesResponse(responseBody);

		return busLines;
	}

	public List<BusLine> parseRequestRoutesResponse(String response)
			throws JsonProcessingException, IOException, BusTimeErrorReceivedException {

		// Parse the response into a directions list.
		List<BusLine> busLines = new ArrayList<BusLine>();

		String routesNode = unwrapRequestRoutesResponse(response);

		TypeReference<List<BusLine>> typeRef = new TypeReference<List<BusLine>>() {};
		busLines = new ObjectMapper().readValue(routesNode, typeRef);
		
		logger.debug("[parseRequestRoutesResponse(String)] - resulting BusLine at index 0: " + busLines.get(0));
		return busLines;
	}
	// TODO: Replace the below commented method with
	// getRequestRoutesString(String).
	// public Iterator<JsonNode> getRequestRoutesIterator(String
	// routesJsonString)
	// throws JsonProcessingException, IOException,
	// BusTimeErrorReceivedException {
	// ObjectMapper mapper = new ObjectMapper();
	// JsonNode busLinesNode = mapper.readTree(routesJsonString);
	//
	// if (busLinesNode.has("error")) {
	// // TODO: Write tests for me!
	// throw new
	// BusTimeErrorReceivedException(busLinesNode.get("msg").asText());
	// }
	//
	// busLinesNode = busLinesNode.get("bustime-response").get("routes");
	// return busLinesNode.elements();
	// }

	public String unwrapRequestRoutesResponse(String response)
			throws JsonProcessingException, IOException, BusTimeErrorReceivedException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode busLinesNode = mapper.readTree(response);

		if (busLinesNode.has("error")) {
			// TODO: Write tests for me!
			throw new BusTimeErrorReceivedException(busLinesNode.get("msg").asText());
		}

		busLinesNode = busLinesNode.get("bustime-response").get("routes");
		String routesJson = mapper.writeValueAsString(busLinesNode);

		return routesJson;
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
		buildRequestURL(RequestType.DIRECTIONS, Parameter.ROUTE + routeCode);
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
		StringBuilder paramsBuilder = new StringBuilder(Parameter.ROUTE.Format).append(rt)
				.append(Parameter.DIRECTION.Format).append(direction);
		buildRequestURL(RequestType.STOPS, paramsBuilder.toString());
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
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return The last request URL built.
	 */
	public URL getRequestURL() {
		return requestURL;
	}

	/**
	 * @return The last received response body.
	 */
	public String getResponseBody() {
		return responseBody;
	}

	/**
	 * @param key
	 *            the key to set
	 * @return
	 */
	public BustimeAPIRequest setKey(String key) {
		this.key = key;
		return this;
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

	/**
	 * @param responseBody
	 *            the responseBody to set
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
}