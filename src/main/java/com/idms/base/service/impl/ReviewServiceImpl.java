package com.idms.base.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.ReviewAverageDto;
import com.idms.base.api.v1.model.dto.ReviewCommentsDto;
import com.idms.base.api.v1.model.dto.ReviewDto;
import com.idms.base.api.v1.model.dto.ReviewMasterDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntity;
import com.idms.base.dao.entity.ReviewCommentsHistory;
import com.idms.base.dao.entity.RouteCategoryMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusRefuelingMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.EtmTBAssignmentRepository;
import com.idms.base.dao.repository.IssueEtmTicketBoxRepository;
import com.idms.base.dao.repository.ReviewCommentsRepository;
import com.idms.base.dao.repository.RouteCategoryHistoryRepository;
import com.idms.base.dao.repository.RouteCategoryRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;
import com.idms.base.util.AlertUtility;

@Service
public class ReviewServiceImpl {

	@Autowired
	BusRefuelingMasterRepository busRefuelingMasterRepo;

	@Autowired
	DailyRoasterRepository dailyRoasterRepository;
	
	@Autowired
	ReviewCommentsRepository reviewCommentsRepo;
	
	@Autowired
	BusMasterRepository busMasterRepo;
	
	@Autowired
	DriverMasterRepository driverMasterRepo;
	
	@Autowired
	ConductorMasterRepository conductorMasterRepo;
	
	@Autowired
	RouteMasterRepository routeMasterRepo;
	
	@Autowired
	DepotMasterRepository depotMasterRepo;
	
	@Autowired
	RouteCategoryHistoryRepository routeCategoryRepo;
	
	@Autowired
	IssueEtmTicketBoxRepository issueEtmRepo;
	
	@Autowired
	EtmTBAssignmentRepository etmAssignmentRepo;
	
	@Autowired
	RouteCategoryRepository categoryRepo;
	
	@Autowired
	AlertUtility alertUtility;
	
	String roleIds[]=RestConstants.AlertRoles;

	public ReviewMasterDto getDetailsOnId(Integer id, String type,String previousReviewDate) throws ParseException {
		ReviewMasterDto output =  new ReviewMasterDto();
		ReviewAverageDto averageDto = new ReviewAverageDto();
		List<ReviewDto> dtoList  = new ArrayList<>();
		int Vtssize =0;
		float totalVts=0;
		int schedueledKmsSize=0;
		float totalScheduledKms = 0;
		int epkmSize=0;
		double epkmSum=0;
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		ReviewDto dto =  new ReviewDto();
		List<DailyRoaster> data = null;
		Date fromDate = new Date();
		if(previousReviewDate==null || previousReviewDate.isEmpty()) {
			Calendar c= Calendar.getInstance();
			c.add(Calendar.DATE, -30);		
			fromDate = c.getTime();
		}
		fromDate = !previousReviewDate.isEmpty() ? sdf.parse(previousReviewDate) : fromDate;
		Date d =sdf.parse(sdf.format(new Date()));
		if (!type.isEmpty() && type.equals("D")) {
			data = dailyRoasterRepository.findLastThirtyDaysRecord(id, 0, 0, 0,fromDate,new Date());
		} else if (!type.isEmpty() && type.equals("C")) {
			data = dailyRoasterRepository.findLastThirtyDaysRecord(0, id, 0, 0,fromDate,new Date());
		} else if (!type.isEmpty() && type.equals("B")) {
			data = dailyRoasterRepository.findLastThirtyDaysRecord(0, 0, id, 0,fromDate,new Date());
		} else if (!type.isEmpty() && type.equals("R")) {
			data = dailyRoasterRepository.findLastThirtyDaysRecord(0, 0, 0, id,fromDate,new Date());
		}
		
		for(DailyRoaster obj :data) {
			dto= new ReviewDto();
			dto.setRouteCode(obj.getRoute().getRouteCode());
			dto.setRouteName(obj.getRoute().getRouteName());
			if(obj.getId()!=null) {
			IssueEtmTicketBoxEntity etm = issueEtmRepo.findByRoasterId(obj.getId());
			if(etm!=null && etm.getId()!=null){
			EtmTBAssignment etmData = etmAssignmentRepo.findByIetmId(etm.getId());
			if(etmData.getEarning()!=null && etmData.getEarning().getEarningPerKM()!=null){
			dto.setEpkm(etmData.getEarning()!=null && etmData.getEarning().getEarningPerKM()!=null ? String.valueOf(etmData.getEarning().getEarningPerKM()) : " ");
				epkmSize++;
				epkmSum=epkmSum+etmData.getEarning().getEarningPerKM();
			}
			}
			}
			if(obj.getRefueling()!=null) {
			dto.setVtsKm(obj.getRefueling().getVtsKms()!=null ? obj.getRefueling().getVtsKms().toString() : " " );
			dto.setTotalKm(obj.getRefueling().getTotalActualKms().toString());
			dto.setKmpl(obj.getRefueling().getKmplAsScheduledKilometer().toString());
			}
			dtoList.add(dto);
			if(obj.getRefueling()!=null && obj.getRefueling().getVtsKms()!=null) {
				Vtssize++;
				totalVts= totalVts+ obj.getRefueling().getVtsKms();
			}
			if(obj.getRefueling()!=null && obj.getRefueling().getKmplAsScheduledKilometer()!=null) {
				schedueledKmsSize++;
				totalScheduledKms =totalScheduledKms+ obj.getRefueling().getKmplAsScheduledKilometer();
			}
		}	
		averageDto.setEpkmAvg(epkmSize>0 ? String.valueOf(epkmSum/epkmSize) : String.valueOf(epkmSum));
		averageDto.setVtsAvg(Vtssize>0 ? String.valueOf(totalVts/Vtssize) : String.valueOf(totalVts));
		averageDto.setScheduledKmAvg(schedueledKmsSize>0 ? String.valueOf(totalScheduledKms/schedueledKmsSize) : String.valueOf(totalScheduledKms));
		output.setAverages(averageDto);
		output.setReviewDto(dtoList);
		return output;
	}
	
