package com.rajneesh.classenrollmentsystem.exception;

import static java.text.MessageFormat.format;

/**
 * user defined exception for a student not found in the database.
 */
public class StudentInfoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -159264386600812023L;

	public StudentInfoNotFoundException(Long studentId) {
		super(format("Student id {0} not found.", studentId));
	}
}
