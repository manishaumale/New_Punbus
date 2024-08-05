package com.idms.base.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.CalibrationDispensingUnitDto;
import com.idms.base.api.v1.model.dto.InspectStockDto;
import com.idms.base.api.v1.model.dto.calibrationExcessShortDto;
import com.idms.base.dao.entity.CalibrationExcessShort;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DispensingUnitCalibration;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.TankCapacityMaster;
import com.idms.base.dao.entity.TankInspection;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DispensingUnitCalibrationRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.DocumentRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.ReadingMasterRepository;
import com.idms.base.dao.repository.TankCapacityMasterRepository;
import com.idms.base.dao.repository.TankInspectionRepository;
import com.idms.base.dao.repository.calibrationExcessShortRepo;
import com.idms.base.service.InspectStockService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InspectStockServiceImpl implements InspectStockService {

	@Autowired
	TankCapacityMasterRepository tankCapacityMasterRepo;

	@Autowired
	FuelTankMasterRepository fuelTankMasterRepo;

	@Autowired
	ReadingMasterRepository readingMasterRepo;

	@Autowired
	DepotMasterRepository depotMasterRepo;

	@Autowired
	TankInspectionRepository tankInspectionRepo;

	// Calibration Dispensing Unit Objects

	@Autowired
	DispensingUnitRepository dispensingUnitRepo;

	@Autowired
	DispensingUnitCalibrationRepository dispensingUnitCalibrationRepo;
	
	@Autowired
	calibrationExcessShortRepo calibrationExcessShortRepo;

	@Value("${file.path}")
	private String filePath;

	@Autowired
	DocumentRepository documentRepository;

	HttpStatus status = HttpStatus.FORBIDDEN;

	@Override
	public ResponseEntity<List<InspectStockDto>> getInspectStockList() {
		List<TankInspection> inspectionRecords = tankInspectionRepo.findAll();
		List<InspectStockDto> inspectStockRecords = new ArrayList<>();
		try {
			/*for (TankInspection inspectionRecord : inspectionRecords) {
				InspectStockDto dto = new InspectStockDto();
				dto.setAccessShort(inspectionRecord.getAccessShort());
				dto.setCapacity(inspectionRecord.getTank().getCapacity().getCapacity());
				dto.setDieselTank(inspectionRecord.getTank().getTankName());
				dto.setDieselTankCode(inspectionRecord.getTank().getTankCode());
				dto.setInspectedVolume(inspectionRecord.getInspectedVolume());
				dto.setInspectionDate(inspectionRecord.getInspectionDate());
				// dto.setInspectionDipReading(inspectionRecord.getInspectedReading().getReading());
				dto.setInspectionTime(Time.valueOf(inspectionRecord.getInspectionTime()));
				dto.setInstallationDate(inspectionRecord.getTank().getInstallationDate());
				dto.setRemarks(inspectionRecord.getRemarks());
				inspectStockRecords.add(dto);
			}*/
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.info("Exception occured while retriving data -> " + status);
		}

		return new ResponseEntity<List<InspectStockDto>>(inspectStockRecords, status);

	}

	@Override
	public ResponseEntity<Boolean> saveInspectStockData(InspectStockDto input) {
		// TODO Auto-generated method stub
		FuelTankMaster tankRecord = fuelTankMasterRepo.findById(input.getId()).get();
		TankInspection tankInspection = new TankInspection();
		Boolean saveStatus = false;
		/*try {
			tankInspection.setAccessShort(input.getAccessShort());
			tankInspection.setInspectedVolume(input.getInspectedVolume());
			tankInspection.setInspectionDate(input.getInspectionDate());
			tankInspection.setInspectionTime(input.getInspectionTime().toLocalTime());
			tankInspection.setRemarks(input.getRemarks());
			tankInspection.setStatus(true);
			tankInspection.setTank(tankRecord);
			// tankInspection.setInspectedReading(inspectedReading);
			tankInspection.setDepot(tankRecord.getDepot());
			tankInspectionRepo.save(tankInspection);
			saveStatus = true;
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.info("Exception occured while saving data" + status);
		}*/
		return new ResponseEntity<Boolean>(saveStatus, status);

	}

	@Override
	public ResponseEntity<InspectStockDto> getInspectStockData(Integer id) {
		// TODO Auto-generated method stub
		InspectStockDto stockDto = new InspectStockDto();
		try {
			FuelTankMaster tank = fuelTankMasterRepo.findById(id).get();
			stockDto.setDieselTank(tank.getTankName());
			stockDto.setDieselTankCode(tank.getTankCode());
			stockDto.setCapacity(tank.getCapacity().getCapacity());
			stockDto.setInstallationDate(tank.getInstallationDate());
			stockDto.setId(tank.getId());
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.info("Exception occured while retriving data -> " + status);
		}
		return new ResponseEntity<>(stockDto, status);

	}

	@Override
	public ResponseEntity<String> getCalibrationDispensingUnitInfo(Integer id) {
		String disUnitCode = "";
		try {
			DispensingUnitMaster dispensingMaster = dispensingUnitRepo.findById(id).get();
			disUnitCode = dispensingMaster.getDisUnitCode() != null ? dispensingMaster.getDisUnitCode() : null;
			status = !disUnitCode.isEmpty() ? HttpStatus.OK : HttpStatus.FORBIDDEN;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("error while fetching calibration dispensing unit data");
		}
		return new ResponseEntity<String>(disUnitCode, status);
	}

	@Override
	public ResponseEntity<Boolean> postCalibrationDispensingUnitInfo(CalibrationDispensingUnitDto input,
			MultipartFile uploadFile) {
		// TODO Auto-generated method stub
		String pattern = "ddMMyyyy";
		String currentDate = new SimpleDateFormat(pattern).format(new Date());
		Document uploadDocument = null;
		boolean saveStatus = false;
		try {
			if (uploadFile != null && !uploadFile.isEmpty()) {
				File dir = new File(filePath + File.separator + "refueling" + File.separator + currentDate);
				log.info(dir.toPath().toString());
				if (!dir.exists())
					dir.mkdirs();
				uploadDocument = new Document();
				Files.copy(uploadFile.getInputStream(), dir.toPath().resolve(uploadFile.getOriginalFilename()),
						StandardCopyOption.REPLACE_EXISTING);
				uploadDocument.setContentType(uploadFile.getContentType());
				uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
				uploadDocument
						.setDocumentPath(filePath + File.separator + "calibrationFiles" + File.separator + currentDate+File.separator + uploadFile.getOriginalFilename());
				uploadDocument = documentRepository.save(uploadDocument);
			}
			DepotMaster depot = depotMasterRepo.findByDepotCode(input.getDepotCode());
			DispensingUnitCalibration calibrationData = new DispensingUnitCalibration();
//			calibrationData.setDieselIssued(input.getDieselIssued());
			calibrationData.setCalibrationDate(input.getCalibrationDate());
			calibrationData.setCertificateValidity(input.getCertificateValidity());
			calibrationData.setRemarks(input.getRemarks());
			calibrationData.setDischarge(input.getDischarge());
			calibrationData.setExcess(input.getExcess());
			calibrationData.setShort(input.getShort());
			calibrationData.setNextInspectionDueDate(input.getNextDueDateforCalibration());
			calibrationData.setFuelTankId(input.getTankId());
			calibrationData.setDepot(depot);
			calibrationData.setCalibrationDate(new Date());
			calibrationData.setDuStartReading(input.getDuStartReading());
			calibrationData.setDuStopReading(input.getDuStopReading());
			calibrationData.setCalibrationDoneFlag(input.getCalibrationDoneFlag());
			calibrationData.setFuelSubmittedFlag(input.getFuelSubmittedFlag());
			calibrationData.setPhysicalMeasurement(input.getPhysicalMeasurement());
			
			//DispensingUnitMaster disMaster = dispensingUnitRepo.findByDispensingCode(input.getDispensingUnitCode());
			calibrationData.setDispensingId(input.getDispensingUnit());
			Optional<FuelTankMaster> fuelMaster = fuelTankMasterRepo.findById(input.getTankId());
			if(fuelMaster.isPresent() && fuelMaster.get().getCurrentValue()!=null && fuelMaster.get().getCurrentValue()>0 && !input.getFuelSubmittedFlag()){
				fuelMaster.get().setCurrentValue(fuelMaster.get().getCurrentValue()-input.getPhysicalMeasurement());
				fuelTankMasterRepo.save(fuelMaster.get());
			}
			if(uploadDocument != null && uploadDocument.getId() != null)
				calibrationData.setCalibrationCertificate(uploadDocument);
			calibrationData=dispensingUnitCalibrationRepo.save(calibrationData);
//			CalibrationExcessShort excessShort = new CalibrationExcessShort();
//			excessShort.setCalibrationId(calibrationData);
//			excessShort.setExcessShort(calibrationData.getExcessShort());
//			calibrationExcessShortRepo.save(excessShort);
			saveStatus = true;
			status = HttpStatus.OK;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.info("error occured while saving calibration details! Please check the file and try submitting again");
		}

		return new ResponseEntity<>(saveStatus, status);

	}
	
	public List<DispensingUnitCalibration> findAll() {
		List<DispensingUnitCalibration>listOut = new ArrayList<>();
		List<DispensingUnitCalibration>list = dispensingUnitCalibrationRepo.findAll();
		for(DispensingUnitCalibration obj : list) {
			if(obj.getDispensingId()!=null){
				Optional<DispensingUnitMaster> record = dispensingUnitRepo.findById(obj.getDispensingId());
				obj.setDispensingUnitCode(record.get().getDisUnitCode());
				obj.setDispensingUnitName(record.get().getDisUnitName());
				listOut.add(obj);
			}
		}
		return listOut;
	}
	
	public Double excessShort(Integer tankId,Double discharge) {
		Double out = null;
		Optional<FuelTankMaster> tankMaster = fuelTankMasterRepo.findById(tankId);
//		Optional<TankCapacityMaster> tankCapacity = tankCapacityMasterRepo.findById(tankId);
		if(tankMaster.isPresent()){
			out  = tankMaster.get().getCurrentValue() -  discharge;
		}	
		return out;
	}
	
	public List<calibrationExcessShortDto> getExcessShortList() {
		List<calibrationExcessShortDto> output = new ArrayList<>();
		for(CalibrationExcessShort dto : calibrationExcessShortRepo.findAll()){
			calibrationExcessShortDto out = new calibrationExcessShortDto();
			out.setExcessShort(dto.getExcessShort());
			if(dto.getCalibrationId().getFuelTankId()!=null){
			Optional<FuelTankMaster>tankName=fuelTankMasterRepo.findById(dto.getCalibrationId().getFuelTankId());
			out.setTankName(tankName.get().getTankName());
			}
			out.setExcessShort(dto.getExcessShort());
			output.add(out);
			}
		return output;
		
	}
}