	public ResponseEntity<ResponseStatus> saveReviewDetails(String comments, Integer id , String type,String depotCode,Integer categoryCode) {
		ReviewCommentsHistory comment = new ReviewCommentsHistory();
		DepotMaster depot = depotMasterRepo.findByDepotCode(depotCode);
		comment.setComments(comments);
		comment.setDepotId(depot);
		Calendar c= Calendar.getInstance();
		c.add(Calendar.DATE, 30);
		Date nextReviewDate=c.getTime();
		comment.setNextReviewDate(nextReviewDate);
		Optional<RouteCategoryMaster> category  = categoryRepo.findById(categoryCode);
		comment.setCategoryId(category.isPresent() ? category.get() : null);
		try {
			if (!type.isEmpty() && type.equals("D")) {
				Optional<DriverMaster> driver = driverMasterRepo.findById(id);
				comment.setDriverMaster(driver.get());
			} else if (!type.isEmpty() && type.equals("C")) {
				Optional<ConductorMaster> conductor = conductorMasterRepo.findById(id);
				comment.setConductorMaster(conductor.get());
			} else if (!type.isEmpty() && type.equals("B")) {
				Optional<BusMaster> bus = busMasterRepo.findById(id); 
				comment.setBusMaster(bus.get());
			} else if (!type.isEmpty() && type.equals("R")) {
				Optional<RouteMaster> route = routeMasterRepo.findById(id);	
				comment.setRouteMaster(route.get());
			}	
			reviewCommentsRepo.save(comment);
			
			return new ResponseEntity<>(new ResponseStatus("Comments are saved successfully.", HttpStatus.OK),
					HttpStatus.OK); 
		} catch (Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		
	}
	
	public List<ReviewCommentsDto> getReviewCommentsList(String depotCode) {
		List<ReviewCommentsDto> reviewComments = new ArrayList<>();
		DepotMaster depot = depotMasterRepo.findByDepotCode(depotCode);
		for(ReviewCommentsHistory comment : reviewCommentsRepo.findByDepot(depot.getId())) {		
			ReviewCommentsDto dto = new ReviewCommentsDto();
			dto.setNextReviewDate(comment.getNextReviewDate());
			dto.setReviewDate(comment.getCreatedOn());
			dto.setRemarks(comment.getComments());
			
			if(comment.getBusMaster()!=null) {
				dto.setId(comment.getBusMaster().getId());
				dto.setName(comment.getBusMaster().getBusRegNumber());
				dto.setType("Bus");		
			} else if(comment.getConductorMaster()!=null) {
				dto.setId(comment.getConductorMaster().getId());
				dto.setName(comment.getConductorMaster().getConductorName());
				Optional<ConductorMaster> conductor = conductorMasterRepo.findById(comment.getConductorMaster().getId());
				dto.setType("Conductor");
				dto.setCategory(conductor.get().getConductorCategory().getRouteCategoryName());
			} else if (comment.getDriverMaster()!=null) {
				dto.setId(comment.getDriverMaster().getId());
				dto.setName(comment.getDriverMaster().getDriverName());
				Optional<DriverMaster> driver = driverMasterRepo.findById(comment.getDriverMaster().getId());
				dto.setType("Driver");
				dto.setCategory(driver.get().getDriverCategory().getRouteCategoryName());
			} else if(comment.getRouteMaster()!=null) {
				dto.setId(comment.getRouteMaster().getId());
				dto.setName(comment.getRouteMaster().getRouteName());
				Optional<RouteMaster> route = routeMasterRepo.findById(comment.getRouteMaster().getId());
				dto.setType("Route");
				dto.setCategory(route.get().getRouteCategoryMaster().getRouteCategoryName());
			}
			reviewComments.add(dto);		
		}
		return reviewComments;
	}
	
	public  List<ReviewCommentsDto> reviewIndividualEntities(String depotCode,String type) {
		List<ReviewCommentsDto> output = new ArrayList<>();
		try {
			if(type.equals("B")) {
			List<BusMaster> busMaster = busMasterRepo.findAllBusesByDepot(depotCode);
			busMaster.sort(Comparator.comparingInt(BusMaster::getId));
			for ( BusMaster busRecord : busMaster) {
				ReviewCommentsDto dto = new ReviewCommentsDto();
				dto.setId(busRecord.getId());
				dto.setName(busRecord.getBusRegNumber()!=null ? busRecord.getBusRegNumber() : " ");
				dto.setType("BUS");
				dto.setTypeCode("B");
				ReviewCommentsHistory historyRecord = reviewCommentsRepo.findByUniqueId(busRecord.getId(), 0,0,0);
				if(historyRecord!=null) {
					dto.setReviewDate(historyRecord.getCreatedOn());
					dto.setNextReviewDate(historyRecord.getNextReviewDate());
					dto.setRemarks(historyRecord.getComments());
				}
				output.add(dto);
			}	
			} else if (type.equals("D")) {
				List<DriverMaster> driverMaster = driverMasterRepo.findAllByDepot(depotCode);
				driverMaster.sort(Comparator.comparingInt(DriverMaster::getId));
				for ( DriverMaster driverRecord : driverMaster) {
					ReviewCommentsDto dto = new ReviewCommentsDto();
					dto.setId(driverRecord.getId());
					dto.setName(driverRecord.getDriverName()!=null ? driverRecord.getDriverName() : " ");
					dto.setType("DRIVER");
					dto.setTypeCode("D");
					dto.setUniqueId(driverRecord.getDriverCode());
					dto.setCategory(driverRecord.getDriverCategory().getRouteCategoryName());
					ReviewCommentsHistory historyRecord = reviewCommentsRepo.findByUniqueId(0, 0,driverRecord.getId(),0);
					if(historyRecord!=null) {
						dto.setReviewDate(historyRecord.getCreatedOn());
						dto.setNextReviewDate(historyRecord.getNextReviewDate());
						dto.setRemarks(historyRecord.getComments());
					}
					output.add(dto);
				}
			}else if (type.equals("C")) {
				List<ConductorMaster> conductorMaster = conductorMasterRepo.findAllByDepot(depotCode);
				conductorMaster.sort(Comparator.comparingInt(ConductorMaster::getId));
				for ( ConductorMaster conductorRecord : conductorMaster) {
					ReviewCommentsDto dto = new ReviewCommentsDto();
					dto.setId(conductorRecord.getId());
					dto.setName(conductorRecord.getConductorName()!=null ? conductorRecord.getConductorName() : " ");	
					dto.setType("CONDUCTOR");
					dto.setTypeCode("C");
					dto.setUniqueId(conductorRecord.getConductorCode());
					dto.setCategory(conductorRecord.getConductorCategory().getRouteCategoryName());
					ReviewCommentsHistory historyRecord = reviewCommentsRepo.findByUniqueId(0, 0,0,conductorRecord.getId());
					if(historyRecord!=null) {
						dto.setReviewDate(historyRecord.getCreatedOn());
						dto.setNextReviewDate(historyRecord.getNextReviewDate());
						dto.setRemarks(historyRecord.getComments());

					}
					output.add(dto);
				}
			}else if (type.equals("R")) {
				List<RouteMaster> conductorMaster = routeMasterRepo.getAllRouteMasterByDepot(depotCode);
				conductorMaster.sort(Comparator.comparingInt(RouteMaster::getId));
				for ( RouteMaster conductorRecord : conductorMaster) {
					ReviewCommentsDto dto = new ReviewCommentsDto();
					dto.setId(conductorRecord.getId());
					dto.setName(conductorRecord.getRouteName()!=null ? conductorRecord.getRouteName() : " ");	
					dto.setType("ROUTE");
					dto.setTypeCode("R");
					dto.setUniqueId(conductorRecord.getRouteId());
					dto.setCategory(conductorRecord.getRouteCategoryMaster().getRouteCategoryName());
					ReviewCommentsHistory historyRecord = reviewCommentsRepo.findByUniqueId(0, conductorRecord.getId(),0,0);
					if(historyRecord!=null) {
						dto.setReviewDate(historyRecord.getCreatedOn());
						dto.setNextReviewDate(historyRecord.getNextReviewDate());
						dto.setRemarks(historyRecord.getComments());
					}
					output.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return output;
	
	}

	public List<ReviewCommentsDto> getIndividualHistoryData(Integer id, String type) {
		 List<ReviewCommentsDto> output = new ArrayList<>();
		List<ReviewCommentsHistory> comments  = new ArrayList<>();
		if (!type.isEmpty() && type.equals("B")) {
			 comments = reviewCommentsRepo.findByUniqueHistory(id, 0, 0, 0);
		} else if (!type.isEmpty() && type.equals("R")) {
			 comments = reviewCommentsRepo.findByUniqueHistory(0, id, 0, 0);
		} else if (!type.isEmpty() && type.equals("D")) {
			 comments = reviewCommentsRepo.findByUniqueHistory(0, 0, id, 0);
		} else if (!type.isEmpty() && type.equals("C")) {
			 comments = reviewCommentsRepo.findByUniqueHistory(0, 0, 0, id);
		}
		for(ReviewCommentsHistory sr: comments) {
			ReviewCommentsDto dto = new ReviewCommentsDto();
			String name = "";
			if (type.equals("B")) {
				name = sr.getBusMaster().getBusRegNumber();
			} else if (type.equals("R")) {
				name = sr.getRouteMaster().getRouteName();
			} else if (type.equals("D")) {
				name = sr.getDriverMaster().getDriverName();
			} else if (type.equals("C")) {
				name = sr.getConductorMaster().getConductorName();
			}
//			name = type.equals("B") && sr.getBusMaster()!=null ? sr.getBusMaster().getBusRegNumber() : " ";
//			name = type.equals("R") && sr.getRouteMaster()!=null ? sr.getRouteMaster().getRouteName(): " ";
//			name = type.equals("D") && sr.getDriverMaster()!=null? sr.getDriverMaster().getDriverName() : " ";
//			name = type.equals("C") && sr.getConductorMaster()!=null? sr.getConductorMaster().getConductorName() : " ";
			dto.setName(name);
			dto.setRemarks(sr.getComments());
			dto.setNextReviewDate(sr.getNextReviewDate());
			dto.setReviewDate(sr.getCreatedOn());
			dto.setId(sr.getId());
			dto.setCategory(sr.getCategoryId()==null || sr.getCategoryId().getRouteCategoryName()==null? "" :sr.getCategoryId().getRouteCategoryName());
			output.add(dto);
		}
		return output;
	}
}
