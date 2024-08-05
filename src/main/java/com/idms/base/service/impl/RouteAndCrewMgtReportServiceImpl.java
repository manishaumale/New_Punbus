package com.idms.base.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.idms.base.api.v1.model.dto.AbsentReportDto;
import com.idms.base.api.v1.model.dto.AuthorizedRouteReportDto;
import com.idms.base.api.v1.model.dto.AttendanceReportDto;
import com.idms.base.api.v1.model.dto.BusComingBackToDepotReportDto;
import com.idms.base.api.v1.model.dto.BusGoingAwayFromDepotReportDto;
import com.idms.base.api.v1.model.dto.BusNotSentOnRouteReportDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.BusWiseKmsReportDto;
import com.idms.base.api.v1.model.dto.DateWiseProductiveReportDto;
import com.idms.base.api.v1.model.dto.DetailBusDriverConductorDto;
import com.idms.base.api.v1.model.dto.DriverOrConWiseRestLeaveGrantedDto;
import com.idms.base.api.v1.model.dto.DriverOrCondPerformingOtherDutyDto;
import com.idms.base.api.v1.model.dto.DriverOrConductorLeaveReportDto;
import com.idms.base.api.v1.model.dto.DutyInspectorOverrideReportDto;
import com.idms.base.api.v1.model.dto.DutyRosterTotalKMsReportDto;
import com.idms.base.api.v1.model.dto.EmploymentTypeDto;
import com.idms.base.api.v1.model.dto.EnrouteBusReportDto;
import com.idms.base.api.v1.model.dto.ExpectedVersusAchievedKmsReportDto;
import com.idms.base.api.v1.model.dto.KMsLikelyToBeReceivedReportDto;
import com.idms.base.api.v1.model.dto.LossProfitMakingReportDto;
import com.idms.base.api.v1.model.dto.MaxKMsReportDto;
import com.idms.base.api.v1.model.dto.OffRouteReportDto;
import com.idms.base.api.v1.model.dto.RouteAnalysisReportDto;
import com.idms.base.api.v1.model.dto.RouteNoToBeOperatedReportDto;
import com.idms.base.api.v1.model.dto.RouteWiseBusAllocationReportDto;
import com.idms.base.api.v1.model.dto.RouteWiseSummaryReportDto;
import com.idms.base.api.v1.model.dto.RoutesInOperationReport;
import com.idms.base.api.v1.model.dto.RoutesLessThanEightReportDto;
import com.idms.base.api.v1.model.dto.ScheduledKmsMissedReportDto;
import com.idms.base.api.v1.model.dto.StaffPerformanceReportDto;
import com.idms.base.api.v1.model.dto.SuspensionReportDto;
import com.idms.base.api.v1.model.dto.TripsNotBeingOperatedReportDto;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.EmploymentTypeRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.service.RouteAndCrewMgtReportService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RouteAndCrewMgtReportServiceImpl implements RouteAndCrewMgtReportService {

	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;

	@Autowired
	DailyRoasterRepository dailyRoasterRepository;

	@Autowired
	RouteMasterRepository routeMasterRepository;
	@Autowired
	ConductorMasterRepository conductorMasterRepository;
	@Autowired
	DriverMasterRepository driverMasterRepository;
	
	@Autowired
	EmploymentTypeRepository employmentTypeRepository;

	@Override
	public List<BusTypeDto> fetchBusTypeListOnLoad() {
		List<BusTypeDto> busTypeList = busTyperMasterRepository.findAllByStatus(true).stream()
				.map(busTypeDto -> new BusTypeDto(busTypeDto.getId(), busTypeDto.getBusTypeName()))
				.collect(Collectors.toList());

		return busTypeList;
	}

	@Override
	public List<OffRouteReportDto> offRouteReport(String driverId, String fromDate, String toDate) {
		List<Object[]> offRouteReportList = null;
		List<OffRouteReportDto> reportList = new ArrayList<>();
		OffRouteReportDto reportObj = null;
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (driverId.equalsIgnoreCase("Driver")) {
			offRouteReportList = driverMasterRepository.driverOrConductorNotSentOnRouteReport(startDate, endDate);
			for (Object[] offRouteObj : offRouteReportList) {
				reportObj = new OffRouteReportDto();
				if (offRouteObj[0] != null)
					reportObj.setDriverOrConductorNumber(offRouteObj[0].toString());
				if (offRouteObj[1] != null){
					if (offRouteObj[1].toString().contains("-"))
					reportObj.setName(offRouteObj[1].toString().substring(0,offRouteObj[1].toString().lastIndexOf(' ')!=-1?offRouteObj[1].toString().lastIndexOf(' '):offRouteObj[1].toString().length()));
					else
						reportObj.setName(offRouteObj[1].toString());
				
				}
				if (offRouteObj[2] != null)
					reportObj.setDriverOrConductorCode(offRouteObj[2].toString());
				if (offRouteObj[3] != null)
					reportObj.setRouteDate(offRouteObj[3].toString());
				reportList.add(reportObj);
			}
		} else {
			offRouteReportList = conductorMasterRepository.driverOrConductorNotSentOnRouteReport(startDate, endDate);
			for (Object[] offRouteObj : offRouteReportList) {
				reportObj = new OffRouteReportDto();
				if (offRouteObj[0] != null)
					reportObj.setDriverOrConductorNumber(offRouteObj[0].toString());
				if (offRouteObj[1] != null){
					if (offRouteObj[1].toString().contains("-"))
					reportObj.setName(offRouteObj[1].toString().substring(0,offRouteObj[1].toString().lastIndexOf(' ')!=-1?offRouteObj[1].toString().lastIndexOf(' '):offRouteObj[1].toString().length()));
					else
						reportObj.setName(offRouteObj[1].toString());
				
				}
				if (offRouteObj[2] != null)
					reportObj.setDriverOrConductorCode(offRouteObj[2].toString());
				if (offRouteObj[3] != null)
					reportObj.setRouteDate(offRouteObj[3].toString());
				reportList.add(reportObj);
			}
		}

		return reportList;
	}

	@Override
	public List<BusNotSentOnRouteReportDto> busNotSentOnRoute(String fromDate, String toDate) {
		List<Object[]> offRouteReportList = null;
		List<BusNotSentOnRouteReportDto> reportList = new ArrayList<>();
		BusNotSentOnRouteReportDto reportObj = null;
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		offRouteReportList = dailyRoasterRepository.busNotSentOnRouteReport(startDate, endDate);
		for (Object[] offRouteObj : offRouteReportList) {
			reportObj = new BusNotSentOnRouteReportDto();
			if (offRouteObj[0] != null)
				reportObj.setTransportName(offRouteObj[0].toString());
			if (offRouteObj[1] != null)
				reportObj.setBusType(offRouteObj[1].toString());
			if (offRouteObj[2] != null)
				reportObj.setBusNumber(offRouteObj[2].toString());
			if (offRouteObj[3] != null)
				reportObj.setDateOfLastRoute(offRouteObj[3].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<BusGoingAwayFromDepotReportDto> busGoingAwayFromDepot(String fromDate, String toDate) {
		List<Object[]> busGoingAwayList = null;
		List<BusGoingAwayFromDepotReportDto> reportList = new ArrayList<>();
		BusGoingAwayFromDepotReportDto reportObj = null;
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		busGoingAwayList = dailyRoasterRepository.busGoingAwayFromDepotReport(startDate, endDate);
		for (Object[] goAwayObj : busGoingAwayList) {
			reportObj = new BusGoingAwayFromDepotReportDto();
			if (goAwayObj[0] != null)
				reportObj.setTime(goAwayObj[0].toString());
			if (goAwayObj[1] != null)
				reportObj.setBusType(goAwayObj[1].toString());
			if (goAwayObj[2] != null)
				reportObj.setBusNumber(goAwayObj[2].toString());
			if (goAwayObj[3] != null)
				reportObj.setLinkVTS(goAwayObj[3].toString());
			if (goAwayObj[4] != null)
				reportObj.setDriverCode(goAwayObj[4].toString());
			if (goAwayObj[5] != null){
				if (goAwayObj[5].toString().contains("-"))
				reportObj.setDriverName(goAwayObj[5].toString().substring(0,goAwayObj[5].toString().lastIndexOf(' ')!=-1?goAwayObj[5].toString().lastIndexOf(' '):goAwayObj[5].toString().length()));
				else
				reportObj.setDriverName(goAwayObj[5].toString());
			}
			if (goAwayObj[6] != null)
				reportObj.setConductorCode(goAwayObj[6].toString());
			if (goAwayObj[7] != null){
				if (goAwayObj[7].toString().contains("-"))
				reportObj.setConductorName(goAwayObj[7].toString().substring(0,goAwayObj[7].toString().lastIndexOf(' ')!=-1?goAwayObj[7].toString().lastIndexOf(' '):goAwayObj[7].toString().length()));
				else
				reportObj.setConductorName(goAwayObj[7].toString());
			}
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<BusComingBackToDepotReportDto> busComingBackToDepot(String fromDate, String toDate) {
		List<Object[]> busComingBackList = null;
		List<BusComingBackToDepotReportDto> reportList = new ArrayList<>();
		BusComingBackToDepotReportDto reportObj = null;
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		busComingBackList = dailyRoasterRepository.busComingBackToDepotReport(startDate, endDate);
		for (Object[] goAwayObj : busComingBackList) {
			reportObj = new BusComingBackToDepotReportDto();
			if (goAwayObj[0] != null)
				reportObj.setTime(goAwayObj[0].toString());
			if (goAwayObj[1] != null)
				reportObj.setBusType(goAwayObj[1].toString());
			if (goAwayObj[2] != null)
				reportObj.setBusNumber(goAwayObj[2].toString());
			if (goAwayObj[3] != null)
				reportObj.setLinkVTS(goAwayObj[3].toString());
			if (goAwayObj[4] != null)
				reportObj.setDriverCode(goAwayObj[4].toString());
			if (goAwayObj[5] != null){
				if (goAwayObj[7].toString().contains("-"))
				reportObj.setDriverName(goAwayObj[5].toString().substring(0,goAwayObj[5].toString().lastIndexOf(' ')!=-1?goAwayObj[5].toString().lastIndexOf(' '):goAwayObj[5].toString().length()));
				else
				reportObj.setDriverName(goAwayObj[5].toString());
			}
			if (goAwayObj[6] != null)
				reportObj.setConductorCode(goAwayObj[6].toString());
			if (goAwayObj[7] != null){
				if (goAwayObj[7].toString().contains("-"))
				reportObj.setConductorName(goAwayObj[7].toString().substring(0,goAwayObj[7].toString().lastIndexOf(' ')!=-1?goAwayObj[7].toString().lastIndexOf(' '):goAwayObj[7].toString().length()));
				else
				reportObj.setConductorName(goAwayObj[7].toString());
			}
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<RoutesInOperationReport> routesInOperationReport(Integer depotId, String fromDate, String toDate) {
		List<Object[]> routesInOperationList = null;
		List<RoutesInOperationReport> reportList = new ArrayList<>();
		RoutesInOperationReport reportObj = null;
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		routesInOperationList = routeMasterRepository.routesInOperationReport(depotId, startDate, endDate);
		for (Object[] routeOperationObj : routesInOperationList) {
			reportObj = new RoutesInOperationReport();
			if (routeOperationObj[0] != null)
				reportObj.setRouteCode(routeOperationObj[0].toString());
			if (routeOperationObj[1] != null)
				reportObj.setRouteName(routeOperationObj[1].toString());
			if (routeOperationObj[2] != null)
				reportObj.setKms(routeOperationObj[2].toString());
//			if (routeOperationObj[3] != null)
//				reportObj.setStateWiseKms(routeOperationObj[3].toString());
			if (routeOperationObj[3] != null)
				reportObj.setOvertime(routeOperationObj[3].toString());
			if (routeOperationObj[4] != null)
				reportObj.setNights(routeOperationObj[4].toString());
			if (routeOperationObj[5] != null)
				reportObj.setHillKilometers(routeOperationObj[5].toString());
			if (routeOperationObj[6] != null)
				reportObj.setPlainlKilometers(routeOperationObj[6].toString());

			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<ExpectedVersusAchievedKmsReportDto> expectedVersusAchieved(String fromDate , String toDate) {
		ExpectedVersusAchievedKmsReportDto obj =null;
		List<ExpectedVersusAchievedKmsReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Object[]> expectedVersusAchieved = routeMasterRepository.expectedVersusAchieved(startDate, endDate);
		for (Object[] o : expectedVersusAchieved) {
			 obj = new ExpectedVersusAchievedKmsReportDto();
			if (o[0] != null)
				obj.setDepotName(o[0].toString());
			if (o[1] != null)
				obj.setExpectedKilometers(o[1].toString());
			if (o[2] != null)
				obj.setAchievedKilometers(o[2].toString());
			if (o[4] != null)
				obj.setDifference(o[4].toString());
			if (o[3] != null)
				obj.setVtsKMs(o[3].toString());
			list.add(obj);
		}
		/*
		 * obj.setAchievedKilometers("100000");
		 * obj.setExpectedKilometers("200000"); obj.setDifference("100000");
		 */

		return list;
	}

	@Override
	public List<BusWiseKmsReportDto> fetchBusWiseKmsReport(Integer busId, Integer busType, String fromDate,
			String toDate) {
		List<Object[]> busWiseReportList = null;
		List<BusWiseKmsReportDto> reportList = new ArrayList<>();
		BusWiseKmsReportDto reportObj = null;
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		busWiseReportList = dailyRoasterRepository.busWiseKmsReport(busId, busType, startDate, endDate);
		for (Object[] busWiseObj : busWiseReportList) {
			reportObj = new BusWiseKmsReportDto();
			if (busWiseObj[0] != null)
				reportObj.setDate(busWiseObj[0].toString());
			if (busWiseObj[1] != null)
				reportObj.setAchievedKilometers(busWiseObj[1].toString());
			if (busWiseObj[2] != null)
				reportObj.setTotalKilometers(busWiseObj[2].toString());
			if (busWiseObj[3] != null)
				reportObj.setSpecialKilometers(busWiseObj[3].toString());
			if (busWiseObj[4] != null)
				reportObj.setMissedKilometers(busWiseObj[4].toString());
			if (busWiseObj[5] != null)
				reportObj.setVtsMissedKMs(busWiseObj[5].toString());
			if (busWiseObj[6] != null)
				reportObj.setBusNumber(busWiseObj[6].toString());
			if (busWiseObj[7] != null)
				reportObj.setBusType(busWiseObj[7].toString());
			if (busWiseObj[8] != null)
				reportObj.setTransportName(busWiseObj[8].toString());
			if (busWiseObj[9] != null)
				reportObj.setVtsKMs(busWiseObj[9].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<ScheduledKmsMissedReportDto> busWiseKmsMissedReport(Integer busTypeId, String fromDate, String toDate) {

		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<ScheduledKmsMissedReportDto> list = new ArrayList<>();

		List<Object[]> busWiseKmsMissedReport = routeMasterRepository.busWiseKmsMissedReport(busTypeId, startDate,
				endDate);
		for (Object[] Ob : busWiseKmsMissedReport) {
			ScheduledKmsMissedReportDto obj = new ScheduledKmsMissedReportDto();

			if (Ob[0] != null)
				obj.setDate(Ob[0].toString());
			if (Ob[1] != null)
				obj.setDepotId(Ob[1].toString());
			if (Ob[2] != null)
				obj.setRoute(Ob[2].toString());
			if (Ob[3] != null)
				obj.setTime(Ob[3].toString());
			if (Ob[4] != null)
				obj.setScheduledKilometers(Ob[4].toString());
			if (Ob[5] != null)
				obj.setActualKMs(Ob[5].toString());
			if (Ob[6] != null)
				obj.setDateOfLastOperation(Ob[6].toString());
			if (Ob[7] != null)
				obj.setBusNo(Ob[7].toString());
			list.add(obj);
		}

		/*
		 * obj.setDate("23/11/2021"); obj.setDateOfLastOperation("23/11/2020");
		 * obj.setDepotName("DP-JAL-1"); obj.setRoute("Delhi-Chandigarh");
		 * obj.setScheduledKilometers("10000"); obj.setTime("10:00");
		 * obj.setTrip("Delhi-Chandigarh"); list.add(obj);
		 */
		return list;
	}

	@Override
	public List<DriverOrConductorLeaveReportDto> driverOrConductorLeaveReport(String driverId, Integer driverType,
			String fromDate, String toDate) {
		DriverOrConductorLeaveReportDto obj = null;
		List<DriverOrConductorLeaveReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

		try {
			if (fromDate != null)
				startDate = sdf.parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = sdf.parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		if (driverId.equalsIgnoreCase("driver")) {
			List<Object[]> getdisciplinaryActionReportDriverNames = driverMasterRepository
					.getDriverOrConductorLeaveReport(driverType, startDate, endDate);

			for (Object[] ob1 : getdisciplinaryActionReportDriverNames) {
				obj = new DriverOrConductorLeaveReportDto();
				if (ob1[0] != null){
					if (ob1[0].toString().contains("-"))
						obj.setName(ob1[1].toString().substring(0, ob1[1].toString().lastIndexOf(' ')!=-1?ob1[1].toString().lastIndexOf(' '):ob1[1].toString().length()));
					else
					obj.setName(ob1[0].toString());
				}
				if (ob1[1] != null)
					obj.setDriverOrConductotNo(ob1[1].toString());
				if (ob1[2] != null)
					obj.setTotalPresents(ob1[2].toString());
				if (ob1[3] != null)
					obj.setTotalAbsents(ob1[3].toString());
				if (ob1[4] != null)
					obj.setTotalOnLeave(ob1[4].toString());
				if (ob1[5] != null)
					obj.setRest(ob1[5].toString());
				if (ob1[6] != null)
					obj.setCl(ob1[6].toString());
				list.add(obj);

			}
		} else {
			List<Object[]> getdisciplinaryActionReportConductorNames = conductorMasterRepository
					.getDriverOrConductorLeaveReport(driverType, startDate, endDate);
			for (Object[] ob1 : getdisciplinaryActionReportConductorNames) {
				obj = new DriverOrConductorLeaveReportDto();
				if (ob1[0] != null){
					if (ob1[0].toString().contains("-"))
						obj.setName(ob1[1].toString().substring(0, ob1[1].toString().lastIndexOf(' ')!=-1?ob1[1].toString().lastIndexOf(' '):ob1[1].toString().length()));
					else
					obj.setName(ob1[0].toString());
				}
				if (ob1[1] != null)
					obj.setDriverOrConductotNo(ob1[1].toString());
				if (ob1[2] != null)
					obj.setTotalPresents(ob1[2].toString());
				if (ob1[3] != null)
					obj.setTotalAbsents(ob1[3].toString());
				if (ob1[4] != null)
					obj.setTotalOnLeave(ob1[4].toString());
				if (ob1[5] != null)
					obj.setRest(ob1[5].toString());
				if (ob1[6] != null)
					obj.setCl(ob1[6].toString());
				list.add(obj);
			}
		}
		return list;
	}

	@Override
	public List<DriverOrConWiseRestLeaveGrantedDto> driverOrConductorWiseRestLeaveGranted(Integer driverId,
			String fromDate, String toDate) {
		/*
		 * Integer id=findbyconductorID(driverOrConductorID).getID(); Integer
		 * cID=findbydriverID(driverOrConductorID).getID(); if(id!=null){
		 * 
		 * }
		 */

		DriverOrConWiseRestLeaveGrantedDto obj = new DriverOrConWiseRestLeaveGrantedDto();
		List<DriverOrConWiseRestLeaveGrantedDto> list = new ArrayList<>();
		return list;
	}

	@Override
	public List<DriverOrCondPerformingOtherDutyDto> driverOrConductorPerformingOtherDuty(String driverOrconductor,
			String fromDate, String toDate) {
		DriverOrCondPerformingOtherDutyDto obj = new DriverOrCondPerformingOtherDutyDto();
		List<DriverOrCondPerformingOtherDutyDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(driverOrconductor.equalsIgnoreCase("driver"))
		{
		List<Object[]> getdisciplinaryActionReportDriverNames = driverMasterRepository.driverPerformingOtherDuty(startDate, endDate);
		
		for(Object[] ob1:getdisciplinaryActionReportDriverNames)
		{
			
			 obj = new DriverOrCondPerformingOtherDutyDto();	
				if (ob1[0] != null)
					obj.setDriverOrConductorCode(ob1[0].toString());
				if (ob1[1] != null){
					if (ob1[1].toString().contains("-"))
					obj.setName(ob1[1].toString().substring(0, ob1[1].toString().lastIndexOf(' ')!=-1?ob1[1].toString().lastIndexOf(' '):ob1[1].toString().length()));
					else
						obj.setName(ob1[1].toString());
				}
				if (ob1[2] != null)
					obj.setDriverOrConductotNo(ob1[2].toString());
				if (ob1[3] != null)
					obj.setOrderNumber(ob1[3].toString());
				if (ob1[4] != null)
					obj.setFromDate(ob1[4].toString());
				if (ob1[5] != null)
					obj.setToDate(ob1[5].toString());
				if (ob1[6] != null)
					obj.setDutyType(ob1[6].toString());
				if (ob1[7] != null)
					obj.setRemarks(ob1[7].toString());
			list.add(obj);
			
		}
		}
		else
		{
		List<Object[]> getdisciplinaryActionReportConductorNames = conductorMasterRepository.conductorPerformingOtherDuty(startDate, endDate);
		for(Object[] ob1:getdisciplinaryActionReportConductorNames)
		{
			 obj = new DriverOrCondPerformingOtherDutyDto();	
			 if (ob1[0] != null)
					obj.setDriverOrConductorCode(ob1[0].toString());
			 if (ob1[1] != null){
					if (ob1[1].toString().contains("-"))
					obj.setName(ob1[1].toString().substring(0, ob1[1].toString().lastIndexOf(' ')!=-1?ob1[1].toString().lastIndexOf(' '):ob1[1].toString().length()));
					else
						obj.setName(ob1[1].toString());
				}
			 if (ob1[2] != null)
					obj.setDriverOrConductotNo(ob1[2].toString());
				if (ob1[3] != null)
					obj.setOrderNumber(ob1[3].toString());
				if (ob1[4] != null)
					obj.setFromDate(ob1[4].toString());
				if (ob1[5] != null)
					obj.setToDate(ob1[5].toString());
				if (ob1[6] != null)
					obj.setDutyType(ob1[6].toString());
				if (ob1[7] != null)
					obj.setRemarks(ob1[7].toString());
				list.add(obj);	
				}
		}
		
		return list;
	}

	@Override
	public List<DriverOrCondPerformingOtherDutyDto> disciplinaryActionReport(String driverOrconductor, String fromDate,
			String toDate) {

		// DriverOrCondPerformingOtherDutyDto obj = new
		// DriverOrCondPerformingOtherDutyDto();
		List<DriverOrCondPerformingOtherDutyDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (driverOrconductor.equalsIgnoreCase("driver")) {
			List<Object[]> getdisciplinaryActionReportDriverNames = driverMasterRepository
					.getdisciplinaryActionReportDriverNames(startDate, endDate);

			for (Object[] ob1 : getdisciplinaryActionReportDriverNames) {

				DriverOrCondPerformingOtherDutyDto obj = new DriverOrCondPerformingOtherDutyDto();
				if (ob1[0] != null)
					obj.setName(ob1[0].toString());
				if (ob1[1] != null)
					obj.setDriverOrConductotNo(ob1[1].toString());
				if (ob1[2] != null)
					obj.setFromDate(ob1[2].toString());
				if (ob1[3] != null)
					obj.setToDate(ob1[3].toString());
				if (ob1[4] != null)
					obj.setRemarks(ob1[4].toString());
				if (ob1[5] != null)
					obj.setOrderDate(ob1[5].toString());
				if (ob1[6] != null)
					obj.setOrderNumber(ob1[6].toString());
				
				list.add(obj);

			}
		} else {
			List<Object[]> getdisciplinaryActionReportConductorNames = conductorMasterRepository
					.getdisciplinaryActionReportConductorNames(startDate, endDate);
			for (Object[] ob : getdisciplinaryActionReportConductorNames) {
				DriverOrCondPerformingOtherDutyDto obj = new DriverOrCondPerformingOtherDutyDto();
				if (ob[0] != null)
					obj.setName(ob[0].toString());
				if (ob[1] != null)
					obj.setDriverOrConductotNo(ob[1].toString());
				if (ob[2] != null)
					obj.setFromDate(ob[2].toString());
				if (ob[3] != null)
					obj.setToDate(ob[3].toString());
				if (ob[4] != null)
					obj.setRemarks(ob[4].toString());
				if (ob[5] != null)
					obj.setOrderDate(ob[5].toString());
				if (ob[6] != null)
					obj.setOrderNumber(ob[6].toString());
				list.add(obj);
			}
		}

		return list;
	}

	@Override
	public List<DutyInspectorOverrideReportDto> dutyInspectorOverrideReport(String driverOrconductorOrbustype,
			String fromDate, String toDate) {
		DutyInspectorOverrideReportDto obj = null;
		List<DutyInspectorOverrideReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;
		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (driverOrconductorOrbustype.equalsIgnoreCase("driver")) {
			List<Object[]> dutyInspectorOverrideReport = driverMasterRepository.dutyInspectorOverrideReport(startDate,
					endDate);
			{

			

				for (Object[] ob : dutyInspectorOverrideReport) {
					obj = new DutyInspectorOverrideReportDto();
					if (ob[0].toString() != null)
						obj.setName(ob[0].toString());
					if (ob[1].toString() != null)
						obj.setDriverOrConductotNo(ob[1].toString());
//					if (ob[2].toString() != null)
//						obj.setRouteName(ob[2].toString());
					if (ob[2].toString() != null)
						obj.setOverrideReason(ob[2].toString());
					if (ob[3].toString() != null)
						obj.setNewRoute(ob[3].toString());
					if (ob[4].toString() != null)
						obj.setRemarks(ob[4].toString());
					list.add(obj);
				}


			}
		} else if (driverOrconductorOrbustype.equalsIgnoreCase("conductor")) {

			List<Object[]> dutyInspectorOverrideReport = conductorMasterRepository
					.dutyInspectorOverrideReport(startDate, endDate);
			{

				

				for (Object[] ob : dutyInspectorOverrideReport) {
					obj = new DutyInspectorOverrideReportDto();
					if (ob[0].toString() != null)
						obj.setName(ob[0].toString());
					if (ob[1].toString() != null)
						obj.setDriverOrConductotNo(ob[1].toString());
//					if (ob[2].toString() != null)
//						obj.setRouteName(ob[2].toString());
					if (ob[2].toString() != null)
						obj.setOverrideReason(ob[2].toString());
					if (ob[3].toString() != null)
						obj.setNewRoute(ob[3].toString());
					if (ob[4].toString() != null)
						obj.setRemarks(ob[4].toString());
					list.add(obj);
				}

			}
		} else {

			List<Object[]> dutyInspectorOverrideReport = busTyperMasterRepository.dutyInspectorOverrideReport(startDate,
					endDate);
			{
			for (Object[] ob : dutyInspectorOverrideReport) {
					obj = new DutyInspectorOverrideReportDto();
					if (ob[0].toString() != null)
						obj.setName(ob[0].toString());
					if (ob[1].toString() != null)
						obj.setDriverOrConductotNo(ob[1].toString());
//					if (ob[2].toString() != null)
//						obj.setRouteName(ob[2].toString());
					if (ob[2].toString() != null)
						obj.setOverrideReason(ob[2].toString());
					if (ob[3].toString() != null)
						obj.setNewRoute(ob[3].toString());
					if (ob[4].toString() != null)
						obj.setRemarks(ob[4].toString());
					list.add(obj);
				}

			}
		}

		return list;
	}

	@Override
	public List<SuspensionReportDto> suspensionReport(String driveOrConductor, String fromDate, String toDate) {
		SuspensionReportDto obj = null;
		List<SuspensionReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;
		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		try{
			if(toDate != null)
				endDate= new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
			}catch(Exception e){
				e.printStackTrace();
			}
		
		if(driveOrConductor.equalsIgnoreCase("driver")){		
			List<Object[]> objects=driverMasterRepository.getSuspensionReport(startDate,endDate);		
			for(Object[] ob : objects)
			{
				obj = new SuspensionReportDto();
				if(ob[0]!=null)
				obj.setDriverOrConductotNo(ob[0].toString());
				if(ob[1]!=null)
				obj.setName(ob[1].toString());
				if(ob[2]!=null)
				obj.setSuspendedSince(ob[2].toString());
				list.add(obj);
			}
		} else {
			List<Object[]> objects = conductorMasterRepository.getSuspensionReport(startDate, endDate);
			for (Object[] ob : objects) {
				obj = new SuspensionReportDto();
				if(ob[0]!=null)
				obj.setDriverOrConductotNo(ob[0].toString());
				if(ob[1]!=null)
				obj.setName(ob[1].toString());
				if(ob[2]!=null)
				obj.setSuspendedSince(ob[2].toString());
				list.add(obj);
			}
		}
		return list;
	}

	@Override
	public List<AbsentReportDto> absentReport(String driverOrconductor, String fromDate, String toDate) {
		AbsentReportDto obj = null;
		List<AbsentReportDto> list = new ArrayList<>();

		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (driverOrconductor.equalsIgnoreCase("driver")) {
			List<Object[]> getabsentReportDriverNames = driverMasterRepository.getabsentReportDriverNames(startDate,
					endDate);
			{

				for (Object[] ob : getabsentReportDriverNames) {
					obj = new AbsentReportDto();
					if (ob[0] != null)
						obj.setDriverOrConductotNo(ob[0].toString());
					if (ob[1] != null){
						if(ob[1].toString().contains("-"))
						   obj.setName(ob[1].toString().substring(0, ob[1].toString().lastIndexOf(' ')!=-1?ob[1].toString().lastIndexOf(' '):ob[1].toString().length()));
						else
							obj.setName(ob[1].toString());
					}
					if (ob[2] != null)
						obj.setAbsentFrom(ob[2].toString());
					if (ob[3] != null)
						obj.setAbsentTo(ob[3].toString());
					if (ob[4] != null)
						obj.setAbsentDays(ob[4].toString());
					list.add(obj);
				}

			}
		} else {

			List<Object[]> getabsentReportConductorNames = conductorMasterRepository.getabsentReportConductorNames(startDate, endDate);
			{

				for (Object[] ob1 : getabsentReportConductorNames) {
					obj = new AbsentReportDto();
					if (ob1[0] != null)
						obj.setDriverOrConductotNo(ob1[0].toString());
					if (ob1[1]!= null){
						if(ob1[1].toString().contains("-"))
							   obj.setName(ob1[1].toString().substring(0, ob1[1].toString().lastIndexOf(' ')!=-1?ob1[1].toString().lastIndexOf(' '):ob1[1].toString().length()));
							else
								obj.setName(ob1[1].toString());
					} 
					if (ob1[2] != null)
						obj.setAbsentFrom(ob1[2].toString());
					if (ob1[3] != null)
						obj.setAbsentTo(ob1[3].toString());
					if (ob1[4] != null)
						obj.setAbsentDays(ob1[4].toString());
					list.add(obj);
				}

			}
		}

		return list;
	}

	@Override
	public List<AbsentReportDto> restDueReport(String driverId, String fromDate, String toDate) {
		AbsentReportDto obj =null;
		List<AbsentReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(driverId.equalsIgnoreCase("Driver")){
			List<Object[]> objects = driverMasterRepository.restDueReport(startDate,endDate);
			for (Object[] ob : objects) {
				obj = new AbsentReportDto();
				if (ob[0] != null)
					obj.setDepotName(ob[0].toString());
				if (ob[1] != null)
					obj.setDriverOrConductotCode(ob[1].toString());
				if (ob[2] != null){
					if (ob[2].toString().contains("-"))
						obj.setName(ob[2].toString().substring(0, ob[2].toString().lastIndexOf(' ')!=-1?ob[2].toString().lastIndexOf(' '):ob[2].toString().length()));
					else
					obj.setName(ob[2].toString());
				}
				if (ob[3] != null)
					obj.setDriverOrConductotNo(ob[3].toString());
				if (ob[4] != null)
					obj.setRestDues(ob[4].toString());
						
				list.add(obj);

			}
		}else{
		List<Object[]> objects = conductorMasterRepository.restDueReport(startDate,endDate);
		for (Object[] ob : objects) {
			obj = new AbsentReportDto();
			obj = new AbsentReportDto();
			if (ob[0] != null)
				obj.setDepotName(ob[0].toString());
			if (ob[1] != null)
				obj.setDriverOrConductotCode(ob[1].toString());
			if (ob[2] != null){
				if (ob[2].toString().contains("-"))
					obj.setName(ob[2].toString().substring(0, ob[2].toString().lastIndexOf(' ')!=-1?ob[2].toString().lastIndexOf(' '):ob[2].toString().length()));
				else
				obj.setName(ob[2].toString());
			}
			if (ob[3] != null)
				obj.setDriverOrConductotNo(ob[3].toString());
			if (ob[4] != null)
				obj.setRestDues(ob[4].toString());
			
			list.add(obj);

		}
	}
		return list;

	}

	@Override
	public List<StaffPerformanceReportDto> staffPerformanceReport(String driverId, String fromDate, String toDate) {
		StaffPerformanceReportDto obj =null;
		List<StaffPerformanceReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(driverId.equalsIgnoreCase("Driver")){
			List<Object[]> objects = driverMasterRepository.staffPerformanceReport(startDate,endDate);
			for (Object[] ob : objects) {
				obj = new StaffPerformanceReportDto();
				if (ob[0] != null)
					obj.setDepotName(ob[0].toString());
				if (ob[1] != null)
					obj.setDriverOrConductotNo(ob[1].toString());
				if (ob[2] != null)
					obj.setName(ob[2].toString());
				if (ob[3] != null)
					obj.setDate(ob[3].toString());
				if (ob[4] != null)
					obj.setDeputedRoute(ob[4].toString());
				if (ob[5] != null)
					obj.setRecieptMonth(ob[5].toString());
				if (ob[5] != null)
					obj.setReason(ob[5].toString());
				
				list.add(obj);

			}
		}else{
		List<Object[]> objects = conductorMasterRepository.staffPerformanceReport(startDate,endDate);
		for (Object[] ob : objects) {
			obj = new StaffPerformanceReportDto();
			if (ob[0] != null)
				obj.setDepotName(ob[0].toString());
			if (ob[1] != null)
				obj.setDriverOrConductotNo(ob[1].toString());
			if (ob[2] != null)
				obj.setName(ob[2].toString());
			if (ob[3] != null)
				obj.setDate(ob[3].toString());
			if (ob[4] != null)
				obj.setDeputedRoute(ob[4].toString());
			if (ob[5] != null)
				obj.setRecieptMonth(ob[5].toString());
			if (ob[5] != null)
				obj.setReason(ob[5].toString());
			
			list.add(obj);

		}
	}
		return list;
	}

	@Override
	public List<RoutesLessThanEightReportDto> routesLessThanEightReport(String fromDate, String toDate) {
		RoutesLessThanEightReportDto obj = null;
		List<RoutesLessThanEightReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Object[]> routesLessThanEightReport = routeMasterRepository.getRoutesLessThanEightReport(startDate,
				endDate);

		for (Object[] ob : routesLessThanEightReport) {
			obj = new RoutesLessThanEightReportDto();
			if (ob[0] != null)
				obj.setRouteNo(ob[0].toString());
			if (ob[1] != null)
				obj.setName(ob[1].toString());
			if (ob[2] != null)
				obj.setRouteCategory(ob[2].toString());
			if (ob[3] != null)
				obj.setScheduledLms(ob[3].toString());
			list.add(obj);

		}
		return list;
	}

	@Override
	public List<RoutesLessThanEightReportDto> nonOperationReport(Integer depotId, String fromDate, String toDate) {
		List<RoutesLessThanEightReportDto> list = new ArrayList<>();

		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Object[]> nonOperationReport = routeMasterRepository.nonOperationReport(depotId, startDate, endDate);

		for (Object[] ob : nonOperationReport) {
			RoutesLessThanEightReportDto obj = new RoutesLessThanEightReportDto();
			if (ob[0] != null)
				obj.setRouteCode(ob[0].toString());
			if (ob[1] != null)
				obj.setName(ob[1].toString());
			if (ob[2] != null)
				obj.setRouteCategory(ob[2].toString());
			if (ob[3] != null)
				obj.setScheduledLms(ob[3].toString());
			list.add(obj);

		}

		/*
		 * obj.setRouteNo("Route-123133"); obj.setName("Rahul");
		 * obj.setRouteCategory("Category"); obj.setScheduledLms("100000");
		 * list.add(obj);
		 */

		return list;
	}

	@Override
	public List<LossProfitMakingReportDto> lossMakingRouteReport(Integer depotId, String fromDate, String toDate) {
		LossProfitMakingReportDto obj = new LossProfitMakingReportDto();
		List<LossProfitMakingReportDto> list = new ArrayList<>();

		return list;
	}

	@Override
	public List<LossProfitMakingReportDto> profitMakingRouteReport(Integer depotId, String fromDate, String toDate) {
		LossProfitMakingReportDto obj = new LossProfitMakingReportDto();
		List<LossProfitMakingReportDto> list = new ArrayList<>();
		// obj.setRouteNo("Route-123133");
		// obj.setName("Rahul");
		// obj.setRouteCategory("Category");
		// obj.setScheduledLms("100000");
		// obj.setRouteReceipt("Receipt");
		// obj.setProfit("10000");
		// list.add(obj);
		return list;
	}

	/*
	 * private
	 * List<DriverOrCondPerformingOtherDutyDto>setDisripacnyeportDetails(Object
	 * [] getdisciplinaryActionReportConductorNames){ return null; }
	 */

	@Override
	public List<RouteAnalysisReportDto> routeAnalysisReport(String fromDate , String toDate) {

		List<RouteAnalysisReportDto> list = new ArrayList<>();
		
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Object[]> routeAnalysisReport = routeMasterRepository.routeAnalysisReport(startDate , endDate);

		for (Object[] o : routeAnalysisReport) {
			RouteAnalysisReportDto obj = new RouteAnalysisReportDto();
			if (o[0] != null)
				obj.setRouteCode(o[0].toString());
			if (o[1] != null)
				obj.setRouteName(o[1].toString());
			if (o[2] != null)
				obj.setRoutestartingTime(o[2].toString());
			if (o[3] != null)
				obj.setScheduledkms(o[3].toString());
			if (o[4] != null)
				obj.setProfitPerKms(o[4].toString());
			if (o[5] != null)
				obj.setLastOperated(o[5].toString());
			if (o[6] != null)
				obj.setNoOfTrips(o[6].toString());
			list.add(obj);
		}
		return list;

		/*
		 * obj.setRouteNo("Route-123133"); obj.setName("Rahul");
		 * obj.setStartingTime("12:00"); obj.setKms("10000");
		 * obj.setProfitPerKms("10"); obj.setTripName("CHD-DELHI");
		 * obj.setFromKms("20000"); obj.setToKms("30000");
		 * obj.setLastOperated("23022"); obj.setNoOfTrips("5");
		 */

	}

	
	@Override
	public List<DriverOrCondPerformingOtherDutyDto> totalDriversConductorsOnotherdutyreport(String driverOrconductor,
			 Integer driverType,String fromDate, String toDate) {
		DriverOrCondPerformingOtherDutyDto obj = new DriverOrCondPerformingOtherDutyDto();
		List<DriverOrCondPerformingOtherDutyDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;


		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(driverOrconductor.equalsIgnoreCase("driver"))
		{
		List<Object[]> getdisciplinaryActionReportDriverNames = driverMasterRepository.totalDriversOnotherdutyreport(driverType,startDate, endDate);
		
		for(Object[] ob1:getdisciplinaryActionReportDriverNames)
		{
			
				obj = new DriverOrCondPerformingOtherDutyDto();
				if (ob1[0] != null)
					obj.setDriverOrConductorCode(ob1[0].toString());
				if (ob1[1] != null){
					if (ob1[1].toString().contains("-"))
						obj.setName(ob1[1].toString().substring(0, ob1[1].toString().lastIndexOf(' ')!=-1?ob1[1].toString().lastIndexOf(' '):ob1[1].toString().length()));
					else
					obj.setName(ob1[1].toString());
				}
				if (ob1[2] != null)
					obj.setDriverOrConductotNo(ob1[2].toString());
				if (ob1[3] != null)
					obj.setFromDate(ob1[3].toString());
				if (ob1[4] != null)
					obj.setToDate(ob1[4].toString());
				if (ob1[5] != null)
					obj.setRemarks(ob1[5].toString());
			
			list.add(obj);
			
		}
		}
		else
		{
		List<Object[]> getdisciplinaryActionReportConductorNames = conductorMasterRepository.totalConductorsOnotherdutyreport(driverType,startDate, endDate);
		for(Object[] ob1:getdisciplinaryActionReportConductorNames)
		{
			 obj = new DriverOrCondPerformingOtherDutyDto();
			 if(ob1[0]!=null)
					obj.setDriverOrConductorCode(ob1[0].toString());
			 if (ob1[1] != null){
					if (ob1[1].toString().contains("-"))
						obj.setName(ob1[1].toString().substring(0, ob1[1].toString().lastIndexOf(' ')!=-1?ob1[1].toString().lastIndexOf(' '):ob1[1].toString().length()));
					else
					obj.setName(ob1[1].toString());
				}
				if(ob1[2]!=null)
				obj.setDriverOrConductotNo(ob1[2].toString());
				if(ob1[3]!=null)
				obj.setFromDate(ob1[3].toString());
				if(ob1[4]!=null)
				obj.setToDate(ob1[4].toString());
				if(ob1[5]!=null)
					obj.setRemarks(ob1[5].toString());
				list.add(obj);	
				}
		}
		
		return list;
	}

	@Override
	public List<RouteWiseSummaryReportDto> routeWiseSummaryReport(String fromDate, String toDate, Integer routeId) {
		// TODO Auto-generated method stub
		RouteWiseSummaryReportDto obj = new RouteWiseSummaryReportDto();
		List<RouteWiseSummaryReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Object[]> objects = routeMasterRepository.routeWiseSummaryReportList(routeId,startDate, endDate);
		
		for(Object[] ob1:objects)
		{
			
			obj = new RouteWiseSummaryReportDto();	
			if(ob1[0]!=null)
			obj.setDate(ob1[0].toString());
			if(ob1[1]!=null)
			obj.setRouteName(ob1[1].toString());
			if(ob1[2]!=null)
			obj.setTrips(ob1[2].toString());
			if(ob1[3]!=null)
			obj.setSchKms(ob1[3].toString());
			if(ob1[4]!=null)
				obj.setAllocatedKms(ob1[4].toString());
			if(ob1[5]!=null)
				obj.setGpsKms(ob1[5].toString());
			list.add(obj);
			
		}
		
		return list;
	 }

	@Override
	public List<DateWiseProductiveReportDto> dateWiseProductiveReport(String fromDate, String toDate, String type) {

		DateWiseProductiveReportDto obj = new DateWiseProductiveReportDto();
		List<DateWiseProductiveReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(type.equalsIgnoreCase("driver"))
		{
		List<Object[]> getdisciplinaryActionReportDriverNames = driverMasterRepository.dateWiseDriverProductiveReport(startDate, endDate);
		
		for(Object[] ob1:getdisciplinaryActionReportDriverNames)
		{
			
			 obj = new DateWiseProductiveReportDto();	
				if(ob1[0]!=null)
				obj.setDate(ob1[0].toString());
				if(ob1[1]!=null)
				obj.setDriverOrConductorCode(ob1[1].toString());
				if(ob1[2]!=null)
				obj.setSchKms(ob1[2].toString());	
				if (ob1[3] != null){
					if (ob1[0].toString().contains("-"))
						obj.setDriverOrConductorName(ob1[3].toString().substring(0, ob1[3].toString().lastIndexOf(' ')!=-1?ob1[3].toString().lastIndexOf(' '):ob1[3].toString().length()));
					else
					obj.setDriverOrConductorName(ob1[3].toString());
				}
				if(ob1[4]!=null)
					obj.setRouteName(ob1[4].toString());
				list.add(obj);	
			
		}
		}
		else
		{
		List<Object[]> getdisciplinaryActionReportConductorNames = conductorMasterRepository.dateWiseConductorProductiveReport(startDate, endDate);
		for(Object[] ob1:getdisciplinaryActionReportConductorNames)
		{
			 obj = new DateWiseProductiveReportDto();	
				if(ob1[0]!=null)
				obj.setDate(ob1[0].toString());
				if(ob1[1]!=null)
				obj.setDriverOrConductorCode(ob1[1].toString());
				if(ob1[2]!=null)
				obj.setSchKms(ob1[2].toString());
				if (ob1[3] != null){
					if (ob1[0].toString().contains("-"))
						obj.setDriverOrConductorName(ob1[3].toString().substring(0, ob1[3].toString().lastIndexOf(' ')!=-1?ob1[3].toString().lastIndexOf(' '):ob1[3].toString().length()));
					else
					obj.setDriverOrConductorName(ob1[3].toString());
				}
				if(ob1[4]!=null)
					obj.setRouteName(ob1[4].toString());
				if(ob1[5]!=null)
					obj.setEpkm(ob1[5].toString());
				list.add(obj);	
		}
		}
		
		return list;
	
	}

	@Override
	public List<EmploymentTypeDto> getEmployementTypeList() {
		// TODO Auto-generated method stub
		List<EmploymentTypeDto> emplooyeeTypeList = employmentTypeRepository.findAllByStatus(true).stream()
				.map(employmentTypeDto -> new EmploymentTypeDto(employmentTypeDto.getId(), employmentTypeDto.getEnrolmentName()))
				.collect(Collectors.toList());
		return emplooyeeTypeList;
	}

	@Override
	public List<MaxKMsReportDto> maxKMsReport(String fromDate, String toDate, Integer routeId) {
		
		MaxKMsReportDto maxKMsReportDto=null;
		List<MaxKMsReportDto> list = new ArrayList();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Object[]> list1 = routeMasterRepository.maxKMsReport(routeId,startDate, endDate);
	    for(Object[] ob1:list1)
		{
			    maxKMsReportDto = new MaxKMsReportDto();	
				if(ob1[0]!=null)
			    maxKMsReportDto.setRouteCode(ob1[0].toString());;
				if(ob1[1]!=null)
			    maxKMsReportDto.setRouteName(ob1[1].toString());
				if(ob1[2]!=null)
				    maxKMsReportDto.setDate(ob1[2].toString());
				if(ob1[3]!=null)
				 maxKMsReportDto.setSchKMs(ob1[3].toString());;
				if(ob1[4]!=null)
				  maxKMsReportDto.setTotalOverTime(ob1[4].toString());;
				if(ob1[5]!=null)
				  maxKMsReportDto.setOverNight(ob1[5].toString());
				list.add(maxKMsReportDto);	
		}
		return list;
	}

	@Override
	public List<RouteWiseBusAllocationReportDto> routeWiseBusAllocationReport(String fromDate, String toDate,
			Integer routeId) {
		RouteWiseBusAllocationReportDto obj = new RouteWiseBusAllocationReportDto();
		List<RouteWiseBusAllocationReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> routeWiseBusAllocationReportDto = routeMasterRepository.routeWiseBusAllocationReport(routeId,startDate, endDate);
		
		for(Object[] ob1:routeWiseBusAllocationReportDto)
		{
			
			 obj = new RouteWiseBusAllocationReportDto();	
				if(ob1[0]!=null)
					obj.setDate(ob1[0].toString());
				if(ob1[1]!=null)
					obj.setRouteName(ob1[1].toString());
				if(ob1[2]!=null)
					obj.setOrdinary(ob1[2].toString());
				if(ob1[3]!=null)
					obj.setHVAC(ob1[3].toString());
				if(ob1[4]!=null)
					obj.setVolvo(ob1[4].toString());
				if(ob1[5]!=null)
					obj.setMidiBus(ob1[5].toString());
				if(ob1[6]!=null)
					obj.setIntegral(ob1[6].toString());
				if(ob1[7]!=null)
					obj.setTotal(ob1[7].toString());
				list.add(obj);	
			
		}
		
		return list;
	}

	@Override
	public List<RouteNoToBeOperatedReportDto> routeNoToBeOperatedReport(String fromDate, String toDate) {
		RouteNoToBeOperatedReportDto obj = null;
		List<RouteNoToBeOperatedReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> routeNoToBeOperatedReportDto = routeMasterRepository.routeNoToBeOperatedReport(startDate, endDate);
		
		for(Object[] ob1:routeNoToBeOperatedReportDto)
		{
			 obj = new RouteNoToBeOperatedReportDto();
			 if(ob1[0]!=null)
				 obj.setRoutename(ob1[0].toString());
//			 if(ob1[1]!=null)
//				 obj.setFromDate(ob1[1].toString());
//			 if(ob1[2]!=null)
//				 obj.setToDate(ob1[2].toString());
			 if(ob1[1]!=null)
				 obj.setSchKMs(ob1[1].toString());
			 list.add(obj);
				 
		}
		
		return list;
	}
	
	@Override
	public List<AuthorizedRouteReportDto> authorizedRouteReport(String fromDate, String toDate, String type) {

		AuthorizedRouteReportDto obj = new AuthorizedRouteReportDto();
		List<AuthorizedRouteReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(type.equalsIgnoreCase("driver"))
		{
		List<Object[]> authorizedRouteReportDto = driverMasterRepository.authorizedRouteReport(startDate, endDate);
		
		for(Object[] ob1:authorizedRouteReportDto)
		{
			
			  obj = new AuthorizedRouteReportDto();
			  if(ob1[0]!=null)
					obj.setDriverOrConductorCode(ob1[0].toString());
					if(ob1[1]!=null)
					obj.setDriverOrConductorName(ob1[1].toString());
					if(ob1[3]!=null)
					obj.setRouteName(ob1[3].toString());
					if(ob1[4]!=null)
						obj.setFromDate(ob1[4].toString());
					if(ob1[5]!=null)
						obj.setUpToDate(ob1[5].toString());
					if(ob1[6]!=null)
						obj.setReason(ob1[6].toString());
				list.add(obj);	
			
		}
		}
		else
		{
		List<Object[]> authorizedRouteReportDto = conductorMasterRepository.authorizedRouteReport(startDate, endDate);
		for(Object[] ob1:authorizedRouteReportDto)
		{
			obj = new AuthorizedRouteReportDto();
				if(ob1[0]!=null)
				obj.setDriverOrConductorCode(ob1[0].toString());
				if(ob1[1]!=null)
				obj.setDriverOrConductorName(ob1[1].toString());
				if(ob1[3]!=null)
				obj.setRouteName(ob1[3].toString());
				if(ob1[4]!=null)
					obj.setFromDate(ob1[4].toString());
				if(ob1[5]!=null)
					obj.setUpToDate(ob1[5].toString());
				if(ob1[6]!=null)
					obj.setReason(ob1[6].toString());
				list.add(obj);	
		}
		}
		
		return list;
	

	}

	@Override
	public List<DutyRosterTotalKMsReportDto> dutyRosterTotalKMsReport(String fromDate, String toDate) {
		DutyRosterTotalKMsReportDto obj = new DutyRosterTotalKMsReportDto();
		List<DutyRosterTotalKMsReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> dutyRosterTotalKMsReportDto = routeMasterRepository.dutyRosterTotalKMsReport(startDate, endDate);
		for(Object[] ob1:dutyRosterTotalKMsReportDto)
		{
		obj = new DutyRosterTotalKMsReportDto();	
			if(ob1[0]!=null)
				obj.setRotaDayCode(ob1[0].toString());
			if(ob1[1]!=null)
				obj.setRotaDate(ob1[1].toString());
			if(ob1[2]!=null)
				obj.setDepoName(ob1[2].toString());
			if(ob1[3]!=null)
				obj.setRosterKms(ob1[3].toString());
			if(ob1[4]!=null)
				obj.setVtsKMs(ob1[4].toString());
			if(ob1[5]!=null)
				obj.setAllotedKms(ob1[5].toString());
			list.add(obj);	
		}
		return list;
	}

	@Override
	public List<EnrouteBusReportDto> enrouteBusReport(Integer busNo) {
		List<EnrouteBusReportDto> enroutebusdetails= new ArrayList<>();

		try {		
			List<Object[]> enroutedetails = routeMasterRepository.getEnrouteBusdetails(busNo);
			for(Object[] ob:enroutedetails)
			{
			EnrouteBusReportDto enb = new EnrouteBusReportDto();
			if(ob[0]!=null)
			enb.setTransportName(ob[0].toString());
			if(ob[1]!=null)
			enb.setBusNumber(ob[1].toString());
			if(ob[2]!=null)
			enb.setBusUpTime(ob[2].toString());			
			if(ob[3]!=null)
			enb.setBusDownTime(ob[3].toString());
			if(ob[4]!=null)
			enb.setBusType(ob[4].toString());
			if(ob[5]!=null)
			enb.setRouteName(ob[5].toString());
			if(ob[6]!=null)
			enb.setDriverCode(ob[6].toString());
			if(ob[7]!=null){
			if(ob[7].toString().contains("-"))
			   enb.setDriverName(ob[7].toString().substring(0,ob[7].toString().lastIndexOf(' ')!=-1?ob[7].toString().lastIndexOf(' '):ob[7].toString().length()));
			else
				enb.setDriverName(ob[7].toString());
			}
			if(ob[8]!=null)
				enb.setConductorCode(ob[8].toString());	
			if(ob[9]!=null){
			if(ob[9].toString().contains("-"))		
			   enb.setConductorName(ob[9].toString().substring(0,ob[9].toString().lastIndexOf(' ')!=-1?ob[9].toString().lastIndexOf(' '):ob[9].toString().length()));
			else
				enb.setConductorName(ob[9].toString());
			}
			if(ob[10]!=null)
			enb.setGpsLink(ob[10].toString());
			enroutebusdetails.add(enb);
			}
		}

			catch (Exception e) {
			e.printStackTrace();
		}

		return enroutebusdetails;
	}

	

	@Override
	public List<AttendanceReportDto> attendanceReportDetails(String type,  Integer month) {
		

		List<Object[]> offRouteReportList = null;
		List<AttendanceReportDto> reportList = new ArrayList<>();
		AttendanceReportDto reportObj = null;
		
		if (type.equalsIgnoreCase("Driver")) {
			offRouteReportList = driverMasterRepository.attendanceReportDetails(month);
			for (Object[] offRouteObj : offRouteReportList) {
				reportObj = new AttendanceReportDto();
				if (offRouteObj[0] != null)
					reportObj.setDriverCodeOrconductorCode(offRouteObj[0].toString());
				if (offRouteObj[1] != null){
					if(offRouteObj[1].toString().contains("-"))
					reportObj.setDriverNameorconductorName(offRouteObj[1].toString().substring(0,offRouteObj[1].toString().lastIndexOf(' ')!=-1?offRouteObj[1].toString().lastIndexOf(' '):offRouteObj[1].toString().length()));
					else
						reportObj.setDriverNameorconductorName(offRouteObj[1].toString());
				}
				if (offRouteObj[2] != null)
					reportObj.setFromDate(offRouteObj[2].toString());
				if (offRouteObj[3] != null)
					reportObj.setToDate(offRouteObj[3].toString());
				if (offRouteObj[4] != null)
					reportObj.setReason(offRouteObj[4].toString());
				reportList.add(reportObj);
			}
		} else {
			offRouteReportList = conductorMasterRepository.attendanceReportDetails(month);
			for (Object[] offRouteObj : offRouteReportList) {
				reportObj = new AttendanceReportDto();
				if (offRouteObj[0] != null)
					reportObj.setDriverCodeOrconductorCode(offRouteObj[0].toString());
				if (offRouteObj[1] != null){
					if(offRouteObj[1].toString().contains("-"))
					reportObj.setDriverNameorconductorName(offRouteObj[1].toString().substring(0,offRouteObj[1].toString().lastIndexOf(' ')!=-1?offRouteObj[1].toString().lastIndexOf(' '):offRouteObj[1].toString().length()));
					else
						reportObj.setDriverNameorconductorName(offRouteObj[1].toString());
				}
				if (offRouteObj[2] != null)
					reportObj.setFromDate(offRouteObj[2].toString());
				if (offRouteObj[3] != null)
					reportObj.setToDate(offRouteObj[3].toString());
				if (offRouteObj[4] != null)
					reportObj.setReason(offRouteObj[4].toString());
				reportList.add(reportObj);
			}
		}

		return reportList;
	
	}

	@Override
	public List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport(String fromDate, String toDate) {
		KMsLikelyToBeReceivedReportDto obj = null;
		List<KMsLikelyToBeReceivedReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> kMsLikelyToBeReceivedReportDto = routeMasterRepository.kmsLikelyToBeReceivedReport(startDate, endDate);
		for(Object[] ob1:kMsLikelyToBeReceivedReportDto)
		{
		obj = new KMsLikelyToBeReceivedReportDto();	
			if(ob1[0]!=null)
				obj.setRotaDayCode(ob1[0].toString());
			if(ob1[1]!=null)
				obj.setRotaDate(ob1[1].toString());
			if(ob1[2]!=null)
				obj.setAllotedKms(ob1[2].toString());
			if(ob1[3]!=null)
				obj.setToBeReceived(ob1[3].toString());
			list.add(obj);	
		}
		return list;
	}

	@Override
	public List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport1(String fromDate, String toDate) {
		KMsLikelyToBeReceivedReportDto obj = null;
		List<KMsLikelyToBeReceivedReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> kMsLikelyToBeReceivedReportDto = routeMasterRepository.kmsLikelyToBeReceivedReport1(startDate, endDate);
		for(Object[] ob1:kMsLikelyToBeReceivedReportDto)
		{
		obj = new KMsLikelyToBeReceivedReportDto();	
			if(ob1[0]!=null)
				obj.setRotaDayCode(ob1[0].toString());
			if(ob1[1]!=null)
				obj.setRotaDate(ob1[1].toString());
			if(ob1[2]!=null)
				obj.setAllotedKms(ob1[2].toString());
			if(ob1[3]!=null)
				obj.setToBeReceived(ob1[3].toString());
			list.add(obj);	
		}
		return list;
	}

	@Override
	public List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport2(String fromDate, String toDate) {
		KMsLikelyToBeReceivedReportDto obj = null;
		List<KMsLikelyToBeReceivedReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> kMsLikelyToBeReceivedReportDto = routeMasterRepository.kmsLikelyToBeReceivedReport2(startDate, endDate);
		for(Object[] ob1:kMsLikelyToBeReceivedReportDto)
		{
		obj = new KMsLikelyToBeReceivedReportDto();	
			if(ob1[0]!=null)
				obj.setRotaDayCode(ob1[0].toString());
			if(ob1[1]!=null)
				obj.setRotaDate(ob1[1].toString());
			if(ob1[2]!=null)
				obj.setAllotedKms(ob1[2].toString());
			if(ob1[3]!=null)
				obj.setToBeReceived(ob1[3].toString());
			list.add(obj);	
		}
		return list;
	}
	
	
	@Override
	public List<TripsNotBeingOperatedReportDto> tripsNotBeingOperatedReport(String fromDate, String toDate) {
		TripsNotBeingOperatedReportDto obj = null;
		List<TripsNotBeingOperatedReportDto> list = new ArrayList<>();
		Date startDate = null;
		Date endDate = null;

		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<Object[]> tripsNotBeingOperatedReportDto = routeMasterRepository.tripsNotBeingOperatedReport(startDate, endDate);
		
		for(Object[] ob1:tripsNotBeingOperatedReportDto)
		{
			 obj = new TripsNotBeingOperatedReportDto();
			 if(ob1[0]!=null)
				 obj.setRouteId(ob1[0].toString());
			 if(ob1[1]!=null)
				 obj.setRouteName(ob1[1].toString());
			 if(ob1[2]!=null)
				 obj.setTripId(ob1[2].toString());
			 if(ob1[3]!=null)
				 obj.setDepoName(ob1[3].toString());
			 if(ob1[4]!=null)
				 obj.setSchKMs(ob1[4].toString());
			 if(ob1[5]!=null)
				 obj.setLastOperatedDate(ob1[5].toString());
			 list.add(obj);
				 
		}
		
		return list;

	}

	@Override
	public List<DetailBusDriverConductorDto> detailBusDriverConductorReport(String fromDate, String toDate, String type) {

		DetailBusDriverConductorDto obj =null;
		List<DetailBusDriverConductorDto> list = new ArrayList<>();
		Date startDate= null;
		Date endDate=null;
		try {
			if (fromDate != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if (toDate != null)
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(type.equalsIgnoreCase("driver wise"))
		{
		List<Object[]> dutyInspectorOverrideReport = driverMasterRepository.detailBusDriverConductorReport(startDate, endDate);
		{
			
			for(Object[] ob :dutyInspectorOverrideReport)
			{
				obj= new DetailBusDriverConductorDto();
				if(ob[0]!=null)
				obj.setDriverOrConductorCode(ob[0].toString());
				if(ob[1]!=null){
					if(ob[1].toString().contains("-"))
				   obj.setDriverOrConductorName(ob[1].toString().substring(0,ob[1].toString().lastIndexOf(' ')!=-1?ob[1].toString().lastIndexOf(' '):ob[1].toString().length()));
					else
						obj.setDriverOrConductorName(ob[1].toString());
				}
				if(ob[2]!=null)
				obj.setDate(ob[2].toString());
				if(ob[3]!=null)
				obj.setRouteName(ob[3].toString());
				if(ob[4]!=null)
				obj.setKmpl(ob[4].toString());
				list.add(obj);
			}
			
		}
		}
		else if(type.equalsIgnoreCase("conductor wise"))
		{
		
		List<Object[]> dutyInspectorOverrideReport = conductorMasterRepository.detailBusDriverConductorReport(startDate, endDate);
		{
			
			for(Object[] ob :dutyInspectorOverrideReport)
			{
				obj= new DetailBusDriverConductorDto();
				if(ob[0]!=null)
					obj.setDriverOrConductorCode(ob[0].toString());
				if(ob[1]!=null){
					if(ob[1].toString().contains("-"))
				   obj.setDriverOrConductorName(ob[1].toString().substring(0,ob[1].toString().lastIndexOf(' ')!=-1?ob[1].toString().lastIndexOf(' '):ob[1].toString().length()));
					else
						obj.setDriverOrConductorName(ob[1].toString());
				}
				if(ob[2]!=null)
					obj.setDate(ob[2].toString());
					if(ob[3]!=null)
					obj.setRouteName(ob[3].toString());
					if(ob[4]!=null)
					obj.setEpkm(ob[4].toString());
					list.add(obj);
			}
			
		}
		}
		else 
		{
		
		List<Object[]> dutyInspectorOverrideReport = busTyperMasterRepository.detailBusDriverConductorReport(startDate, endDate);
		{
			
			for(Object[] ob :dutyInspectorOverrideReport)
			{
				obj= new DetailBusDriverConductorDto();
				if(ob[0]!=null)
				obj.setBusNo(ob[0].toString());
				if(ob[1]!=null)
				obj.setDate(ob[1].toString());
				if(ob[2]!=null)
				obj.setRouteName(ob[2].toString());
				if(ob[3]!=null)
				obj.setKmpl(ob[3].toString());
				if(ob[4]!=null)
				obj.setEpkm(ob[4].toString());
				
				list.add(obj);
			}
			
		}
		}
    	return list;
	
	}
	
	
	
}

