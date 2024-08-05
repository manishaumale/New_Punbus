package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AssignSubStockToDepoDto;
import com.idms.base.api.v1.model.dto.AssignedSubStockFromLoadDto;
import com.idms.base.api.v1.model.dto.DepotTicketStockDto;
import com.idms.base.api.v1.model.dto.MasterStockSerialDto;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.dao.entity.TicketDepotAssignment;
import com.idms.base.support.persist.ResponseStatus;

@Service
public interface AssignSubStockService {
	
	List<AssignSubStockToDepoDto> getAllSubStockDetailsBasedOnDepoCode(String depotCode);

	String addSubStockDetails(AssignSubStockToDepoDto assignSubStock);

	AssignSubStockToDepoDto findSubStockDtls(Integer subStockId);

	List<AssignSubStockToDepoDto> findSubStockDtlsOfDepo(String depotName);

	AssignedSubStockFromLoadDto fromOnLoadDetails(String depotCode);

	List<MasterStockSerialDto> getTicketBooks(Integer denoId, Integer transportId);

	ResponseEntity<ResponseStatus> saveTicketDepotAssignment(List<MasterStock> masterStockList);

	List<DepotTicketStockDto> getDepotTicketStockList(String tpList);

	List<DepotTicketStockDto> getTicketStockByDenomination(Integer denomination,Integer centralStockId);

}
