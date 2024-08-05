package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Bus Sub Type Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusSubTypeDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "Bus SubType Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
	@ApiModelProperty(notes = "Bus SubType Description")
	//@Size(min = 1, max = 4)
    private String busSubTypeDescription;
    
    @ApiModelProperty(notes = "Bus SubType Name")
	//@Size(min = 1, max = 50)
    private String busSubTypeName;
    
    
    @ApiModelProperty(notes = "Status")
    private Boolean status;
    
    public BusSubTypeDto(Integer id, String busSubTypeName){
    	super();
    	this.id = id;
    	this.busSubTypeName = busSubTypeName;
    }
    

}
