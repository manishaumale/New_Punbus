package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Tax Type Model")
public class TypeOfTaxDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "Tax Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
	
    
    @ApiModelProperty(notes = "Tax Type Name")
	//@Size(min = 1, max = 50)
    private String taxTypeName;
    
    
    @ApiModelProperty(notes = "Status")
    private Boolean status;

}
