package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
//@ApiModel(description = "Tyre Model")
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class BusTyreAssoParentWrapper extends BaseEntityDto {

	@ApiModelProperty(notes = "Tyre Id", accessMode = AccessMode.READ_ONLY)
	private Integer id;
	
	@ApiModelProperty(notes = "Transport Model")
	private TransportDto transportUnit;
	
	private String invoiceNumber;
	
	private Date invoiceDate;
	
	private BusMasterDto busMaster;
	
	@ApiModelProperty(notes = "Depot Model")
	private DepotMasterDto depot;
	
	private List<BusTyreAssoChildWrapper> childDto;
	
	

}
