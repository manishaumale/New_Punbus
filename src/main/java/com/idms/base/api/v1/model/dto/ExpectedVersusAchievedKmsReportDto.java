package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Expected Versus Achieved Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpectedVersusAchievedKmsReportDto {

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Expexted Kilometers")
    private String expectedKilometers;
    
	@ApiModelProperty(notes = "Achieved Kilometers")
    private String achievedKilometers;
    
    
    @ApiModelProperty(notes = "Difference")
    private String difference;
    
    @ApiModelProperty(notes = "Depot Name")
    private String depotName;
    
    @ApiModelProperty(notes = "VTS KMs")
    private String vtsKMs;
    
}
