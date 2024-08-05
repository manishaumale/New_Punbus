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
@ApiModel(description = "Toll Tax Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TollTaxMasterDto extends BaseEntityDto{
	
	/**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Toll Tax Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Toll Name")
    private String tollName;
    
    public TollTaxMasterDto(Integer id, String tollName) {
		super();
		this.id = id;
		this.tollName = tollName;
	}

}
