package com.idms.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.AddBlueDrumMasterDto;
import com.idms.base.api.v1.model.dto.BusRegNoDto;
import com.idms.base.api.v1.model.dto.BusTypeTyreTypeSizeForm;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.DepotMasterDetailsDto;
import com.idms.base.api.v1.model.dto.MakerTyreDetailsFormDto;
import com.idms.base.api.v1.model.dto.MobilOilDrumMasterDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.TaxMasterDto;
import com.idms.base.dao.entity.AddBlueDrumMaster;
import com.idms.base.dao.entity.BusSubTypeMaster;
import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.BusTyreTypeSizeMapping;
import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MakerTyreDetails;
import com.idms.base.dao.entity.MobilOilDrumMaster;
import com.idms.base.dao.entity.StateMaster;
import com.idms.base.dao.entity.TaxMaster;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TypeOfTaxMaster;
import com.idms.base.dao.entity.TyreSize;
import com.idms.base.dao.entity.TyreType;
import com.idms.base.support.persist.ResponseStatus;

public interface BasicMasterService {

	List<StateMaster> findAllStatesByActiveStatus();

	List<CityMaster> findAllCityByActiveStatus(Integer id);

	List<TransportUnitMaster> findAllTransportUnitByActiveStatus();

	ResponseEntity<ResponseStatus> saveDepotMaster(DepotMaster depotMaster);

	List<DepotMaster> findAllDepotMasterByActiveStatus(List<Integer> tpIds);

	ResponseEntity<ResponseStatus> saveStateMaster(StateMaster stateMaster);

	ResponseEntity<ResponseStatus> saveCityMaster(CityMaster cityMaster);

	ResponseEntity<ResponseStatus> saveTransportMaster(TransportUnitMaster transportUnitMaster);

	List<CityDto> findAllCityByActiveStatus();

	List<TypeOfTaxMaster> findAllTaxTypeByStatus();

	ResponseEntity<ResponseStatus> saveBusTypetMaster(BusTyperMaster busTyperMaster);

	List<BusTyperMaster> getAllBusTypeMaster();

	ResponseEntity<ResponseStatus> saveBusSubTypetMaster(BusSubTypeMaster busSubTypeMaster);

	List<BusSubTypeMaster> getAllBusSubTypeMaster();

	ResponseEntity<ResponseStatus> saveTaxMaster(TaxMaster taxMaster);

	List<TaxMasterDto> getAllTaxMaster();

	StateDto findByStateId(Integer id);

	ResponseEntity<ResponseStatus> updateStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateCityMasterStatus(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateTransportMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateDepotMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateBusTypeStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateBusSubTypeStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateTaxMasterStatusFlag(Integer id, Boolean flag);

	List<TransportUnitMaster> allTransportMasterByDepot(Integer depotId);

	Optional<DepotMaster> getDepotById(Integer id);
	
	List<TransportUnitMaster> findAllTUByActiveStatus();

	List<TyreType> getAllTyreType();

	ResponseEntity<ResponseStatus> saveTyreTypetMaster(TyreType tyreType);

	ResponseEntity<ResponseStatus> updateTyreTypeStatusFlag(Integer id, Boolean flag);

	List<TyreSize> getAllTyreSize();

	ResponseEntity<ResponseStatus> saveTyreSizeMaster(TyreSize tyreSize);

	ResponseEntity<ResponseStatus> updateTyreSizeStatusFlag(Integer id, Boolean flag);

	BusTypeTyreTypeSizeForm getBusTyreTypeSizeFormData();

	List<BusTyreTypeSizeMapping> getAllBusTyreTypeSizeMapping();

	ResponseEntity<ResponseStatus> saveBusTyreTypeSizeMapping(BusTyreTypeSizeMapping map);

	MakerTyreDetailsFormDto getFormLoadTyreMakerDetails();

	List<MakerTyreDetails> getTyreMakerDetailsList();

	ResponseEntity<ResponseStatus> saveTyreMakerDetails(MakerTyreDetails map);

	List<BusRegNoDto> getBusRegNoList();

	List<MobilOilDrumMasterDto> getDrumMasterListOnLoad(String depotCode);

	ResponseEntity<ResponseStatus> saveDrumMaster(MobilOilDrumMaster mobilOilDrumMaster);

	List<AddBlueDrumMasterDto> getAddBlueDrumMasterList(String depotCode);

	ResponseEntity<ResponseStatus> saveAddBlueDrumMaster(AddBlueDrumMaster map);
	
	List<DepotMasterDetailsDto> getDepotmasterIdAndDepocode();
	ResponseEntity<ResponseStatus> updateBusTyreTypeSizeMapping(Integer id, Boolean flag);

}
