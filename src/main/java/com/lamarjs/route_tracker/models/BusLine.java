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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.lamarjs.route_tracker.services.BustimeAPIRequest;

/**
 * This object represents a single Bus Line. That is, one bus route, it's
 * directions, and all of the stops associated with the two directions.
 * 
 * @author Lamar J. Smith
 */

@JsonRootName(value = "bustime-response")
public class BusLine {

	private final String rt; // route code (9, 6, 1152, X9)
	private final String rtnm; // route name
	private final String rtclr; // route color hex value stored as a string
	@JsonProperty("directions")
	private ArrayList<Direction> directions; // List of direction objects

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

	/**
	 *
	 * @return
	 */
	public String getRouteCode() {
		return rt;
	}

	/**
	 *
	 * @return
	 */
	public String getRouteName() {
		return rtnm;
	}

	/**
	 *
	 * @return
	 */
	public String getRouteColor() {
		return rtclr;
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

	/**
	 * This method returns a String list of stops for this route based on the
	 * chosen direction.
	 *
	 * @param dir
	 * @return List of stops on this route.
	 */
	public ArrayList<Stop> getStops(Direction dir) {
		return dir.stops;
	}

	/**
	 * This method requests the list of bus directions from the CTA API for this
	 * bus line and initializes the directions property.
	 *
	 * @throws java.net.MalformedURLException
	 *             if an invalid URL is constructed.
	 * @throws java.io.IOException
	 *             if a problem occurs when sending the API request.
	 */
	public void refreshDirections() throws MalformedURLException, IOException {

		BustimeAPIRequest request = new BustimeAPIRequest();
		request.buildRequestURL(BustimeAPIRequest.GET_BUS_DIRECTIONS, BustimeAPIRequest.RT + rt);
		String responseBody = request.send().getResponse();
		
		// TODO: translate json into direction object
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	    ObjectReader reader = mapper.readerForUpdating(directions).withRootName("bustime-response");
	    directions = reader.readValue(responseBody);
		
	}

	/**
	 * This method requests the list of bus stops from the CTA API for each of
	 * the directions in this bus line and initializes their stops properties.
	 *
	 * @throws java.net.MalformedURLException
	 * @throws java.io.IOException
	 */

	public void refreshStops() throws MalformedURLException, IOException {
		/*
		 * "responseBody" is the initial destination for the raw response data from
		 * the URL request. A JsonParser object converts the buffered reader
		 * into an object that can be further manipulated by the Gson API.
		 */
		for (Direction direction : directions) {

			BustimeAPIRequest request = new BustimeAPIRequest();
			request.buildRequestURL(BustimeAPIRequest.GET_BUS_STOPS,
					BustimeAPIRequest.RT + rt, BustimeAPIRequest.DIR + direction.getPercEncodedDirection());
			String responseBody = request.send().getResponse();
			// TODO: Map stops responseBody to direction.stops = request.parseStops();
		}
	}

	/**
	 * Direction class used to model CTA API direction data for the Gson parser.
	 */
	@JsonRootName(value = "bustime-response")
	public class Direction {
		@JsonProperty("dir")
		private final String dir;
		@JsonProperty("stops")
		private ArrayList<Stop> stops;

		/**
		 *
		 * @param dir
		 */
		public Direction(String dir) {
			this.dir = dir;
		}

		/**
		 *
		 * @return
		 */
		public String getDirection() {
			return dir;
		}

		/**
		 *
		 * @return the stops associated with this direction.
		 */
		public ArrayList<Stop> getStops() {
			return stops;
		}

		/**
		 *
		 * @return A string representing this direction.
		 */
		@Override
		public String toString() {
			return dir;
		}
		
        /**
        *
        * @return direction as a string with percent encoding for spaces.
        */
       public String getPercEncodedDirection() {
           return dir.replaceAll(" ", "%20");
       }

	}

	/**
	 * Stop class used to model CTA API stop data for the Gson parser.
	 */
	public class Stop {

		private final int stpid; // stop id
		private final String stpnm; // stop name
		private final double lat; // latitude
		private final double lon; // longitude

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

		/**
		 * @return the stop name
		 */
		public String getStpnm() {
			return stpnm;
		}

		/**
		 * @return the latitude
		 */
		public double getLat() {
			return lat;
		}

		/**
		 * @return the longitude
		 */
		public double getLon() {
			return lon;
		}

	}
}
