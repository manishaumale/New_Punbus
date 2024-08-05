package com.idms.base.service;

import java.util.List;

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

public interface TicketReportService {

	List<List<ETMStockReportResponseModel>> getEtmStockReport(String from, String to, Long depoId);

	List<List<ETMIssuanceReportResponseModel>> getIssuanceReport(String from, String to, Integer depoId, Integer etmNo);

	List<List<ETMIssuanceReportResponseModel>> getNonIssuanceOfETMReport(String from, String to, Integer depoId,Integer etmNo);

	List<List<TicketBoxMachIssuReportResModel>> ticketBoxAndMachineIssuanceReport(String from, String to, Long depoId);

	List<TicketBoxMachIssuReportResModel> issuanceSameTicketBoxMachSameCondRep(String from, String to, Integer depoId);

	List<FuelDispenWiseFuelIssuReportResModel> fuelDispenserWiseFuelIssuanceReport(String from, String to, Long stateId);

	List<FuelDispenWiseFuelIssuReportResModel> depotWisePaperTicketStockReport(String from, String to, Long depotId);

	List<List<MachineNotRecSchReportDto>> machineNotReceivedScheduledReport(String from, String to, Integer depotId);

	List<MachineNotRecSchReportDto> frequentlyIssuanceMachineWithTicketBoxReport(String from, String to, Integer depotId);

	List<KMPLTicketReportDto> kmplReport(String from, String to, Integer conductor);

	List<KMPLTicketReportDto> lateCashDepositReport(String from, String to, Long conductor);

	List<AddaFeesTollFeeReportDto> addaFeesAndTollFeeReport(String from, String to, Long depot);

	List<KMReceivedLessthanSchduledKMsDto> kMReceivedLessthanSchduledKMsReport(String from, String to, Long route);

	List<KMReceivedLessthanSchduledKMsDto> nonScheduleKMsReport(String from, String to, Long route);

	List<ComparisonReportDutyRosterDto> comparisonReportDutyRosterKMs(String from, String to, Long depot);

	List<RepairBillReportDto> repairBillReport(String from, String to, Long depot);

	List<BusStandWiseAdvanceBookingDto> busStandWiseAdvanceBookingReport(String from, String to, Long depot);

	List<BusStandWiseAdvanceBookingDto> freeTravelingConcessionTravelingReport(String from, String to);

	List<List<BusStandWiseAdvanceBookingDto>> busTypeSubTypeCashDepositReport(String from, String to, Long busType,Integer depotId);
	
	List<List<MachineNotRecSchReportDto>> machineNotReceivedAsperScheduledAndDelayInhrsReport(String from, String to,Integer depotId);

	
}
