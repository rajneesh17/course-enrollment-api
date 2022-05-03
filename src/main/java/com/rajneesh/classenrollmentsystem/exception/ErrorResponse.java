package com.rajneesh.classenrollmentsystem.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Placeholder class for error response.
 */
@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	private HttpStatus status;
	private String message;
	private List<ValidationError> errors;

	public void addValidationError(String field, Object rejectedValue,
			String message) {
		if (errors == null) {
			errors = new ArrayList<>();
		}
		errors.add(new ValidationError(field, rejectedValue, message));
	}

	public void addValidationErrors(MethodArgumentNotValidException ex) {
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> addValidationError(error.getField(),
						error.getRejectedValue(), error.getDefaultMessage()));

	}

	public void addValidationErrors(ConstraintViolationException ex) {
		ex.getConstraintViolations()
				.forEach(error -> addValidationError(
						error.getPropertyPath().toString(),
						error.getInvalidValue(), error.getMessage()));

	}

}