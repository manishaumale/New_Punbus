package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Inventory Report")
public class BusWiseTyreInventoryReportDto {
	
	
	@ApiModelProperty(notes = "Depot Name")
	private String  depotName;
	
	@ApiModelProperty(notes = "Bus No")
	private String  busNumber;
	
	private String name;
	
	private String tyrecount;
	
	@ApiModelProperty(notes = "Front Right")
	private String  frontRight;
	
	@ApiModelProperty(notes = "Front Left")
	private String  frontLeft;
	
	
	@ApiModelProperty(notes = "Rear Right Inner")
	private String  rearRightInner;
	
	@ApiModelProperty(notes = "Rear Right Outer")
	private String  rearRightOuter;
	
	
	@ApiModelProperty(notes = "Rear Left Inner")
	private String  rearLeftInner;
	
	@ApiModelProperty(notes = "Rear Left Outer")
	private String  rearLeftOuter;
	
	@ApiModelProperty(notes = "Spare No")
	private String  spareNumber;
	
}
