package com.lamarjs.route_tracker.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.lamarjs.route_tracker.exceptions.BusTimeErrorReceivedException;
import com.lamarjs.route_tracker.models.BusLine;
import com.lamarjs.route_tracker.models.Prediction;
import com.lamarjs.route_tracker.services.BustimeAPIRequest;

@RestController
public class BustimeRequestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	BustimeAPIRequest requestService;

	@Autowired
	public BustimeRequestController(BustimeAPIRequest requestService) {
		this.requestService = requestService;
	}

	@RequestMapping(value = "/getbuslines", method = RequestMethod.GET)
	public List<BusLine> getBusLines() {

		List<BusLine> busLines = null;

		try {
			busLines = requestService.requestRoutes();

		} catch (RestClientException | MalformedURLException | BusTimeErrorReceivedException | URISyntaxException e) {
			// TODO Auto-generated catch block
			log.error("[getBusLines()] - Exception thrown on requestRoutes() call: ", e);
			e.printStackTrace();
		}

		for (BusLine line : busLines) {
			try {
				line.initialize(requestService);
			} catch (BusTimeErrorReceivedException | IOException e) {
				// TODO Auto-generated catch block
				log.error("[getBusLines()] - Exception thrown on intialize() call for BusLine: " + line + ". "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
		return busLines;

		// TODO: Write tests for me! Return error message when appropriate.
	}

	@RequestMapping(value = "/getpredictions", method = RequestMethod.GET)
	public List<Prediction> getPredictions(@RequestParam(value = "stpids", required = true) String stpids,
			@RequestParam(value = "rts", required = false, defaultValue = "") String rts,
			@RequestParam(value = "top", required = false, defaultValue = "5") int top) {

		List<Prediction> predictions = null;

		try {
			predictions = requestService.requestPredictions(stpids, rts, top);
		} catch (MalformedURLException | BusTimeErrorReceivedException e) {
			// TODO Auto-generated catch block
			log.error("[getPredictions()] - Exception thrown on requestPredictions() call for stops: " + stpids + ". "
					+ e.getMessage());
			e.printStackTrace();
		}

		return predictions;

		// TODO: Write tests for me! Return error message when appropriate.
	}

}
