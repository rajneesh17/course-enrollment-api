package com.rajneesh.classenrollmentsystem.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  entity class to store students data
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STUDENT")
public class StudentEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Student_ID", nullable = false)
	private Long studentId;
	private Long id;
	private String firstName;
	private String lastName;
	private String nationality;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<EnrollmentEntity> enrollments = new ArrayList<>();

	public void addEnrollment(EnrollmentEntity enrollment) {
		enrollment.setStudent(this);
		enrollments.add(enrollment);
	}

}
