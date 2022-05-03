package com.rajneesh.classenrollmentsystem.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This entity class store the enrollment data.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ENROLLMENT")
public class EnrollmentEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "enrollment_id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private StudentEntity student;

	@ManyToOne
	@JoinColumn(name = "semester_id")
	private SemesterEntity semester;

	private Boolean isFullTime;

	@OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
	private List<CourseEnrollmentEntity> courseEnrollments = new ArrayList<>();

	public void addCourseEnrollment(CourseEnrollmentEntity courseEnrollment) {
		courseEnrollment.setEnrollment(this);
		courseEnrollments.add(courseEnrollment);
	}
}
