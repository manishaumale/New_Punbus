package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.AddaFeesTollFeeReportDto;
import com.idms.base.api.v1.model.dto.BusStandWiseAdvanceBookingDto;
import com.idms.base.api.v1.model.dto.ComparisonReportDutyRosterDto;
import com.idms.base.api.v1.model.dto.ETMIssuanceReportResponseModel;
import com.idms.base.api.v1.model.dto.ETMStockReportResponseModel;
import com.idms.base.api.v1.model.dto.FuelDispenWiseFuelIssuReportResModel;
import com.idms.base.api.v1.model.dto.KMPLTicketReportDto;
import com.idms.base.api.v1.model.dto.KMReceivedLessthanSchduledKMsDto;
import com.idms.base.api.v1.model.dto.MachineNotRecSchReportDto;
import com.idms.base.api.v1.model.dto.RepairBillReportDto;
import com.idms.base.api.v1.model.dto.TicketBoxMachIssuReportResModel;
import com.idms.base.service.TicketReportService;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/ticketReport")
@Log4j2
public class TicketReportController {

	@Autowired
	TicketReportService ticketReportService;

	/**
	 * 1.ETM Stock Report
	 * @param from
	 * @param to
	 * @param depoId
	 * @return
	 */
	@ApiOperation("ETM Stock Report")
	@GetMapping("/etmStockReport/{fromDate}/{toDate}/{depoId}")
	public List<List<ETMStockReportResponseModel>> etmStockReport(@PathVariable("fromDate") String from, @PathVariable("toDate") String to,
			@PathVariable("depoId") Long depoId) {
		List<List<ETMStockReportResponseModel>> model = ticketReportService.getEtmStockReport(from, to, depoId);
		log.info("etmStockReportController");
		return model;
	}
	
	/**
	 * 2.ETM Issuance Report 
	 * @param from
	 * @param to
	 * @param depoId
	 * @param etmNo
	 * @return
	 */
	@ApiOperation("ETM Issuance Report")
	@GetMapping("/etmIssuanceReport/{fromDate}/{toDate}/{depoId}/{etmNo}")
	public List<List<ETMIssuanceReportResponseModel>> etmIssuanceReport(@PathVariable("fromDate") String from, @PathVariable("toDate") String to,
			@PathVariable("depoId") Integer depoId, @PathVariable("etmNo") Integer etmNo) {
		List<List<ETMIssuanceReportResponseModel>> model = ticketReportService.getIssuanceReport(from, to, depoId, etmNo);
		return model;
	}

	/**
	 * 3.Non Issuance of ETM Report
	 * @param from
	 * @param to
	 * @param depoId
	 * @return
	 */
	@ApiOperation("Non Issuance of ETM Report")
	@GetMapping("/nonIssuanceOfETMReport/{fromDate}/{toDate}/{depotId}/{etmNo}")
	public List<List<ETMIssuanceReportResponseModel>> nonIssuanceOfETMReport(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, @PathVariable("depotId") Integer depotId,@PathVariable("etmNo") Integer etmNo) {
		List<List<ETMIssuanceReportResponseModel>> model = ticketReportService.getNonIssuanceOfETMReport(from, to, depotId,etmNo);
		return model;
	}

	/**
	 * 4.Ticket Box and Machine Issuance Report
	 * @param from
	 * @param to
	 * @param depoId
	 * @return
	 */
	@ApiOperation("Ticket Box and Machine Issuance Report")
	@GetMapping("/ticketBoxAndMachineIssuanceReport/{fromDate}/{toDate}/{depotId}")
	public List<List<TicketBoxMachIssuReportResModel>> ticketBoxAndMachineIssuanceReport(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, @PathVariable("depotId") Long depotId) {
		 List<List<TicketBoxMachIssuReportResModel>>  model = ticketReportService.ticketBoxAndMachineIssuanceReport(from, to, depotId);
		return model;
	}

	/**
	 * 5.Issuance of Same Ticket Box and Machine to Same Conductor Report
	 * @param from
	 * @param to
	 * @param depoId
	 * @return
	 */
	@ApiOperation("Issuance of Same Ticket Box and Machine to Same Conductor Report")
	@GetMapping("/issuanceSameTicketBoxMachSameCondRep/{fromDate}/{toDate}/{depotId}")
	public List<TicketBoxMachIssuReportResModel> issuanceSameTicketBoxMachSameCondRep(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, @PathVariable("depotId") Integer depoId) {
		List<TicketBoxMachIssuReportResModel> model = ticketReportService.issuanceSameTicketBoxMachSameCondRep(from, to, depoId);
		return model;
	}

