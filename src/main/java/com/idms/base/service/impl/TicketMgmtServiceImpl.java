package com.idms.base.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.ETMFormLoadDto;
import com.idms.base.api.v1.model.dto.ETMMakerMasterDto;
import com.idms.base.api.v1.model.dto.GSMAndGPSMasterDto;
import com.idms.base.api.v1.model.dto.RFIDMasterDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.ETMMaster;
import com.idms.base.dao.entity.TicketBoxMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.ETMMakerMasterRepository;
import com.idms.base.dao.repository.ETMMasterRepository;
import com.idms.base.dao.repository.GSMAndGPSMasterRepository;
import com.idms.base.dao.repository.RFIDMasterRepository;
import com.idms.base.dao.repository.TicketBoxRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.TicketMgmtService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TicketMgmtServiceImpl implements TicketMgmtService{
	
	@Autowired
	TicketBoxRepository ticketBoxRepository;
	
	@Autowired
	TransportUnitRepository transportUnitRepository;
	
	@Autowired
	ETMMakerMasterRepository etmMakerMasterRepository;
	
	@Autowired
	GSMAndGPSMasterRepository gsmAndGPSMasterRepository;
	
	@Autowired
	RFIDMasterRepository rfidMasterRepository;
	
	@Autowired
	ETMMasterRepository etmMasterRepository;
	
	@Autowired
	DepotMasterRepository depoRepo;
	
	@Override
	public ResponseEntity<ResponseStatus> saveTicketBoxMaster(TicketBoxMaster ticketBoxMaster) {
		log.info("Entering into saveTicketBoxMaster service");
		try {
			if (ticketBoxMaster.getId() == null) {
				DepotMaster depot = depoRepo.findByDepotCode(ticketBoxMaster.getDepotCode());
				ticketBoxMaster.setDepot(depot);
				ticketBoxRepository.save(ticketBoxMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Ticket box master has been persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}
	
	@Override
	public List<TicketBoxMaster> listOfAllTicketBoxMaster(String dpCode) {
		log.info("Entering into listOfAllTicketBoxMaster service");
		
		List<TicketBoxMaster> ticketBoxList = null;
		try {
			ticketBoxList = ticketBoxRepository.findAllBoxesByDepotCode(dpCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ticketBoxList;
	}

	
	@Override
	public ETMFormLoadDto etmMasterFormOnLoad() {
		log.info("Entering into etmMasterFormOnLoad service");
		ETMFormLoadDto eTMFormLoadDto = new ETMFormLoadDto();
		try {

			List<TransportDto> transportList = transportUnitRepository.findAllByStatus(true).stream()
					.map(transportDto -> new TransportDto(transportDto.getId(), transportDto.getTransportName()))
					.collect(Collectors.toList());
			if (transportList != null && transportList.size() > 0)
				eTMFormLoadDto.setTransportList(transportList);
			
			
			List<ETMMakerMasterDto> makerList = etmMakerMasterRepository.findAllByStatus(true).stream()
					.map(maker -> new ETMMakerMasterDto(maker.getId(), maker.getName()))
					.collect(Collectors.toList());
			if (makerList != null && makerList.size() > 0)
				eTMFormLoadDto.setMakersList(makerList);
			
			List<GSMAndGPSMasterDto> gpsList = gsmAndGPSMasterRepository.findAllByStatus(true).stream()
					.map(gsm -> new GSMAndGPSMasterDto(gsm.getId(), gsm.getValue()))
					.collect(Collectors.toList());
			if (gpsList != null && gpsList.size() > 0)
				eTMFormLoadDto.setGsmAndGPsList(gpsList);
			
			List<RFIDMasterDto> rfidList = rfidMasterRepository.findAllByStatus(true).stream()
					.map(rfid -> new RFIDMasterDto(rfid.getId(), rfid.getValue()))
					.collect(Collectors.toList());
			if (rfidList != null && rfidList.size() > 0)
				eTMFormLoadDto.setRfidList(rfidList);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return eTMFormLoadDto;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveETMMaster(ETMMaster etmMaster) {
		log.info("Entering into saveTicketBoxMaster service");
		try {
			if (etmMaster.getId() == null) {
				DepotMaster depot = depoRepo.findByDepotCode(etmMaster.getDepotCode());
				etmMaster.setDepot(depot);
				etmMaster.setEtmAvailable(true);
				etmMasterRepository.save(etmMaster);
				return new ResponseEntity<>(
						new ResponseStatus("ETM master has been persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}

	@Override
	public List<ETMMaster> listOfAllETMMaster(String dpCode) {
		log.info("Entering into listOfAllETMMaster service");
		List<ETMMaster> etmList = null;
		try {
			etmList = etmMasterRepository.findAllETMbyDpCode(dpCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return etmList;
	}
	
	

}
