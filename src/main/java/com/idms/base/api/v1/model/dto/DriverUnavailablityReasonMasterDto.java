package com.idms.base.api.v1.model.dto;

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
@ApiModel(description = "Driver Unavailable Reason Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverUnavailablityReasonMasterDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "DriverUnavailableReason Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Reason")
    private String reason;
    
    public DriverUnavailablityReasonMasterDto(Integer id, String reason) {
		super();
		this.id = id;
		this.reason = reason;
	}

}
