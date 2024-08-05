package com.idms.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.dao.entity.DipReadingMaster;
import com.idms.base.dao.entity.ReadingMaster;
import com.idms.base.dao.repository.DipReadingMasterRepository;
import com.idms.base.dao.repository.ReadingMasterRepository;
import com.idms.base.service.DipReadingMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DipReadingMasterServiceImpl implements DipReadingMasterService{
	
	
	@Autowired
	DipReadingMasterRepository repository;
	
	@Autowired
	ReadingMasterRepository rmRepo;
	
	@Override
	public ResponseEntity<ResponseStatus> saveDipReadingMaster(DipReadingMaster dipReadingMaster) {
		log.info("Entering into saveDipReadingMaster service");
		try {
			if (dipReadingMaster.getId() == null) {
				if(dipReadingMaster.getReadings()!=null && dipReadingMaster.getReadings().size()>0) {
					for(ReadingMaster rm : dipReadingMaster.getReadings()) {
						rm.setDip(dipReadingMaster);
//						rmRepo.save(rm);
					}
				}
				repository.save(dipReadingMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Dip Reading master has been persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}
	
	public List<DipReadingMaster> listOfAllDipReadingMaster() {
		log.info("Entering into listOfAllDipReadingMaster service");
		List<DipReadingMaster> dipMasterList = null;
		try {
			dipMasterList = repository.findAllByStatus(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dipMasterList;
	}

}
