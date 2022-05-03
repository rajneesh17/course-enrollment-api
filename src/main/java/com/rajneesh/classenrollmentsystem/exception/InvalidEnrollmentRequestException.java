package com.rajneesh.classenrollmentsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A user defined exception for student exceeds the enrollment limit per semester.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidEnrollmentRequestException extends RuntimeException {

	private static final long serialVersionUID = -6702055005654941371L;

	public InvalidEnrollmentRequestException() {
		super("Each student is only allowed to be enrolled in a maximum of 20 credits of each semester.");
	}
}