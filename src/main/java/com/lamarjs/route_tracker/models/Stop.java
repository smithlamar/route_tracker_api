package com.lamarjs.route_tracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Stop class used to model CTA API stop data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
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

	/**
	 *
	 * @return A string representation (stop name) of this stop.
	 */
	@Override
	public String toString() {
		return stpnm;
	}

}