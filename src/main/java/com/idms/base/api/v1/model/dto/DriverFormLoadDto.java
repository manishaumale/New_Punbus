package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class DriverFormLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Depot List")
	private DepotMasterDto depotList;
	
	@ApiModelProperty(notes = "Employee Type List")
	private List<EmploymentTypeDto> employeeTypeList;
	
	@ApiModelProperty(notes = "Transport List")
	private List<TransportDto> transportList;
	
	@ApiModelProperty(notes = "Preferred Route List")
	private List<DepotMasterDto> depotOfInductionList;
	
	@ApiModelProperty(notes = "Driver Category List")
	private List<RouteCategoryDto> categoryList;
	
	
	
}
