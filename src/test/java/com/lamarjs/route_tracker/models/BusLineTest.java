package com.lamarjs.route_tracker.models;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lamarjs.route_tracker.exceptions.BusTimeErrorReceivedException;
import com.lamarjs.route_tracker.services.BustimeAPIRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BusLineTest {

	// TODO: Re-write these tests to align with the new general desing. Add more
	// tests.

	BusLine line;
	BustimeAPIRequest request;

	@Before
	public void setUp() {
		request = new BustimeAPIRequest(new RestTemplateBuilder());
		/*
		 * Initialize a predefined BusLine object (based on a real BusLine) and
		 * add a set of known directions to it with null for the list of stops
		 * on each direction.
		 */
		line = new BusLine("1", "Bronzeville/Union Station", "#ffffff");

		ArrayList<Direction> directions = new ArrayList<>();
		directions.add(new Direction("Northbound", null));

		line.setDirections(directions);
	}

	@Test
	public void initialize_directions_rt_1_gets_north_south_when_intially_null() throws BusTimeErrorReceivedException {
		/*
		 * We null out the directions property and test that it gets initialized
		 * correctly when initializeDirections() is called.
		 * 
		 */
		line.setDirections(null);
		try {
			line.initializeDirections(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(line.getDirections().toString().equals("[Northbound, Southbound]"));
	}

	@Test
	public void initialize_directions_rt_1_overwrites_initial_values() throws BusTimeErrorReceivedException {
		/*
		 * We null out the directions property and test that it gets initialized
		 * correctly when initializeDirections() is called.
		 * 
		 */

		try {
			line.initializeDirections(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(line.getDirections().toString().equals("[Northbound, Southbound]"));
	}

	@Test
	public void direction_initialize_stops_test() throws BusTimeErrorReceivedException {
		try {
			Direction dir = line.getDirections().get(0);
			dir.initializeStops(request, line.getRouteCode());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue("1509 S Michigan".equals(line.getDirections().get(0).getStops().get(0).toString()));
	}

}
