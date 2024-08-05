package com.idms.base.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.AlertDto;
import com.idms.base.api.v1.model.dto.DipChartReadingsDto;
import com.idms.base.api.v1.model.dto.ExcessShortDto;
import com.idms.base.api.v1.model.dto.FuelTankFormLoadDto;
import com.idms.base.api.v1.model.dto.FuelTankMasterDto;
import com.idms.base.api.v1.model.dto.TankCapacityMasterDto;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface FuelTankMasterService {

	FuelTankFormLoadDto fuelTankMasterFormOnLoad();

	ResponseEntity<ResponseStatus> saveFuelTankMaster(FuelTankMaster fuelTankMaster, MultipartFile uploadFile);

	ResponseEntity<ResponseStatus> updateFuelTankMasterStatusFlag(Integer id, Boolean flag);

	List<FuelTankMasterDto> listOfAllFuelTankMaster(String depotCode);
	// List<FuelTankMaster> listOfAllFuelTankMaster(String depotCode);

	public ResponseEntity<ResponseStatus> updateExplosiveLicense(Integer id, Date explosiveRenewalDate,
			MultipartFile uploadFile);

	public List<AlertDto> explosiveCertificateExpiry() throws ParseException;

	public ResponseEntity<ResponseStatus> addTankCapacity(TankCapacityMasterDto input);

	public ResponseEntity<ResponseStatus> updateCleaningDate(Integer id, Date cleaingRenewalDate);

	public ResponseEntity<ResponseStatus> saveDipChartReadings(DipChartReadingsDto input);

	public List<DipChartReadingsDto> getAllDipChartReadings(String depot, String userName);

	public List<TankCapacityMasterDto> findAllTankCapacityMaster();

	public ExcessShortDto calculateExcessShort(Integer tankId, Double volume);

	public ExcessShortDto calculateExcessShort2(Integer tankId, Double volume);

	public ResponseEntity<ResponseStatus> deleteFuelTankMasterStatusFlag(Integer id);

	public FuelTankMasterDto checkTankCode(String tankCode);

	public String sendSms() throws Exception;

	Float mixtureCostCalculation(Integer tankId);

	DipChartReadingsDto viewDipChartObjectById(Integer id);

	ResponseEntity<ResponseStatus> validateDuplicateCode(String tankCode);

}
