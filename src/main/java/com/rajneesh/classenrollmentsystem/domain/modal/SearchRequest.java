package com.rajneesh.classenrollmentsystem.domain.modal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * A Data Transformation Object for searching the student records based on the
 * criteria.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SearchRequest {

	@Schema(description = "Name of the Semester.", example = "Summer2022")
	private String semester;
	@Schema(description = "Name of the Course.", example = "classA")
	private String course;
	@Schema(description = "Page Number", example = "1")
	private Integer pageNo;
	@Schema(description = "Page Size", example = "10")
	private Integer pageSize;
	@Schema(description = "Direction (ASC/DESC)", example = "ASC")
	private String direction;
	@Schema(description = "Sort By", example = "id")
	private String sortBy;
}
