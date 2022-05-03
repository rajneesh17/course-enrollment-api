package com.rajneesh.classenrollmentsystem.exception;

/**
 * The type {@link RequestException} is a custom exception for this
 * application.
 */
public class RequestException extends RuntimeException {

	private static final long serialVersionUID = 4048997951057125742L;

	/**
	 * Instantiates a new ApiRequestException
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public RequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequestException(String message) {
		super(message);
	}
}
