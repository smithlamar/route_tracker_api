package com.lamarjs.route_tracker.models;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class CTAResponseWrapper {

	private List<Map<String, String>> error;
	List<BusLine> routes;
	List<Direction> directions;
	List<Stop> stops;
	List<Prediction> predictions;

	public CTAResponseWrapper() {
	};

	public List<Map<String, String>> getError() {
		return error;
	}

	public void setError(List<Map<String, String>> error) {
		this.error = error;
	}

	public List<BusLine> getRoutes() {
		return routes;
	}

	public void setRoutes(List<BusLine> routes) {
		this.routes = routes;
	}

	/**
	 * @return the directions
	 */
	public List<Direction> getDirections() {
		return directions;
	}

	/**
	 * @param directions
	 *            the directions to set
	 */
	public void setDirections(List<Direction> directions) {
		this.directions = directions;
	}

	/**
	 * @return the stops
	 */
	public List<Stop> getStops() {
		return stops;
	}

	/**
	 * @param stops
	 *            the stops to set
	 */
	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	/**
	 * @return the predictions
	 */
	public List<Prediction> getPredictions() {
		return predictions;
	}

	/**
	 * @param predictions
	 *            the predictions to set
	 */
	public void setPredictions(List<Prediction> predictions) {
		this.predictions = predictions;
	}
}
