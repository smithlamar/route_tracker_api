package com.lamarjs.controllers;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lamarjs.route_tracker.models.BusLine;

@RestController
public class BustimeRequestController {
	
	@RequestMapping(value = "/getbuslines")
	public ArrayList<BusLine> getBusLines() {
		ArrayList<BusLine> busLines = null;
		return busLines;
	}
}
