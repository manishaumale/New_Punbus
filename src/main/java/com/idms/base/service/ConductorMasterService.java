package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.ConductorAndDriverBlockHistoryDto;
import com.idms.base.api.v1.model.dto.ConductorFormLoadDto;
import com.idms.base.api.v1.model.dto.DriverBlockHistoryDto;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface ConductorMasterService {

	ConductorFormLoadDto conductorMasterFormOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> updateConductorMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateConductorMasterIsDeletedFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> saveConductorMaster(ConductorMaster conductorMaster);

	List<ConductorMaster> getAllConductorMaster(String dpCode);

	List<ConductorMaster> fetchAllConductorMaster();
	List<ConductorMaster> getConductorNames();

	ResponseEntity<ResponseStatus> updateConductorMasterCategoryById(Integer conductorId, Integer categoryId);
	
	public ResponseEntity<ResponseStatus> unBlockConductor(Integer id,String type);
	
	public ResponseEntity<ResponseStatus> blockConductor(Integer id, String reason,Date fromDate,Date toDate,String type,String depotCode,String orderNo,Date orderDate);
	
	
	public List<ConductorAndDriverBlockHistoryDto> fetchAllBlockedConductor(String depotCode);
	
	public List<ConductorAndDriverBlockHistoryDto> fetchAllBlockedRoutes(String depotCode);
	
	public ResponseEntity<ResponseStatus> newBlockOrRouteOff(String reason,Integer id,String type,String depotId,Boolean routeOff,Boolean blocked,String orderNumber,Date orderDate);
	
	public ResponseEntity<ResponseStatus> unBlock(Integer id);
	
	public List<DriverBlockHistoryDto> findAllrouteOff(String depotCode);
	
	public List<ConductorMaster> findUnblockedConductors(String depotCode);



}
