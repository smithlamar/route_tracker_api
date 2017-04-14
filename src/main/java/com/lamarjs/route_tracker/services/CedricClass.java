package com.lamarjs.route_tracker.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CedricClass {
	private String foo;
	
	public String getFoo() {
		return this.foo;
	}
	
	void hello() {
		log.info("Hello world");
	}
}
