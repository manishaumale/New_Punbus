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
@ApiModel(description = "Bus Type Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTypeDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "Bus Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
	@ApiModelProperty(notes = "Bus Type Code")
	//@Size(min = 1, max = 4)
    private String busTypeCode;
    
    @ApiModelProperty(notes = "Bus Type Name")
	//@Size(min = 1, max = 50)
    private String busTypeName;
    
    
    @ApiModelProperty(notes = "Status")
    private Boolean status;
    
    public BusTypeDto(Integer id, String busTypeName) {
		super();
		this.id = id;
		this.busTypeName = busTypeName;
	}

}
