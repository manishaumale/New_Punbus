package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusTyreAssoParentWrapper;
import com.idms.base.api.v1.model.dto.BusTyreAssociationForm;
import com.idms.base.api.v1.model.dto.BusTyreAssociationList;
import com.idms.base.api.v1.model.dto.MakerTyreDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.api.v1.model.dto.OldTyreMasterDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyreMasterFormDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.entity.TyreSize;
import com.idms.base.dao.entity.TyreType;
import com.idms.base.support.persist.ResponseStatus;

public interface TyreManagementService {

	TyreMasterFormDto getTyreMasterForm(String dpCode);

	List<TyreMasterDto> getAllTyreListByDepot(String dpCode);

	List<TyreType> getTyreTypeList(Integer makerId);

	List<TyreSize> getTyreSizeList(Integer tyreMakerId, Integer tyreTypeId);

	MakerTyreDetailsDto getTyreCostMilage(Integer tyreMakerId, Integer tyreTypeId, Integer tyreSizeId);

	ResponseEntity<ResponseStatus> saveTyreDetails(TyreMaster typeMaster);

	List<BusTyreAssociationList> getAllBusTyreAssociationList(String dpCode);

	BusTyreAssociationForm busTyreAssociationFormLoad(Integer busId);

	ResponseEntity<ResponseStatus> saveBusTyreAssociations(BusMaster tyreMaster);

	TyreMasterDto getTyreDetails(Integer tyreId);

	List<TyreConditionDto> getTyreConditionList();

	List<TyreMasterDto> getAllTyresByStatus();
	ResponseEntity<ResponseStatus> updateTyreDtls(String fromdate,String todate,Float oldMileage,Integer Id);
	TyreMasterDto getuseTyreId(String dpCode, Integer tyreConditionId);
	TyreMasterFormDto getNumbers(String dpCode, Integer tyreConditionId);

	ResponseEntity<ResponseStatus> saveTyreDetailsAndAssociations(BusTyreAssoParentWrapper parentDto);

	ResponseEntity<ResponseStatus> saveOldTyreUnfitted(TyreMaster tyreMaster);
	
	List<MarkSpareBusDetailsDto> getAllBusDetails(Integer transportId);

	List<OldTyreMasterDto> getAllOldTyreListByDepot(String dpCode);

	BusMasterDto getPurchaseDateByBus(Integer busId);

	List<TyreMasterDto> getTyreListBySizeMakeAndType(Integer sizeId, Integer makeId, Integer typeId, Integer positionId);

	OldTyreMasterDto getOldTyreByTyreId(Integer tyreId);

	ResponseEntity<ResponseStatus> validateDuplicateByTyreNo(String tyreNo);
	

}
