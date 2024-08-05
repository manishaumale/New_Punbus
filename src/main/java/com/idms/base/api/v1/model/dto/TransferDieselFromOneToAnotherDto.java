package com.idms.base.api.v1.model.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Transfer Diesel From One To Another Dto Model")
public class TransferDieselFromOneToAnotherDto {
	
	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	private TransportDto fromTransport;
	
	private TransportDto toTransport;
	
	private Boolean transferFlag;
	
	private FuelTankMasterDto fromFuelTank;
	
	private FuelTankMasterDto toFuelTank;
	
    private Double duStartReading;
	
	private Double duEndReading;
	
	private Float issuedDiesel;
	
	private DispensingUnitMasterDto dispensingDto;
	
    private Float dipReadingBefore;
	
	private Float dipReadingAfter;
	
    private Float volumeBeforeSupply;
	
	private Float volumeAfterSupply;
	
    private String intentNumber;
	
	private Date  intentDate;
	
	private Float dieselRate;
	
}
