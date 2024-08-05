package com.idms.base.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreCurrentMileageReportDto;
import com.idms.base.api.v1.model.dto.TyreDueForInspectionDto;
import com.idms.base.api.v1.model.dto.TyreDueForRetredingReportDto;
import com.idms.base.api.v1.model.dto.TyreNotAuctionReportDto;
import com.idms.base.api.v1.model.dto.TyreIssueSlipDto;
import com.idms.base.api.v1.model.dto.TyreIssueSlipListDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreNotSentForRetreadingReport;
import com.idms.base.api.v1.model.dto.TyreNotTakenOffAfterCompletedMileageReportDto;
import com.idms.base.api.v1.model.dto.TyreReportInStockDto;
import com.idms.base.api.v1.model.dto.TyreSentForRetreadingReport;
import com.idms.base.api.v1.model.dto.TyreTakenOffBeforeExpectedLife;
import com.idms.base.api.v1.model.dto.TyreTakenOffNotRefittedDto;
import com.idms.base.api.v1.model.dto.TyresAuctionReport;
import com.idms.base.api.v1.model.dto.TyresLyingForAuctionReport;
import com.idms.base.dao.entity.BusTyreAssociation;
import com.idms.base.dao.entity.BusTyreAssociationHistory;
import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusTyreAssociationHistoryRepository;
import com.idms.base.dao.repository.BusTyreAssociationRepository;
import com.idms.base.dao.repository.DocketTyreAssociationRepository;
import com.idms.base.dao.repository.TyreConditionRepository;
import com.idms.base.dao.repository.TyreMakersRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.service.TyreMgtReportService;
import com.idms.base.util.TyreCostCalculationUtility;
import com.idms.base.util.TyreManagementUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TyreMgtReportServiceImpl implements TyreMgtReportService {

	@Autowired
	BusTyreAssociationRepository busTyreAssociationRepository;

	@Autowired
	BusMasterRepository busMasterRepository;

	@Autowired
	TyreMasterRepository tyreMasterRepository;
	
	@Autowired
	TyreConditionRepository tyreConditionRepo;
	
	@Autowired
	TyreMakersRepository tyreMakerRepo;
	
	@Autowired
	BusTyreAssociationRepository busTyreAssosiationRepo;
	
	@Autowired
	BusTyreAssociationHistoryRepository busTyreAssoHisRepo;
	
	@Autowired
	TyreManagementUtility tyreManagementUtility;
	
	@Autowired
	DocketTyreAssociationRepository docketTyreAssoRepo;
	
	@Autowired
	TyreCostCalculationUtility tyreCostUtility;

	@Override
	public List<DepotWiseTyreReportDto> depotWiseTyreSummaryReport(Integer depotId, String fromDate , String toDate) {
		DepotWiseTyreReportDto reportObj= null;
		List<DepotWiseTyreReportDto> reportList = new ArrayList<>();
		
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
		List<Object[]> findAllByDepot = tyreMasterRepository.depotWiseTyreSummaryReport(depotId, startDate, endDate);

		for (Object[] ob : findAllByDepot) {
			reportObj = new DepotWiseTyreReportDto();
			if (ob[0] != null)
				reportObj.setDepotName(ob[0].toString());
			if (ob[1] != null)
				reportObj.setTyreStatus(ob[1].toString());
			if (ob[2] != null)
				reportObj.setTyreCondition(ob[2].toString());
			if (ob[3] != null)
				reportObj.setCount(ob[3].toString());
//			if (ob[4] != null)
//				reportObj.setMakae(ob[4].toString());
//			if (ob[5] != null)
//				reportObj.setTyreSize(ob[5].toString());
			reportList.add(reportObj);

		}

			return reportList;

	}

	@Override
	public List<DepotWiseTakenOffReportDto> depotWiseTakenOffReport(Integer depotId, String fromDate, String toDate) {
		List<TyreMaster> tyreMasterList = null;
		List<DepotWiseTakenOffReportDto> reportList = new ArrayList<>();
		DepotWiseTakenOffReportDto reportObj = null;
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

		List<Object[]> findAllTyreByDepotAndStatus = tyreMasterRepository.depotWiseTakenOffReport(depotId,
				startDate, endDate);

		for (Object[] ob : findAllTyreByDepotAndStatus) {
			reportObj = new DepotWiseTakenOffReportDto();
			if (ob[0] != null)
				reportObj.setTyreNo(ob[0].toString());
			if (ob[1] != null)
				reportObj.setTyreStatus(ob[1].toString());
			if (ob[2] != null)
				reportObj.setDate(ob[2].toString());
			if (ob[3] != null)
				reportObj.setTyreName(ob[3].toString());
			if (ob[4] != null)
				reportObj.setTyreSize(ob[4].toString());
			if (ob[5] != null)
				reportObj.setReason(ob[5].toString());
			if (ob[6] != null)
				reportObj.setTyreTag(ob[6].toString());

			reportList.add(reportObj);
		}

		/*
		 * tyreMasterList =
		 * tyreMasterRepository.findAllTyreByDepotAndStatus(depotCode, true);
		 * for (TyreMaster tyreMasterObj : tyreMasterList) { if
		 * (tyreMasterObj.getTyreCondition() != null &&
		 * (!tyreMasterObj.getTyreCondition().getName().equals("New"))) {
		 * reportObj = new DepotWiseTakenOffReportDto(); if (tyreMasterObj !=
		 * null && tyreMasterObj.getTyreNumber() != null)
		 * reportObj.setTyreNo(tyreMasterObj.getTyreNumber()); if (tyreMasterObj
		 * != null && tyreMasterObj.getTyreCondition() != null)
		 * reportObj.setTyreStatus(tyreMasterObj.getTyreCondition().getName());
		 * if (tyreMasterObj != null && tyreMasterObj.getTakenOffReason() !=
		 * null && tyreMasterObj.getTakenOffReason().getCreatedOn() != null)
		 * reportObj.setDate(tyreMasterObj.getTakenOffReason().getCreatedOn().
		 * toString()); if (tyreMasterObj != null &&
		 * tyreMasterObj.getTakenOffReason() != null)
		 * reportObj.setReason(tyreMasterObj.getTakenOffReason().getReasonName()
		 * ); reportList.add(reportObj); } }
		 */
		return reportList;
	}

	@Override
	public List<BusWiseTyreInventoryReportDto> busWiseTyreInventoryReport(Integer busId, String fromDate,String toDate) {

		List<BusWiseTyreInventoryReportDto> busWiseInventory = new ArrayList<>();
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
		List<Object[]> reportData = busMasterRepository.getBusTyreInventoryReport(busId, startDate , endDate);
		for (Object[] busTyreObj : reportData) {
			BusWiseTyreInventoryReportDto reportObj = new BusWiseTyreInventoryReportDto();

			if (busTyreObj[0] != null)
				reportObj.setDepotName(busTyreObj[0].toString());
			if (busTyreObj[1] != null)
				reportObj.setBusNumber(busTyreObj[1].toString());

			if (busTyreObj[2] != null)
				reportObj.setFrontRight(busTyreObj[2].toString());

			if (busTyreObj[3] != null )
				reportObj.setFrontLeft(busTyreObj[3].toString());

			if (busTyreObj[4] != null)
				reportObj.setRearRightInner(busTyreObj[4].toString());

			if (busTyreObj[5] != null)
				reportObj.setRearRightOuter(busTyreObj[5].toString());

			if (busTyreObj[6] != null)
				reportObj.setRearLeftInner(busTyreObj[6].toString());

			if (busTyreObj[7] != null)
				reportObj.setRearLeftOuter(busTyreObj[7].toString());

			if (busTyreObj[8] != null)
				reportObj.setSpareNumber(busTyreObj[8].toString());

			busWiseInventory.add(reportObj);

		}
		return busWiseInventory;
	}

	@Override
	public List<TotalBusesTyreReportByDepotDto> getTotalBusesTyreReport(Integer depotId, String fromDate,
			String toDate) {
		List<Object[]> busTyresList = null;
		List<TotalBusesTyreReportByDepotDto> reportList = new ArrayList<>();
		TotalBusesTyreReportByDepotDto reportObj = null;
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
		busTyresList = busMasterRepository.getTotalBusesTyreReport(depotId, startDate, endDate);
		for (Object[] busTyreObj : busTyresList) {
			reportObj = new TotalBusesTyreReportByDepotDto();
			if (busTyreObj[0] != null)
				reportObj.setDepotName(busTyreObj[0].toString());
			if (busTyreObj[1] != null)
				reportObj.setTotalBuses(busTyreObj[1].toString());
			if (busTyreObj[2] != null)
				reportObj.setTotalNewTyres(busTyreObj[2].toString());
			if (busTyreObj[3] != null)
				reportObj.setNewTyrePosition(busTyreObj[3].toString());
			if (busTyreObj[4] != null)
				reportObj.setFirstResole(busTyreObj[4].toString());
			if (busTyreObj[5] != null)
				reportObj.setSecondResole(busTyreObj[5].toString());
			if (busTyreObj[6] != null)
				reportObj.setThirdResole(busTyreObj[6].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<TyreTakenOffNotRefittedDto> tyreTakenOffNotRefittedReport(Integer depotId, String fromDate,
			String toDate) {
		List<Object[]> takenOffNotRefittedList = null;
		List<TyreTakenOffNotRefittedDto> reportList = new ArrayList<>();
		TyreTakenOffNotRefittedDto reportObj = null;
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
		takenOffNotRefittedList = tyreMasterRepository.tyreTakenOFFNotFittedReport(depotId, startDate, endDate);
		for (Object[] takenOffObj : takenOffNotRefittedList) {
			reportObj = new TyreTakenOffNotRefittedDto();
			if (takenOffObj[0] != null)
				reportObj.setTyreNumber(takenOffObj[0].toString());
			if (takenOffObj[1] != null)
				reportObj.setTakenOffDate(takenOffObj[1].toString());
			if (takenOffObj[2] != null)
				reportObj.setPosition(takenOffObj[2].toString());
			if (takenOffObj[3] != null)
				reportObj.setReason(takenOffObj[3].toString());
			if (takenOffObj[4] != null)
				reportObj.setTyreTag(takenOffObj[4].toString());
			if (takenOffObj[5] != null)
				reportObj.setDepot(takenOffObj[5].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<NewAndResoledTyreIssueDto> newTyreIssuedReport(Integer depotId, String fromDate, String toDate) {
		List<Object[]> newTyresList = null;
		List<NewAndResoledTyreIssueDto> reportList = new ArrayList<>();
		NewAndResoledTyreIssueDto reportObj = null;
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
		newTyresList = tyreMasterRepository.newTyreIssuedReportReport(depotId, startDate, endDate);
		for (Object[] newTyreObj : newTyresList) {
			reportObj = new NewAndResoledTyreIssueDto();
			if (newTyreObj[0] != null)
				reportObj.setTyreNumber(newTyreObj[0].toString());
			if (newTyreObj[1] != null)
				reportObj.setPosition(newTyreObj[1].toString());
			if (newTyreObj[2] != null)
				reportObj.setTyreTag(newTyreObj[2].toString());
			if (newTyreObj[3] != null)
				reportObj.setTyreSize(newTyreObj[3].toString());
			if (newTyreObj[4] != null)
				reportObj.setMake(newTyreObj[4].toString());
			if (newTyreObj[5] != null)
				reportObj.setDepot(newTyreObj[5].toString());
			if (newTyreObj[6] != null)
				reportObj.setBusNo(newTyreObj[6].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<NewAndResoledTyreIssueDto> resoleTyreIssuedReport(Integer depotId, String fromDate, String toDate) {
		List<Object[]> resolvedTyresList = null;
		List<NewAndResoledTyreIssueDto> reportList = new ArrayList<>();
		NewAndResoledTyreIssueDto reportObj = null;
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
		resolvedTyresList = tyreMasterRepository.resoleTyreIssuedReportReport(depotId, startDate, endDate);
		for (Object[] newTyreObj : resolvedTyresList) {
			reportObj = new NewAndResoledTyreIssueDto();
			if (newTyreObj[0] != null)
				reportObj.setTyreNumber(newTyreObj[0].toString());
			if (newTyreObj[1] != null)
				reportObj.setConditionName(newTyreObj[1].toString());
			if (newTyreObj[2] != null)
				reportObj.setPosition(newTyreObj[2].toString());
			if (newTyreObj[3] != null)
				reportObj.setTyreTag(newTyreObj[3].toString());
			if (newTyreObj[4] != null)
				reportObj.setTyreSize(newTyreObj[4].toString());
			if (newTyreObj[5] != null)
				reportObj.setMake(newTyreObj[5].toString());
			if (newTyreObj[6] != null)
				reportObj.setDepot(newTyreObj[6].toString());
			if (newTyreObj[7] != null)
				reportObj.setBusNo(newTyreObj[7].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<ExpectedTyreLifeReport> expectedTyreLifeReport(Integer depotId, String fromDate, String toDate) {
		List<Object[]> expectedList = null;
		List<ExpectedTyreLifeReport> reportList = new ArrayList<>();

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
		expectedList = tyreMasterRepository.expectedLifeReport(depotId, startDate, endDate);
		for (Object[] expectedObj : expectedList) {

			ExpectedTyreLifeReport reportObj = new ExpectedTyreLifeReport();
			// reportObj = new ExpectedTyreLifeReport();
			if (expectedObj[0] != null)
				reportObj.setDepotName(expectedObj[0].toString());
			if (expectedObj[1] != null)
				reportObj.setTyreTag(expectedObj[1].toString());

			if (expectedObj[2] != null)
				reportObj.setTyreNumber(expectedObj[2].toString());
			if (expectedObj[3] != null)
				reportObj.setNewTyreExpectedLife(expectedObj[3].toString());
			else
				reportObj.setNewTyreExpectedLife("No record in new Tyre Resole");
			if (expectedObj[4] != null)
				reportObj.setFirstResoleTyreLife(expectedObj[4].toString());
			else
				reportObj.setFirstResoleTyreLife("No record in  R1");
			if (expectedObj[5] != null)
				reportObj.setSecondResoleTyreLife(expectedObj[5].toString());
			else
				reportObj.setSecondResoleTyreLife("No record in  R2");
			if (expectedObj[6] != null)
				reportObj.setThirdResoleTyreLife(expectedObj[6].toString());
			else
				reportObj.setThirdResoleTyreLife("No record in  R3");
			
//			if (expectedObj[3] != null) {
//				if (expectedObj[3].toString().equals("1")) {
//					reportObj.setNewTyreExpectedLife(expectedObj[3].toString());
//				} else {
//					reportObj.setNewTyreExpectedLife("No record in new TyreResole");
//				}
//				if (expectedObj[3] != null)
//					if (expectedObj[3].toString().equals("5")) {
//						reportObj.setFirstResoleTyreLife(expectedObj[3].toString());
//					} else {
//						reportObj.setFirstResoleTyreLife("No record in  R1");
//					}
//				if (expectedObj[3] != null)
//					if (expectedObj[3].toString().equals("6")) {
//						reportObj.setSecondResoleTyreLife(expectedObj[3].toString());
//					} else {
//						reportObj.setSecondResoleTyreLife("No record in  R2");
//					}
//				if (expectedObj[3] != null)
//					if (expectedObj[3].toString().equals("7")) {
//						reportObj.setThirdResoleTyreLife(expectedObj[3].toString());
//					} else {
//						reportObj.setThirdResoleTyreLife("No record in  R3");
//					}
//
//			}

			// reportObj.setTyreConditionId(Integer.parseInt(expectedObj[3].toString()));

			reportList.add(reportObj);

			/*
			 * if (expectedObj[1] != null) { if
			 * (expectedObj[1].toString().equals("New")) {
			 * reportObj.setNewTyreExpectedLife(expectedObj[2].toString()); } if
			 * (expectedObj[1].toString().equals("R1")) {
			 * reportObj.setFirstResoleTyreLife(expectedObj[2].toString()); } if
			 * (expectedObj[1].toString().equals("R2")) {
			 * reportObj.setSecondResoleTyreLife(expectedObj[2].toString()); }
			 * if (expectedObj[1].toString().equals("R3")) {
			 * reportObj.setThirdResoleTyreLife(expectedObj[2].toString()); } }
			 */
			// reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<MileageAchievedReportDto> mileageAchievedReport(String fromDate, String toDate) {
		List<Object[]> mileageReportList = null;
		List<MileageAchievedReportDto> reportList = new ArrayList<>();
		MileageAchievedReportDto reportObj = null;
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
		mileageReportList = tyreMasterRepository.mileageAchievedReport(startDate, endDate);
		for (Object[] mileageReportObj : mileageReportList) {
			reportObj = new MileageAchievedReportDto();
			if (mileageReportObj[0] != null)
				reportObj.setDepotName(mileageReportObj[0].toString());
			if (mileageReportObj[1] != null)
				reportObj.setTyreNumber(mileageReportObj[1].toString());
			if (mileageReportObj[2] != null)
				reportObj.setExpectedLife(mileageReportObj[2].toString());
			if (mileageReportObj[3] != null)
				reportObj.setMileageAtTakenOff(mileageReportObj[3].toString());
			if (mileageReportObj[4] != null)
				reportObj.setTakenOffDate(mileageReportObj[4].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<CondemnTyreReportDto> condemnTyreReport(Integer depotId, String fromDate, String toDate) {
		List<Object[]> condemnReportList = null;
		List<CondemnTyreReportDto> reportList = new ArrayList<>();
		CondemnTyreReportDto reportObj = null;
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
		condemnReportList = tyreMasterRepository.condemnTyreReport(depotId, startDate, endDate);
		for (Object[] condemnReportObj : condemnReportList) {
			reportObj = new CondemnTyreReportDto();
			if (condemnReportObj[0] != null)
				reportObj.setDepotName(condemnReportObj[0].toString());
			if (condemnReportObj[1] != null)
				reportObj.setTyreNumber(condemnReportObj[1].toString());
			if (condemnReportObj[2] != null)
				reportObj.setMake(condemnReportObj[2].toString());
			if (condemnReportObj[3] != null)
				reportObj.setExpectedMileage(condemnReportObj[3].toString());
			if (condemnReportObj[4] != null)
				reportObj.setMileageAtTakenOff(condemnReportObj[4].toString());
			if (condemnReportObj[5] != null)
				reportObj.setHillKms(condemnReportObj[5].toString());
			if (condemnReportObj[6] != null)
				reportObj.setPlainKms(condemnReportObj[6].toString());
			reportList.add(reportObj);
		}
		return reportList;
	}

	@Override
	public List<CurrentTyreStatusReportDto> currentTyreStatusReport(Integer tyreId, String fromDate, String toDate) {
		CurrentTyreStatusReportDto obj = null;
		List<CurrentTyreStatusReportDto> list = new ArrayList<>();
		
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

		List<Object[]> currentTyreStatusReportData = tyreMasterRepository.currentTyreStatusReport(tyreId,
				startDate, endDate);

		for (Object[] ob : currentTyreStatusReportData) {
			obj = new CurrentTyreStatusReportDto();
			if (ob[0] != null)
				obj.setDepotName(ob[0].toString());
			if (ob[1] != null)
				obj.setTyreNumber(ob[1].toString());
			if (ob[2] != null)
				obj.setTyrePosition(ob[2].toString());
			if (ob[3] != null)
				obj.setTakenOff(ob[3].toString());
			if (ob[4] != null)
				obj.setCondemnation(ob[4].toString());
			if (ob[5] != null)
				obj.setRefitting(ob[5].toString());
			if (ob[6] != null)
				obj.setAtplant(ob[6].toString());
			if (ob[7] != null)
				obj.setAuctionDate(ob[7].toString());
			list.add(obj);
		}
		return list;
	}

	@Override
	public List<CompletionOfExpectedTyreReportDto> completionOfExpectedTyreReport(Integer depotId, String fromDate,
			String toDate) {

		List<CompletionOfExpectedTyreReportDto> list = new ArrayList<>();

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

		List<Object[]> completionOfExpectedTyreReport = tyreMasterRepository.completionOfExpectedTyreReport(depotId,
				startDate, endDate);

		for (Object[] ob : completionOfExpectedTyreReport) {
			CompletionOfExpectedTyreReportDto obj = new CompletionOfExpectedTyreReportDto();
			if (ob[0] != null)
				obj.setTransName(ob[0].toString());
			if (ob[1] != null)
				obj.setDepotName(ob[1].toString());
			if (ob[2] != null)
				obj.setBusNo(ob[2].toString());
			if (ob[3] != null)
				obj.setTyreTag(ob[3].toString());
			if (ob[4] != null)
				obj.setTyreNumber(ob[4].toString());
			if (ob[5] != null)
				obj.setExpectedLife(ob[5].toString());
			if (ob[6] != null)
				obj.setKmsAchieved(ob[6].toString());
			if (ob[7] != null)
				obj.setStatus(ob[7].toString());

			list.add(obj);

		}

		/*
		 * obj.setDepotName("DP-JAL-1"); obj.setDueDate("23/11/2021");
		 * obj.setExpectedLife("10"); obj.setStatus("New");
		 * obj.setTyreNumber("T-121112");
		 */

		return list;
	}

	@Override
	public List<TyreDueForInspectionDto> tyreDueForInspection(Integer depotId, String Date) {

		List<TyreDueForInspectionDto> list = new ArrayList<>();

		Date startDate = null;
		// Date endDate = null;

		try {
			if (Date != null)
				startDate = new SimpleDateFormat("yyyy-MM-dd").parse(Date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*
		 * try { if (toDate != null) endDate = new
		 * SimpleDateFormat("yyyy-MM-dd").parse(toDate); } catch (ParseException
		 * e) { e.printStackTrace(); }
		 */

		List<Object[]> tyreDueForInspection = tyreMasterRepository.tyreDueForInspection(depotId, startDate);
		for (Object[] ob : tyreDueForInspection) {
			TyreDueForInspectionDto obj = new TyreDueForInspectionDto();

			if (ob[0] != null)
				obj.setDepotName(ob[0].toString());
			if (ob[1] != null)
				obj.setTyreNumber(ob[1].toString());
			if (ob[2] != null)
				obj.setDueDate(ob[2].toString());
			if (ob[3] != null)
				obj.setExpectedLife(ob[3].toString());
			if (ob[4] != null)
				obj.setStatus(ob[4].toString());
			list.add(obj);
		}
		/*
		 * List<TyreDueForInspectionDto> list = new ArrayList<>();
		 * obj.setDepotName("DP-JAL-1"); obj.setDueDate("23/11/2021");
		 * obj.setExpectedLife("10"); obj.setStatus("New");
		 * obj.setTyreNumber("T-121112");
		 */

		return list;
	}

	@Override
	public List<TyreTakenOffBeforeExpectedLife> tyreTakenOffBeforeExpectedLife(Integer depotId, String fromDate,
			String toDate) {

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

		List<TyreTakenOffBeforeExpectedLife> list = new ArrayList<TyreTakenOffBeforeExpectedLife>();
		List<Object[]> objt = tyreMasterRepository.tyreTakenOffBeforeExpectedLife(depotId, startDate, endDate);
		for (Object[] objects : objt) {
			TyreTakenOffBeforeExpectedLife obj = new TyreTakenOffBeforeExpectedLife();
			if (objects[0] != null)
				obj.setDepotName(objects[0].toString());
			if (objects[1] != null)
				obj.setTyreTag(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreNumber(objects[2].toString());
			if (objects[3] != null)
				obj.setExpectedMileage(objects[3].toString());
			if (objects[4] != null)
				obj.setMileageAtTakenOff(objects[4].toString());
			if (objects[5] != null)
				obj.setDifference(objects[5].toString());
			list.add(obj);
		}

		return list;
	}

	@Override
	public List<NewTyreIssuedForRearReportDto> newTyreIssuedForRearReport(Integer depotId, String fromDate,
			String toDate) {
		Date startDate = null;
		Date endDate = null;
		NewTyreIssuedForRearReportDto obj = null;

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
		List<NewTyreIssuedForRearReportDto> list = new ArrayList<NewTyreIssuedForRearReportDto>();
		List<Object[]> objt = tyreMasterRepository.newTyreIssuedForRearReport(depotId, startDate, endDate);

		try {
			for (Object[] objects : objt) {

				obj = new NewTyreIssuedForRearReportDto();
				if (objects[0] != null) {

					obj.setDepotName(objects[0].toString());
				}
				if (objects[1] != null) {

					obj.setTyreNumber(objects[1].toString());
				}
				if (objects[2] != null) {

					obj.setTyrePosition(objects[2].toString());
				}
				if (objects[3] != null) {
					obj.setDateOfInstallation(objects[3].toString());
				}
				if (objects[4] != null) {
					obj.setReason(objects[4].toString());
				}
				if (objects[5] != null) {
					obj.setTyreTag(objects[5].toString());
				}
				/*
				 * Optional<NewTyreIssuedForRearReportDto> ofNullable = Optional
				 * .ofNullable(new
				 * NewTyreIssuedForRearReportDto(objects[0].toString(),
				 * objects[1].toString(), objects[2].toString(),
				 * objects[3].toString(), objects[4].toString()));
				 * 
				 * String str=objects[4].toString(); obj.setReason(str);
				 * 
				 * // obj.setTyrePosition(objects[4].toString());
				 */ list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<TyreCondemnReport> tyreCondemnReport(Integer depotId, String fromDate, String toDate) {
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
		List<Object[]> objt = tyreMasterRepository.tyreCondemnReport(depotId, startDate, endDate);
		List<TyreCondemnReport> list = new ArrayList<>();

		for (Object[] objects : objt) {
			TyreCondemnReport obj = new TyreCondemnReport();
			if (objects[0] != null)
				obj.setTyreNumber(objects[0].toString());
			if (objects[1] != null)
				obj.setTyreStatus(objects[1].toString());
			if (objects[2] != null)
				obj.setDate(objects[2].toString());
			if (objects[3] != null)
				obj.setReason(objects[3].toString());
			if (objects[4] != null)
				obj.setTyreTag(objects[4].toString());

			list.add(obj);
		}

		return list;
	}

	@Override
	public List<TyreReportInStockDto> tyreReportInStock(String fromDate, String toDate) {
		List<Object[]> stockTyreList = null;
		List<TyreReportInStockDto> reportList = new ArrayList<>();
		TyreReportInStockDto  reportObj=null;
		
		Map<Integer, String> tyreConditionIDs = new HashMap<Integer, String>();
//		
//		List<TyreConditionDto> tyreConditionList = tyreConditionRepo.findAllByStatus(true).stream()
//				.map(tyreCondition -> new TyreConditionDto(tyreCondition.getId(),tyreCondition.getName())).collect(Collectors.toList());
//		
//		for(TyreConditionDto list : tyreConditionList){
//			tyreConditionIDs.put(list.getId(), list.getName());
//		}
		
		/*tyreConditionIDs.put(1, "New");
		tyreConditionIDs.put(5, "R1");
		tyreConditionIDs.put(6, "R2");
		tyreConditionIDs.put(7, "R3");
		TyreReportInStockDto reportObj = null;*/
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
		stockTyreList = tyreMasterRepository.tyreReportInStock(startDate, endDate);
		
		for (Object[] stockTyreObj : stockTyreList) {

				reportObj = new TyreReportInStockDto();
				reportObj.setNewOrResoleTyre(stockTyreObj[0].toString());
				reportObj.setTyreNumber(stockTyreObj[1].toString());
				reportObj.setValue(stockTyreObj[2].toString());
				reportList.add(reportObj);

		}
		return reportList;
	}

	@Override
	public List<ResoleTyreMileageReport> resoleTyreMileageReport(Integer depotId, String fromDate, String toDate) {
		ResoleTyreMileageReport obj = null;
		List<ResoleTyreMileageReport> list = new ArrayList<>();
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
		List<Object[]> objec = tyreMasterRepository.resoleTyreMileageReport(depotId, startDate, endDate);
		for (Object[] objects : objec) {
			obj = new ResoleTyreMileageReport();
			if(objects[0]!=null)
			obj.setDepotName(objects[0].toString());
			if(objects[1]!=null)
			obj.setTyreTag(objects[1].toString());
			if(objects[2]!=null)
			obj.setTyreNumber(objects[2].toString());
			if(objects[3]!=null)
			obj.setExpectedMileage(objects[3].toString());
			if(objects[4]!=null)
			obj.setKmsAchieved(objects[4].toString());
//			if(objects[5]!=null)
//			obj.setGpsMileage(objects[5].toString());

			list.add(obj);
		}

		return list;
	}

	@Override
	public List<TyreSentForRetreadingReport> tyreSentForRetreadingReport(Integer depotId, String fromDate,
			String toDate) {
		// TyreSentForRetreadingReport obj = new TyreSentForRetreadingReport();
		List<TyreSentForRetreadingReport> list = new ArrayList<>();
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
		List<Object[]> object = tyreMasterRepository.tyreSentForRetreadingReport(depotId, startDate, endDate);
		for (Object[] objects : object) {

			TyreSentForRetreadingReport obj = new TyreSentForRetreadingReport();
			if(objects[0]!=null)
			obj.setDepotName(objects[0].toString());
			if(objects[1]!=null)
			obj.setTyreNumber(objects[1].toString());
			if(objects[2]!=null)
			obj.setTakenOffDate(objects[2].toString());
			if(objects[3]!=null)
				obj.setSentDate(objects[3].toString());
			if(objects[4]!=null)
				obj.setTyreTag(objects[4].toString());
			list.add(obj);
		}

		return list;
	}

	@Override
	public List<TyreNotSentForRetreadingReport> tyreNotSentForRetreadingReport(Integer depotId, String fromDate,
			String toDate) {

		List<TyreNotSentForRetreadingReport> list = new ArrayList<>();

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

		List<Object[]> tyreNotSentForRetreadingReport = tyreMasterRepository.tyreNotSentForRetreadingReport(depotId,
				startDate, endDate);

		for (Object[] ob : tyreNotSentForRetreadingReport) {
			TyreNotSentForRetreadingReport obj = new TyreNotSentForRetreadingReport();
			if (ob[0] != null)
				obj.setDepotName(ob[0].toString());
			if (ob[1] != null)
				obj.setTyreNumber(ob[1].toString());
			if (ob[2] != null)
				obj.setTakenOffDate(ob[2].toString());
			if (ob[3] != null)
				obj.setStatus(ob[3].toString());
			if (ob[4] != null)
				obj.setRecievedDate(ob[4].toString());
			if (ob[5] != null)
				obj.setTyreTag(ob[5].toString());
			if (ob[6] != null)
				obj.setDocketNumber(ob[6].toString());
			list.add(obj);

		}

		/*
		 * obj.setDepotName("DP-JAL-1"); obj.setTyreNumber("T-121112");
		 * obj.setTakenOffDate("23/11/2021");
		 * obj.setSinceRecieved("30/11/2021"); obj.setStatus("Resole2");
		 */

		return list;
	}

	@Override
	public List<CountOfRetreadingTyresReport> countOfRetreadingTyresReport(Integer depotId, String fromDate,
			String toDate) {
		CountOfRetreadingTyresReport obj = null;
		List<CountOfRetreadingTyresReport> list = new ArrayList<>();
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
		List<Object[]> object = tyreMasterRepository.countOfRetreadingTyresReport(depotId, startDate, endDate);
		for (Object[] objects : object) {
			obj = new CountOfRetreadingTyresReport();
			if (objects[0] != null)
				obj.setDepotName(objects[0].toString());
			if (objects[1] != null)
				obj.setTotal(objects[1].toString());
			if (objects[2] != null)
				obj.setFirstRetreading(objects[2].toString());
			if (objects[3] != null)
				obj.setSecondRetreading(objects[3].toString());
			if (objects[4] != null)
				obj.setThirdRetreading(objects[4].toString());
			obj.setToDate(toDate);

			list.add(obj);
		}

		return list;
	}

	@Override
	public List<TyreBifurcationReportDto> tyreBifurcationReport(Integer depotId, String fromDate, String toDate) {
		TyreBifurcationReportDto obj = new TyreBifurcationReportDto();
		List<TyreBifurcationReportDto> list = new ArrayList<>();
		return list;
	}

	@Override
	public List<TyreAuctionReportDto> tyresAuctionReport(String fromDate, String toDate) {

		List<TyreAuctionReportDto> list = new ArrayList<>();
		TyreAuctionReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.tyreAuctionReport(startDate, endDate);
		for (Object[] objects : object) {
			 obj = new TyreAuctionReportDto();
			if (objects[0] != null)
				obj.setDocketNumber(objects[0].toString());
			if (objects[1] != null)
				obj.setCount(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreSize(objects[2].toString());
			if (objects[3] != null)
				obj.setAuctionDate(objects[3].toString());
			if (objects[4] != null)
				obj.setBidderName(objects[4].toString());
			if (objects[5] != null)
				obj.setAmount(objects[5].toString());
			 			
			list.add(obj);
		}
		return list;
	
	}

	@Override
	public List<TyresLyingForAuctionReport> tyresLyingForAuctionReport(Integer depotId, String fromDate,
			String toDate) {

		List<TyresLyingForAuctionReport> list = new ArrayList<>();
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
		List<Object[]> object = tyreMasterRepository.tyresLyingForAuctionReport(depotId, startDate, endDate);
		for (Object[] objects : object) {
			TyresLyingForAuctionReport obj = new TyresLyingForAuctionReport();
			if(objects[0]!=null)
			obj.setDepotName(objects[0].toString());
//			if(objects[1]!=null)
//			obj.setTyreCount(objects[1].toString());
			if(objects[1]!=null)
			obj.setTyreNumber(objects[1].toString());
			if(objects[2]!=null)
			obj.setMake(objects[2].toString());
			if(objects[3]!=null)
			obj.setCondemnDate(objects[3].toString());
			// obj.setTotal(objects[4].toString());
			list.add(obj);
		}
		return list;
	}

	@Override
	public List<NewTyreAndResoleTyreQuantityReportDto> newTyreAndResoleTyreQuantityReport(String fromDate,
			String toDate) {
		List<NewTyreAndResoleTyreQuantityReportDto> list = new ArrayList<>();
		NewTyreAndResoleTyreQuantityReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.newTyreAndResoleTyreQuantityReport(startDate, endDate);
		for (Object[] objects : object) {
			 obj = new NewTyreAndResoleTyreQuantityReportDto();
			 if(objects[0]!=null)
			obj.setTyreType(objects[0].toString());
			 
			obj.setToDate(toDate);
			
			obj.setFromDate(fromDate);
			
			 if(objects[3]!=null)
			obj.setCount(objects[3].toString());
			 if(objects[4]!=null)
			obj.setValueInRs(objects[4].toString());
			
			list.add(obj);
		}
		return list;
	}
	
	public TyreIssueSlipListDto getTyreChangeReplacementData(Integer id) {
		TyreIssueSlipListDto output = new TyreIssueSlipListDto();
		List<TyreIssueSlipDto> t2 = new ArrayList<>();
		List<TyreIssueSlipDto> t = new ArrayList<>();
		try {
			List<BusTyreAssociation> assosiation = new ArrayList<>();
			assosiation = busTyreAssociationRepository.findByBusId(id);
			for (BusTyreAssociation repacementRecord : assosiation) {
				TyreIssueSlipDto afterTyreData = new TyreIssueSlipDto();
				afterTyreData.setTyreNumber(repacementRecord.getTyre().getTyreNumber());
				afterTyreData.setTyreDepo(repacementRecord.getTyre().getDepot().getDepotName());
				afterTyreData.setTyreManufacturer(repacementRecord.getTyre().getTyreMake().getName());
				afterTyreData
						.setTyrePosition(repacementRecord != null && repacementRecord.getTyrePosition() != null
								? repacementRecord.getTyrePosition().getName() : "  ");
				afterTyreData.setTyreSize(repacementRecord.getTyre().getTyreSize().getSize());
				afterTyreData.setTyreTag(tyreManagementUtility.concatConditionWithTag(repacementRecord.getTyre().getId()));
				afterTyreData.setTyreCondition(repacementRecord.getTyreCondition().getName());
				if(repacementRecord.getTyreCondition().getName().equals("New")) {
					afterTyreData.setExpectedMileage(repacementRecord.getTyre().getExpectedLife());
				} else {
					DocketTyreAssociation docket = docketTyreAssoRepo.findObjectByTyreIdAndCondition(id, repacementRecord.getTyreCondition().getName());
					afterTyreData.setExpectedMileage(docket!=null && docket.getExpectedLife()!=null ? docket.getExpectedLife().toString() :" ");
				}
				afterTyreData.setActualTyreMileage(tyreCostUtility.calculateTyreKmsByCondition(repacementRecord.getTyre().getId()));
				t2.add(afterTyreData);
			}
			List<BusTyreAssociationHistory> assosiationHistory = new ArrayList<>();
			assosiationHistory = busTyreAssoHisRepo.findByBusId(id);
			for (BusTyreAssociationHistory his : assosiationHistory) {
				TyreIssueSlipDto beforeTyreData = new TyreIssueSlipDto();
				beforeTyreData.setTyreNumber(his!=null && his.getTyre()!=null ?his.getTyre().getTyreNumber()  :" ");
				beforeTyreData.setTyreDepo(his.getTyre().getDepot().getDepotName());
				beforeTyreData.setTyreManufacturer(his.getTyre().getTyreMake().getName());
				beforeTyreData.setTyrePosition(
						his != null && his.getTyrePosition() != null ? his.getTyrePosition().getName() : "  ");
				beforeTyreData.setTyreSize(his.getTyre().getTyreSize().getSize());
				beforeTyreData.setTyreTag(tyreManagementUtility.concatConditionWithTag(his.getTyre().getId()));
				beforeTyreData.setTyreCondition(his.getTyreCondition().getName());
				if(his.getTyreCondition().getName().equals("New")) {
					beforeTyreData.setExpectedMileage(his.getTyre().getExpectedLife());
				} else {
					DocketTyreAssociation docket = docketTyreAssoRepo.findObjectByTyreIdAndCondition(id, his.getTyreCondition().getName());
					beforeTyreData.setExpectedMileage(docket!=null && docket.getExpectedLife()!=null ? docket.getExpectedLife().toString() :" ");
				}
				
				t.add(beforeTyreData);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		output.setTyreReplacement(t);
		output.setBeforeTyreChange(t2);
		return output;
	}

	@Override
	public List<TyreNotAuctionReportDto> tyreNotAuctionReport(Integer depotId, String fromDate, String toDate) {
		List<TyreNotAuctionReportDto> list = new ArrayList<>();
		TyreNotAuctionReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.tyreNotAuctionReport(depotId,startDate, endDate);
		for (Object[] objects : object) {
			 obj = new TyreNotAuctionReportDto();
			if (objects[0] != null)
				obj.setMake(objects[0].toString());
			if (objects[1] != null)
				obj.setTyreType(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreSize(objects[2].toString());
			if (objects[3] != null)
				obj.setTyreNumber(objects[3].toString());
			if (objects[4] != null)
				obj.setTakenOffDate(objects[4].toString());
			if (objects[5] != null)
				obj.setReason(objects[5].toString());
					
			list.add(obj);
		}
		
		return list;
	}

	@Override
	public List<NewTyreAndResoleTyreMileageReportDto> newTyreAndResoleTyreMileageReport(String fromDate,
			String toDate) {
		List<NewTyreAndResoleTyreMileageReportDto> list = new ArrayList<>();
		NewTyreAndResoleTyreMileageReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.newTyreAndResoleTyreMileageReport(startDate, endDate);
		for (Object[] objects : object) {
			obj = new NewTyreAndResoleTyreMileageReportDto();
			if (objects[0] != null)
				obj.setTyreTag(objects[0].toString());
			if (objects[1] != null)
				obj.setTyreNumber(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreCondition(objects[2].toString());
			//if (objects[3] != null)
				obj.setFromDate(fromDate);
			//if (objects[4] != null)
				obj.setToDate(toDate);
			if (objects[5] != null)
				obj.setKmsDone(objects[5].toString());
			list.add(obj);
		}
		
		return list;
	}

	@Override
	public List<MakeWiseComparisonReportDto> makeWiseComparisonReport(Integer depotId, Integer tyreSizeId,
			String fromDate, String toDate,Integer makeId) {
		List<MakeWiseComparisonReportDto> list = new ArrayList<>();
		MakeWiseComparisonReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.makeWiseComparisonReport(depotId,tyreSizeId,startDate, endDate,makeId);
		for (Object[] objects : object) {
			 obj = new MakeWiseComparisonReportDto();
			if (objects[0] != null)
				obj.setDepotName(objects[0].toString());
			if (objects[1] != null)
				obj.setSize(objects[1].toString());
			if (objects[2] != null)
				obj.setMake(objects[2].toString());
			if (objects[3] != null)
				obj.setTyreType(objects[3].toString());
			if (objects[4] != null)
				obj.setAvgKMs(objects[4].toString());
			if (objects[5] != null)
				obj.setCostKMs(objects[5].toString());
			
			list.add(obj);
		}
		return list;
	}

	@Override
	public List<TyreCurrentMileageReportDto> tyreCurrentMileageReport(String fromDate, String toDate) {
		List<TyreCurrentMileageReportDto> list = new ArrayList<>();
		TyreCurrentMileageReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.tyreCurrentMileageReport(startDate, endDate);
		for (Object[] objects : object) {
			obj = new TyreCurrentMileageReportDto();
			if (objects[0] != null)
				obj.setTyreTag(objects[0].toString());
			if (objects[1] != null)
				obj.setTyreNumber(objects[1].toString());
			if (objects[2] != null)
				obj.setTyrePosition(objects[2].toString());
			if (objects[3] != null)
				obj.setTyreCondition(objects[3].toString());
			if (objects[4] != null)
				obj.setExpectedMileage(objects[4].toString());
			if (objects[5] != null)
				obj.setAchievedKMs(objects[5].toString());
			if (objects[6] != null)
				obj.setBusNumber(objects[6].toString());
			if (objects[7] != null)
				obj.setAchievedKMsDate(objects[7].toString());
			
			list.add(obj);
		}
		return list;
	}

	@Override
	public List<TyreDueForRetredingReportDto> tyreDueForRetredingReport(Integer depotId, String fromDate,
			String toDate) {
		List<TyreDueForRetredingReportDto> list = new ArrayList<>();
		TyreDueForRetredingReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.tyreDueForRetredingReport(depotId,startDate, endDate);
		for (Object[] objects : object) {
			obj = new TyreDueForRetredingReportDto();
			if (objects[0] != null)
				obj.setTransName(objects[0].toString());
			if (objects[1] != null)
				obj.setDepotName(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreTag(objects[2].toString());
			if (objects[3] != null)
				obj.setTyreNumber(objects[3].toString());
			if (objects[4] != null)
				obj.setTyreManufacture(objects[4].toString());
			if (objects[5] != null)
				obj.setSize(objects[5].toString());
			if (objects[6] != null)
				obj.setTyrePosition(objects[6].toString());
			if (objects[7] != null)
				obj.setTyreRecoveredCost(objects[7].toString());
			if (objects[8] != null)
				obj.setTyreInstallationDate(objects[8].toString());
			if (objects[9] != null)
				obj.setExpectedLife(objects[9].toString());
			if (objects[10] != null)
				obj.setTotalKmsDone(objects[10].toString());
			if (objects[11] != null)
				obj.setExtraKmsDone(objects[11].toString());
			if (objects[12] != null)
				obj.setDriverName(objects[12].toString());
			if (objects[13] != null)
				obj.setBusNo(objects[13].toString());
			if (objects[14] != null)
				obj.setRouteName(objects[14].toString());
			if (objects[15] != null)
				obj.setRetreadingDuedate(objects[15].toString());
			list.add(obj);
		}
		return list;

	}

	@Override
	public List<TyreNotTakenOffAfterCompletedMileageReportDto> tyreNotTakenOffAfterCompletedMileageReport(Integer depotId, String fromDate, String toDate) {
		List<TyreNotTakenOffAfterCompletedMileageReportDto> list = new ArrayList<>();
		TyreNotTakenOffAfterCompletedMileageReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.tyreNotTakenOffAfterCompletedMileageReport(depotId,startDate, endDate);
		for (Object[] objects : object) {
			obj = new TyreNotTakenOffAfterCompletedMileageReportDto();
			if (objects[0] != null)
				obj.setTransName(objects[0].toString());
			if (objects[1] != null)
				obj.setDepotName(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreTag(objects[2].toString());
			if (objects[3] != null)
				obj.setTyreNumber(objects[3].toString());
			if (objects[4] != null)
				obj.setTyreManufacture(objects[4].toString());
			if (objects[5] != null)
				obj.setSize(objects[5].toString());
			if (objects[6] != null)
				obj.setTyreCost(objects[6].toString());
			if (objects[7] != null)
				obj.setTyreRecoveredCost(objects[7].toString());
			if (objects[8] != null)
				obj.setTyreInstallationDate(objects[8].toString());
			if (objects[9] != null)
				obj.setKms(objects[9].toString());
			if (objects[10] != null)
				obj.setTotalKmsDone(objects[10].toString());
			list.add(obj);
		}
		return list;
	}

	@Override
	public List<TyreNotTakenOffAfterCompletedMileageReportDto> condemnMileageReport(Integer depotId, String fromDate, String toDate) {
		List<TyreNotTakenOffAfterCompletedMileageReportDto> list = new ArrayList<>();
		TyreNotTakenOffAfterCompletedMileageReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.condemnMileageReport(depotId,startDate, endDate);
		for (Object[] objects : object) {
			obj = new TyreNotTakenOffAfterCompletedMileageReportDto();
			if (objects[0] != null)
				obj.setTransName(objects[0].toString());
			if (objects[1] != null)
				obj.setDepotName(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreTag(objects[2].toString());
			if (objects[3] != null)
				obj.setTyreNumber(objects[3].toString());
			if (objects[4] != null)
				obj.setTyreManufacture(objects[4].toString());
			if (objects[5] != null)
				obj.setSize(objects[5].toString());
			if (objects[6] != null)
				obj.setTyreCost(objects[6].toString());
			if (objects[7] != null)
				obj.setTyreRecoveredCost(objects[7].toString());
			if (objects[8] != null)
				obj.setTyreInstallationDate(objects[8].toString());
			if (objects[9] != null)
				obj.setKms(objects[9].toString());
			if (objects[10] != null)
				obj.setTotalKmsDone(objects[10].toString());
			list.add(obj);
		}
		return list;

	}

	@Override
	public List<DepotWiseTyreConsumptionReportDto> depotWiseTyreConsumptionReport(Integer depotId, String fromDate,
			String toDate) {
		List<DepotWiseTyreConsumptionReportDto> list = new ArrayList<>();
		DepotWiseTyreConsumptionReportDto obj=null;
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
		List<Object[]> object = tyreMasterRepository.depotWiseTyreConsumptionReport(depotId,startDate, endDate);
		for (Object[] objects : object) {
			obj = new DepotWiseTyreConsumptionReportDto();
			if (objects[0] != null)
				obj.setTransName(objects[0].toString());
			if (objects[1] != null)
				obj.setDepotName(objects[1].toString());
			if (objects[2] != null)
				obj.setTyreTag(objects[2].toString());
			if (objects[3] != null)
				obj.setTyreNumber(objects[3].toString());
			if (objects[4] != null)
				obj.setTyreManufacture(objects[4].toString());
			if (objects[5] != null)
				obj.setSize(objects[5].toString());
			if (objects[6] != null)
				obj.setTyreType(objects[6].toString());
			if (objects[7] != null)
				obj.setTyreCondition(objects[7].toString());
			if (objects[8] != null)
				obj.setTyreCost(objects[8].toString());
			if (objects[9] != null)
				obj.setTyreRecoveredCost(objects[9].toString());
			if (objects[10] != null)
				obj.setTyreInstallationDate(objects[10].toString());
			if (objects[11] != null)
				obj.setKms(objects[11].toString());
			if (objects[12] != null)
				obj.setHillKms(objects[12].toString());
			if (objects[13] != null)
				obj.setPlainKms(objects[13].toString());
			if (objects[14] != null)
				obj.setTotalKmsDone(objects[14].toString());
			list.add(obj);
		}
		return list;
	}

	@Override
	public List<TyreMakerDto> tyreMakerList() {
		List<TyreMakerDto> tyreMakerList = tyreMakerRepo.findAllByStatus(true).stream()
				.map(tyreMaker -> new TyreMakerDto(tyreMaker.getId(),tyreMaker.getName())).collect(Collectors.toList());
		return tyreMakerList;
	}
	
	
	
}
