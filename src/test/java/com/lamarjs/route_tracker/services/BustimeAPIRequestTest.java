package com.lamarjs.route_tracker.services;


import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class BustimeAPIRequestTest extends junit.framework.TestSuite{

	BustimeAPIRequest request;
	@Before
	public void setUp() {
		request = new BustimeAPIRequest();
	}

	@Test
	public void test() {
		try {
			request.buildGetRoutesRequest();
			request.send();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(request.getLastRequest().toString());
		System.out.println(request.getResponse());
	}
}
