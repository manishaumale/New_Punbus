package com.idms.base.api.v1.model.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "FormRotaListDto Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormRotaListDto {
	
	private List<RouteMasterDto> route;
	
	private List<TripMasterDto> trip;
	
	private List<DriverMasterDto> driverList;
	
	private List<ConductorMasterDto> conductorList;
	
	private List<BusMasterDto> busList;

}
