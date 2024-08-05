package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.DepotMaster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Depot Transport Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepotTransportDto extends BaseEntityDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Depot Transport Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Transport Model")
	private TransportDto transportUnitMaster;
	
	@JsonIgnore
	@ApiModelProperty(notes = "Depot Model")
	private DepotMasterDto depotMaster;
	
	

}

