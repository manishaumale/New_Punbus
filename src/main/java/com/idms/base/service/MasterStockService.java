package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.CentralTicketStockDto;
import com.idms.base.api.v1.model.dto.MasterFormListDataDto;
import com.idms.base.api.v1.model.dto.MasterStockDenominationDTO;
import com.idms.base.api.v1.model.dto.MasterStockDto;
import com.idms.base.api.v1.model.dto.MasterStockfrmLoadDto;
import com.idms.base.dao.entity.CentralTicketStock;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.support.persist.ResponseStatus;

public interface MasterStockService {

	MasterStockfrmLoadDto masterFormOnLoad(String dpcode);

	ResponseEntity<ResponseStatus> saveCentralStock(CentralTicketStock centralTicketStock);

	List<CentralTicketStockDto> getMasterStockListUpdated(String tpGroup);

	List<MasterStockDto> getMasterStockTicketList(Integer tpId, Integer denoId);

}
