package com.idms.base.api.v1.model.dto;

import java.util.Date;

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
@ApiModel(description = "Bus Unavailability Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusUnavailabilityMasterDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Bus Unavailability Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Bus Model")
	private BusMasterDto busMaster;
	
	@ApiModelProperty(notes = "Reason Model")
	private BusUnavailabilityMasterDto busUnavailablityReasonMaster;
	
    private Date detentionDate;
	
	private Date likelyToReadyDate;
	
	private String detailedReason;
    

}
