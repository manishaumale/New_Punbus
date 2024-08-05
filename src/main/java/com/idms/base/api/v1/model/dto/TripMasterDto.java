package com.idms.base.api.v1.model.dto;

import java.util.Date;
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
@ApiModel(description = "Trip Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripMasterDto extends BaseEntityDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Scheduled Kms")
	private Integer scheduledKms;
	
	@ApiModelProperty(notes = "Dead Kms")
	private Integer deadKms;
	
	@ApiModelProperty(notes = "Trip Start Date")
    private Date tripStartDate;
	
	@ApiModelProperty(notes = "Trip End Date")
	private Date tripEndDate;
	
	@ApiModelProperty(notes = "Trip Night Halt")
	private Integer nightHalt;
	
	@ApiModelProperty(notes = "Route Model")
	@JsonIgnore
	private RouteMasterDto routeMaster;
	
	@ApiModelProperty(notes = "Stopages Master List")
	private List<StopagesMasterDto> stopagesMasterList;
	
	@ApiModelProperty(notes = "Toll Tax Wrapper List")
	private List<TollTaxWrapperDto> tollTaxWrapperList;
	
	@ApiModelProperty(notes = "Bus Stand Wrapper List")
	private List<BusStandWrapperDto> busStandWrapperList;
	
	private String tripStartTime;
	
	private String tripEndTime;
	
	private String serviceId;
	
	private CityDto fromCity;
	
	private CityDto toCity;
	
	private String upDown;
	
	private String routeName;
	
	private TotalNightsDto totalNightsMaster;
	
	private String totalOt;
		
	private Integer dutyCounts;
	
	private List<TripRotationDto> tripRotation;
	
	private String tripCode;
	
	
	public TripMasterDto(Integer id, String tripStartTime) {
		super();
		this.id = id;
		this.tripStartTime = tripStartTime;
	}
	
	
}
