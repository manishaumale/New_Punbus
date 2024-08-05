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
@ApiModel(description = "RetreadingDocket Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RetreadingDocketDto {
	
	private Integer id;
	
	private String docketNumber;
	
	private List<DocketTyreAssociationDto> tyreLists;
	
	private DepotMasterDto depot;
	
	private boolean isClosed;
	
	private String dpCode;
	
	private Integer tyreCount;
	
	private String date;
	

}
