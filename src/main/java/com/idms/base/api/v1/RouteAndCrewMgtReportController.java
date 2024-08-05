package com.idms.base.api.v1;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.idms.base.service.RouteAndCrewMgtReportService;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/routeReport")
@Log4j2
public class RouteAndCrewMgtReportController {
	
	@Autowired
	private ModelMapper mapper;
	
	
	@Autowired
	RouteAndCrewMgtReportService service;
	
	
	@ApiOperation("Returns List of  Bus Type Master When Load")
	@GetMapping(path = "/busTypeMasterOnLoad")
	public List<BusTypeDto> busMasterFormOnLoad() {
		log.info("Enter into busTypeMasterOnLoad service");
		List<BusTypeDto> busMasterFormOnLoad = this.service.fetchBusTypeListOnLoad();
		return busMasterFormOnLoad;
	}
	
	@ApiOperation("Returns List of  Bus Wise Scheduled Kms Report")
	@GetMapping(path = "/busWiseKmsReport/{busId}/{busType}/{fromDate}/{toDate}")
	public List<BusWiseKmsReportDto> busWiseKmsReport(@PathVariable("busId") Integer busId,@PathVariable("busType") Integer busType,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into busWiseKmsReport service");
		List<BusWiseKmsReportDto> list = this.service.fetchBusWiseKmsReport(busId,busType,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Bus Wise Scheduled Kms Missed Report")
	@GetMapping(path = "/busWiseKmsMissedReport/{id}/{fromDate}/{toDate}")
	public List<ScheduledKmsMissedReportDto> busWiseKmsMissedReport(@PathVariable("id") Integer busTypeId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into busWiseKmsMissedReport service");
		List<ScheduledKmsMissedReportDto> list = this.service.busWiseKmsMissedReport(busTypeId,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Off Route Report")
	@GetMapping(path = "/offRouteReport/{driverId}/{fromDate}/{toDate}")
	public List<OffRouteReportDto> offRouteReport(@PathVariable("driverId") String driverId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into offRouteReport service");
		List<OffRouteReportDto> OffRouteReportList = this.service.offRouteReport(driverId,fromDate,toDate);
		return OffRouteReportList;
	}
	
	@ApiOperation("Returns List of  Bus Not Sent On Route Report")
	@GetMapping(path = "/busNotSentOnRoute/{fromDate}/{toDate}")
	public List<BusNotSentOnRouteReportDto> busNotSentOnRoute(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into busNotSentOnRoute service");
		List<BusNotSentOnRouteReportDto> list = this.service.busNotSentOnRoute(fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Bus Going Away From Depot Report")
	@GetMapping(path = "/busGoingAwayFromDepot/{fromDate}/{toDate}")
	public List<BusGoingAwayFromDepotReportDto> busGoingAwayFromDepot(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into busGoingAwayFromDepot service");
		List<BusGoingAwayFromDepotReportDto> list = this.service.busGoingAwayFromDepot(fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Bus Coming Back To Depot Report")
	@GetMapping(path = "/busComingBackToDepot/{fromDate}/{toDate}")
	public List<BusComingBackToDepotReportDto> busComingBackToDepot(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into busComingBackToDepot service");
		List<BusComingBackToDepotReportDto> list = this.service.busComingBackToDepot(fromDate,toDate);
		return list;
	}
	
	
	@ApiOperation("Returns Routes In Operation Report")
	@GetMapping(path = "/routesInOperationReport/{depotId}/{fromDate}/{toDate}")
	public List<RoutesInOperationReport> routesInOperationReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into routesInOperationReport service");
		List<RoutesInOperationReport> list = this.service.routesInOperationReport(depotId,fromDate,toDate);
		return list;
	}
	
	
	@ApiOperation("Returns Expected Versus Achieved Report")
	@GetMapping(path = "/expectedVersusAchieved/{fromDate}/{toDate}")
	public List<ExpectedVersusAchievedKmsReportDto> expectedVersusAchieved(@PathVariable("fromDate") String fromDate,@PathVariable("toDate") String toDate) {
		log.info("Enter into expectedVersusAchieved service");
		List<ExpectedVersusAchievedKmsReportDto> list = this.service.expectedVersusAchieved(fromDate,toDate);
		return list;
	}
	
	
	@ApiOperation("Returns List of  Conductors And Driver Leave Report")
	@GetMapping(path = "/driverOrConductorLeaveReport/{driverId}/{driverType}/{fromDate}/{toDate}")
	public List<DriverOrConductorLeaveReportDto> driverOrConductorLeaveReport(@PathVariable("driverId") String driverId,@PathVariable("driverType") Integer driverType,@PathVariable("fromDate") String fromDate
			,@PathVariable("toDate") String toDate) {
		log.info("Enter into offRouteReport service");
		List<DriverOrConductorLeaveReportDto> list = this.service.driverOrConductorLeaveReport(driverId,driverType,fromDate,toDate);
		return list;
	}
	
	
	@ApiOperation("Returns List of  Conductors And Driver Wise Rest Granted Leave Report")
	@GetMapping(path = "/driverOrConductorWiseRestLeaveGranted/{driverId}/{fromDate}/{toDate}")
	public List<DriverOrConWiseRestLeaveGrantedDto> driverOrConductorWiseRestLeaveGranted(@PathVariable("driverId") Integer driverId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into driverOrConductorWiseRestLeaveGranted service");
		List<DriverOrConWiseRestLeaveGrantedDto> list = this.service.driverOrConductorWiseRestLeaveGranted(driverId,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Disciplinary Action Report for Absence Report")
	@GetMapping(path = "/disciplinaryActionReport/{driverOrconductor}/{fromDate}/{toDate}")
	public ResponseEntity<List<DriverOrCondPerformingOtherDutyDto>>disciplinaryActionReport(@PathVariable("driverOrconductor") String driverOrconductor,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into disciplinaryActionReport service");
		
		List<DriverOrCondPerformingOtherDutyDto> listNames = this.service.disciplinaryActionReport(driverOrconductor,fromDate,toDate);
		return new ResponseEntity<List<DriverOrCondPerformingOtherDutyDto>>(listNames,HttpStatus.OK);
	}
	
	
	@ApiOperation("Returns List of  Duty Inspector Override Report")
	@GetMapping(path = "/dutyInspectorOverrideReport/{driveOrconductorOrBustype}/{fromDate}/{toDate}")
	public List<DutyInspectorOverrideReportDto> dutyInspectorOverrideReport(@PathVariable("driveOrconductorOrBustype") String driveOrconductorOrBustype,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into dutyInspectorOverrideReport service");
		List<DutyInspectorOverrideReportDto> list = this.service.dutyInspectorOverrideReport(driveOrconductorOrBustype,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Suspension Report")
	@GetMapping(path = "/suspensionReport/{driverOrConductor}/{fromDate}/{toDate}")
	public List<SuspensionReportDto> suspensionReport(@PathVariable("driverOrConductor") String driverOrConductor,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into suspensionReport service");
		List<SuspensionReportDto> list = this.service.suspensionReport(driverOrConductor,fromDate,toDate);
		return list;
	}
	
	
	@ApiOperation("Returns List of  Absent Report")
	@GetMapping(path = "/absentReport/{driverOrconductor}/{fromDate}/{toDate}")
	public ResponseEntity<List<AbsentReportDto>> absentReport(@PathVariable("driverOrconductor") String driverOrconductor,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into absentReport service");
		List<AbsentReportDto> list = this.service.absentReport(driverOrconductor,fromDate,toDate);
		return new ResponseEntity<List<AbsentReportDto>>(list,HttpStatus.OK);
	}
	
	@ApiOperation("Returns List of  Rest Due Report")
	@GetMapping(path = "/restDueReport/{driverId}/{fromDate}/{toDate}")
	public List<AbsentReportDto> restDueReport(@PathVariable("driverId") String driverId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into restDueReport service");
		List<AbsentReportDto> list = this.service.restDueReport(driverId,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Staff Performance Report")
	@GetMapping(path = "/staffPerformanceReport/{driverId}/{fromDate}/{toDate}")
	public List<StaffPerformanceReportDto> staffPerformanceReport(@PathVariable("driverId") String driverId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into staffPerformanceReport service");
		List<StaffPerformanceReportDto> list = this.service.staffPerformanceReport(driverId,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Routes Less Than 8 Report")
	@GetMapping(path = "/routesLessThanEightReport/{fromDate}/{toDate}")
	public List<RoutesLessThanEightReportDto> routesLessThanEightReport(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into RoutesLessThanEightReport service");
		List<RoutesLessThanEightReportDto> list = this.service.routesLessThanEightReport(fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Non Operation Report")
	@GetMapping(path = "/nonOperationReport/{depotId}/{fromDate}/{toDate}")
	public List<RoutesLessThanEightReportDto> nonOperationReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into RoutesLessThanEightReport service");
		List<RoutesLessThanEightReportDto> list = this.service.nonOperationReport(depotId,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Loss Making Report")
	@GetMapping(path = "/lossMakingRouteReport/{depotId}/{fromDate}/{toDate}")
	public List<LossProfitMakingReportDto> lossMakingRouteReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into Loss Making Route Report service");
		List<LossProfitMakingReportDto> list = this.service.lossMakingRouteReport(depotId,fromDate,toDate);
		return list;
	}
	
	
	@ApiOperation("Returns List of  Profit Making Report")
	@GetMapping(path = "/profitMakingRouteReport/{depotId}/{fromDate}/{toDate}")
	public List<LossProfitMakingReportDto> profitMakingRouteReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into Profit Making Route Report service");
		List<LossProfitMakingReportDto> list = this.service.profitMakingRouteReport(depotId,fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Route Analysis  Report")
	@GetMapping(path = "/routeAnalysisReport/{fromDate}/{toDate}")
	public List<RouteAnalysisReportDto> routeAnalysisReport(@PathVariable("fromDate") String fromDate,@PathVariable("toDate") String toDate) {
		log.info("Enter into Route Analysis Report service");
		List<RouteAnalysisReportDto> list = this.service.routeAnalysisReport(fromDate,toDate);
		return list;
	}
	
	@ApiOperation("Returns List of  Driver Or Conductor Performing Other Duty")
	@GetMapping(path = "/driverOrConductorPerformingOtherDuty/{driverOrconductor}/{fromDate}/{toDate}")
	public List<DriverOrCondPerformingOtherDutyDto> driverOrConductorPerformingOtherDuty(@PathVariable("driverOrconductor") String driverOrconductor,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into staffPerformanceReport service");
		List<DriverOrCondPerformingOtherDutyDto> list = this.service.driverOrConductorPerformingOtherDuty(driverOrconductor,fromDate,toDate);
		return list;
	}
	@ApiOperation("Returns List of  Conductors And Driver Leave Report")
	@GetMapping(path = "/totalDriversConductorsOnotherdutyreport/{driverId}/{driverType}/{fromDate}/{toDate}")
	public List<DriverOrCondPerformingOtherDutyDto> totalDriversConductorsOnotherdutyreport(@PathVariable("driverId") String driverId,@PathVariable("driverType") Integer driverType,@PathVariable("fromDate") String fromDate
			,@PathVariable("toDate") String toDate) {
		log.info("Enter into totalDriversConductorsOnotherdutyreport service");
		List<DriverOrCondPerformingOtherDutyDto> list = this.service.totalDriversConductorsOnotherdutyreport(driverId,driverType,fromDate,toDate);
		return list;
	}
	@ApiOperation("Returns List Route Wise Summary Report ")
	@GetMapping(path="/routeWiseSummaryReport/{fromDate}/{toDate}/{routeId}")
	public List<RouteWiseSummaryReportDto> routeWiseSummaryReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate,@PathVariable("routeId") Integer routeId)
	{
		log.info("Enter into routeWiseSummaryReport service");
		List<RouteWiseSummaryReportDto> routeWiseSummaryReport = service.routeWiseSummaryReport(fromDate,toDate,routeId);
		return routeWiseSummaryReport;
		
	}
@ApiOperation("Returns enrouteBusReportDetails ")
@GetMapping(path="/enrouteBusReport/{busNo}")	
public ResponseEntity<List<EnrouteBusReportDto>> enrouteBusReportDetails(@PathVariable("busNo") Integer busNo)
{
	  log.info("Enter into ListofenrouteBusReports service");
		 List<EnrouteBusReportDto> enrouteBusReport = service.enrouteBusReport(busNo);
		return new ResponseEntity<List<EnrouteBusReportDto>>(enrouteBusReport,HttpStatus.OK);
	}


@ApiOperation("Returns List attendanceReportDetails")
@GetMapping(path="/attendanceReport/{type}/{month}")
public List<AttendanceReportDto> attendanceReport(@PathVariable("type") String type,@PathVariable("month") Integer month )
{
	List<AttendanceReportDto> attendanceReportDetails = service.attendanceReportDetails(type, month);
	return attendanceReportDetails;

}

	@ApiOperation("Returns List Date Wise driver conductor productive report ")
	@GetMapping(path="/dateWiseProductiveReport/{fromDate}/{toDate}/{type}")
	public List<DateWiseProductiveReportDto> dateWiseProductiveReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate,@PathVariable("type") String type)
	{
		log.info("Enter into dateWiseProductiveReport service");
		List<DateWiseProductiveReportDto> routeWiseSummaryReport = service.dateWiseProductiveReport(fromDate,toDate,type);
		return routeWiseSummaryReport;
		
	}
	
	@ApiOperation("Returns Employement Type ")
	@GetMapping(path="/findEmployementType")
	public List<EmploymentTypeDto> findEmployementType()
	{
		log.info("Enter into Employment Type service");
		List<EmploymentTypeDto> empList = service.getEmployementTypeList();
		return empList;
		
	}
	
	@ApiOperation("Returns List Maximum KMs Minimum Overtime Minimum Nights Routes report ")
	@GetMapping(path="/maxKMsReport/{fromDate}/{toDate}/{routeId}")
	public List<MaxKMsReportDto> maxKMsReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate,@PathVariable("routeId") Integer routeId)
	{
		log.info("Enter into maxKMsReport service");
		List<MaxKMsReportDto> maxKMsReportDto = service.maxKMsReport(fromDate,toDate,routeId);
		return maxKMsReportDto;
		
	}
	
	
	@ApiOperation("Returns List Route Widse Bus Allocation  report ")
	@GetMapping(path="/routeWiseBusAllocationReport/{fromDate}/{toDate}/{routeId}")
	public List<RouteWiseBusAllocationReportDto> routeWiseBusAllocationReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate,@PathVariable("routeId") Integer routeId)
	{
		log.info("Enter into routeWiseBusAllocationReport service");
		List<RouteWiseBusAllocationReportDto> routeWiseBusAllocationReportDto = service.routeWiseBusAllocationReport(fromDate,toDate,routeId);
		return routeWiseBusAllocationReportDto;
		
	}
	
	@ApiOperation("Returns List Route No To be operated report ")
	@GetMapping(path="/routeNoToBeOperatedReport/{fromDate}/{toDate}")
	public List<RouteNoToBeOperatedReportDto> routeNoToBeOperatedReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate)
	{
		log.info("Enter into routeNoToBeOperatedReport service");
		List<RouteNoToBeOperatedReportDto> routeNoToBeOperatedReport = service.routeNoToBeOperatedReport(fromDate,toDate);
		return routeNoToBeOperatedReport;
		
	}
	
	@ApiOperation("Returns List Authorized route  report ")
	@GetMapping(path="/authorizedRouteReport/{fromDate}/{toDate}/{type}")
	public List<AuthorizedRouteReportDto> authorizedRouteReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate,@PathVariable("type") String type)
	{
		log.info("Enter into authorizedRouteReport service");
		List<AuthorizedRouteReportDto> authorizedRouteReport = service.authorizedRouteReport(fromDate,toDate,type);
		return authorizedRouteReport;
		
	}
	
	@ApiOperation("Returns List duty roster total kms report ")
	@GetMapping(path="/dutyRosterTotalKMsReport/{fromDate}/{toDate}")
	public List<DutyRosterTotalKMsReportDto> dutyRosterTotalKMsReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate)
	{
		log.info("Enter into dutyRosterTotalKMsReport service");
		List<DutyRosterTotalKMsReportDto> dutyRosterTotalKMsReport = service.dutyRosterTotalKMsReport(fromDate,toDate);
		return dutyRosterTotalKMsReport;
		
	}
	
	@ApiOperation("Returns List KMs likely to be received report N+0")
	@GetMapping(path="/kmsLikelyToBeReceivedReport/{fromDate}/{toDate}")
	public List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate)
	{
		log.info("Enter into kmsLikelyToBeReceivedReport service");
		List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport = service.kmsLikelyToBeReceivedReport(fromDate,toDate);
		return kmsLikelyToBeReceivedReport;
		
	}
	
	@ApiOperation("Returns List KMs likely to be received report N+1")
	@GetMapping(path="/kmsLikelyToBeReceivedReport1/{fromDate}/{toDate}")
	public List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport1(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate)
	{
		log.info("Enter into kmsLikelyToBeReceivedReport1 service");
		List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport = service.kmsLikelyToBeReceivedReport1(fromDate,toDate);
		return kmsLikelyToBeReceivedReport;
		
	}
	
	@ApiOperation("Returns List KMs likely to be received report N+2")
	@GetMapping(path="/kmsLikelyToBeReceivedReport2/{fromDate}/{toDate}")
	public List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport2(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate)
	{
		log.info("Enter into kmsLikelyToBeReceivedReport2 service");
		List<KMsLikelyToBeReceivedReportDto> kmsLikelyToBeReceivedReport = service.kmsLikelyToBeReceivedReport2(fromDate,toDate);
		return kmsLikelyToBeReceivedReport;
		
	}
	
	@ApiOperation("Returns List Detail Bus/Driver/Conductor  report ")
	@GetMapping(path="/detailBusDriverConductorReport/{fromDate}/{toDate}/{type}")
	public List<DetailBusDriverConductorDto> detailBusDriverConductor(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate,@PathVariable("type") String type)
	{
		log.info("Enter into detailBusDriverConductorReport service");
		List<DetailBusDriverConductorDto> detailBusDriverConductor = service.detailBusDriverConductorReport(fromDate,toDate,type);
		return detailBusDriverConductor;
		
	}
	
	@ApiOperation("Returns ListTrips not being operated report report ")
	@GetMapping(path="/tripsNotBeingOperatedReport/{fromDate}/{toDate}")
	public List<TripsNotBeingOperatedReportDto> tripsNotBeingOperatedReport(@PathVariable("fromDate")String fromDate,@PathVariable("toDate") String toDate)
	{
		log.info("Enter into tripsNotBeingOperatedReport service");
		List<TripsNotBeingOperatedReportDto> tripsNotBeingOperatedReport = service.tripsNotBeingOperatedReport(fromDate,toDate);
		return tripsNotBeingOperatedReport;
		
	}
	

}	