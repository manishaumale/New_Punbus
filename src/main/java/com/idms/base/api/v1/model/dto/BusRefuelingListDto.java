package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Refueling List Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusRefuelingListDto {
	
	private Integer id;
	
	private String busNumber;
	
	private Float dieselIssued;
	
	private Float dieselFromOutside;
	
	private Date dieselIssuDate;
	
	private Integer grossKms;
	
	private Float kmplAsGrossKms;
	
	private Float totalActualKms;
	
	private Float kmplAsTotalKms;
	
	private String routeName;
	
	private String vtsKms;
	
	private String kmplAsPerVts;
	
    private String scheduledKms;
	
	private String kmplAsPerScheduled; 
	
	private DispensingUnitHelperDto dispensingUnitDto;

}
