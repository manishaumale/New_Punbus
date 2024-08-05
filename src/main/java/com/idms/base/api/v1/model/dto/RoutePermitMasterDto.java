package com.idms.base.api.v1.model.dto;

import java.util.List;

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
@ApiModel(description = "Route Permit Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutePermitMasterDto extends BaseEntityDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Route Model")
	@JsonIgnore
	private RouteMasterDto route;
	
	@ApiModelProperty(notes = "Permit Model")
	//@JsonIgnore
	private PermitDetailsDto permitDetailsMaster;

	
	
	
}
