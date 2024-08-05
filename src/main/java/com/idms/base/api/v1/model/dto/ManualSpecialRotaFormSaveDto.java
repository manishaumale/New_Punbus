package com.idms.base.api.v1.model.dto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Manual Special Save Rota")
public class ManualSpecialRotaFormSaveDto {
	
	private Integer routeId;
	
	private Integer tripId;
	
	private Integer busId;
	
	private Integer driverId;
	
	private Integer conductorId;
	
	private Integer rotaId;
	
	private String overrideReason;
	
	private String remarks;
	
}


