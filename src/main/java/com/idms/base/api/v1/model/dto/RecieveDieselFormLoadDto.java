package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class RecieveDieselFormLoadDto {
	
	@ApiModelProperty(notes = "Tank Master List")
	private List<FuelTankMasterDto> fuelTankList;
	
	@ApiModelProperty(notes = "User Role List")
	private List<RoleDto> userRoleIst;
	
	@ApiModelProperty(notes = "Transport List")
	private List<TransportDto> transportList;
	
	@ApiModelProperty(notes = "Dispensing List")
	private List<DispensingUnitMasterDto> dispensingList;
	
	@ApiModelProperty(notes = "Closing List")
	private List<BookReadingClosingDto> closingList;
	
}
