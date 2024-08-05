package com.idms.base.api.v1.model.dto;

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
@ApiModel(description = "Bus Stand Wrapper Sec Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusStandWrapperSecTripDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Bus Stand Wrapper Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Bus Stand Model")
	private BusStandDto busStandMaster;
	
	@ApiModelProperty(notes = "Trip Master Model")
	@JsonIgnore
	private TripMasterDto tripMasterObj;
    

}