package com.idms.base.service;

import java.util.Date;
import java.util.List;

import com.idms.base.api.v1.model.dto.BusWiseDriverRouteKmplDto;
import com.idms.base.api.v1.model.dto.BusesNotGettingDieselIssuedOnTimeReportDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DepotStateWiseBusTypeComparReportDto;
import com.idms.base.api.v1.model.dto.DepotWiseDieselStockReportDto;
import com.idms.base.api.v1.model.dto.DieselIssueOtherPurposeReportDto;
import com.idms.base.api.v1.model.dto.DispensingUnitReportDto;
import com.idms.base.api.v1.model.dto.DriverWiseBusRouteKMPLDto;
import com.idms.base.api.v1.model.dto.DueDateCleaningReportDto;
import com.idms.base.api.v1.model.dto.ExplosiveLicenseReportDto;
import com.idms.base.api.v1.model.dto.FuelDispenserWiseReport;
import com.idms.base.api.v1.model.dto.FuelTypeDto;
import com.idms.base.api.v1.model.dto.InspectionCarriedOutReportDto;
import com.idms.base.api.v1.model.dto.InspectionDoneVersusDueReportDto;
import com.idms.base.api.v1.model.dto.InspectionDueDateReportDto;
import com.idms.base.api.v1.model.dto.IssuanceReceiptStmntReportDto;
import com.idms.base.api.v1.model.dto.KMPLReportDto;
import com.idms.base.api.v1.model.dto.KmplComparisonReportDto;
import com.idms.base.api.v1.model.dto.SupplyReceivedReportDto;
import com.idms.base.api.v1.model.dto.TotalTankAndCapacityDto;
import com.idms.base.api.v1.model.dto.VariationBeyondReportDto;

public interface TankAndCapacityService {
	
	List<DepotMasterDto> findAllDepot();
	List<TotalTankAndCapacityDto> listOfTankCapacityReport(Integer dpcode,Date date);
	List<ExplosiveLicenseReportDto> listOfexplosiveReport(Integer dpcode,Date date);
	List<DispensingUnitReportDto> listOfDispensingReport(Integer dpcode);
	List<KmplComparisonReportDto> listOfKMPLComparisonReport(Date fromDate,Date toDate,Integer routeId);
	List<FuelDispenserWiseReport> listOfFuelDispenserReport(Integer dpcode,Date fromDate,Date toDate);
	List<SupplyReceivedReportDto> supplyReceivedReport(Integer depoId,Date from, Date to);
	List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmpl(String busNo,Date startDate, Date endDate);
	List<BusWiseDriverRouteKmplDto> routeWiseBusDriverKmpl(Date startDate, Date endDate,Integer driverId);
	List<BusWiseDriverRouteKmplDto> busTypeBusWiseDieselIssuanceReport(String bus,Date startDate, Date endDate,String busType);
	List<IssuanceReceiptStmntReportDto> issuanceReceiptStmntReport(Date startDate, Date endDate);
	List<IssuanceReceiptStmntReportDto> deadKMSReport(Integer routeId,String date);
	List<BusWiseDriverRouteKmplDto> bestWorstDriverReport(Date startDate, Date endDate,Integer depoId,Double startRange,Double endRange);
	List<DueDateCleaningReportDto> dueDateCleaningReport(Integer depoId,Date date);
	List<InspectionDueDateReportDto> inspectionDueDateReport(Integer depoId,Date date);
	List<DepotWiseDieselStockReportDto> depotWiseDieselStockReport(Integer depoId,Date startDate, Date endDate, Integer fuelTypeId);
	List<DepotWiseDieselStockReportDto> advancePaymentReport(Date startDate, Date endDate, Integer depoId);
	List<KMPLReportDto> kmplReport(Date startDate, Date endDate, Integer busType, String grouping, Integer depoId,
			String select);
	List<DriverWiseBusRouteKMPLDto> driverWiseBusRouteKMPL(Integer driverId,Integer routeId,Date startDate, Date endDate);
	List<BusWiseDriverRouteKmplDto> busTypeBusWiseDieselMobileReport(String busNo,Date startDate, Date endDate, Integer busType);
	List<VariationBeyondReportDto> variationBeyondReport(Integer depoId,Date fromDate,Date toDate);
	List<InspectionCarriedOutReportDto> inspectionCarriedOutReport( Integer depoId,Date startDate, Date endDate);
	List<IssuanceReceiptStmntReportDto> grossKMSDepositedCashierVsDpaReport(Date startDate, Date endDate);
	List<IssuanceReceiptStmntReportDto> dieselNotIssuedReport(Date startDate, Date endDate);
	List<DieselIssueOtherPurposeReportDto> dieselIssueOtherPurposeReport(Date startDate, Date endDate,Integer depoId);
	List<DepotStateWiseBusTypeComparReportDto> depotStateWiseBusTypeComparReport(Date startDate, Date endDate,
			Integer depoId, Integer busType);
	List<InspectionDoneVersusDueReportDto> inspectionDoneVersusDueReport(String lastDate, String month, Date startDate, Date endDate, String depotCode, String fromDate);
	List<BusesNotGettingDieselIssuedOnTimeReportDto> busesNotGettingDieselIssueReport(Date startDate, Date endDate,
			Integer depoId);
	List<FuelTypeDto> fetchFuelTypeList();	

}
