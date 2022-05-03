package com.rajneesh.classenrollmentsystem.domain.modal.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * An Annotation to mark the field student identifier field to be unique. It
 * uses {@link UniqueStudentIdValidator} type to validate the given id with the
 * database.
 */
@Constraint(validatedBy = {UniqueStudentIdValidator.class})
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueStudentId {
	String message() default "Student id must be unique.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}