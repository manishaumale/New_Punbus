package com.idms.base.api.v1.model.dto;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Master Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BusMasterDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Bus Registered No")
	private String  busRegNumber;
	
	@ApiModelProperty(notes = "Bus Maker Model")
	private BusMakerDto maker;
	
	@ApiModelProperty(notes = "Bus Model No")
	private String busModel;
	
	@ApiModelProperty(notes = "Transport Unit Model")
	private TransportDto transportUnit;
	
	@ApiModelProperty(notes = "Bus Type Model")
	private BusTypeDto busType;
	
	@ApiModelProperty(notes = "Bus Sub Type Model")
	private BusSubTypeDto busSubType;
	
    private Double busCost;
	
	private Double chessisCost;
	
	private Double bodyCost;
	
	
	private String busTypeName;
	private String busTypes;
	
	@ApiModelProperty(notes = "Depot Model")
	private DepotMasterDto depot;
	
	@ApiModelProperty(notes = "Driver Model")
	private DriverMasterDto primaryDriver;
	
	@ApiModelProperty(notes = "Driver Model")
	private DriverMasterDto secondaryDriver;
	
    private String chessisNumber;
	
	private String engineNumber;
	
	private String frontAxle;
	
	private String rearAxle;
	
	private Integer totalSeats;
	
	private Date chessisPurDate;
	
	private Date bodyFabricateDate;
	
	private String fabricatorName;
	
	private Date busPassingDate;
	
	@ApiModelProperty(notes = "Euro Model")
	private EuroNormsDto euroNorm;
	
	@ApiModelProperty(notes = "Bus Layout Model")
	private BusLayoutDto layout;
	
	@ApiModelProperty(notes = "Bus Assembly List")
	private List<BusAssemblyDto> assemblies;
	
	private String serviceId;
	
	private List<TyreMasterDto> tyreList;
	
	private BusTyreCountDto tyreCount;
	
	private List<BusTyreAssociationDto> tyreLists;
	
	private Boolean status;
	
	private LocalTime availableTime; 
	
	private CityDto availableCity;
	
	private Boolean spareBus;
	
	public BusMasterDto(Integer id,String  busRegNumber) {
		this.id = id;
		this.busRegNumber = busRegNumber;
	}
	
	List<TyrePositionDto> tyrePositions;
	
	
}
