package com.lamarjs.route_tracker.services;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class BustimeAPIRequestTest extends junit.framework.TestSuite {

	BustimeAPIRequest request;

	@Before
	public void setUp() {
		request = new BustimeAPIRequest();
	}

	// TODO: Add real tests! (Especially now that the classes have appropriate
	// seams.)
	@Test
	public void test() {
		try {
			request.buildGetRoutesRequest();
			request.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(request.getRequestURL().toString());
		System.out.println(request.getResponseBody());
	}
}
