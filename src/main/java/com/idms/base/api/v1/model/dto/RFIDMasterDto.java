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
@ApiModel(description = "RFID Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFIDMasterDto extends BaseEntityDto{
	
	 /**
		 * @author Hemant Makkar
		 */
		
    @ApiModelProperty(notes = "RFID Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    private String value;
    
    public RFIDMasterDto(Integer id,String value) {
    	this.id = id;
    	this.value = value;
    }
    
}
