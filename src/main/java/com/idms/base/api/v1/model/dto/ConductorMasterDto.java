package com.idms.base.api.v1.model.dto;

import java.time.LocalTime;
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
@ApiModel(description = "Conductor Master Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConductorMasterDto extends BaseEntityDto{
	
	@ApiModelProperty(notes = "Conductor Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    private String conductorName;
	
	private String conductorCode;
	
	private String fatherName;
	
	private String licenceNo;
	
	private String licenceIssuePlace;
	
	private Date licenceValidity;
	
	private String badgeNumber;
	
    private String epfGpfNumber;
	
	private String esiNumber;
	
	private String address;
	
	private String mobileNumber;
	
	@ApiModelProperty(notes = "Depot Model")
	private DepotMasterDto depot;
	
	@ApiModelProperty(notes = "Depot Model")
	private DepotMasterDto inductionDepot;
	
	@ApiModelProperty(notes = "Employee Model")
	private EmploymentTypeDto employmentType;
	
	@ApiModelProperty(notes = "Transport Model")
	private TransportDto transportUnit;
	
	@ApiModelProperty(notes = "Route Category Model")
	private RouteCategoryDto conductorCategory;
	
	private LocalTime availableTime; 
	
	private CityDto availableCity;
	
	public ConductorMasterDto(Integer id, String conductorName) {
		super();
		this.id = id;
		this.conductorName = conductorName;
	}

}
