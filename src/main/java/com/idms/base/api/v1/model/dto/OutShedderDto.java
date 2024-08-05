package com.idms.base.api.v1.model.dto;

import java.time.LocalTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "Out Shedder")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OutShedderDto {

	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Bus Master")
	private BusMasterDto bus;
	
	@ApiModelProperty(notes = "Driver Master")
	private DriverMasterDto driver;
	
	@ApiModelProperty(notes = "Conductor Master")
	private ConductorMasterDto conductor;
	
	private String routeName;

	private String rotaId;

	private String dailyId;

	@ApiModelProperty(notes = "Depot Master")
	private DepotMasterDto depot;

	private String rotaDate;

	private String fromState;

	private String toState;

	private String inTime;

	private String outTime;
	
	private String outFlag;
	
    private String tripStartTime;
	
	private String tripEndTime;
	
	
}
