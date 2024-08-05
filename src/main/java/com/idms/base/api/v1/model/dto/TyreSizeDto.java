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
@ApiModel(description = "Tyre Size Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreSizeDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Tyre Size Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Tyre Size Name")
    private String size;
    
    public TyreSizeDto(Integer id, String size) {
		super();
		this.id = id;
		this.size = size;
	}

}
