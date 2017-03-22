package com.lamarjs.route_tracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CTAResponseWrapper {
	private String error;

	public String getError() {
		return error;
	}
	@JsonProperty("bustime-response.error.msg")
	public void setError(String error) {
		this.error = error;
	}
}
