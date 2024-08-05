package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Employee Type Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmploymentTypeDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Employee Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Enrollment Name")
	private String enrolmentName;
	
	@ApiModelProperty(notes = "Enrollment Code")
	private String enrolmentCode;
    
    public EmploymentTypeDto(Integer id, String enrolmentName) {
		super();
		this.id = id;
		this.enrolmentName = enrolmentName;
	}

}