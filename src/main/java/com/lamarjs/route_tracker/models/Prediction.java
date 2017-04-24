package com.lamarjs.route_tracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Prediction {
	String tmstmp; // Ex: "20170314 11:25" TODO: Make datetime
	String typ; // Ex: "A"
	String stpnm; // Ex: "Michigan \u0026 Balbo"
	int stpid; // Ex: "1584"
	int vid; // Ex: "1315"
	int dstp; // Ex: 4833
	String rt; // Ex: "4"
	String rtdir; // Ex: "Northbound"
	String des; // Ex: "Illinois Center"
	String prdtm; // Ex: "20170314 11:32"
	String tablockid; // Ex: "4 -716"
	String tatripid; // Ex: "10001019"
	boolean dly; // Ex: false
	String prdctdn; // Ex: "7"
	String zone; // Ex: ""

	@Override
	public String toString() {
		return prdctdn + "m";
	}
}
