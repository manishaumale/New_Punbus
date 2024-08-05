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
@ApiModel(description = "Bus Unavailable Reason Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusUnavailablityReasonMasterDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "BusUnavailableReason Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Reason")
    private String reason;
    
    public BusUnavailablityReasonMasterDto(Integer id, String reason) {
		super();
		this.id = id;
		this.reason = reason;
	}

}
