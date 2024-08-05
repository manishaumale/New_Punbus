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
@ApiModel(description = "Euro Norms Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EuroNormsDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Norm Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Norm Name")
    private String normName;
    
    public EuroNormsDto(Integer id, String name) {
		super();
		this.id = id;
		this.normName = name;
	}

}