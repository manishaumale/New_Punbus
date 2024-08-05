package com.idms.base.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AllDriverOrConductorsByTPTypeDto;
import com.idms.base.api.v1.model.dto.DriverAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.DriverUnavailablityReasonMasterDto;
import com.idms.base.api.v1.model.dto.UnavailableConductorsDetailsDto;
import com.idms.base.api.v1.model.dto.UnavailableDriversDetailsDto;
import com.idms.base.dao.entity.ConductorUnavailabilityMaster;
import com.idms.base.dao.entity.DriverUnavailabilityMaster;
import com.idms.base.dao.entity.MasterStatus;
import com.idms.base.dao.repository.ConductorUnavailableRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.DriverUnavailableRepository;
import com.idms.base.dao.repository.DriverUnavailablityReasonRepository;
import com.idms.base.dao.repository.MasterStatusRepository;
import com.idms.base.service.DriverAvailabilityService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DriverAvailabilityServiceImpl implements DriverAvailabilityService {
	
	
	@Autowired
	DriverUnavailablityReasonRepository driverUnavailablityReasonRepository;
	
	@Autowired
	DriverMasterRepository driverMasterRepository;
	
	@Autowired
	DriverUnavailableRepository driverUnavailableRepository;
	
	@Autowired
	ConductorUnavailableRepository conductorUnavailableRepository;
	
	@Autowired
	MasterStatusRepository masterStatusRepository;
	
	@Autowired
	SMSTriggerServiceImpl smsTrigger;
	
	
	@Override
	public DriverAvailabilityFormLoadDto driverAvailabilityMasterFormOnLoad(String dpCode) {
		log.info("Entering into driverAvailabilityMasterFormOnLoad service");
		DriverAvailabilityFormLoadDto driverAvailabilityFormLoadDto = new DriverAvailabilityFormLoadDto();
		try {

			List<DriverUnavailablityReasonMasterDto> reasonList = driverUnavailablityReasonRepository.findAllByStatus(true).stream()
					.map(driverUnavailablityReasonMasterDto -> new DriverUnavailablityReasonMasterDto(driverUnavailablityReasonMasterDto.getId(), driverUnavailablityReasonMasterDto.getReason()))
					.collect(Collectors.toList());
			if (reasonList != null && reasonList.size() > 0)
				driverAvailabilityFormLoadDto.setReasonList(reasonList);
			
			
			List<DriverMasterDto> driverList = driverMasterRepository.findAllByDepotByStatus(dpCode).stream()
					.map(driverMasterDto -> new DriverMasterDto(driverMasterDto.getId(), driverMasterDto.getDriverName()))
					.collect(Collectors.toList());
			if (driverList != null && driverList.size() > 0)
				driverAvailabilityFormLoadDto.setDriverList(driverList);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return driverAvailabilityFormLoadDto;
	}
	
	@Override
	public ResponseEntity<ResponseStatus> saveUnavailableDriverMaster(DriverUnavailabilityMaster driverUnavailabilityMaster) {
		log.info("Entering into saveUnavailableDriverMaster service");
		try {
			if (driverUnavailabilityMaster.getId() == null) {
				MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Pending");
				driverUnavailabilityMaster.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
				driverUnavailabilityMaster.setModifiedOn(new Timestamp(System.currentTimeMillis()));
				driverUnavailabilityMaster.setMasterStatus(masterStatus);
				driverUnavailableRepository.save(driverUnavailabilityMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Driver Unavailable master has been persisted successfully.", HttpStatus.OK),
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
	public List<UnavailableDriversDetailsDto> listOfUnavailableDriversOnClick(Integer employmentId,Integer transportId, String dpCode){
		log.info("Entering into listOfUnavailableDriversOnClick service");
		List<UnavailableDriversDetailsDto> list = new ArrayList<>();
		UnavailableDriversDetailsDto unavailableObj = null;
		List<Object[]> unavailableDriverList = driverUnavailableRepository.listOfUnavailableDrivers(employmentId,transportId,dpCode);
		for(Object[] unavailableList : unavailableDriverList) {
			unavailableObj = new UnavailableDriversDetailsDto();
			if(unavailableList[0] != null)
			unavailableObj.setDriverName(unavailableList[0].toString());
			if(unavailableList[1] != null)
				unavailableObj.setReason(unavailableList[1].toString());
			if(unavailableList[2] != null)
				unavailableObj.setUnAvailableUpTo(unavailableList[2].toString());
			if(unavailableList[3] != null)	
				unavailableObj.setRemarks(unavailableList[3].toString());
			if(unavailableList[4] != null)	
				unavailableObj.setDriverType(unavailableList[4].toString());
			if(unavailableList[5] != null)	
				unavailableObj.setUnAvailableUpFrom(unavailableList[5].toString());
			if(unavailableList[6] != null)
				unavailableObj.setId(Integer.parseInt(unavailableList[6].toString()));
			if(unavailableList[7] != null)	
				unavailableObj.setFromDate(unavailableList[7].toString());
			if(unavailableList[8] != null)	
				unavailableObj.setToDate(unavailableList[8].toString());
			list.add(unavailableObj);
		}
		return list;
	}

	@Override
	public List<AllDriverOrConductorsByTPTypeDto> listOfAllDriversOnBasisOfTransportType(String[] groupIds, String dpCode ) {
		log.info("Entering into listOfAllDriversOnBasisOfTransportType service");
		AllDriverOrConductorsByTPTypeDto obj = null;
		Integer availableDrivers = null;
		List<String> groupList = new ArrayList<>();
		//Integer spareDrivers = null;
		List<AllDriverOrConductorsByTPTypeDto> list = new ArrayList<>();
		if(groupIds != null && groupIds.length > 0) {
			groupList = Arrays.asList(groupIds);
			}
		List<Object[]> allDriversList = driverUnavailableRepository.listOfAllDrivers(groupList, dpCode);
		for(Object[] driverObj : allDriversList) {
			obj = new AllDriverOrConductorsByTPTypeDto();
			if(driverObj[0] != null)
				obj.setEmploymentType(driverObj[0].toString());
			if(driverObj[1] != null)
				obj.setEmploymentId(driverObj[1].toString());
			if(driverObj[2] != null)
				obj.setTotalDriversOrConductors(driverObj[2].toString());
			if(driverObj[3] != null)
				obj.setUnavailableDriversOrConductors(driverObj[3].toString());
			if(driverObj[4] != null)
				obj.setTransportName(driverObj[4].toString());
			if(driverObj[5] != null)
				obj.setTransportId(driverObj[5].toString());
			if(driverObj[2] != null && driverObj[3] != null) {
				availableDrivers = (Integer.parseInt(driverObj[2].toString()) - Integer.parseInt(driverObj[3].toString()));
				 obj.setAvailableDriversOrConductors(availableDrivers.toString());
			}
			
			list.add(obj);
		}
		return list;
	}
	
	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> updateDriverToDate(Integer id, Date readyDate) {
		log.info("Entering into updateDriverLikelyReadyDate service");
		try {
			int i = driverUnavailableRepository.updateDriverToDate(id,readyDate);
			if(i==1)
				return new ResponseEntity<>(new ResponseStatus("Ready date has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
 			return   new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}

	@Override
	public List<UnavailableDriversDetailsDto> listOfAllDriversAndConductors(String depotCode) {
		log.info("Entering into listOfAllDriversAndConductors service");
		List<UnavailableDriversDetailsDto> list = new ArrayList<>();
		UnavailableDriversDetailsDto unavailableObj = null;
		
		MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Pending");
		List<Object[]> unavailableDriverList = driverUnavailableRepository.listOfAllUnavailableDriversByStatusId(depotCode,masterStatus.getId());
		for(Object[] unavailableList : unavailableDriverList) {
			unavailableObj = new UnavailableDriversDetailsDto();
			if(unavailableList[0] != null)
			unavailableObj.setDriverName(unavailableList[0].toString());
			if(unavailableList[1] != null)
				unavailableObj.setReason(unavailableList[1].toString());
			if(unavailableList[2] != null)
				unavailableObj.setUnAvailableUpTo(unavailableList[2].toString());
			if(unavailableList[3] != null)	
				unavailableObj.setRemarks(unavailableList[3].toString());
			if(unavailableList[4] != null)	
				unavailableObj.setDriverType(unavailableList[4].toString());
			if(unavailableList[5] != null)	
				unavailableObj.setUnAvailableUpFrom(unavailableList[5].toString());
			if(unavailableList[6] != null)
				unavailableObj.setId(Integer.parseInt(unavailableList[6].toString()));
			if(unavailableList[7] != null)	
				unavailableObj.setFromDate(unavailableList[7].toString());
			if(unavailableList[8] != null)	
				unavailableObj.setToDate(unavailableList[8].toString());
			if(unavailableList[9] != null)
				unavailableObj.setStatus(unavailableList[9].toString());
			unavailableObj.setDriverConductorFlag("D");
			if(unavailableList[10] != null)	
			unavailableObj.setModifiedBy(unavailableList[10].toString());
			if(unavailableList[11] != null)	
				unavailableObj.setModifiedOn(unavailableList[11].toString());
			list.add(unavailableObj);
		}
		List<Object[]> unavailableConductorList = conductorUnavailableRepository.listOfAllUnavailableConductors(depotCode,masterStatus.getId());
		UnavailableDriversDetailsDto unavailableConductorObj = null;
		for(Object[] unavailableList : unavailableConductorList) {
			unavailableConductorObj = new UnavailableDriversDetailsDto();
			if(unavailableList[0] != null)
			unavailableConductorObj.setDriverName(unavailableList[0].toString());
			if(unavailableList[1] != null)
				unavailableConductorObj.setReason(unavailableList[1].toString());
			if(unavailableList[2] != null)
				unavailableConductorObj.setUnAvailableUpTo(unavailableList[2].toString());
			if(unavailableList[3] != null)	
				unavailableConductorObj.setRemarks(unavailableList[3].toString());
			if(unavailableList[5] != null)
				unavailableConductorObj.setUnAvailableUpFrom(unavailableList[5].toString());
			if(unavailableList[6] != null)
				unavailableConductorObj.setId(Integer.parseInt(unavailableList[6].toString()));
			if(unavailableList[7] != null)
				unavailableConductorObj.setStatus(unavailableList[7].toString());
			if(unavailableList[8] != null)
				unavailableConductorObj.setModifiedBy(unavailableList[8].toString());
			if(unavailableList[9] != null)
				unavailableConductorObj.setModifiedOn(unavailableList[9].toString());
			unavailableConductorObj.setDriverConductorFlag("C");
			list.add(unavailableConductorObj);
		}
		return list;
	}
	
	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> approveAttendenceStatus(Integer id, String driverConductorFlg) {
		String response = "";
		MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Approved");
		if (driverConductorFlg.equals("D")) {
			Optional<DriverUnavailabilityMaster> driver = driverUnavailableRepository.findById(id);
			driverUnavailableRepository.updateDriverApprovalStatus(id, masterStatus.getId(),
					SecurityContextHolder.getContext().getAuthentication().getName());
			try {
				response = smsTrigger.sendAbsentSMS(id, masterStatus.getShortCode(), driverConductorFlg,
						driver.get().getFromDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (response.equals("sent")) {
				return new ResponseEntity<>(
						new ResponseStatus("Driver attendance, has been approved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
			return new ResponseEntity<>(
					new ResponseStatus("Driver attendance, has been approved successfully but message not sent",
							HttpStatus.OK),
					HttpStatus.OK);
		} else if (driverConductorFlg.equals("C")) {
			Optional<ConductorUnavailabilityMaster> conductor = conductorUnavailableRepository.findById(id);
			conductorUnavailableRepository.updateConductorApprovalStatus(id, masterStatus.getId(),
					SecurityContextHolder.getContext().getAuthentication().getName());
			try {
				response = smsTrigger.sendAbsentSMS(id, masterStatus.getShortCode(), driverConductorFlg,
						conductor.get().getFromDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (response.equals("sent")) {
				return new ResponseEntity<>(
						new ResponseStatus("Conductor attendance, has been approved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
			return new ResponseEntity<>(
					new ResponseStatus("Conductor attendance, has been approved successfully but message not sent",
							HttpStatus.OK),
					HttpStatus.OK);
		}
		return null;
	}
	
	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> rejectAttendenceStatus(Integer id, String driverConductorFlg) {
		MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Rejected");
		if(driverConductorFlg.equals("D")){
		driverUnavailableRepository.updateDriverApprovalStatusAndRejectedBy(id, masterStatus.getId(),SecurityContextHolder.getContext().getAuthentication().getName());
		return new ResponseEntity<>(new ResponseStatus("Driver attendance, has been rejected successfully.", HttpStatus.OK),
				HttpStatus.OK);
		}
		else if(driverConductorFlg.equals("C")){
		conductorUnavailableRepository.updateConductorApprovalStatusAndRejectedBy(id, masterStatus.getId(),SecurityContextHolder.getContext().getAuthentication().getName());
		return new ResponseEntity<>(new ResponseStatus("Conductor attendance, has been rejected successfully.", HttpStatus.OK),
				HttpStatus.OK);
		}
		return null;
	}

}
