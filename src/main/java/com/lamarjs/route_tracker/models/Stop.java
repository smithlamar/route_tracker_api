package com.lamarjs.route_tracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stop class used to model CTA API stop data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
	 *            The unique id that identifies this stop along this direction
	 *            on this BusLine.
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
	 *            The unique id that identifies this stop along this direction
	 *            on this BusLine.
	 */
	@JsonProperty(value = "stpid")
	public void setStopId(int stpid) {
		this.stpid = stpid;
	}

	/**
	 * 
	 * @param stpnm
	 *            The human readable reference for this stop.
	 */
	@JsonProperty(value = "stpnm")
	public void setStopName(String stpnm) {
		this.stpnm = stpnm;
	}

	/**
	 * 
	 * @param lat
	 *            The latitudinal location of this stop as a signed Double
	 *            value.
	 */
	@JsonProperty(value = "lat")
	public void setLatitude(Double lat) {
		this.lat = lat;
	}

	/**
	 * 
	 * @param lon
	 *            The longitudinal location of this stop as a signed Double
	 *            value.
	 */
	@JsonProperty(value = "lon")
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