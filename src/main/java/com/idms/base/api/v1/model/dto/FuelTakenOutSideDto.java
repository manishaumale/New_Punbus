package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Fuel Taken OutSide Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuelTakenOutSideDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Bus Master Model")
	private BusMasterDto busMaster;
	
	private Float quantity;
	
	@ApiModelProperty(notes = "Fuel Source Master Model")
	private FuelSourceMasterDto fuelSourceMaster;
	
	private String billNo;
	
	private Date fuelTakenDate;
	
	private Float amount;
	
	private Boolean status;
	
	private DocumentDto billDocument;
}
