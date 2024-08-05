package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Book Reading Closing")
public class BookReadingClosingDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Book Reading Closing Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Book Reading Closing Name")
    private String name;

	public BookReadingClosingDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
	

}
