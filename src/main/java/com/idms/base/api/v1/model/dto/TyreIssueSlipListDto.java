package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class TyreIssueSlipListDto {

	private List<TyreIssueSlipDto> beforeTyreChange;
	
	private List<TyreIssueSlipDto> tyreReplacement;
}
