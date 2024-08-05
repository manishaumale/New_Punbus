package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Mark Spare List Model")
public class MarkSpareListDto {
	
	private String remarks;
	
	private String spareName;
	
	private String sparetype;
	
	
}
