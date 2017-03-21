package com.lamarjs.route_tracker.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamarjs.route_tracker.exceptions.BusTimeErrorReceivedException;
import com.lamarjs.route_tracker.models.BusLine;
import com.lamarjs.route_tracker.services.BustimeAPIRequest;

@RestController
public class BustimeRequestController {
	@Autowired
	BustimeAPIRequest requestService;

	@RequestMapping(value = "/getbuslines")
	public List<BusLine> getBusLines() {

		List<BusLine> busLines = null;

		try {
			busLines = requestService.requestRoutes();
		} catch (IOException | BusTimeErrorReceivedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (BusLine line : busLines) {
			try {
				line.initialize(requestService);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return busLines;

		// TODO: Write tests for me!
	}

	// TODO: /"getpredictions" request mapping. should be able to return
	// predictions for the requested stops along a particular BusLine (route) in
	// one
	// direction.
}
