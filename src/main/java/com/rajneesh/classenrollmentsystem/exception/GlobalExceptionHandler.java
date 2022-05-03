package com.rajneesh.classenrollmentsystem.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

/**
 * This class handles centralized exception handling across all @RequestMapping methods
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles if the request body is invalid or not readable.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex
            , HttpHeaders headers
            , HttpStatus status
            , WebRequest request) {
        log.error("Malformed JSON request", ex);
        return buildErrorResponse("Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles if the method argument annotated with @Valid failed during the
     * validation.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        log.error("Request has errors", ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Request has errors");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.addValidationErrors(ex);
        return build(errorResponse);
    }

    /**
     * Handles if the value is missing in the request parameter.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        log.error("Missing Parameters", ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Missing Parameters");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.addValidationError(ex.getParameterName(), null,
                "parameter is missing");
        return build(errorResponse);
    }

    /**
     * Handles if the requested method is not allowed
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        log.error("Method not allowed", ex);
        return buildErrorResponse("Method not allowed",
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Handles the ApiRequestException if any.
     *
     * @param ex RequestException
     * @return ResponseEntity
     */
    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<Object> handleApiRequestException(RequestException ex) {
        log.error("Unable to process the request", ex);
        return ResponseEntity.badRequest().build();

    }

    /**
     * Handles if the requested information is not available in the database.
     *
     * @param ex RecordNotFoundException
     * @return ResponseEntity
     */
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRecordNotFoundException(
            RecordNotFoundException ex) {
        log.error("Requested record not found", ex);
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles if the requested student information is not available in the
     * database.
     *
     * @param ex StudentInfoNotFoundException
     * @return ResponseEntity
     */
    @ExceptionHandler(StudentInfoNotFoundException.class)
    public ResponseEntity<Object> handleStudentInfoNotFoundException(StudentInfoNotFoundException ex) {
        log.error("Requested student record Not found", ex);
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        log.error("Error Occured: Unable to process your request at this time.",
                exception);
        return buildErrorResponse(
                "Error Occured: Unable to process your request at this time.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle if the method/class annotated with @Validated failed during the
     * validation.
     *
     * @param ex ConstraintViolationException
     * @return ResponseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {

        log.error("Request has constraint violations", ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Request has constraint violations");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.addValidationErrors(ex);
        return build(errorResponse);
    }

    /**
     * Handle if the method argument type is mismatch
     *
     * @param ex MethodArgumentTypeMismatchException
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type Mismatch", ex);
        return buildErrorResponse("Type Mismatch", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus) {
        log.error(Optional.ofNullable(NestedExceptionUtils.getRootCause(exception))
                .map(Throwable::getMessage)
                .orElse(""), exception);
        return buildErrorResponse(exception.getMessage(), httpStatus);
    }

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setStatus(httpStatus);
        return build(errorResponse);
    }

    private ResponseEntity<Object> build(ErrorResponse errorResponse) {
        return ResponseEntity.status(errorResponse.getStatus())
                .body(errorResponse);
    }
}
