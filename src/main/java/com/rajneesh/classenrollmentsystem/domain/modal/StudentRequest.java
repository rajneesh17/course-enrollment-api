package com.rajneesh.classenrollmentsystem.domain.modal;

import com.rajneesh.classenrollmentsystem.domain.entity.StudentEntity;
import com.rajneesh.classenrollmentsystem.domain.modal.validation.CreationValidationGroup;
import com.rajneesh.classenrollmentsystem.domain.modal.validation.UniqueStudentId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * A Data Transformation Object for create/update the {@link StudentEntity} document.
 * 
 */
@Getter
@Setter
@NoArgsConstructor
public class StudentRequest {

	@Schema(description = "Unique Identifier of the Student.", example = "1", required = true)
	@NotNull(message = "{add.student.id.notnull}")
	@UniqueStudentId(groups = CreationValidationGroup.class)
	private Long id;

	@Schema(description = "First Name of the Student.", example = "Rajneesh", required = true)
	@NotBlank(message = "{add.student.firstName.notempty}")
	private String firstName;

	@Schema(description = "Last Name of the Student.", example = "Bhatti", required = true)
	@NotBlank(message = "{add.student.lastName.notempty}")
	private String lastName;

	@Schema(description = "Nationality of the Student.", example = "US", required = true)
	private String nationality;
}
