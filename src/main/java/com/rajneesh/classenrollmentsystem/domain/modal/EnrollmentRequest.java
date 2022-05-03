package com.rajneesh.classenrollmentsystem.domain.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rajneesh.classenrollmentsystem.domain.entity.EnrollmentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A Data Transformation Object for {@link EnrollmentEntity} requests to enroll a
 * student into a class.
 */
@Getter
@Setter
@NoArgsConstructor
public class EnrollmentRequest {

	@Schema(description = "Unique Identifier of the Student.", example = "1", required = true)
	@JsonProperty("id")
	@NotNull(message = "{request.enrollment.studentid.notnull}")
	private Long studentId;

	@Schema(description = "Name of the Semester.", example = "Summer2022", required = true)
	@NotEmpty(message = "{request.enrollment.semester.notempty}")
	private String semester;

	@Schema(description = "Name of the Course.", example = "classA", required = true)
	@JsonProperty("course")
	@NotEmpty(message = "{request.enrollment.course.notempty}")
	private String course;
}
