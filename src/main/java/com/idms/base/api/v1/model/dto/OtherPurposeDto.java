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
@ApiModel(description = "Other Purpose Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherPurposeDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Other Purpose Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Other Purpose Name")
    private String purposeName;
	
	private String shortCode;
	
	public OtherPurposeDto(Integer id,String purposeName) {
		   this.id = id;
		   this.purposeName =  purposeName;
	}
	
}
