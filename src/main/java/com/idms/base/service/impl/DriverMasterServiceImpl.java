package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DriverFormLoadDto;
import com.idms.base.api.v1.model.dto.EmploymentTypeDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.RouteCategoryHistory;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.EmploymentTypeRepository;
import com.idms.base.dao.repository.RouteCategoryHistoryRepository;
import com.idms.base.dao.repository.RouteCategoryRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.DriverMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DriverMasterServiceImpl implements DriverMasterService{
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	TransportUnitRepository transportUnitRepository;
	
	@Autowired
	DepotMasterRepository depotMasterRepository;
	
	@Autowired
	EmploymentTypeRepository employmentTypeRepository;
	
	@Autowired
	CityMasterRepository cityMasterRepository;
	
	@Autowired
	DriverMasterRepository driverMasterRepository;
	
	@Autowired
	RouteCategoryRepository routeCategoryRepository;
	
	@Autowired
	RouteCategoryHistoryRepository routeCategoryHistoryRepository;
	
	@Override
	public DriverFormLoadDto driverMasterFormOnLoad(String dpCode) {

		log.info("Entering into driverMasterFormOnLoad service");
		DriverFormLoadDto driverFormLoadDto = new DriverFormLoadDto();
		try {
			
			DepotMaster depotMaster = depotMasterRepository.findByDepotCode(dpCode);
			
			if (depotMaster != null)
				driverFormLoadDto.setDepotList(this.mapper.map(depotMaster, DepotMasterDto.class));

			List<TransportDto> transportList = transportUnitRepository.allTransportMasterByDepot(depotMaster.getId()).stream()
					.map(transport -> new TransportDto(transport.getTransportUnitMaster().getId(), transport.getTransportUnitMaster().getTransportName()))
					.collect(Collectors.toList());
			if (transportList != null && transportList.size() > 0)
				driverFormLoadDto.setTransportList(transportList);
			
			
			List<EmploymentTypeDto> emplooyeeTypeList = employmentTypeRepository.findAllByStatus(true).stream()
					.map(employmentTypeDto -> new EmploymentTypeDto(employmentTypeDto.getId(), employmentTypeDto.getEnrolmentName()))
					.collect(Collectors.toList());
			if (emplooyeeTypeList != null && emplooyeeTypeList.size() > 0)
				driverFormLoadDto.setEmployeeTypeList(emplooyeeTypeList);
			
			List<DepotMasterDto> depotInductionList = depotMasterRepository.findAllByStatus(true).stream()
					.map(depot -> new DepotMasterDto(depot.getId(), depot.getDepotName()))
					.collect(Collectors.toList());
			if (depotInductionList != null && depotInductionList.size() > 0)
				driverFormLoadDto.setDepotOfInductionList(depotInductionList);
			
			List<RouteCategoryDto> categoryList = routeCategoryRepository.findAllByStatus(true).stream()
					.map(routeCategoryDto -> new RouteCategoryDto(routeCategoryDto.getId(), routeCategoryDto.getRouteCategoryName()))
					.collect(Collectors.toList());
			if (categoryList != null && categoryList.size() > 0)
				driverFormLoadDto.setCategoryList(categoryList);

		} catch (Exception e) {
			e.printStackTrace();
		}

      return driverFormLoadDto;
}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateDriverMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateDriverMasterStatusFlag service");
		try {
			int i = driverMasterRepository.updateDriverMasterStatusFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Status has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateDriverMasterIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updateBusMasterIsDeletedFlag service");
		try {
			int i = driverMasterRepository.updateDriverMasterIsDeletedFlag(flag,id);
			if(i == 1)
			return new ResponseEntity<>(new ResponseStatus("Driver master has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);	
		} catch (Exception e) {
			e.printStackTrace();
 			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}
	
	@Override
	public ResponseEntity<ResponseStatus> saveDriverMaster(DriverMaster driverMaster) {
		log.info("Entering into saveDriverMaster service");
		try {
			if (driverMaster.getId() == null) {
				if(driverMaster.getDriverName() == null || driverMaster.getDriverName().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver name is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getDriverCode() == null || driverMaster.getDriverCode().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver code is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getFatherName() == null || driverMaster.getFatherName().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Father name is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getLicenceNo() == null || driverMaster.getLicenceNo().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver's licence no is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getLicenceIssuePlace() == null || driverMaster.getLicenceIssuePlace().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver's licence place is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getLicenceValidity() == null || driverMaster.getLicenceValidity().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver's licence validity is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getDepot() == null) {
					return new ResponseEntity<>(new ResponseStatus("Depot is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getBadgeNumber() == null || driverMaster.getBadgeNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Badge no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getInductionDepot() == null) {
					return new ResponseEntity<>(new ResponseStatus("Depot of induction is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getEmploymentType() == null) {
					return new ResponseEntity<>(new ResponseStatus("Employment type is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getTransportUnit() == null) {
					return new ResponseEntity<>(new ResponseStatus("Transport unit is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getEpfGpfNumber() == null || driverMaster.getEpfGpfNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("EPF/GPF no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getEsiNumber() == null || driverMaster.getEpfGpfNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("ESI no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getAddress() == null || driverMaster.getAddress().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Address is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getMobileNumber() == null || driverMaster.getMobileNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Mobile no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(driverMaster.getDriverCategory() == null) {
					return new ResponseEntity<>(new ResponseStatus("Driver Category is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}	
				driverMaster.setIsDeleted(false);;
				driverMasterRepository.save(driverMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Driver master has been persisted successfully.", HttpStatus.OK),
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
	public List<DriverMaster> getAllDriverMaster(String dpCode) {
		log.info("Entering into getAllDriverMaster service");
		List<DriverMaster> driversMasterList = null;
		try {
			driversMasterList = driverMasterRepository.findAllByDepot(dpCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return driversMasterList;
	}

	@Override
	public List<DriverMaster> fetchAllDriverMaster() {
		log.info("Entering into fetchAllDriverMaster service");
		List<DriverMaster> driversMasterList = null;
		try {
			driversMasterList = driverMasterRepository.findAllByStatus(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return driversMasterList;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> updateDriverMasterCategoryById(Integer driverId, Integer categoryId) {
		int i = 0;
		RouteCategoryHistory history = new RouteCategoryHistory();
		try {
			i = driverMasterRepository.updateDriverMasterCategoryById(driverId, categoryId);
			if (i > 0) {
				Optional<DriverMaster> driver = driverMasterRepository.findById(driverId);
				history.setDriverMaster(driver.get());
				history.setRouteCategoryMaster(driver.get().getDriverCategory());
				routeCategoryHistoryRepository.save(history);
				return new ResponseEntity<>(new ResponseStatus("Driver category updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}
	
	public List<DriverMaster> findAllunBlockedDrivers(String depotCode){
		DepotMaster depot = depotMasterRepository.findByDepotCode(depotCode);
		List<DriverMaster> output = new ArrayList<DriverMaster>();
		List<Object[]> objects = driverMasterRepository.findUnblockedDrivers(depot.getId());
		for (Object a[] : objects) {
			DriverMaster master = new DriverMaster();
			master.setId(Integer.parseInt(a[0].toString()));
			master.setDriverName(a[1].toString());
			output.add(master);
		}
		return output;
	}

}
