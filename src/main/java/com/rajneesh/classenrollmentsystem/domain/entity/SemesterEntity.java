package com.rajneesh.classenrollmentsystem.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *  entity class to save semester data
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SEMESTER")
public class SemesterEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEMESTER_ID", nullable = false)
	private Long id;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;

}
