package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.DepotTransportUnit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Depot Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepotMasterDto extends BaseEntityDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Depot Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Depot Name")
	// @Size(min = 1, max = 50)
	private String depotName;
	
	@ApiModelProperty(notes = "Depot Address")
	// @Size(min = 1, max = 50)
	private String depotAddress;

	@ApiModelProperty(notes = "Depot Code")
	// @Size(min = 1, max = 10)
	private String depotCode;

	@ApiModelProperty(notes = "State Model")
	private StateDto state;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto city;
	
	@ApiModelProperty(notes = "Transport Model")
	private TransportDto transport;
	
	@ApiModelProperty(notes = "Pin Code")
	private String pinCode;
	
	@ApiModelProperty(notes = "Fuel Level")
	private String fuelLevel;
	
	private List<DepotTransportDto> depotTransportList;
	
	public DepotMasterDto(Integer id, String depotName) {
		super();
		this.id = id;
		this.depotName = depotName;
	}
	

}
