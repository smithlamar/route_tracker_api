package com.lamarjs.route_tracker.models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lamarjs.route_tracker.services.BustimeAPIRequest;

/**
 * Direction class used to model CTA API direction data of a BusLine. Each
 * direction also contains an associated set of stops.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Direction {

	private String dir; // The name of this direction.
	private ArrayList<Stop> stops; // This direction's stops.

	public Direction() {
	};

	/**
	 *
	 * @param dir
	 *            A string representing the direction traveled e.g. "Westbound"
	 * @param stops
	 */
	public Direction(String dir, ArrayList<Stop> stops) {
		this.dir = dir;
		this.stops = stops;
	}

	/**
	 * Initializes the stops property for this Direction by requesting the list
	 * of associated bus stops from the CTA API.
	 *
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	public void initializeStops(BustimeAPIRequest requestService, String rt) throws MalformedURLException, IOException {
		stops = requestService.requestStops(rt, dir);
	}

	public String getDirectionName() {
		return dir;
	}

	/**
	 *
	 * @return this Direction's list of stops.
	 */
	public ArrayList<Stop> getStops() {
		return stops;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public void setStops(ArrayList<Stop> stops) {
		this.stops = stops;
	}

	/**
	 *
	 * @return A string representing this direction.
	 */
	@Override
	public String toString() {
		return dir;
	}

}