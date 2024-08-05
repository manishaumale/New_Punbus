package com.idms.base.service;

import java.util.List;

import com.idms.base.api.v1.model.dto.BusWiseTyreInventoryReportDto;
import com.idms.base.api.v1.model.dto.CompletionOfExpectedTyreReportDto;
import com.idms.base.api.v1.model.dto.CondemnMileageReportDto;
import com.idms.base.api.v1.model.dto.CondemnTyreReportDto;
import com.idms.base.api.v1.model.dto.CountOfRetreadingTyresReport;
import com.idms.base.api.v1.model.dto.CurrentTyreStatusReportDto;
import com.idms.base.api.v1.model.dto.DepotWiseTakenOffReportDto;
import com.idms.base.api.v1.model.dto.DepotWiseTyreConsumptionReportDto;
import com.idms.base.api.v1.model.dto.DepotWiseTyreReportDto;
import com.idms.base.api.v1.model.dto.ExpectedTyreLifeReport;
import com.idms.base.api.v1.model.dto.MakeWiseComparisonReportDto;
import com.idms.base.api.v1.model.dto.MileageAchievedReportDto;
import com.idms.base.api.v1.model.dto.NewAndResoledTyreIssueDto;
import com.idms.base.api.v1.model.dto.NewTyreAndResoleTyreMileageReportDto;
import com.idms.base.api.v1.model.dto.NewTyreAndResoleTyreQuantityReportDto;
import com.idms.base.api.v1.model.dto.NewTyreIssuedForRearReportDto;
import com.idms.base.api.v1.model.dto.ResoleTyreMileageReport;
import com.idms.base.api.v1.model.dto.TotalBusesTyreReportByDepotDto;
import com.idms.base.api.v1.model.dto.TyreAuctionReportDto;
import com.idms.base.api.v1.model.dto.TyreBifurcationReportDto;
import com.idms.base.api.v1.model.dto.TyreCondemnReport;
import com.idms.base.api.v1.model.dto.TyreCurrentMileageReportDto;
import com.idms.base.api.v1.model.dto.TyreDueForInspectionDto;
import com.idms.base.api.v1.model.dto.TyreDueForRetredingReportDto;
import com.idms.base.api.v1.model.dto.TyreNotAuctionReportDto;
import com.idms.base.api.v1.model.dto.TyreIssueSlipListDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreNotSentForRetreadingReport;
import com.idms.base.api.v1.model.dto.TyreNotTakenOffAfterCompletedMileageReportDto;
import com.idms.base.api.v1.model.dto.TyreReportInStockDto;
import com.idms.base.api.v1.model.dto.TyreSentForRetreadingReport;
import com.idms.base.api.v1.model.dto.TyreTakenOffBeforeExpectedLife;
import com.idms.base.api.v1.model.dto.TyreTakenOffNotRefittedDto;
import com.idms.base.api.v1.model.dto.TyresLyingForAuctionReport;

public interface TyreMgtReportService {

	List<DepotWiseTyreReportDto> depotWiseTyreSummaryReport(Integer depotId, String date, String todate);

	List<DepotWiseTakenOffReportDto> depotWiseTakenOffReport(Integer depotId, String fromDate, String toDate);

	List<BusWiseTyreInventoryReportDto> busWiseTyreInventoryReport(Integer busId, String fromDate, String toDate);

	List<TotalBusesTyreReportByDepotDto> getTotalBusesTyreReport(Integer depotId, String fromDate, String toDate);

	List<TyreTakenOffNotRefittedDto> tyreTakenOffNotRefittedReport(Integer depotId, String fromDate, String toDate);

	List<NewAndResoledTyreIssueDto> newTyreIssuedReport(Integer depotId, String fromDate, String toDate);

	List<NewAndResoledTyreIssueDto> resoleTyreIssuedReport(Integer depotId, String fromDate, String toDate);

	List<ExpectedTyreLifeReport> expectedTyreLifeReport(Integer depotId, String fromDate, String toDate);

	List<MileageAchievedReportDto> mileageAchievedReport(String fromDate, String toDate);

	List<CondemnTyreReportDto> condemnTyreReport(Integer depotId, String fromDate, String toDate);

	List<CurrentTyreStatusReportDto> currentTyreStatusReport(Integer tyreId, String fromDate, String toDate);

	List<CompletionOfExpectedTyreReportDto> completionOfExpectedTyreReport(Integer depotId, String fromDate,
			String toDate);

	List<TyreDueForInspectionDto> tyreDueForInspection(Integer depotId,String Date);

	List<TyreTakenOffBeforeExpectedLife> tyreTakenOffBeforeExpectedLife(Integer depotId, String fromDate,
			String toDate);

	List<NewTyreIssuedForRearReportDto> newTyreIssuedForRearReport(Integer depotId, String fromDate, String toDate);

	List<TyreCondemnReport> tyreCondemnReport(Integer depotId, String fromDate, String toDate);

	List<TyreReportInStockDto> tyreReportInStock(String fromDate, String toDate);

	List<ResoleTyreMileageReport> resoleTyreMileageReport(Integer depotId, String fromDate, String toDate);

	List<TyreSentForRetreadingReport> tyreSentForRetreadingReport(Integer depotId, String fromDate, String toDate);

	List<TyreNotSentForRetreadingReport> tyreNotSentForRetreadingReport(Integer depotId, String fromDate,
			String toDate);

	List<CountOfRetreadingTyresReport> countOfRetreadingTyresReport(Integer depotId, String fromDate, String toDate);

	List<TyreBifurcationReportDto> tyreBifurcationReport(Integer depotId, String fromDate, String toDate);

	List<TyreAuctionReportDto> tyresAuctionReport(String fromDate, String toDate);

	List<TyresLyingForAuctionReport> tyresLyingForAuctionReport(Integer depotId, String fromDate, String toDate);
	
	List<NewTyreAndResoleTyreQuantityReportDto> newTyreAndResoleTyreQuantityReport(String fromDate , String toDate);
	
	//List<TyreAuctionReportDto> tyreAuctionReport(String fromDate, String toDate);
	
	List<TyreNotAuctionReportDto> tyreNotAuctionReport(Integer depotId, String fromDate, String toDate);
	
	List<NewTyreAndResoleTyreMileageReportDto> newTyreAndResoleTyreMileageReport(String fromDate, String toDate);
	
	public TyreIssueSlipListDto getTyreChangeReplacementData(Integer id);

	List<MakeWiseComparisonReportDto> makeWiseComparisonReport(Integer depotId, Integer tyreSizeId,String fromDate, String toDate, Integer makeId);

	List<TyreCurrentMileageReportDto> tyreCurrentMileageReport(String fromDate, String toDate);

	List<TyreDueForRetredingReportDto> tyreDueForRetredingReport(Integer depotId, String fromDate, String toDate);

	List<TyreNotTakenOffAfterCompletedMileageReportDto> tyreNotTakenOffAfterCompletedMileageReport(Integer depotId,
			String fromDate, String toDate);

	List<TyreNotTakenOffAfterCompletedMileageReportDto> condemnMileageReport(Integer depotId, String fromDate, String toDate);

	List<DepotWiseTyreConsumptionReportDto> depotWiseTyreConsumptionReport(Integer depotId, String fromDate,
			String toDate);

	List<TyreMakerDto> tyreMakerList();


}
