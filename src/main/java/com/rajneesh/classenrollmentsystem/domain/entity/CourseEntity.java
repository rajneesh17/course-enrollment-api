package com.rajneesh.classenrollmentsystem.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * entity class store the class/course data
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COURSE")
public class CourseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COURSE_ID", nullable = false)
	private Long id;

	private String name;

	private Integer credit;

}
