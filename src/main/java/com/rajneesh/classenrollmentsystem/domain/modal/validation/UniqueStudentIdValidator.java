package com.rajneesh.classenrollmentsystem.domain.modal.validation;

import com.rajneesh.classenrollmentsystem.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.text.MessageFormat.format;

/**
 * 
 * {@link UniqueStudentIdValidator}Fs defines the logic to validate a given student id is unique.
 *
 */
@Slf4j
public class UniqueStudentIdValidator
		implements
			ConstraintValidator<UniqueStudentId, Long> {

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {

		// Checks the id already exists in the database. If it does, then it is
		// invalid.
		if (value != null && studentRepository.existsStudentById(value)) {
			log.error(format("Student id {0} already exists", value));
			return false;
		}
		return true;
	}

}