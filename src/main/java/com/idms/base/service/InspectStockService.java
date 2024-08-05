package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.CalibrationDispensingUnitDto;
import com.idms.base.api.v1.model.dto.InspectStockDto;
import com.idms.base.api.v1.model.dto.calibrationExcessShortDto;
import com.idms.base.dao.entity.DispensingUnitCalibration;

@Service
public interface InspectStockService {

	public ResponseEntity<List<InspectStockDto>> getInspectStockList();

	public ResponseEntity<Boolean> saveInspectStockData(InspectStockDto input);

	public ResponseEntity<InspectStockDto> getInspectStockData(Integer id);

	public ResponseEntity<String> getCalibrationDispensingUnitInfo(Integer id);

	public ResponseEntity<Boolean> postCalibrationDispensingUnitInfo(CalibrationDispensingUnitDto input,
			MultipartFile uploadFile);
	public List<DispensingUnitCalibration> findAll();
	
	public Double excessShort(Integer tankId,Double discharge); 
	
	public List<calibrationExcessShortDto> getExcessShortList();
}
