package com.idms.base.api.v1.model.dto;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Transient;

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
@ApiModel(description = "Driver Master Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverMasterDto extends BaseEntityDto{

    @ApiModelProperty(notes = "Driver Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Driver Name")
    private String driverName;
	
    private String driverCode;
	
	private String fatherName;
	
	private String licenceNo;
	
	private String licenceIssuePlace;
	
	private Date licenceValidity;
	
	@ApiModelProperty(notes = "Depot Model")
	private DepotMasterDto depot;
	
	private String badgeNumber;
	
	@ApiModelProperty(notes = "Depot Model")
	private DepotMasterDto inductionDepot;
	
	@ApiModelProperty(notes = "Employee Model")
	private EmploymentTypeDto employmentType;
	
	@ApiModelProperty(notes = "Transport Model")
	private TransportDto transportUnit;
	
    private String epfGpfNumber;
	
	private String esiNumber;
	
	private String address;
	
	private String mobileNumber;
	
	@ApiModelProperty(notes = "Route Category Model")
	private RouteCategoryDto driverCategory;
	
	private LocalTime availableTime; 
	
	private CityDto availableCity;
    
    public DriverMasterDto(Integer id, String driverName) {
		super();
		this.id = id;
		this.driverName = driverName;
	}

}
