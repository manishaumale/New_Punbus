package com.idms.base.api.v1.model.dto;

import java.util.Date;
import java.util.List;

import com.idms.base.dao.entity.AuctionedDocket;
import com.idms.base.dao.entity.TyreMaster;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "AucDocketTyreAssociationDto Model")
public class AucDocketTyreAssociationDto {
	
	private Integer id;
	
	private AuctionedDocketDto docket;
	
	private TyreMasterDto tyre;

}
