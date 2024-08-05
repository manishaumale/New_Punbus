package com.idms.base.service;

import java.util.List;

import com.idms.base.api.v1.model.dto.AbsentReportDto;
import com.idms.base.api.v1.model.dto.AttendanceReportDto;
import com.idms.base.api.v1.model.dto.AuthorizedRouteReportDto;
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

public interface RouteAndCrewMgtReportService {

	List<BusTypeDto> fetchBusTypeListOnLoad();
	
	List<RouteWiseSummaryReportDto> routeWiseSummaryReport(String fromDate,String toDate,Integer routeId);

	List<OffRouteReportDto> offRouteReport(String driverId,String fromDate, String toDate);

	List<BusNotSentOnRouteReportDto> busNotSentOnRoute(String fromDate, String toDate);

	List<BusGoingAwayFromDepotReportDto> busGoingAwayFromDepot(String fromDate, String toDate);

	List<BusComingBackToDepotReportDto> busComingBackToDepot(String fromDate, String toDate);

	List<RoutesInOperationReport> routesInOperationReport(Integer depotId, String fromDate, String toDate);

	List<ExpectedVersusAchievedKmsReportDto> expectedVersusAchieved(String fromDate, String toDate);

	List<ScheduledKmsMissedReportDto> busWiseKmsMissedReport(Integer busTypeId, String fromDate, String toDate);

	List<DriverOrConductorLeaveReportDto> driverOrConductorLeaveReport(String driverId, Integer driverType,String fromdate,String toDate);

	List<DriverOrConWiseRestLeaveGrantedDto> driverOrConductorWiseRestLeaveGranted(Integer driverId, String fromDate,
			String toDate);

	List<DriverOrCondPerformingOtherDutyDto> driverOrConductorPerformingOtherDuty(String driverId,
			String fromDate, String toDate);

	List<DriverOrCondPerformingOtherDutyDto> disciplinaryActionReport(String driverOrconductor, String fromDate, String toDate);

	List<DutyInspectorOverrideReportDto> dutyInspectorOverrideReport(String driverOrconductorOrbustype, String fromDate, String toDate);

	List<SuspensionReportDto> suspensionReport(String  driverOrConductor, String fromDate, String toDate);

	List<AbsentReportDto> absentReport(String driverOrconductor, String fromDate, String toDate);

	List<AbsentReportDto> restDueReport(String driverId, String fromDate, String toDate);

	List<StaffPerformanceReportDto> staffPerformanceReport(String driverId, String fromDate, String toDate);

	List<RoutesLessThanEightReportDto> routesLessThanEightReport(String fromDate, String toDate);

	List<RoutesLessThanEightReportDto> nonOperationReport(Integer depotId, String fromDate, String toDate);

	List<LossProfitMakingReportDto> lossMakingRouteReport(Integer depotId, String fromDate, String toDate);

	List<LossProfitMakingReportDto> profitMakingRouteReport(Integer depotId, String fromDate, String toDate);

	List<RouteAnalysisReportDto> routeAnalysisReport(String fromDate,String toDate);
	
	List<BusWiseKmsReportDto> fetchBusWiseKmsReport(Integer busId,Integer busType, String fromDate, String toDate);
	
	List<DriverOrCondPerformingOtherDutyDto> totalDriversConductorsOnotherdutyreport(String driverId, Integer driverType,String fromdate,String toDate);
	
	List<DateWiseProductiveReportDto> dateWiseProductiveReport(String fromDate,String toDate,String type);
	
	List<EmploymentTypeDto> getEmployementTypeList();
	
	List<MaxKMsReportDto> maxKMsReport(String fromDate,String toDate,Integer routeId);
	
	List<RouteWiseBusAllocationReportDto> routeWiseBusAllocationReport(String fromDate,String toDate,Integer routeId);
	
	List<RouteNoToBeOperatedReportDto> routeNoToBeOperatedReport(String fromDate,String toDate);
	
	List<AuthorizedRouteReportDto> authorizedRouteReport(String fromDate,String toDate, String type);
	
	List<DutyRosterTotalKMsReportDto> dutyRosterTotalKMsReport(String fromDate, String toDate);
	
	List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport(String fromDate,String toDate);
	
	List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport1(String fromDate,String toDate);
	
	List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport2(String fromDate,String toDate);
	
	List<DetailBusDriverConductorDto> detailBusDriverConductorReport(String fromDate,String toDate,String type);
	
	List<TripsNotBeingOperatedReportDto> tripsNotBeingOperatedReport(String fromDate , String toDate);

	
	List<EnrouteBusReportDto> enrouteBusReport(Integer busNo);
	
	
	List<AttendanceReportDto> attendanceReportDetails(String type,Integer month);
}
