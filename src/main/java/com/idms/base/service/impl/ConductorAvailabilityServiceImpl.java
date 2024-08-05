package com.idms.base.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AllDriverOrConductorsByTPTypeDto;
import com.idms.base.api.v1.model.dto.ConductorAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DriverUnavailablityReasonMasterDto;
import com.idms.base.api.v1.model.dto.UnavailableConductorsDetailsDto;
import com.idms.base.dao.entity.ConductorUnavailabilityMaster;
import com.idms.base.dao.entity.MasterStatus;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.ConductorUnavailableRepository;
import com.idms.base.dao.repository.DriverUnavailablityReasonRepository;
import com.idms.base.dao.repository.MasterStatusRepository;
import com.idms.base.service.ConductorAvailabilityService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConductorAvailabilityServiceImpl implements ConductorAvailabilityService {
	
	
	@Autowired
	DriverUnavailablityReasonRepository driverUnavailablityReasonRepository;
	
	@Autowired
	ConductorMasterRepository conductorMasterRepository;
	
	@Autowired
	ConductorUnavailableRepository conductorUnavailableRepository;
	
	@Autowired
	MasterStatusRepository masterStatusRepository;
	
	
	@Override
	public ConductorAvailabilityFormLoadDto conductorAvailabilityMasterFormOnLoad(String dpCode) {
		log.info("Entering into conductorAvailabilityMasterFormOnLoad service");
		ConductorAvailabilityFormLoadDto conductorAvailabilityFormLoadDto = new ConductorAvailabilityFormLoadDto();
		try {

			List<DriverUnavailablityReasonMasterDto> reasonList = driverUnavailablityReasonRepository.findAllByStatus(true).stream()
					.map(driverUnavailablityReasonMasterDto -> new DriverUnavailablityReasonMasterDto(driverUnavailablityReasonMasterDto.getId(), driverUnavailablityReasonMasterDto.getReason()))
					.collect(Collectors.toList());
			if (reasonList != null && reasonList.size() > 0)
				conductorAvailabilityFormLoadDto.setReasonList(reasonList);
			
			
			List<ConductorMasterDto> conductorList = conductorMasterRepository.findAllByDepotByStatus(dpCode).stream()
					.map(conductorMasterDto -> new ConductorMasterDto(conductorMasterDto.getId(), conductorMasterDto.getConductorName()))
					.collect(Collectors.toList());
			if (conductorList != null && conductorList.size() > 0)
				conductorAvailabilityFormLoadDto.setConductorList(conductorList);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return conductorAvailabilityFormLoadDto;
	}
	
	@Override
	public ResponseEntity<ResponseStatus> saveUnavailableConductorMaster(ConductorUnavailabilityMaster conductorUnavailabilityMaster) {
		log.info("Entering into saveUnavailableConductorMaster service");
		try {
			if (conductorUnavailabilityMaster.getId() == null) {
				MasterStatus masterStatus = masterStatusRepository.findAllByStatusName("Pending");
				conductorUnavailabilityMaster.setMasterStatus(masterStatus);
				conductorUnavailabilityMaster.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
				conductorUnavailabilityMaster.setModifiedOn(new Timestamp(System.currentTimeMillis()));
				conductorUnavailableRepository.save(conductorUnavailabilityMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Conductor Unavailable master has been persisted successfully.", HttpStatus.OK),
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
	public List<UnavailableConductorsDetailsDto> listOfUnavailableConductorsOnClick(Integer employmentId,Integer transportId, String dpCode) {
		log.info("Entering into listOfUnavailableConductorsOnClick service");
		List<UnavailableConductorsDetailsDto> list = new ArrayList<>();
		UnavailableConductorsDetailsDto unavailableObj = null;
		List<Object[]> unavailableDriverList = conductorUnavailableRepository.listOfUnavailableConductors(employmentId,transportId,dpCode);
		for(Object[] unavailableList : unavailableDriverList) {
			unavailableObj = new UnavailableConductorsDetailsDto();
			if(unavailableList[0] != null)
			unavailableObj.setConductorName(unavailableList[0].toString());
			if(unavailableList[1] != null)
				unavailableObj.setReason(unavailableList[1].toString());
			if(unavailableList[2] != null)
				unavailableObj.setUnAvailableUpTo(unavailableList[2].toString());
			if(unavailableList[3] != null)	
				unavailableObj.setRemarks(unavailableList[3].toString());
			if(unavailableList[4] != null)	
				unavailableObj.setConductorType(unavailableList[4].toString());
			if(unavailableList[5] != null)
				unavailableObj.setUnAvailableFrom(unavailableList[5].toString());
			if(unavailableList[6] != null)
				unavailableObj.setId(Integer.parseInt(unavailableList[6].toString()));
			list.add(unavailableObj);
		}
		return list;
	}

	@Override
	public List<AllDriverOrConductorsByTPTypeDto> listOfAllConductorsOnBasisOfTransportType(String[] groupIds, String dpCode) {
		log.info("Entering into listOfAllConductorsOnBasisOfTransportType service");
		AllDriverOrConductorsByTPTypeDto obj = null;
		Integer availableConductors = null;
		List<String> groupList = new ArrayList<>();
		//Integer spareConductors = null;
		List<AllDriverOrConductorsByTPTypeDto> list = new ArrayList<>();
		if(groupIds != null && groupIds.length > 0) {
		groupList = Arrays.asList(groupIds);
		}
		List<Object[]> allConductorsList = conductorUnavailableRepository.listOfAllConductors(groupList, dpCode);
		for(Object[] conductorObj : allConductorsList) {
			obj = new AllDriverOrConductorsByTPTypeDto();
			if(conductorObj[0] != null)
				obj.setEmploymentType(conductorObj[0].toString());
			if(conductorObj[1] != null)
				obj.setEmploymentId(conductorObj[1].toString());
			if(conductorObj[2] != null)
				obj.setTotalDriversOrConductors(conductorObj[2].toString());
			if(conductorObj[3] != null)
				obj.setUnavailableDriversOrConductors(conductorObj[3].toString());
			if(conductorObj[4] != null)
				obj.setTransportName(conductorObj[4].toString());
			if(conductorObj[5] != null)
				obj.setTransportId(conductorObj[5].toString());
			if(conductorObj[2] != null && conductorObj[3] != null) {
				availableConductors = (Integer.parseInt(conductorObj[2].toString()) - Integer.parseInt(conductorObj[3].toString()));
				 obj.setAvailableDriversOrConductors(availableConductors.toString());
			}
			
			list.add(obj);
		}
		return list;
	}

	
	
	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> updateConductorToDate(Integer id, Date readyDate) {
		log.info("Entering into updateDriverLikelyReadyDate service");
		try {
			int i = conductorUnavailableRepository.updateConductorToDate(id,readyDate);
			if(i==1)
				return new ResponseEntity<>(new ResponseStatus("To date of conductor has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
 			return   new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
}