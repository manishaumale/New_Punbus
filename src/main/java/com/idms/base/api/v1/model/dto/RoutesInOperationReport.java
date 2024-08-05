package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Routes In Operation Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutesInOperationReport {

    /**
	 * @author Hemant Makkar
	 */
	
	private String routeCode;
	@ApiModelProperty(notes = "Route No")
    private String routeName;
	
	@ApiModelProperty(notes = "Kilometeres")
    private String kms;
	
    private String totalKms;
	@ApiModelProperty(notes = "Route Details")
    private String routeDetails;
	
	
	@ApiModelProperty(notes = "State Wise Kilometeres")
    private String stateWiseKms;
	
	@ApiModelProperty(notes = "OverTime")
    private String overtime;
	
	@ApiModelProperty(notes = "Nights")
    private String nights;
	
	@ApiModelProperty(notes = "Hill Kilometers")
    private String hillKilometers;
	
	@ApiModelProperty(notes = "Plain Kilometers")
    private String plainlKilometers;
    
}
