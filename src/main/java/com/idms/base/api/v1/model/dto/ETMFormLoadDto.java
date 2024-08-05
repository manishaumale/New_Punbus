package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class ETMFormLoadDto {
	
	/**
	 * @author Hemant Makkar
	 */

	@ApiModelProperty(notes = "Transport Master List")
	private List<TransportDto> transportList;
	
	@ApiModelProperty(notes = "Makers List")
	private List<ETMMakerMasterDto> makersList;
	
	@ApiModelProperty(notes = "gsmAndGPs List")
	private List<GSMAndGPSMasterDto> gsmAndGPsList;
	
	@ApiModelProperty(notes = "RFID  List")
	private List<RFIDMasterDto> rfidList;
	
	
	
	
}
