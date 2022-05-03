package com.rajneesh.classenrollmentsystem.domain.modal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rajneesh.classenrollmentsystem.domain.entity.SemesterEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.rajneesh.classenrollmentsystem.util.Constants.APP_DATE_FORMAT;


/**
 * A Data Transformation Object for {@link SemesterEntity} entity.
 * 
 */
@Getter
@Setter
@NoArgsConstructor
public class Semester {

	@Schema(description = "Name of the Semester.", example = "Summer2022", required = true)
	@NotNull(message = "{add.semester.name.notempty}")
	private String name;

	@Schema(description = "Start Date of the Semester.", example = "2021-03-14", required = false)
	@JsonFormat(pattern = APP_DATE_FORMAT)
	private LocalDate startDate;

	@Schema(description = "End Date of the Semester.", example = "2021-06-14", required = false)
	@JsonFormat(pattern = APP_DATE_FORMAT)
	private LocalDate endDate;
}
