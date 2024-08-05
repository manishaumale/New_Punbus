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
@ApiModel(description = "City Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityDto extends BaseEntityDto{

    @ApiModelProperty(notes = "City Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "City Name")
   	//@Size(min = 1, max = 50)
    private String cityName;
    
    
    @ApiModelProperty(notes = "City Code")
   	//@Size(min = 1, max = 4)
    private String cityCode;
    
    @ApiModelProperty(notes = "Status")
    private Boolean status;
    
    @ApiModelProperty(notes = "State Model")
    private StateDto state;
    

    public CityDto(Integer id, String cityName) {
		super();
		this.id = id;
		this.cityName = cityName;
	}

}