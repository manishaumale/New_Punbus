package com.idms.base.api.v1.model.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.RoleDieselSupply;
import com.idms.base.dao.entity.TransportUnitMaster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Recieve Diesel Supply Model")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecieveDieselSupplyMasterDto extends BaseEntityDto{
	
	 /**
		 * @author Hemant Makkar
		 */
		
    @ApiModelProperty(notes = "Diesel Supply Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    private String invoiceNumber;
	
	private Date  invoiceDate;
	
	private String tankerNumber;
	
	private Float quantityRecieved;
	
	private Date  entryDate;
	
	private LocalDateTime entryTime;
	
	private Float temprature;
	
	private Float hydrometerReading;
	
	private Float dieselRate;
	
	@ApiModelProperty(notes = "Update Supply Model")
	private UpdateSupplyDto updateSupply;
	
	private List<RoleDieselSupplyDto> roleList;
    
    private TransportDto transportUnitMaster;
	

}
