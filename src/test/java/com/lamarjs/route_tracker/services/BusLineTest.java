package com.lamarjs.route_tracker.services;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.lamarjs.route_tracker.models.BusLine;
import com.lamarjs.route_tracker.models.BusLine.Direction;

public class BusLineTest {

	BusLine line;
	BustimeAPIRequest request;

	@Before
	public void setUp() {
		/*
		 * Initialize a predefined BusLine object (based on a real BusLine) and
		 * add a set of known directions to it with null for the list of stops
		 * on each direction.
		 */
		line = new BusLine("1", "Bronzeville/Union Station", "#ffffff");
		ArrayList<Direction> directions = new ArrayList<>();
		directions.add(line.new Direction("Northbound", null));
		line.setDirections(directions);
	}

	@Test
	public void initialize_directions_rt_1_gets_north_south_when_intially_null() {
		/*
		 * We null out the directions property and test that it gets initialized
		 * correctly when initializeDirections() is called.
		 * 
		 */
		line.setDirections(null);
		try {
			line.initializeDirections();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(line.getDirections().toString().equals("[Northbound, Southbound]"));
	}

	@Test
	public void initialize_directions_rt_1_overwrites_initial_values() {
		/*
		 * We null out the directions property and test that it gets initialized
		 * correctly when initializeDirections() is called.
		 * 
		 */
		line.setDirections(null);
		try {
			line.initializeDirections();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(line.getDirections().toString().equals("[Northbound, Southbound]"));
	}

	@Test
	public void direction_initialize_stops_test() {
		try {
			Direction dir = line.getDirections().get(0);
			dir.initializeStops();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue("1509 S Michigan".equals(line.getDirections().get(0).getStops().get(0).toString()));
	}

}
