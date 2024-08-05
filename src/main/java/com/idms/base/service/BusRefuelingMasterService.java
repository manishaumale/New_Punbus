package com.idms.base.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.BusDieselCorrectionDto;
import com.idms.base.api.v1.model.dto.BusRefuelingListDtoParent;
import com.idms.base.api.v1.model.dto.DailyRosterViewOnlyDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.RefuelingViewDto;
import com.idms.base.dao.entity.BusRefuelingMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface BusRefuelingMasterService {

	ResponseEntity<Object> busRefuelingMasterOnLoad(String depotCode);

	DailyRosterViewOnlyDto fetchRosterDetailsByBusId(Integer busId,Integer reason);

	ResponseEntity<ResponseStatus> saveBusRefuelingMaster(BusRefuelingMaster busRefuelingMaster, MultipartFile uploadFile, MultipartFile mobilOilBillFile, MultipartFile redBlueBillFile);

	DispensingUnitMasterDto fetchDispensingDataByDuId(Integer dispensingId);

	List<BusRefuelingMaster> getAllBusRefuelingMaster();

	BusRefuelingListDtoParent getBusRefuelingListByDepot(String dpCode);

	RefuelingViewDto getBusRefuelingById(Integer id);
	
	ResponseEntity<BusDieselCorrectionDto> getBusfuelingCorrectionData(Integer busId,String refuelingDate) throws ParseException;
	
	ResponseEntity<Boolean> postBusfuelingCorrectionData(BusDieselCorrectionDto input);
	
	List<BusDieselCorrectionDto> getAllBusfuelingCorrectionData();

	ResponseEntity<ResponseStatus> deleteBusRefuellingByStatusFlag(Integer id);

	//ResponseEntity<ResponseStatus> transferDieselFromOneToAnother(TransferDieselFromOneToAnotherDto transferDto);

	//List<TransferDieselFromOneToAnotherDto> getAllTransferedRecords();

}
