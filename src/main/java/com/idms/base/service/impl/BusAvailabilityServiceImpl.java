package com.idms.base.service.impl;

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
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AllBusesByTransportTypeDto;
import com.idms.base.api.v1.model.dto.BusAvailabilityFormLoadDto;
import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusUnavailablityReasonMasterDto;
import com.idms.base.api.v1.model.dto.UnavailableBusesDetailsDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusUnavailabilityMaster;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusUnavailableRepository;
import com.idms.base.dao.repository.BusUnavailablityReasonRepository;
import com.idms.base.service.BusAvailabilityService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;
import com.idms.base.util.AlertUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BusAvailabilityServiceImpl implements BusAvailabilityService {
	
	@Autowired
	BusUnavailablityReasonRepository busUnavailablityReasonRepository;
	
	@Autowired
	BusMasterRepository busMasterRepository;
	
	@Autowired
	BusUnavailableRepository busUnavailableRepository;
	
	@Autowired
	AlertUtility alertUtility;
	
	String busAlert = "BUS";
	String roleIds[]=RestConstants.AlertRoles;
	
	@Override
	public BusAvailabilityFormLoadDto busAvailabilityMasterFormOnLoad(String dpCode) {
		log.info("Entering into busAvailabilityMasterFormOnLoad service");
		BusAvailabilityFormLoadDto busAvailabilityFormLoadDto = new BusAvailabilityFormLoadDto();
		try {

			List<BusUnavailablityReasonMasterDto> reasonList = busUnavailablityReasonRepository.findAllByStatus(true).stream()
					.map(busUnavailablityReasonMasterDto -> new BusUnavailablityReasonMasterDto(busUnavailablityReasonMasterDto.getId(), busUnavailablityReasonMasterDto.getReason()))
					.collect(Collectors.toList());
			if (reasonList != null && reasonList.size() > 0)
				busAvailabilityFormLoadDto.setReasonList(reasonList);
			
			
			List<BusMasterDto> busList = busMasterRepository.findAllBusesByDepotAndStatus(dpCode).stream()
					.map(busMasterDto -> new BusMasterDto(busMasterDto.getId(), busMasterDto.getBusRegNumber()))
					.collect(Collectors.toList());
			if (busList != null && busList.size() > 0)
				busAvailabilityFormLoadDto.setBusList(busList);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return busAvailabilityFormLoadDto;
	}
	
	@Override
	public ResponseEntity<ResponseStatus> saveUnavailableBusMaster(BusUnavailabilityMaster busUnavailabilityMaster) {
		log.info("Entering into saveUnavailableBusMaster service");
		try {
			if (busUnavailabilityMaster.getId() == null) {
				Optional<BusMaster> busSubTypeMaster = busMasterRepository.findById(busUnavailabilityMaster.getBusMaster().getId());
				for(String s : roleIds) {
					alertUtility.insertNotification("Bus:- "+busSubTypeMaster.get().getBusRegNumber()+" ("+busSubTypeMaster.get().getBusType().getBusTypeName()+")"+" Transport Unit:- "+busSubTypeMaster.get().getTransportUnit().getTransportName()+ " is Unavailable", busAlert, "Bus", busSubTypeMaster.get().getId(),  busSubTypeMaster.get().getDepot().getId(), s);
					}
				busUnavailableRepository.save(busUnavailabilityMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Bus Unavailable master has been persisted successfully.", HttpStatus.OK),
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
	public List<UnavailableBusesDetailsDto> listOfUnavailableBusesOnClick(Integer busTypeId,Integer transportId, String dpCode) {
		log.info("Entering into listOfUnavailableBusesOnClick service");
		List<UnavailableBusesDetailsDto> list = new ArrayList<>();
		UnavailableBusesDetailsDto unavailableObj = null;
		List<Object[]> unavailableBusList = busUnavailableRepository.listOfUnavailableBuses(busTypeId,transportId, dpCode);
		for(Object[] unavailableList : unavailableBusList) {
			unavailableObj = new UnavailableBusesDetailsDto();
			if(unavailableList[0] != null)
			unavailableObj.setBusNo(unavailableList[0].toString());
			if(unavailableList[1] != null)
				unavailableObj.setBusTypeName(unavailableList[1].toString());
			if(unavailableList[2] != null)
				unavailableObj.setReason(unavailableList[2].toString());
			if(unavailableList[3] != null)	
				unavailableObj.setRemarks(unavailableList[3].toString());
			if(unavailableList[4] != null)
				unavailableObj.setId(Integer.parseInt(unavailableList[4].toString()));
			if(unavailableList[5] != null)
				unavailableObj.setLikelyToBeReadyDate(unavailableList[5].toString());
			if(unavailableList[6] != null)
				unavailableObj.setDetentionDate(unavailableList[6].toString());
			list.add(unavailableObj);
		}
		System.out.println(""+ unavailableBusList);
		
		return list;
	}

	@Override
	public List<AllBusesByTransportTypeDto> listOfAllBusesOnBasisOfTransportType(String [] groupIds, String dpCode) {
		log.info("Entering into listOfAllBusesOnBasisOfTransportType service");
		AllBusesByTransportTypeDto obj = null;
		Integer availableBuses = null;
		List<String> groupList = new ArrayList<>();
		//Integer spareBuses = null;
		List<AllBusesByTransportTypeDto> list = new ArrayList<>();
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");      
	   // Date todayDate = new Date();
	    if(groupIds != null && groupIds.length > 0) {
			groupList = Arrays.asList(groupIds);
		}
		List<Object[]> allBusList = busUnavailableRepository.listOfAllBuses(groupList, dpCode);
		for(Object[] busObj : allBusList) {
			obj = new AllBusesByTransportTypeDto();
			if(busObj[0] != null)
				obj.setBusTypeName(busObj[0].toString());
			if(busObj[1] != null)
				obj.setTotalBuses(busObj[1].toString());
			if(busObj[2] != null)
				obj.setUnavailableBuses(busObj[2].toString());
			if(busObj[1] != null && busObj[2] != null) {
				 availableBuses = (Integer.parseInt(busObj[1].toString()) - Integer.parseInt(busObj[2].toString()));
				 obj.setAvailableBuses(availableBuses.toString());
			}
			/*
			 * if(busObj[1] != null && busObj[2] != null && availableBuses != null) {
			 * spareBuses = Integer.parseInt(busObj[1].toString()) -
			 * Integer.parseInt(busObj[2].toString()) - availableBuses;
			 * obj.setSpareBuses(spareBuses.toString()); }
			 */
			if(busObj[3] != null)
				obj.setBusTypeId(busObj[3].toString());
			if(busObj[4] != null)
				obj.setTransportName(busObj[4].toString());
			if(busObj[5] != null)
				obj.setTransportId(busObj[5].toString());
			list.add(obj);
		}
		System.out.println("" + allBusList);
		return list;
	}
	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> updateBusLikelyReadyDate(Integer id, Date readyDate) {
		log.info("Entering into updateBusLikelyReadyDate service");
		try {
			int i = busUnavailableRepository.updateBusLikelyReadyDate(id,readyDate);
			if(i==1)
				return new ResponseEntity<>(new ResponseStatus("Ready date has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
 			return   new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
	
	

}
