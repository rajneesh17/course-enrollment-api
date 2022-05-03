package com.rajneesh.classenrollmentsystem.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This entity class store class/course and enrollment relation
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COURSE_ENROLLMENT")
public class CourseEnrollmentEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURSE_ENROLLMENT_ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    EnrollmentEntity enrollment;

    @ManyToOne
    @JoinColumn(name = "course_id")
    CourseEntity course;

}
