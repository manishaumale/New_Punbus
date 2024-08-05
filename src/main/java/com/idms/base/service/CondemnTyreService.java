package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.AuctionedDocketDto;
import com.idms.base.api.v1.model.dto.RetreadingDocketDto;
import com.idms.base.api.v1.model.dto.TyreCondemnFormDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.dao.entity.AuctionedDocket;
import com.idms.base.support.persist.ResponseStatus;

public interface CondemnTyreService {

	TyreCondemnFormDto getCondemnTyreForm(String dpCode);

	List<TyreMasterDto> getTyreList(Integer tpId, Integer sizeId, String dpCode);

	ResponseEntity<ResponseStatus> saveMarkedForAuction(RetreadingDocketDto docketDto);

	List<TyreMasterDto> getCondemnAvailTyreList(Integer tpId, Integer sizeId, String dpCode);

	ResponseEntity<ResponseStatus> saveAuctionedCondemnTyres(AuctionedDocketDto docketDto);

	List<AuctionedDocketDto> getAuctionedDocketList(String dpCode);

	List<TyreMasterDto> getAuctionedTyreList(Integer docketId);

}
