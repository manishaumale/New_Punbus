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
@ApiModel(description = "Penalty Type Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PenaltyTypeDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Penalty Type Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Penalty Type Name")
    private String name;
    
    public PenaltyTypeDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
    private DriverMasterDto driver;
    
    private ConductorMasterDto conductor;
}
