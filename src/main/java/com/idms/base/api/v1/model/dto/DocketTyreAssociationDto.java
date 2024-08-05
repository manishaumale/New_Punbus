package com.idms.base.api.v1.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.TyreMaster;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "DocketTyreAssociation Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocketTyreAssociationDto {
	
	private Integer id;
	
	private RetreadingDocketDto docket;
	
	private TyreMasterDto tyre;
	
	private boolean isReceived;
	
	private boolean isTreaded;
	
	private Date receivedDate;
	
	private String remarks;
	
	private String totalKilometerDone;
	
	private Integer expectedLife;
		
   private Double tyreCost;
	
	

}
