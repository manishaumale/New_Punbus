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
@ApiModel(description = "Tyre Type Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreTypeDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Tyre Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Tyre Type Name")
    private String name;
    
    public TyreTypeDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
