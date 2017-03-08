/*
 * Copyright (C) 2016 Lamar J. Smith
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.lamarjs.route_tracker.services.BustimeAPIRequest;

/**
 * This object represents a single Bus Line. That is, one bus route, it's
 * directions, and all of the stops associated with the two directions.
 * 
 * @author Lamar J. Smith
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
	 *
	 * @return
	 */
	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	/**
	 *
	 * @return
	 */
	public String getRtnm() {
		return rtnm;
	}

	public void setRtnm(String rtnm) {
		this.rtnm = rtnm;
	}

	/**
	 *
	 * @return
	 */
	public String getRtclr() {
		return rtclr;
	}

	public void setRtclr(String rtclr) {
		this.rtclr = rtclr;
	}

	@Override
	public String toString() {
		String nameInfo = rt + " - " + rtnm;
		return nameInfo;
	}

	/**
	 * Getter for directions property.
	 *
	 * @return Returns the list of directions objects attached to this route.
	 */
	public ArrayList<Direction> getDirections() {
		return directions;
	}

	public void setDirections(ArrayList<Direction> directions) {
		this.directions = directions;
	}

	/**
	 * This method returns a String list of stops for this route based on the
	 * chosen direction.
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
	 * This method requests the list of bus directions from the CTA API for this
	 * bus line and initializes the directions property.
	 *
	 * @throws java.io.IOException
	 *             if a problem occurs when sending the API request.
	 */
	public void initializeDirections() throws MalformedURLException, IOException {
		
		directions = new ArrayList<Direction>();
		
		// Request a list of directions from the CTA API
		BustimeAPIRequest request = new BustimeAPIRequest();
		request.buildRequestURL(BustimeAPIRequest.GET_BUS_DIRECTIONS, BustimeAPIRequest.RT + rt);
		String responseBody = request.send().getResponse();

		// TODO: Map the returned Directions for this BusLine to it's directions property using Jackson.
		
		ArrayList<String> dirs;
		
		// TODO: Something about the unwrap root value config setting isn't working the way that I expect.
		try {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
			
			TypeReference<ArrayList<String>> ref = new TypeReference<ArrayList<String>>() {
			};
			
			dirs = mapper.readValue(responseBody, ref);
			
			System.out.println(dirs.toString());
			
			for (String dir : dirs) {
				Direction newDirection = new Direction();
				newDirection.setDirection(dir);
				directions.add(newDirection);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method requests the list of bus stops from the CTA API for each of
	 * the directions in this bus line and initializes their stops properties.
	 *
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */

	public void initializeStops() throws MalformedURLException, IOException {
		for (Direction dir : directions) {
			dir.initializeStops();
		}
	}

	/**
	 * Direction class used to model CTA API direction data for the Json parser.
	 */
	public class Direction {

		// Directions
		public static final String NORTH = "North Bound";
		public static final String SOUTH = "South Bound";
		public static final String EAST = "East Bound";
		public static final String WEST = "West Bound";

		private String dir;
		private ArrayList<Stop> stops;

		public Direction() {
		};

		/**
		 *
		 * @param dir
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
		public void initializeStops() throws MalformedURLException, IOException {

			BustimeAPIRequest request = new BustimeAPIRequest();
			request.buildRequestURL(BustimeAPIRequest.GET_BUS_STOPS, BustimeAPIRequest.RT + rt,
					BustimeAPIRequest.DIR + dir);
			String responseBody = request.send().getResponse();

			// TODO: translate json into direction object
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
			ObjectReader reader = mapper.readerForUpdating(stops).withRootName("bustime-response");
			stops = reader.readValue(responseBody);
		}

		/**
		 *
		 * @return
		 */
		public String getDirection() {
			return dir;
		}

		public void setDirection(String dir) {
			this.dir = dir;
		}

		/**
		 *
		 * @return the stops associated with this direction.
		 */
		public ArrayList<Stop> getStops() {
			return stops;
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
	 * Stop class used to model CTA API stop data for the Gson parser.
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
		 * @param stpnm
		 * @param lat
		 * @param lon
		 */
		public Stop(int stpid, String stpnm, double lat, double lon) {
			this.stpid = stpid;
			this.stpnm = stpnm;
			this.lat = lat;
			this.lon = lon;
		}

		/**
		 *
		 * @return A string representation (stop name) of this stop.
		 */
		@Override
		public String toString() {
			return getStpnm();
		}

		/**
		 * @return the stop id
		 */
		public int getStpid() {
			return stpid;
		}

		public void setStpid(int stpid) {
			this.stpid = stpid;
		}

		/**
		 * @return the stop name
		 */
		public String getStpnm() {
			return stpnm;
		}

		public void setStpnm(String stpnm) {
			this.stpnm = stpnm;
		}

		/**
		 * @return the latitude
		 */
		public double getLat() {
			return lat;
		}

		public void setLat(Double lat) {
			this.lat = lat;
		}

		/**
		 * @return the longitude
		 */
		public double getLon() {
			return lon;
		}

		public void setLon(Double lon) {
			this.lon = lon;
		}

	}
}
