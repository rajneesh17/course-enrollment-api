package com.rajneesh.classenrollmentsystem.exception;

import static java.text.MessageFormat.format;

/**
 * user defined exception for a student not found in the database.
 */
public class AlreadyCourseEnrollException extends RuntimeException {

	private static final long serialVersionUID = -159264386600812023L;

	public AlreadyCourseEnrollException(Long studentId, String course) {
		super(format("Student id {0} already enrolled for course {1}.", studentId, course));
	}
}
