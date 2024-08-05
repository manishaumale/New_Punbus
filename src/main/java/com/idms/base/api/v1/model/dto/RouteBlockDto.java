package com.idms.base.api.v1.model.dto;

import com.idms.base.dao.entity.DepotMaster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "Out Shedder")
@Data
public class RouteBlockDto {

	@ApiModelProperty(notes = "Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;

	@ApiModelProperty(notes = "Route Master")
	private RouteMasterDto route;
	
	@ApiModelProperty(notes = "Depot Master")
	private DepotMasterDto depot;
	
	private String fromDate;

	private String toDate;

	private String detailedReason;
}
