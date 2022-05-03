package com.rajneesh.classenrollmentsystem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A Class for holding the validation errors.
 */
@Getter
@Setter
@AllArgsConstructor
public class ValidationError {
	private String field;
	private Object rejectedValue;
	private String message;
}
