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
@ApiModel(description = "Ticket Box Master Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketBoxMasterDto extends BaseEntityDto{
	
	@ApiModelProperty(notes = "Ticket Box Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    private String ticketBoxNumber;
	
	private TransportDto transportUnitMaster;
	
	private DepotMasterDto depot;
	
	private String depotCode;
    
	private Integer parentId;
}
