package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DipReadingMaster;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.entity.TankCapacityMaster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Fuel Tank Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuelTankMasterDto {
	

	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Fuel Tank Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Fuel Tank Name")
    private String tankName;
	
	@ApiModelProperty(notes = "Fuel Tank Code")
	private String tankCode;
	
	@ApiModelProperty(notes = "Tank Capacity Model")
	private TankCapacityMaster capacity;//vivek changed to entity from TankCapacityMasterDto
	
	@ApiModelProperty(notes = "Fuel Tank Diameter")
    private Float diameter;
	
	@ApiModelProperty(notes = "Fuel Tank Length")
	private Float length;
	
	@ApiModelProperty(notes = "Fuel Tank Radius")
	private Float radius;
	
	@ApiModelProperty(notes = "Fuel Tank DeadBoards")
	private Float deadBoards;
	
	@ApiModelProperty(notes = "Fuel Tank Installation Date")
	private Date installationDate;
	
	@ApiModelProperty(notes = "Fuel Tank Validity")
	private Date tankValidity;
	
	@ApiModelProperty(notes = "Fuel Tank Reorder Level")
	private Float reorderLevel;
	
	private DipReadingMasterDto2 dip;// vivek changed to entity from DipReadingMasterDto
	
	private Date explosiveLicenceValidity;
	
	private Double currentValue;
	
	private Date cleaningDate;
	
	private String tankUniqueId;
	
	private String explosiveCertificate;
	
	private Integer explosiveCertificateId;

	public FuelTankMasterDto(Integer id,String tankName) {
		   this.id = id;
		   this.tankName =  tankName;
	}
	
	public FuelTankMasterDto(Integer id,String tankName,Double currentValue) {
		   this.id = id;
		   this.tankName =  tankName;
		   this.currentValue = currentValue;
	}
	
	public FuelTankMasterDto(Integer id,String tankName,Double currentValue,Float currentPrice ) {
		   this.id = id;
		   this.tankName =  tankName;
		   this.currentValue = currentValue;
		   this.currentPrice = currentPrice;
	}
	
	@ApiModelProperty(notes = "status")
	private boolean status;
	

	public FuelTankMasterDto(Integer id, String tankName, String tankCode, TankCapacityMaster capacity, Float diameter,
			Float length, Float radius, Float deadBoards, Date installationDate, Date tankValidity, Float reorderLevel,
			 Date document, Double currentValue, Date cleaningDate,
			boolean status,DipReadingMasterDto2 dip,String tankUniqueId) {
		this.id = id;
		this.tankName = tankName;
		this.tankCode = tankCode;
		this.capacity = capacity;
		this.diameter = diameter;
		this.length = length;
		this.radius = radius;
		this.deadBoards = deadBoards;
		this.installationDate = installationDate;
		this.tankValidity = tankValidity;
		this.reorderLevel = reorderLevel;
		this.explosiveLicenceValidity = document;
		this.currentValue = currentValue;
		this.cleaningDate = cleaningDate;
		this.status = status;
		this.dip = dip;
		this.tankUniqueId=tankUniqueId;
	}
	
	private boolean checkTankCode;
	
	private String lastCleaningDate;
	
	private Float currentPrice;
}
