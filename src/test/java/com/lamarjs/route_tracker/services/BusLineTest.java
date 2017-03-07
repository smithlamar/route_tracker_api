package com.lamarjs.route_tracker.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.lamarjs.route_tracker.models.BusLine;
import com.lamarjs.route_tracker.models.BusLine.Direction;

public class BusLineTest {
	
	BusLine line;
	
	BustimeAPIRequest request;
	@Before
	public void setUp() {
		line = new BusLine("1", "Bronzeville/Union Station", "#336633");
	}

	@Test
	public void RefreshDirectionstest() {
		try {
			line.refreshDirections();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Direction dir : line.getDirections()) {
			System.out.println(dir.toString());
		}
	}

}