	/**
	 * 6.Fuel Dispenser wise Fuel Issuance Report State Wise
	 * @param from
	 * @param to
	 * @param stateId
	 * @return List<FuelDispenWiseFuelIssuReportResModel>
	 */
	@ApiOperation("Fuel Dispenser wise Fuel Issuance Report By StateWise")
	@GetMapping("/fuelDispWiseFuelIssuanceReport/{from}/{to}/{stateId}")
	public List<FuelDispenWiseFuelIssuReportResModel> fuelDispenserWiseFuelIssuanceReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("stateId") Long stateId) {
		List<FuelDispenWiseFuelIssuReportResModel> model = ticketReportService.fuelDispenserWiseFuelIssuanceReport(from, to, stateId);
		return model;
	}
	/**
	 * 7.Depot Wise Paper Ticket Stock Report
	 * @param from
	 * @param to
	 * @param depotId
	 * @return List<FuelDispenWiseFuelIssuReportResModel>
	 */
	@ApiOperation("Depot Wise Paper Ticket Stock Report")
	@GetMapping("/depotWisePaperTicketStockReport/{fromDate}/{toDate}/{depotId}")
	public List<FuelDispenWiseFuelIssuReportResModel> depotWisePaperTicketStockReport(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, @PathVariable("depotId") Long depotId) {
		List<FuelDispenWiseFuelIssuReportResModel> model = ticketReportService.depotWisePaperTicketStockReport(from, to, depotId);
		return model;
	}
	/**
	 * 8.Machine not Received as per Scheduled Report
	 * @param from
	 * @param to
	 * @param depotId
	 * @return List<MachineNotRecSchReportDto>
	 */
	@ApiOperation("Machine not Received as per Scheduled Report")
	@GetMapping("/machineNotReceivedScheduledReport/{fromDate}/{toDate}/{depotId}")
	public List<List<MachineNotRecSchReportDto>> machineNotReceivedScheduledReport(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, @PathVariable("depotId") Integer depotId) {
		List<List<MachineNotRecSchReportDto>> model = ticketReportService.machineNotReceivedScheduledReport(from, to, depotId);
		return model;
	}
	/**
	 * 9.Frequently issuance of Machine with Ticket Box Report
	 * @param from
	 * @param to
	 * @param depotId
	 * @return List<MachineNotRecSchReportDto>
	 */
	@ApiOperation("Frequently issuance of Machine with Ticket Box Report")
	@GetMapping("/frequentlyIssuanceMachineWithTicketBoxReport/{fromDate}/{toDate}/{depotId}")
	public List<MachineNotRecSchReportDto> frequentlyIssuanceMachineWithTicketBoxReport(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, @PathVariable("depotId") Integer depotId) {
		List<MachineNotRecSchReportDto> model = ticketReportService.frequentlyIssuanceMachineWithTicketBoxReport(from, to, depotId);
		return model;
	}
	/**
	 * 10.KMPL Report
	 * @param from
	 * @param to
	 * @param conductor
	 * @return List<KMPLTicketReportDto>
	 */
	@ApiOperation("KMPL Report")
	@GetMapping("/conductorwisePaperTicketusedReport/{fromDate}/{toDate}/{conductor}")
	public List<KMPLTicketReportDto>  conductorwisePaperTicketusedReport(@PathVariable("fromDate") String from,
			@PathVariable("toDate") String to, @PathVariable("conductor") Integer conductor) {
		List<KMPLTicketReportDto> model = ticketReportService.kmplReport(from, to, conductor);
		return model;
	}
	/**
	 * 11.Late Cash Deposit Report
	 * @param from
	 * @param to
	 * @param conductor
	 * @return List<KMPLTicketReportDto> 
	 */
	@ApiOperation("Late Cash Deposit Report")
	@GetMapping("/lateCashDepositReport/{from}/{to}/{conductor}")
	public List<KMPLTicketReportDto> lateCashDepositReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("conductor") Long conductor) {
		List<KMPLTicketReportDto> model = ticketReportService.lateCashDepositReport(from, to, conductor);
		return model;
	}
	
	/** 
	 * 12. Adda Fees and Toll Fee Report
	 * @param from
	 * @param to
	 * @param depot
	 * @return List<AddaFeesTollFeeReportDto>
	 */
	@ApiOperation("Adda Fees and Toll Fee Report")
	@GetMapping("/addaFeesAndTollFeeReport/{from}/{to}/{depot}")
	public List<AddaFeesTollFeeReportDto> addaFeesAndTollFeeReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("depot") Long depot) {
		List<AddaFeesTollFeeReportDto> model = ticketReportService.addaFeesAndTollFeeReport(from, to, depot);
		return model;
	}
	
	
	/**
	 * 13.KM Received Less than Schduled KMs Report
	 * @param from
	 * @param to
	 * @param route
	 * @return List<KMReceivedLessthanSchduledKMsDto>
	 */
	@ApiOperation("KM Received Less than Schduled KMs Report")
	@GetMapping("/kMReceivedLessthanSchduledKMsReport/{from}/{to}/{route}")
	public List<KMReceivedLessthanSchduledKMsDto> kMReceivedLessthanSchduledKMsReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("route") Long route) {
		List<KMReceivedLessthanSchduledKMsDto> model = ticketReportService.kMReceivedLessthanSchduledKMsReport(from, to, route);
		return model;
	}
	/**
	 * 14.Non Schedule KMs Report
	 * @param from
	 * @param to
	 * @param route
	 * @return List<KMReceivedLessthanSchduledKMsDto>
	 */
	@ApiOperation("Non Schedule KMs Report")
	@GetMapping("/nonScheduleKMsReport/{from}/{to}/{route}")
	public List<KMReceivedLessthanSchduledKMsDto> nonScheduleKMsReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("route") Long route) {
		List<KMReceivedLessthanSchduledKMsDto> model = ticketReportService.nonScheduleKMsReport(from, to, route);
		return model;
	}
	
	/**
	 * 15.Comparison Report Duty Roster KMs, DPA KMs, Bus KMs, Driver KMs, Conductor KMs,Route KMS
	 * @param from
	 * @param to
	 * @param depot
	 * @return List<ComparisonReportDutyRosterDto>
	 */
	@ApiOperation("Comparison Report Duty Roster KMs, DPA KMs, Bus KMs, Driver KMs, Conductor KMs,Route KMS")
	@GetMapping("/comparisonReportDutyRosterKMs/{from}/{to}/{depot}")
	public List<ComparisonReportDutyRosterDto> comparisonReportDutyRosterKMs(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("depot") Long depot) {
		List<ComparisonReportDutyRosterDto> model = ticketReportService.comparisonReportDutyRosterKMs(from, to, depot);
		return model;
	}
	
	/**
	 * 16.Repair Bill Report
	 * @param from
	 * @param to
	 * @param depot
	 * @return List<RepairBillReportDto> 
	 */
	@ApiOperation("Repair Bill Report")
	@GetMapping("/repairBillReport/{from}/{to}/{depot}")
	public List<RepairBillReportDto> repairBillReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("depot") Long depot) {
		List<RepairBillReportDto> model = ticketReportService.repairBillReport(from, to, depot);
		return model;
	}
	/**
	 * 17.Bus Stand Wise Advance Booking Report
	 * @param from
	 * @param to
	 * @param depot
	 * @return List<BusStandWiseAdvanceBookingDto>
	 */
	@ApiOperation("Bus Stand Wise Advance Booking Report")
	@GetMapping("/busStandWiseAdvanceBookingReport/{from}/{to}/{depot}")
	public List<BusStandWiseAdvanceBookingDto> busStandWiseAdvanceBookingReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("depot") Long depot) {
		List<BusStandWiseAdvanceBookingDto> model = ticketReportService.busStandWiseAdvanceBookingReport(from, to, depot);
		return model;
	}
	/**
	 * 18. Free Traveling and Concession Traveling Report
	 * @param from
	 * @param to
	 * @return List<BusStandWiseAdvanceBookingDto>
	 */
	@ApiOperation("Free Traveling and Concession Traveling Report")
	@GetMapping("/freeTravelingConcessionTravelingReport/{from}/{to}")
	public List<BusStandWiseAdvanceBookingDto> freeTravelingConcessionTravelingReport(@PathVariable("from") String from,
			@PathVariable("to") String to) {
		List<BusStandWiseAdvanceBookingDto> model = ticketReportService.freeTravelingConcessionTravelingReport(from, to);
		return model;
	}
	/**
	 * 19.Bus Type, Sub Type Cash Deposit Report
	 * @param from
	 * @param to
	 * @param busType
	 * @param subType
	 * @return  List<BusStandWiseAdvanceBookingDto>
	 */
	@ApiOperation("Bus Type, Sub Type Cash Deposit Report")
	@GetMapping("/busTypeSubTypeCashDepositReport/{from}/{to}/{busType}/{depotId}")
	public List<List<BusStandWiseAdvanceBookingDto>> busTypeSubTypeCashDepositReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("busType") Long busType,@PathVariable("depotId") Integer depotId) {
		List<List<BusStandWiseAdvanceBookingDto>> model = ticketReportService.busTypeSubTypeCashDepositReport(from, to,busType,depotId);
		return model;
	}
	/**
	 * 20.Machine not Received as per scheduled and delay in hrs report
	 * @param from
	 * @param to
	 * @param depotId
	 * 
	 */
	
	@ApiOperation("Machine not Received as per Scheduled Report")
	@GetMapping("/machineNotReceivedScheduledAndDelayInhrsReport/{from}/{to}/{depotId}")
	public List<List<MachineNotRecSchReportDto>> machineNotReceivedScheduledAndDelayInhrsReport(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("depotId") Integer depotId) {
		List<List<MachineNotRecSchReportDto>> model = ticketReportService.machineNotReceivedAsperScheduledAndDelayInhrsReport(from,to,depotId);
		return model;
	}
	
}
