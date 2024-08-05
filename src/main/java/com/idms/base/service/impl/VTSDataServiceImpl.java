package com.idms.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.VTSData;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.VTSDataRepository;
import com.idms.base.service.VTSDataService;
import com.idms.base.support.persist.ResponseStatus;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class VTSDataServiceImpl implements VTSDataService {
	
	@Autowired
	BusMasterRepository busRepo;
	
	@Autowired
	VTSDataRepository vtsRepo;

	@Override
	public ResponseStatus saveVTSData(VTSData vtsData) {
		log.info("Entering into saveStateMaster service");
		try {
			
//			if(vtsData.getBusNumber()==null || vtsData.getBusNumber().trim().equals("")) {
//				return new ResponseStatus("Bus Number is missing.", HttpStatus.MOVED_PERMANENTLY);
//			}
//			
////			if(vtsData.getDepotCode()==null || vtsData.getDepotCode().trim().equals("")) {
////				return new ResponseStatus("Depot Code is missing.", HttpStatus.MOVED_PERMANENTLY);
////			}
//			
////			if(vtsData.getRouteId() == null || vtsData.getRouteId().trim().equals("")) {
////				return new ResponseStatus("Route ID is missing.", HttpStatus.MOVED_PERMANENTLY);
////			}
//			
//			if(vtsData.getVtsKms() == null || vtsData.getVtsKms() <= 0 ) {
//				return new ResponseStatus("VTS Km is missing.", HttpStatus.MOVED_PERMANENTLY);
//			}
//			
//			BusMaster bus = busRepo.findByBusRegNumber(vtsData.getBusNumber());
//			if(bus==null) {
//				return new ResponseStatus("No bus exist with provided bus number.", HttpStatus.MOVED_PERMANENTLY);
//			}
//			
//			vtsRepo.save(vtsData);
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN);
		}
		return new ResponseStatus("Success", HttpStatus.OK);
	}

}
