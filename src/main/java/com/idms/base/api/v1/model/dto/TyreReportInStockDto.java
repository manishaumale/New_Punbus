package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Report In Stock")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreReportInStockDto {
	
	
	@ApiModelProperty(notes = "New/Resole Tyre")
	private String  newOrResoleTyre;
	
	private String tyreNumber;
	
	@ApiModelProperty(notes = "Value")
	private String  value;
	
	

}
