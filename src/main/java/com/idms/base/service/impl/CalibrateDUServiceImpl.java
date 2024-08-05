package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DispensingUnitCalibration;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.repository.DUCalbirationRepository;
import com.idms.base.dao.repository.DUReadingHistoryRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.DocumentRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.service.CalibrateDUService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CalibrateDUServiceImpl implements CalibrateDUService {
	
	@Autowired
	DUCalbirationRepository calibRepo;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	DispensingUnitRepository duRepo;
	
	@Autowired
	DocumentRepository documentRepository;
	
	@Autowired
	DUReadingHistoryRepository duHistoryRepo;
	
	@Autowired
	FuelTankMasterRepository fuelTankMasterRepo;
	
	@Value("${file.path}")
	private String filePath;

	@Override
	public List<DispensingUnitCalibration> findAllCalibrationsByDepot(String dpCode) {
		List<DispensingUnitCalibration> list = new ArrayList<>();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(dpCode);
			list = calibRepo.findAllByDepot(depot.getId());
			for(DispensingUnitCalibration dispenseObj : list){
				if(dispenseObj.getFuelTankId()!=null){
					Optional<FuelTankMaster>tankName=fuelTankMasterRepo.findById(dispenseObj.getFuelTankId());
					dispenseObj.setTankName(tankName.get().getTankName());
					}
				
				if(dispenseObj.getDispensingId()!=null){
					Optional<DispensingUnitMaster>dispObj=duRepo.findById(dispenseObj.getDispensingId());
					dispenseObj.setDispensingUnitCode(dispObj.get().getDisUnitCode());
					dispenseObj.setDispensingUnitName(dispObj.get().getDisUnitName());
					}
				
				if(dispenseObj.getCalibrationCertificate() == null){
					Document doc = new Document();
					doc.setId(null);
					doc.setDocumentName("");
					dispenseObj.setCalibrationCertificate(doc);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<DispensingUnitMaster> getCalibrationFormLoad(String dpCode) {
		List<DispensingUnitMaster> list = new ArrayList<>();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(dpCode);
			list = duRepo.findAllByStatusAndDepotId(true, depot.getId());
			for(DispensingUnitMaster duObj : list){
				Object[] object = duRepo.findMaxDateByDispensingId(duObj.getId());
				if(object[0] != null){
					duObj.setCalibrationDate(object[0].toString());
				}
				System.out.println(object);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveDUCalibration(DUCalbiration duCalibration, MultipartFile uploadFile) {
		Document uploadDocument = null;
		String pattern = "ddMMyyyy";
		String currentDate =new SimpleDateFormat(pattern).format(new Date());
		try {
			
			DispensingUnitMaster du = null;
			
			if(duCalibration.getDispensingUnit()==null) {
				return new ResponseEntity<>(new ResponseStatus("Dispensing Unit is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(duCalibration.getDispensingUnit().getId()==null) {
				return new ResponseEntity<>(new ResponseStatus("Dispensing Unit is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			} else {
				du = duRepo.findById(duCalibration.getDispensingUnit().getId()).get();
				if(du==null) {
					return new ResponseEntity<>(new ResponseStatus("Provide valid Dispensing Unit is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
			}
			
			if(duCalibration.getCalibrationDate()==null) {
				return new ResponseEntity<>(new ResponseStatus("Calibration Date is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(duCalibration.getCertificateValidity()==null) {
				return new ResponseEntity<>(new ResponseStatus("Certificate Validity Date is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(duCalibration.getStartReading()==null) {
				return new ResponseEntity<>(new ResponseStatus("Start Reading is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(duCalibration.getRemarks()==null) {
				return new ResponseEntity<>(new ResponseStatus("Remarks is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(duCalibration.getStartReading()==null) {
				return new ResponseEntity<>(new ResponseStatus("End Reading is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(uploadFile == null && uploadFile.isEmpty()) {
				return new ResponseEntity<>(new ResponseStatus("Certificate is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
			}
			
			if(uploadFile != null && !uploadFile.isEmpty()) {
				String calibrationDate = new SimpleDateFormat(pattern).format(duCalibration.getCalibrationDate());
				File dir = new File(filePath+File.separator+"CalibrationDate"+File.separator+calibrationDate+File.separator+currentDate);
	    		log.info(dir.toPath());
	            if (!dir.exists()) {
	                dir.mkdirs();
	            }
            	uploadDocument = new Document();
            	Files.copy(uploadFile.getInputStream(), dir.toPath().resolve(uploadFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            	uploadDocument.setContentType(uploadFile.getContentType());
            	uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
            	uploadDocument.setDocumentPath(dir+File.separator+uploadDocument.getDocumentName());
            	uploadDocument = documentRepository.save(uploadDocument);
			} 
			
			if(uploadDocument != null && uploadDocument.getId() != null) {
				duCalibration.setCalibrationCertificate(uploadDocument);
			}
			
			//long ltime=duCalibration.getCalibrationDate().getTime()+du.getInspectionPeriod()*24*60*60*1000;
			Date nextInspectionDate = du.getInspectionPeriod();
			duCalibration.setNextCalibrationDate(nextInspectionDate);
			duCalibration.setDepot(du.getDepot());
			calibRepo.save(duCalibration);
			
			DUReadingHistory duHistory = new DUReadingHistory();
			duHistory.setDuStartReading(duCalibration.getStartReading());
			duHistory.setDuEndReading(duCalibration.getEndReading());
			duHistory.setDispensingUnit(duCalibration.getDispensingUnit());
			duHistory.setDuCalibration(duCalibration);
			duHistoryRepo.save(duHistory);
			
			return new ResponseEntity<>(
					new ResponseStatus("DU Calibration has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}*/

}
