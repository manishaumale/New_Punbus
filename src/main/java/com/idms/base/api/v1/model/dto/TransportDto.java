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
@ApiModel(description = "Transport Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransportDto extends BaseEntityDto{
	
	/**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Transport Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Transport Name")
   	//@Size(min = 1, max = 50)
    private String transportName;
    
    
    @ApiModelProperty(notes = "Transport Code")
   	//@Size(min = 1, max = 4)
    private String transportCode;
    
    @ApiModelProperty(notes = "Status")
    private Boolean status;
    

    public TransportDto(Integer id, String transportName) {
		super();
		this.id = id;
		this.transportName = transportName;
	}

}
