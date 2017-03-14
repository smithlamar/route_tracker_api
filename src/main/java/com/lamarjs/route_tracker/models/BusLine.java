/*
 * Copyright (C) 2017 Lamar J. Smith
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.lamarjs.route_tracker.models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.lamarjs.route_tracker.services.BustimeAPIRequest;

/**
 * <p>
 * This object represents a single bus route/bus line. That is: one bus route,
 * its directions, and all of the stops associated with each of those
 * directions. The available CTA BusTracker API calls offer the data associated
 * with a BusLine across a disparate set of end-points. Generally, a "route", as
 * the CTA API provides it does not include its associated directions and stops.
 * </p>
 * <p>
 * The end result is that several initialization steps are required to build a
 * complete BusLine object.
 * </p>
 * <br>
 * <ol>
 * <li>A list of routes (BusLine objects) must be requested from the CTA API.
 * Each BusLine object returned only contains the route code, route name, and
 * route color properties.</li>
 * <li>The "directions" property for a single BusLine must then be initialized
 * by a second call to the CTA API.</li>
 * <li>For each Direction returned for a BusLine, the stops along that direction
 * must be initialized with separate CTA API calls.</li>
 * </ol>
 * CTA API calls are handled by {@link BustimeAPIRequest}
 * 
 * @author Lamar J. Smith
 * 
 */
public class BusLine {

	private String rt; // route code (9, 6, 1152, X9)
	private String rtnm; // route name
	private String rtclr; // route color hex value stored as a string
	private ArrayList<Direction> directions; // List of direction objects

	public BusLine() {
	};

	/**
	 *
	 * @param rt
	 * @param rtnm
	 * @param rtclr
	 */
	public BusLine(String rt, String rtnm, String rtclr) {
		this.rt = rt;
		this.rtnm = rtnm;
		this.rtclr = rtclr;
	}

	public BusLine(String rt, String rtnm, String rtclr, ArrayList<Direction> directions) {
		this.rt = rt;
		this.rtnm = rtnm;
		this.rtclr = rtclr;
		this.directions = directions;
	}

	/**
	 * Convenience method makes all necessary requests to the CTA API to get
	 * this BusLine's serviced directions and associated stops.
	 *
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	public void initialize(BustimeAPIRequest request) throws MalformedURLException, IOException {
		initializeDirections(request);
		initializeStops(request);
	}

	/**
	 * This method requests the list of directions from the CTA API for that
	 * this BusLine services and initializes the directions.
	 *
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	public void initializeDirections(BustimeAPIRequest request) throws MalformedURLException, IOException {
		directions = request.requestDirections(rt);
	}

	/**
	 * This method requests the list of bus stops from the CTA API for each of
	 * the directions attached to this BusLine and initializes their stops
	 * properties.
	 *
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */
	public void initializeStops(BustimeAPIRequest request) throws MalformedURLException, IOException {
		for (Direction dir : directions) {
			dir.initializeStops(request);
		}
	}

	/**
	 * This method returns the list of stops for this route based on the chosen
	 * direction.
	 *
	 * @param dir
	 * @return List of stops on this route.
	 */
	public ArrayList<Stop> getStops(Direction dir) throws NoSuchElementException {
		if (!directions.contains(dir)) {
			throw new NoSuchElementException("Error attempting to access stops for direction: " + dir
					+ ". This direction does not exist for the BusLine object: " + this);
		}
		return dir.stops;
	}

	/**
	 * @return
	 */
	public String getRouteCode() {
		return rt;
	}

	/**
	 *
	 * @return
	 */
	public String getRoutetName() {
		return rtnm;
	}

	/**
	 *
	 * @return
	 */
	public String getRouteColor() {
		return rtclr;
	}

	/**
	 * @return Returns the list of the Direction objects attached to this
	 *         BusLine. Each direction also contains an associated list of
	 *         stops.
	 */
	public ArrayList<Direction> getDirections() {
		return directions;
	}

	public void setRouteCode(String rt) {
		this.rt = rt;
	}

	public void setRouteName(String rtnm) {
		this.rtnm = rtnm;
	}

	public void setRouteColor(String rtclr) {
		this.rtclr = rtclr;
	}

	public void setDirections(ArrayList<Direction> directions) {
		this.directions = directions;
	}

	@Override
	public String toString() {
		String nameInfo = rt + " - " + rtnm;
		return nameInfo;
	}

	/**
	 * Direction class used to model CTA API direction data of a BusLine. Each
	 * direction also contains an associated set of stops.
	 */
	public class Direction {

		private String dir; // The name of this direction.
		private ArrayList<Stop> stops; // This direction's stops.

		public Direction() {
		};

		/**
		 *
		 * @param dir
		 *            A string representing the direction traveled e.g.
		 *            "Westbound"
		 * @param stops
		 */
		public Direction(String dir, ArrayList<Stop> stops) {
			this.dir = dir;
			this.stops = stops;
		}

		/**
		 * Initializes the stops property for this Direction by requesting the
		 * list of associated bus stops from the CTA API.
		 *
		 * @throws java.net.MalformedURLException
		 * @throws java.io.IOException
		 */
		public void initializeStops(BustimeAPIRequest request) throws MalformedURLException, IOException {
			stops = request.requestStops(rt, dir);
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

	/**
	 * Stop class used to model CTA API stop data.
	 */
	public class Stop {

		private int stpid; // stop id
		private String stpnm; // stop name
		private double lat; // latitude
		private double lon; // longitude

		public Stop() {
		};

		/**
		 *
		 * @param stpid
		 *            The unique id that identifies this stop along this
		 *            direction on this BusLine.
		 * @param stpnm
		 *            The human readable reference for this stop.
		 * @param lat
		 *            The latitudinal location of this stop as a signed Double
		 *            value.
		 * @param lon
		 *            The longitudinal location of this stop as a signed Double
		 *            value.
		 */
		public Stop(int stpid, String stpnm, double lat, double lon) {
			this.stpid = stpid;
			this.stpnm = stpnm;
			this.lat = lat;
			this.lon = lon;
		}

		public int getStopId() {
			return stpid;
		}

		public String getStopName() {
			return stpnm;
		}

		public double getLatitude() {
			return lat;
		}

		public double getLongitude() {
			return lon;
		}

		/**
		 * 
		 * @param stpid
		 *            The unique id that identifies this stop along this
		 *            direction on this BusLine.
		 */
		public void setStopId(int stpid) {
			this.stpid = stpid;
		}

		/**
		 * 
		 * @param stpnm
		 *            The human readable reference for this stop.
		 */
		public void setStopName(String stpnm) {
			this.stpnm = stpnm;
		}

		/**
		 * 
		 * @param lat
		 *            The latitudinal location of this stop as a signed Double
		 *            value.
		 */
		public void setLatitude(Double lat) {
			this.lat = lat;
		}

		/**
		 * 
		 * @param lon
		 *            The longitudinal location of this stop as a signed Double
		 *            value.
		 */
		public void setLongitude(Double lon) {
			this.lon = lon;
		}

		/**
		 *
		 * @return A string representation (stop name) of this stop.
		 */
		@Override
		public String toString() {
			return getStopName();
		}

	}
}
