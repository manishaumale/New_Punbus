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
@ApiModel(description = "Stopage Type Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StopageTypeDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Stopage Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Stopage Name")
    private String stopTypeName;
    
    public StopageTypeDto(Integer id, String stopTypeName) {
		super();
		this.id = id;
		this.stopTypeName = stopTypeName;
	}

}
