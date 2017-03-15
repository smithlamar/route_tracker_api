package com.lamarjs.route_tracker.exceptions;

public class BusTimeErrorReceivedException extends Exception {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -8598221869812722283L;

	public BusTimeErrorReceivedException() {
		super("A request to the Bustime API returned an error message.");
	}

	public BusTimeErrorReceivedException(String message) {
		super(message);
	}
}