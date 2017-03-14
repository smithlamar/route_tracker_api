package com.lamarjs.controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamarjs.route_tracker.models.BusLine;
import com.lamarjs.route_tracker.services.BustimeAPIRequest;

@RestController
public class BustimeRequestController {

	@RequestMapping(value = "/getbuslines")
	public ArrayList<BusLine> getBusLines() {
		ArrayList<BusLine> busLines = null;
		try {
			busLines = new BustimeAPIRequest().requestRoutes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return busLines;
	}

	// TODO: /"getpredictions" request mapping. should be able to return
	// predictions for the requested stops along a particular BusLine (route) in one
	// direction.
}
