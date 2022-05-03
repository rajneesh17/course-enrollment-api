package com.rajneesh.classenrollmentsystem.domain.modal;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * A Data Transformation Object for {@link StudentEntity} entity.
 */
@Getter
@Setter
@NoArgsConstructor
public class Student {

	private Long id;
	private String firstName;
	private String lastName;
	private String nationality;

	private List<Enrollment> enrollments;
}
