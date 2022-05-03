package com.rajneesh.classenrollmentsystem.domain.modal;

import com.rajneesh.classenrollmentsystem.domain.entity.CourseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A Data Transformation Object for {@link CourseEntity}.
 * 
 */
@Getter
@Setter
@NoArgsConstructor
public class Course {

	@Schema(description = "Name of the Course.", example = "classA", required = true)
	@NotEmpty(message = "{add.course.name.notempty}")
	private String name;

	@Schema(description = "Fixed Credit/Unit of the Course. Some harder classes be 4 credits while easier one could 2 or 3 credits.", example = "4", required = true)
	@NotNull(message = "{add.course.credit.notnull}")
	@Max(value = 4, message = "{add.course.credit.max}")
	private Integer credit;

}
