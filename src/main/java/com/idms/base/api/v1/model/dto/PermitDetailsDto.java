package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

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
@ApiModel(description = "Permit Details Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermitDetailsDto extends BaseEntityDto {
	
	@ApiModelProperty(notes = "Permit Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Permit Number")
	private String permitNumber;
	
	@ApiModelProperty(notes = "Permit Name")
	private String permitName;
	
	@ApiModelProperty(notes = "Permit Issuing Authority")
	private String permitIssuingAuthority;

	@ApiModelProperty(notes = "Permit Valid Datte")
	private Date validUpTo;
	
	@ApiModelProperty(notes = "Permit Issuing Datte")
	private Date issueDate;

	@ApiModelProperty(notes = "State Model")
	private StateDto fromState;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto fromCity;
	
	@ApiModelProperty(notes = "State Model")
	private StateDto toState;
	
	@ApiModelProperty(notes = "City Model")
	private CityDto toCity;
	
	@ApiModelProperty(notes = "Bus Type Model")
	private BusTypeDto busTyperMaster;
	
	@ApiModelProperty(notes = "Depot  Model")
	private DepotMasterDto depotMaster;
	
	@ApiModelProperty(notes = "Transport  Model")
	private TransportDto transport;
	
	@ApiModelProperty(notes = "Total Trips")
    private Integer totalTrips;
	
	@ApiModelProperty(notes = "Total Kms")
	private Integer totalKms;
	
	@ApiModelProperty(notes = "Plain Kms")
	private Integer plainKms;
	
	@ApiModelProperty(notes = "Hill Kms")
	private Integer hillKms;
	
	@ApiModelProperty(notes = "Via Info List")
	private List<ViaInformationDto> viaInfoList;
	
	@ApiModelProperty(notes = "State Wise Km List")
	private List<StateWiseKmDto> stateWiseKmList;
	
	private DocumentDto document;
	
	public PermitDetailsDto(Integer id, String permitNumber) {
		super();
		this.id = id;
		this.permitNumber = permitNumber;
	}
	
	public PermitDetailsDto(Integer id, String permitNumber,Integer totalTrips,Date validUpTo) {
		super();
		this.id = id;
		this.permitNumber = permitNumber;
		this.totalTrips = totalTrips;
		this.validUpTo =  validUpTo;
	}
	
	
}