package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idms.base.dao.entity.TransportUnitMaster;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "AuctionedDocketDto Model")
public class AuctionedDocketDto {
	
	private Integer id;
	
	private String docketNumber;
	
	//@JsonIgnore
	private List<AucDocketTyreAssociationDto> tyreLists;
	
	private TransportDto transport;
	
	//@JsonIgnore
	private DepotMasterDto depot;
	
	private Date auctionDate;
	
	private String bidderName;
	
	private Double bidAmount;
	
	private String dpcode;

}
