package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel(description = "Toll Tax Wrapper Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TollTaxWrapperDto extends BaseEntityDto{
	
	/**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Toll Tax Wrapper Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Toll Tax Model")
	private TollTaxMasterDto tollTaxMaster;
	
	@ApiModelProperty(notes = "Trip Master Model")
	@JsonIgnore
	private TripMasterDto trip;
	
	@ApiModelProperty(notes = "Route Rotation Model")
	@JsonIgnore
	private RouteRotationDto routeRotation;
	
    private String tollName;
	
	private Integer tollFees;

}
