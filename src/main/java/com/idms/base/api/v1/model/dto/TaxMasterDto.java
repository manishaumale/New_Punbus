package com.idms.base.api.v1.model.dto;

import java.util.Date;

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
@ApiModel(description = "Tax Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaxMasterDto extends BaseEntityDto{
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Tax Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Tax Amount")
	// @Size(min = 1, max = 50)
	private String taxAmount;

	@ApiModelProperty(notes = "Status")
	private Boolean status;
	
	@ApiModelProperty(notes = "Bus Type Model")
	private BusTypeDto busTyperMaster;

	@ApiModelProperty(notes = "State Model")
	private StateDto state;
	
	@ApiModelProperty(notes = "Type Of Tax Model")
	private TypeOfTaxDto typeOfTaxMaster;
	
	@ApiModelProperty(notes = "Applicable From")
	private Date applicableFrom;
	
	
	
	}