package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.ConductorAndDriverBlockHistoryDto;
import com.idms.base.api.v1.model.dto.ConductorFormLoadDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DriverBlockHistoryDto;
import com.idms.base.api.v1.model.dto.EmploymentTypeDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.BlockOrRouteOffEntity;
import com.idms.base.dao.entity.BusBlockHistory;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.ConductorBlockHistory;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverBlockHistory;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.RouteBlockHistory;
import com.idms.base.dao.entity.RouteCategoryHistory;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.repository.BlockOrRouteOffRepository;
import com.idms.base.dao.repository.BusBlockHistoryRepository;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.ConductorBlockHistoryRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.EmploymentTypeRepository;
import com.idms.base.dao.repository.RouteBlockHistoryRepository;
import com.idms.base.dao.repository.RouteCategoryHistoryRepository;
import com.idms.base.dao.repository.RouteCategoryRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.driverBlockHistoryRepository;
import com.idms.base.service.ConductorMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;
import com.idms.base.util.AlertUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ConductorMasterServiceImpl implements ConductorMasterService {
	
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
	ConductorMasterRepository conductorMasterRepository;
	
	@Autowired
	RouteCategoryRepository routeCategoryRepository;
	
	@Autowired
	RouteCategoryHistoryRepository routeCategoryHistoryRepository;
	
	@Autowired
	ConductorBlockHistoryRepository conductorBlockHistoryRepository;
	
	@Autowired
	DriverMasterRepository driverMasterRepository;
	
	@Autowired
	driverBlockHistoryRepository driverBlockHistoryRepository;
	
	@Autowired
	RouteMasterRepository routeMasterRepo;
	
	@Autowired
	BusMasterRepository busMasterRepo;
	
	@Autowired
	RouteBlockHistoryRepository routeBlockHistoryRepo;
	
	@Autowired
	BusBlockHistoryRepository busBlockHistoryRepo;
	
	@Autowired
	DepotMasterRepository depotMasterRepo;
	
	@Autowired
	BlockOrRouteOffRepository blockOrRouteOffRepo;
	
	@Autowired
	AlertUtility alertUtility;
	
	String blockConductorAlert = "BLOCKCON";
	String routeOffDriverAlert = "ROUTEOFF";
	String roleIds[]=RestConstants.AlertRoles;
	@Override
	public ConductorFormLoadDto conductorMasterFormOnLoad(String dpCode) {
		log.info("Entering into conductorMasterFormOnLoad service");
		ConductorFormLoadDto conductorFormLoadDto = new ConductorFormLoadDto();
		try {
		
		DepotMaster depotMaster = depotMasterRepository.findByDepotCode(dpCode);
		
		if (depotMaster != null)
			conductorFormLoadDto.setDepotList(this.mapper.map(depotMaster, DepotMasterDto.class));
		
		List<TransportDto> transportList = transportUnitRepository.allTransportMasterByDepot(depotMaster.getId()).stream()
				.map(transport -> new TransportDto(transport.getTransportUnitMaster().getId(), transport.getTransportUnitMaster().getTransportName()))
				.collect(Collectors.toList());
		if (transportList != null && transportList.size() > 0)
			conductorFormLoadDto.setTransportList(transportList);
		
		List<EmploymentTypeDto> emplooyeeTypeList = employmentTypeRepository.findAllByStatus(true).stream()
				.map(employmentTypeDto -> new EmploymentTypeDto(employmentTypeDto.getId(), employmentTypeDto.getEnrolmentName()))
				.collect(Collectors.toList());
		if (emplooyeeTypeList != null && emplooyeeTypeList.size() > 0)
			conductorFormLoadDto.setEmployeeTypeList(emplooyeeTypeList);
		
		List<DepotMasterDto> depotInductionList = depotMasterRepository.findAllByStatus(true).stream()
				.map(depot -> new DepotMasterDto(depot.getId(), depot.getDepotName()))
				.collect(Collectors.toList());
		if (depotInductionList != null && depotInductionList.size() > 0)
				conductorFormLoadDto.setDepotOfInductionList(depotInductionList);
		
		List<RouteCategoryDto> categoryList = routeCategoryRepository.findAllByStatus(true).stream()
				.map(routeCategoryDto -> new RouteCategoryDto(routeCategoryDto.getId(), routeCategoryDto.getRouteCategoryName()))
				.collect(Collectors.toList());
		if (categoryList != null && categoryList.size() > 0)
			conductorFormLoadDto.setCategoryList(categoryList);

	} catch (Exception e) {
		e.printStackTrace();
	}

   return conductorFormLoadDto;
	}
	
	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateConductorMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateConductorMasterStatusFlag service");
		try {
			int i = conductorMasterRepository.updateConductorMasterStatusFlag(flag,id);
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
	public ResponseEntity<ResponseStatus> updateConductorMasterIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updateConductorMasterIsDeletedFlag service");
		try {
			int i = conductorMasterRepository.updateConductorMasterIsDeletedFlag(flag,id);
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
	public ResponseEntity<ResponseStatus> saveConductorMaster(ConductorMaster conductorMaster) {
		log.info("Entering into saveDriverMaster service");
		try {
			if (conductorMaster.getId() == null) {
				if(conductorMaster.getConductorName() == null || conductorMaster.getConductorName().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Conductor name is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getConductorCode() == null || conductorMaster.getConductorCode().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Conductor code is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getFatherName() == null || conductorMaster.getFatherName().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Father name is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getLicenceNo() == null || conductorMaster.getLicenceNo().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver's licence no is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getLicenceIssuePlace() == null || conductorMaster.getLicenceIssuePlace().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver's licence place is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getLicenceValidity() == null || conductorMaster.getLicenceValidity().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Driver's licence validity is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getDepot() == null) {
					return new ResponseEntity<>(new ResponseStatus("Depot is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getBadgeNumber() == null || conductorMaster.getBadgeNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Badge no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getInductionDepot() == null) {
					return new ResponseEntity<>(new ResponseStatus("Depot of induction is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getEmploymentType() == null) {
					return new ResponseEntity<>(new ResponseStatus("Employment type is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getTransportUnit() == null) {
					return new ResponseEntity<>(new ResponseStatus("Transport unit is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getEpfGpfNumber() == null || conductorMaster.getEpfGpfNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("EPF/GPF no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getEsiNumber() == null || conductorMaster.getEpfGpfNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("ESI no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getAddress() == null || conductorMaster.getAddress().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Address is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getMobileNumber() == null || conductorMaster.getMobileNumber().equals("")) {
					return new ResponseEntity<>(new ResponseStatus("Mobile no. is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}else if(conductorMaster.getConductorCategory() == null) {
					return new ResponseEntity<>(new ResponseStatus("Conductor Category is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					
				}
				conductorMaster.setIsDeleted(false);;
				conductorMasterRepository.save(conductorMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Conductor master has been persisted successfully.", HttpStatus.OK),
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
	public List<ConductorMaster> getAllConductorMaster(String dpCode) {
		log.info("Entering into getAllConductorMaster service");
		List<ConductorMaster> conductorsMasterList = null;
		try {
			conductorsMasterList = conductorMasterRepository.findAllByDepot(dpCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conductorsMasterList;
	}

	@Override
	public List<ConductorMaster> fetchAllConductorMaster() {
		log.info("Entering into getAllConductorMaster service");
		List<ConductorMaster> conductorsMasterList = null;
		try {
			conductorsMasterList = conductorMasterRepository.findAllByStatus(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conductorsMasterList;
	}

	@Override
	public List<ConductorMaster> getConductorNames() {
		List<ConductorMaster> conductorNM= new ArrayList<ConductorMaster>();
		List<Object[]> conductorNames = conductorMasterRepository.getConductorNames();
		ConductorMaster conductorName=null;
		for(Object[]ob:conductorNames)
		{
			 conductorName= new ConductorMaster();
			conductorName.setId(Integer.parseInt(ob[0].toString()));
			conductorName.setConductorName(ob[1].toString());
			conductorNM.add(conductorName);
		}
		return conductorNM;
	}
	
	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> updateConductorMasterCategoryById(Integer conductorId, Integer categoryId) {
		int i = 0;
		RouteCategoryHistory history = new RouteCategoryHistory();
		try {
			i = conductorMasterRepository.updateDriverMasterCategoryById(conductorId, categoryId);
			if (i > 0) {
				Optional<ConductorMaster> conductor = conductorMasterRepository.findById(conductorId);
				history.setConductorMaster(conductor.get());
				history.setRouteCategoryMaster(conductor.get().getConductorCategory());
				routeCategoryHistoryRepository.save(history);
				return new ResponseEntity<>(new ResponseStatus("Conductor category updated successfully.", HttpStatus.OK),
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
	
	public ResponseEntity<ResponseStatus> blockConductor(Integer id, String reason,Date fromDate,Date toDate,String type,String depotCode,String orderNo,Date orderDate ) {
//	 type - true -> conductor  type - false -> driver
		try {
			DepotMaster depot = depotMasterRepo.findByDepotCode(depotCode);

			if(!type.isEmpty() && type.equals("C")){
			Optional<ConductorMaster> masterRecord = conductorMasterRepository.findById(id);
			if (masterRecord.isPresent()) {
				ConductorBlockHistory history = conductorBlockHistoryRepository.findLatestRepord(masterRecord.get().getId(), depot.getId());
				System.out.println(history==null ? " null" : history.getReason());
				if(history==null || (history.getToDate()!=null && history.getToDate().before(new Date()))){
//				masterRecord.get().setBlocked(true);
//				conductorMasterRepository.save(masterRecord.get());
				ConductorBlockHistory conductorBlockHistory = new ConductorBlockHistory();
				conductorBlockHistory.setConductorId(masterRecord.get());
				conductorBlockHistory.setReason(reason);
				conductorBlockHistory.setFromDate(fromDate);
				conductorBlockHistory.setToDate(toDate);
				conductorBlockHistory.setDepotId(depot);
				conductorBlockHistory.setOrderDate(orderDate);
				conductorBlockHistory.setOrderNumber(orderNo);
				conductorBlockHistory.setIsDeleted(true);
				conductorBlockHistoryRepository.save(conductorBlockHistory);
				return new ResponseEntity<>(new ResponseStatus("Conductor "+ masterRecord.get().getConductorName()+ " is blocked successfully.", HttpStatus.OK),
						HttpStatus.OK); 
				}
				return new ResponseEntity<>(new ResponseStatus("Conductor "+ masterRecord.get().getConductorName()+ " is already blocked", HttpStatus.OK),
						HttpStatus.OK);
				}
				
			} else if(!type.isEmpty() && type.equals("D")) {
				Optional<DriverMaster> driverMasterRecord = driverMasterRepository.findById(id);
				if(driverMasterRecord.isPresent()) {
					DriverBlockHistory driverHistoryRecord = driverBlockHistoryRepository.findLatestRepord(driverMasterRecord.get().getId(),depot.getId());
					if(driverHistoryRecord==null || (driverHistoryRecord.getToDate()!=null && driverHistoryRecord.getToDate().before(new Date()))) {
//					driverMasterRecord.get().setBlocked(true);
//					driverMasterRepository.save(driverMasterRecord.get());
					DriverBlockHistory driverHistory =  new DriverBlockHistory();
					driverHistory.setDriverId(driverMasterRecord.get());
					driverHistory.setFromDate(fromDate);
					driverHistory.setToDate(toDate);
					driverHistory.setReason(reason);
					driverHistory.setDepotId(depot);
					driverHistory.setOrderDate(orderDate);
					driverHistory.setOrderNumber(orderNo);
					driverHistory.setIsDeleted(true);
					driverBlockHistoryRepository.save(driverHistory);
					return new ResponseEntity<>(new ResponseStatus("Driver "+driverMasterRecord.get().getDriverName()+" is blocked successfully.", HttpStatus.OK),
							HttpStatus.OK);
				} 
					return new ResponseEntity<>(new ResponseStatus("Driver "+driverMasterRecord.get().getDriverName()+" is already blocked.", HttpStatus.OK),
							HttpStatus.OK);
				}
			} else if (!type.isEmpty() && type.equals("R")) {
				Optional<RouteMaster> routeMasterRecord = routeMasterRepo.findById(id);
				if(routeMasterRecord.isPresent())  {
					RouteBlockHistory routeHistoryObj = routeBlockHistoryRepo.findLatestRepord(routeMasterRecord.get().getId(),
							depot.getId());
					if(routeHistoryObj==null || (routeHistoryObj.getToDate()!=null && routeHistoryObj.getToDate().before(new Date()))) {
//						routeMasterRecord.get().setBlocked(true);
//						routeMasterRepo.save(routeMasterRecord.get());
						RouteBlockHistory routeHistory = new RouteBlockHistory();
						routeHistory.setReason(reason);
						routeHistory.setRouteId(routeMasterRecord.get());
						routeHistory.setFromDate(fromDate);
						routeHistory.setToDate(toDate);
						routeHistory.setDepotId(depot);
						routeHistory.setOrderDate(orderDate);
						routeHistory.setOrderNumber(orderNo);
						routeHistory.setIsDeleted(true);
						routeBlockHistoryRepo.save(routeHistory);
						return new ResponseEntity<>(new ResponseStatus("Route "+routeMasterRecord.get().getRouteName()+" is blocked successfully.", HttpStatus.OK),
								HttpStatus.OK);
					} 
					return new ResponseEntity<>(new ResponseStatus("Route "+routeMasterRecord.get().getRouteName()+" is already blocked.", HttpStatus.OK),
							HttpStatus.OK);
				}
			} else if (!type.isEmpty() && type.equals("B")) {
				Optional<BusMaster> busMasterRecord = busMasterRepo.findById(id);
				if(busMasterRecord.isPresent()) {
					BusBlockHistory busHistoryObj = busBlockHistoryRepo.findLatestRepord(busMasterRecord.get().getId(),depot.getId());
					if(busHistoryObj==null || (busHistoryObj.getToDate()!=null && busHistoryObj.getToDate().before(new Date()))) {
//						busMasterRecord.get().setBlocked(true);
//						busMasterRepo.save(busMasterRecord.get());
						BusBlockHistory busHistory = new BusBlockHistory();
						busHistory.setFromDate(fromDate);
						busHistory.setToDate(toDate);
						busHistory.setBusId(busMasterRecord.get());
						busHistory.setReason(reason);
						busHistory.setDepotId(depot);
						busHistory.setOrderDate(orderDate);
						busHistory.setOrderNumber(orderNo);
						busHistory.setIsDeleted(true);
						busBlockHistoryRepo.save(busHistory);
						return new ResponseEntity<>(new ResponseStatus("Bus "+busMasterRecord.get().getBusRegNumber()+" is blocked successfully.", HttpStatus.OK),
								HttpStatus.OK);
					}
				}
				return new ResponseEntity<>(new ResponseStatus("Bus "+busMasterRecord.get().getBusRegNumber()+" is already blocked.", HttpStatus.OK),
						HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}

		return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN), HttpStatus.OK);

	}

	public ResponseEntity<ResponseStatus> unBlockConductor(Integer id,String type) {
		try {
			if(!type.isEmpty() && type.equals("C")){
			Optional<ConductorMaster> masterRecord = conductorMasterRepository.findById(id);
			if (masterRecord.isPresent()) {
				if (masterRecord.get().getBlocked()!=null && masterRecord.get().getBlocked()) {
					masterRecord.get().setBlocked(false);
					conductorMasterRepository.save(masterRecord.get());
					return new ResponseEntity<>(
							new ResponseStatus("Conductor is unblocked successfully.", HttpStatus.OK), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							new ResponseStatus("Conductor is not blocked in first place.", HttpStatus.OK),
							HttpStatus.OK);
				}
			}
			} else if(!type.isEmpty() && type.equals("D")) {
				Optional<DriverMaster> driverMasterRecord = driverMasterRepository.findById(id);
				if (driverMasterRecord.get().getBlocked()!=null && driverMasterRecord.get().getBlocked()) {
					driverMasterRecord.get().setBlocked(false);
					driverMasterRepository.save(driverMasterRecord.get());
					return new ResponseEntity<>(
							new ResponseStatus("Driver "+driverMasterRecord.get().getDriverName()+" is unblocked successfully.", HttpStatus.OK), HttpStatus.OK);
				}else {
					return new ResponseEntity<>(
							new ResponseStatus("Driver "+driverMasterRecord.get().getDriverName()+" is not blocked in first place.", HttpStatus.OK),
							HttpStatus.OK);
				}
			} else if (!type.isEmpty() && type.equals("R")) {
				Optional<RouteMaster> routeMasterRecord = routeMasterRepo.findById(id);
				if(routeMasterRecord.isPresent() && routeMasterRecord.get().getBlocked()!=null && routeMasterRecord.get().getBlocked()) {
					routeMasterRecord.get().setBlocked(false);
					routeMasterRepo.save(routeMasterRecord.get());
					return new ResponseEntity<>(
							new ResponseStatus("Route "+routeMasterRecord.get().getRouteName()+" is unblocked successfully.", HttpStatus.OK), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							new ResponseStatus("Route "+routeMasterRecord.get().getRouteName()+" is not blocked in first place.", HttpStatus.OK), HttpStatus.OK);
				}

			} else if(!type.isEmpty() && type.equals("B")) {
				Optional<BusMaster> busMasterRecord = busMasterRepo.findById(id);
				if(busMasterRecord.isPresent() && busMasterRecord.get().getBlocked()!=null && busMasterRecord.get().getBlocked()) {
					busMasterRecord.get().setBlocked(false);
					busMasterRepo.save(busMasterRecord.get());
					return new ResponseEntity<>(
							new ResponseStatus("bus "+busMasterRecord.get().getBusRegNumber()+" is unblocked successfully.", HttpStatus.OK), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							new ResponseStatus("bus "+busMasterRecord.get().getBusRegNumber()+" is not blocked in first place.", HttpStatus.OK), HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Something went wrong,Please Try again later", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new ResponseStatus("Something went wrong,Please try again later", HttpStatus.FORBIDDEN), HttpStatus.OK);
	}
	
	public List<ConductorAndDriverBlockHistoryDto> fetchAllBlockedConductor(String depotCode) {
		List<DriverBlockHistoryDto> driver =  new ArrayList<>();
		List<ConductorAndDriverBlockHistoryDto> output = new ArrayList<>();
		ConductorAndDriverBlockHistoryDto blockedList= new ConductorAndDriverBlockHistoryDto();
		try {
//			DepotMaster depot = depotMasterRepo.findByDepotCode(depotCode);
			
//			List<Object[]>objects = conductorMasterRepository.findAllBlockedConductors();
//			for(Object[] obj : objects){
				DriverBlockHistoryDto dto = new DriverBlockHistoryDto();
				
				List<ConductorBlockHistory> historyRecord = conductorBlockHistoryRepository.findAll();
				for(ConductorBlockHistory c : historyRecord) {
					dto = new DriverBlockHistoryDto();
				dto.setCode(c.getConductorId().getConductorCode());
				dto.setId(c.getConductorId().getId());
				dto.setName(c.getConductorId().getConductorName());
				dto.setBadge(c.getConductorId().getConductorCode());
				dto.setFromDate(c.getFromDate());
				dto.setToDate(c.getToDate());
				dto.setReason(c.getReason());
//				dto.setBlocked(Boolean.valueOf(obj[4].toString()));
				dto.setEntity("Conductor");
				dto.setOrderDate(c.getOrderDate());
				dto.setOrderNumber(c.getOrderNumber());
				dto.setType("C");
				driver.add(dto);
				}
//			}
//			for(DriverMaster driverObj : driverObjs) {
				DriverBlockHistoryDto driverDto = new DriverBlockHistoryDto();
				List<DriverBlockHistory> driverHistoryRecords = driverBlockHistoryRepository.findAll();
				
				for(DriverBlockHistory driverHistory : driverHistoryRecords) {
					driverDto = new DriverBlockHistoryDto();
					driverDto.setId(driverHistory.getDriverId().getId());
					driverDto.setName(driverHistory.getDriverId().getDriverName());
				driverDto.setFromDate(driverHistory.getFromDate());
				driverDto.setToDate(driverHistory.getToDate());
				driverDto.setBadge(driverHistory.getDriverId().getBadgeNumber());
				driverDto.setReason(driverHistory.getReason());
				driverDto.setOrderDate(driverHistory.getOrderDate());
				driverDto.setOrderNumber(driverHistory.getOrderNumber());
//				driverDto.setBlocked(driverHistory.getDriverId());
				driverDto.setEntity("Driver");
				driverDto.setType("D");
				driver.add(driverDto);
				}
				
//			}
			
//			List<BusMaster> busObjs = busMasterRepo.findAllBlockedRoutes();
//			for(BusMaster busObj : busObjs) {
				DriverBlockHistoryDto busDto = new DriverBlockHistoryDto();
				List<BusBlockHistory> busHistoryRecords = busBlockHistoryRepo.findAll();
			
				for(BusBlockHistory busHistory : busHistoryRecords) {
					busDto = new DriverBlockHistoryDto();
				busDto.setId(busHistory.getId());
				busDto.setName(busHistory.getBusId().getBusRegNumber());
				busDto.setFromDate(busHistory.getFromDate());
				busDto.setToDate(busHistory.getToDate());
				busDto.setBadge(busHistory.getBusId().getChessisNumber());
				busDto.setReason(busHistory.getReason());
				busDto.setOrderDate(busHistory.getOrderDate());
				busDto.setOrderNumber(busHistory.getOrderNumber());
//				busDto.setBlocked(busHistory.getBlocked());
				busDto.setEntity("Bus");
				busDto.setType("B");
				driver.add(busDto);
				}
				
//			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		blockedList.setDriverBlockHistory(driver);
		output.add(blockedList);
		return output;
	}
	
	public List<ConductorAndDriverBlockHistoryDto> fetchAllBlockedRoutes(String depotCode) {
		List<DriverBlockHistoryDto> driver = new ArrayList<>();
		List<ConductorAndDriverBlockHistoryDto> output = new ArrayList<>();
		ConductorAndDriverBlockHistoryDto blockedList = new ConductorAndDriverBlockHistoryDto();
		try {
//			DepotMaster depot = depotMasterRepo.findByDepotCode(depotCode);

//			List<RouteMaster> routeObjs = routeMasterRepo.findAllBlockedRoutes();
//			for (RouteMaster routeObj : routeObjs) {
				DriverBlockHistoryDto routeDto = new DriverBlockHistoryDto();
				List<RouteBlockHistory> routeHistoryRecords = routeBlockHistoryRepo.findAll();
				for(RouteBlockHistory routeHistory : routeHistoryRecords){
					routeDto = new DriverBlockHistoryDto();
				routeDto.setId(routeHistory.getRouteId().getId());
				routeDto.setName(routeHistory.getRouteId().getRouteName());
				routeDto.setFromDate(routeHistory.getFromDate());
				routeDto.setToDate(routeHistory.getToDate());
				routeDto.setBadge(routeHistory.getRouteId().getRouteCode());
				routeDto.setReason(routeHistory.getReason());
				routeDto.setOrderDate(routeHistory.getOrderDate());
				routeDto.setOrderNumber(routeHistory.getOrderNumber());
//				routeDto.setBlocked(routeHistory.getRouteId().getBlocked());
				routeDto.setEntity("Route");
				routeDto.setType("R");
				driver.add(routeDto);
				}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		blockedList.setDriverBlockHistory(driver);
		output.add(blockedList);

		return output;

	}
	
	
	public ResponseEntity<ResponseStatus> newBlockOrRouteOff(String reason,Integer id,String type,String depotId,Boolean routeOff,Boolean blocked,String orderNumber,Date orderDate) {
		try {
			BlockOrRouteOffEntity newEntity = new BlockOrRouteOffEntity();
			DepotMaster depot = depotMasterRepo.findByDepotCode(depotId);
			if(id!=null && type.equals("C")){
				Optional<ConductorMaster> masterRecord = conductorMasterRepository.findById(id);
				newEntity.setConductorId(masterRecord.get());
			}
			if(id!=null && type.equals("D")){
				Optional<DriverMaster> driverMasterRecord = driverMasterRepository.findById(id);
				newEntity.setDriverId(driverMasterRecord.get());

			}
			newEntity.setReason(reason);
			newEntity.setBlocked(blocked);
			newEntity.setOrderDate(orderDate);
			newEntity.setOrderNumber(orderNumber);
			newEntity.setRouteOff(routeOff);
			newEntity.setIsBlocked(true);
			newEntity.setDepotId(depot);
			blockOrRouteOffRepo.save(newEntity);
			//AlertUtility alertUtility=new AlertUtility();
			//String displayId=null;
			 String NextReviewDateStr = new SimpleDateFormat("dd-MM-yyyy").format(orderDate);
			if(type.equals("C")){		
				for(String s : roleIds) {
				alertUtility.insertNotification("Conductor:- "+ newEntity.getConductorId().getConductorName()+" ("+newEntity.getConductorId().getConductorCode()+")" +" is Blocked on "+NextReviewDateStr, blockConductorAlert, "Conductor", id, newEntity.getDepotId().getId(), s);
				}
				}
			if(type.equals("D")){
				for(String s : roleIds) {
				alertUtility.insertNotification("Driver:- "+newEntity.getDriverId().getDriverName()+ " ("+newEntity.getDriverId().getDriverCode()+")" + " is Route Off "+NextReviewDateStr, routeOffDriverAlert, "Driver", id, newEntity.getDepotId().getId(), s);
				}
				}
			return new ResponseEntity<>(
					new ResponseStatus("Blocked Successfully", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please check", HttpStatus.OK),
					HttpStatus.OK);
		}
		
	}
	
	@Transactional
	public ResponseEntity<ResponseStatus> unBlock(Integer id) {
		Optional<BlockOrRouteOffEntity> existingRecord = blockOrRouteOffRepo.findById(id);
		
		if(existingRecord.isPresent()){
			
			if(existingRecord.get().getIsBlocked()){
				
					existingRecord.get().setIsBlocked(false);
					blockOrRouteOffRepo.save(existingRecord.get());
					 String NextReviewDateStr = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
					if(existingRecord.get().getConductorId()!=null){
						for(String s : roleIds) {
						alertUtility.insertNotification("Conductor:- "+existingRecord.get().getConductorId().getConductorName()+" ("+existingRecord.get().getConductorId().getConductorCode()+")" + " is Unblocked on "+NextReviewDateStr, blockConductorAlert, "Conductor", existingRecord.get().getConductorId().getId(), existingRecord.get().getDepotId().getId(), " ");
						}
						}
					if(existingRecord.get().getDriverId()!=null){
						for(String s : roleIds) {
						alertUtility.insertNotification("Driver:- "+existingRecord.get().getDriverId().getDriverName()+" ("+existingRecord.get().getDriverId().getDriverCode()+")" + " is Unblocked on "+NextReviewDateStr, routeOffDriverAlert, "Driver", existingRecord.get().getDriverId().getId(), existingRecord.get().getDepotId().getId(), " ");
						}
						}
					return new ResponseEntity<>(
							new ResponseStatus("Unblocked Successfully!", HttpStatus.OK),
							HttpStatus.OK);
					} else {
						return new ResponseEntity<>(
								new ResponseStatus("This record is not unblocked", HttpStatus.OK),
								HttpStatus.OK);
					}
		} else {
			return new ResponseEntity<>(
					new ResponseStatus("This record dosen't exist", HttpStatus.OK),
					HttpStatus.OK);
		}
	
		
	}
	
	public List<DriverBlockHistoryDto> findAllrouteOff(String depotCode) {
		DepotMaster depot = depotMasterRepo.findByDepotCode(depotCode);
		List<DriverBlockHistoryDto> output =new ArrayList<>();
		for(BlockOrRouteOffEntity entity : blockOrRouteOffRepo.findAllByDepot(depot.getId())){
			DriverBlockHistoryDto dto = new DriverBlockHistoryDto();
			dto.setId(entity.getId());
			dto.setOrderDate(entity.getOrderDate());
			dto.setOrderNumber(entity.getOrderNumber());
			dto.setReason(entity.getReason());
			if(entity.getConductorId()!=null){
				dto.setName(entity.getConductorId().getConductorName());
				dto.setType("C");
				dto.setEntity("Conductor");
				dto.setCode(entity.getConductorId().getConductorCode());
				dto.setBadge(entity.getConductorId().getTransportUnit().getTransportName());
			}
			if(entity.getDriverId()!=null){
				dto.setName(entity.getDriverId().getDriverName());
				dto.setType("D");
				dto.setEntity("Driver");
				dto.setCode(entity.getDriverId().getDriverCode());
				dto.setBadge(entity.getDriverId().getTransportUnit().getTransportName());
			}
			dto.setIsBlocked(entity.getIsBlocked());
			dto.setBlocked(entity.getBlocked());
			dto.setRouteOff(entity.getRouteOff());
			output.add(dto);
		}
		return output;
	}
	
	public List<ConductorMaster> findUnblockedConductors(String depotCode) {
		DepotMaster depot = depotMasterRepo.findByDepotCode(depotCode);
		System.out.println(depot.getId());
		List<ConductorMaster> output = new ArrayList<>();
		List<Object[]> objects = conductorMasterRepository.findUnblockedConductors(depot.getId());
		for (Object a[] : objects) {
			ConductorMaster master = new ConductorMaster();
			master.setId(Integer.parseInt(a[0].toString()));
			master.setConductorName(a[1].toString());
			output.add(master);
		}
		return output;
	}
	
	

}
