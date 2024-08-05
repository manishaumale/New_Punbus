package com.idms.base.api.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.idms.base.service.TankAndCapacityService;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/tankcapacity")
@Log4j2
public class TankAndCapacityReportController {
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private TankAndCapacityService service;
	
	@ApiOperation("Return List of Depot ")
	@GetMapping(path = "/listOfDepot")
	public List<DepotMasterDto> listOfDepot() {
		log.info("Return List of Depot");
		List<DepotMasterDto> depot = this.service.findAllDepot();
		return depot;
	}
	
	@ApiOperation("Returns Total Tank Capacity Report")
	@GetMapping(path = "/listOfTankCapacityReport/{dpCode}/{date}")
	public List<TotalTankAndCapacityDto> listOfTankCapacityReport(@PathVariable("dpCode") Integer dpCode,@PathVariable("date") String date) {
		log.info("Return List of Tank Capacity Report");
		Date startDate = null;
		/*Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		try {
			if(date != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<TotalTankAndCapacityDto> totalTankAndCapacityDto = this.service.listOfTankCapacityReport(dpCode,startDate);
		return totalTankAndCapacityDto;
		

	}
	
	
	@ApiOperation("Returns List of  explosive License renewal Report  ")
	@GetMapping(path = "/listOfexplosiveReport/{dpCode}/{date}")
	public List<ExplosiveLicenseReportDto> listOfexplosiveReport(@PathVariable("dpCode") Integer dpCode,@PathVariable("date") String date) {
		log.info("Returns List of  explosive License renewal Report");
		Date startDate = null;
		//Date endDate = null;
		try {
			if(date != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		List<ExplosiveLicenseReportDto> explosiveLicenseReportDto = this.service.listOfexplosiveReport(dpCode,startDate);
		return explosiveLicenseReportDto;

	}
	
	@ApiOperation("Returns List of   Dispensing Unit and Type Report ")
	@GetMapping(path = "/listOfDispensingReport/{dpCode}")
	public List<DispensingUnitReportDto> listOfDispensingReport(@PathVariable("dpCode") Integer dpCode) {
		log.info("Returns List of   Dispensing Unit and Type Report");
		/*Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		List<DispensingUnitReportDto> dispensingUnitReportDto = this.service.listOfDispensingReport(dpCode);
		return dispensingUnitReportDto;

	}
	
	@ApiOperation("Returns List of Fuel Dispenser wise Fuel Issuance Report  ")
	@GetMapping(path = "/listOfFuelDispenserReport/{fromDate}/{toDate}/{dpCode}")
	public List<FuelDispenserWiseReport> listOfFuelDispenserReport( @PathVariable("dpCode") Integer dpCode,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Return List of Tank Capacity Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<FuelDispenserWiseReport> fuelDispenserWiseReport = this.service.listOfFuelDispenserReport(dpCode,startDate,endDate);
		return fuelDispenserWiseReport;

	}
	
	@ApiOperation("Returns List of KMPL Comparison Report Scheduled KMs Vs GPS  Report ")
	@GetMapping(path = "/listOfKMPLComparisonReport/{fromDate}/{toDate}/{routeId}")
	public List<KmplComparisonReportDto> listOfKMPLComparisonReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("routeId") Integer routeId) {
		log.info("Returns List of KMPL Comparison Report Scheduled KMs Vs GPS  Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<KmplComparisonReportDto> kmplComparisonReportDto = this.service.listOfKMPLComparisonReport(startDate,endDate,routeId);
		return kmplComparisonReportDto;

	}
	
	/**
	 * 7.Supply Received Report
	 * @param fromDate
	 * @param toDate
	 * @param route
	 * @return List<SupplyReceivedReportDto>
	 */
	@ApiOperation("Supply Received Report ")
	@GetMapping(path = "/supplyReceivedReport/{depoId}/{fromDate}/{toDate}")
	public List<SupplyReceivedReportDto> supplyReceivedReport(@PathVariable("depoId") Integer depoId,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Returns List supplyReceivedReport");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<SupplyReceivedReportDto> listOfSupplyRecieved = this.service.supplyReceivedReport(depoId,startDate,endDate);
		return listOfSupplyRecieved;

	}
	
	/**
	 * 11.BusWise-Driver,Route KMPL
	 * @param fromDate
	 * @param toDate
	 * @param busNo
	 * @return List<BusWiseDriverRouteKmplDto>
	 */
	@ApiOperation("BusWise-Driver,Route KMPL ")
	@GetMapping(path = "/busWiseDriverRouteKmpl/{busNo}/{fromDate}/{toDate}")
	public List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmpl(@PathVariable("busNo") String busNo,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Returns List busWiseDriverRouteKmpl");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusWiseDriverRouteKmplDto> listBusWiseDriverRouteKmpl = this.service.busWiseDriverRouteKmpl(busNo,startDate,endDate);
		return listBusWiseDriverRouteKmpl;

	}
	
	/**
	 * 13.Route Wise-Bus,Driver KMPL
	 * @param fromDate
	 * @param toDate
	 * @param driver
	 * @return List<BusWiseDriverRouteKmplDto>
	 */
	@ApiOperation("Route Wise-Bus,Driver KMPL ")
	@GetMapping(path = "/routeWiseBusDriverKmpl/{fromDate}/{toDate}/{driverId}")
	public List<BusWiseDriverRouteKmplDto> routeWiseBusDriverKmpl(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("driverId") Integer driverId) {
		log.info("Returns List routeWiseBusDriverKmpl");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusWiseDriverRouteKmplDto> listrouteWiseBusDriverKmpl = this.service.routeWiseBusDriverKmpl(startDate,endDate,driverId);
		return listrouteWiseBusDriverKmpl;

	}
	
	/**
	 * 14.Bus Type/Bus Wise Diesel Issuance Report
	 * @param fromDate
	 * @param toDate
	 * @param bus
	 * @return List<BusWiseDriverRouteKmplDto>
	 */
	@ApiOperation("Bus Type/Bus Wise Diesel Issuance Report ")
	@GetMapping(path = "/busTypeBusWiseDieselIssuanceReport/{bus}/{fromDate}/{toDate}/{busType}")
	public List<BusWiseDriverRouteKmplDto> busTypeBusWiseDieselIssuanceReport(@PathVariable("bus") String bus,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("busType")String busType) {
		log.info("Returns List busTypeBusWiseDieselIssuanceReport");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusWiseDriverRouteKmplDto> listbusTypeBusWiseDieselIssuanceReport = this.service.busTypeBusWiseDieselIssuanceReport(bus,startDate,endDate,busType);
		return listbusTypeBusWiseDieselIssuanceReport;

	}
	/**
	 * 20.Issuance & Receipt Statement Report
	 * @param fromDate
	 * @param toDate
	 * @return List<IssuanceReceiptStmntReportDto>
	 */
	@ApiOperation("Issuance & Receipt Statement Report ")
	@GetMapping(path = "/issuanceReceiptStmntReport/{fromDate}/{toDate}")
	public List<IssuanceReceiptStmntReportDto> issuanceReceiptStmntReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Returns List issuanceReceiptStmntReport");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<IssuanceReceiptStmntReportDto> listIssuanceReceiptStmntReport = this.service.issuanceReceiptStmntReport(startDate,endDate);
		return listIssuanceReceiptStmntReport;

	}
	/**
	 * 21.Dead KMS Report 
	 * @param fromDate
	 * @param toDate
	 * @param route
	 * @return List<IssuanceReceiptStmntReportDto>
	 */
	@ApiOperation("Dead KMS Report  ")
	@GetMapping(path = "/deadKMSReport/{routeId}/{date}")
	public List<IssuanceReceiptStmntReportDto> deadKMSReport(@PathVariable("routeId") Integer routeId,@PathVariable("date") String date) {
		log.info("Returns List deadKMSReport");
		/*Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		List<IssuanceReceiptStmntReportDto> listdeadKMSReport = this.service.deadKMSReport(routeId,date);
		return listdeadKMSReport;

	}
	
	/**
	 * 22.Best and Worst Driver Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @return List<BusWiseDriverRouteKmplDto>
	 */
	@ApiOperation("Best and Worst Driver Report ")
	@GetMapping(path = "/bestWorstDriverReport/{fromDate}/{toDate}/{depoId}/{startRange}/{endRange}")
	public List<BusWiseDriverRouteKmplDto> bestWorstDriverReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("depoId") Integer depoId,@PathVariable("startRange") Double startRange,@PathVariable("endRange") Double endRange) {
		log.info("Returns List bestWorstDriverReport");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusWiseDriverRouteKmplDto> listbestWorstDriverReport = this.service.bestWorstDriverReport(startDate,endDate,depoId,startRange,endRange);
		return listbestWorstDriverReport;

	}
	
	/**
	 * 3.Due Date Cleaning Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @return List<DueDateCleaningReportDto>
	 */
	@ApiOperation("Due Date Cleaning Report ")
	@GetMapping(path = "/dueDateCleaningReport/{depoId}/{date}")
	public List<DueDateCleaningReportDto> dueDateCleaningReport(@PathVariable("depoId") Integer depoId,@PathVariable("date") String date) {
		log.info("Returns List Due Date Cleaning Report");
		Date startDate = null;
		/*Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		try {
			if(date != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DueDateCleaningReportDto> listdueDateCleaningReportDto = this.service.dueDateCleaningReport(depoId,startDate);
		return listdueDateCleaningReportDto;

	}
	
	/**
	 * 5.Inspection Due Date Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @return List<InspectionDueDateReportDto>
	 */
	@ApiOperation("Inspection Due Date Report")
	@GetMapping(path = "/inspectionDueDateReport/{depoId}/{date}")
	public List<InspectionDueDateReportDto> inspectionDueDateReport(@PathVariable("depoId") Integer depoId,@PathVariable("date") String date) {
		log.info("Returns List Inspection Due Date Report");
		Date startDate = null;
		//Date endDate = null;
		try {
			if(date != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		List<InspectionDueDateReportDto> listinspectionDueDateReportDto = this.service.inspectionDueDateReport(depoId,startDate);
		return listinspectionDueDateReportDto;

	}
	
	
	/**
	 * 8.Depot Wise Diesel Stock Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @param makeWise
	 * @return List<InspectionDueDateReportDto>
	 */
	@ApiOperation("Depot Wise Diesel Stock Report")
	@GetMapping(path = "/depotWiseDieselStockReport/{depoId}/{fromDate}/{toDate}/{fuelTypeId}")
	public List<DepotWiseDieselStockReportDto> depotWiseDieselStockReport(@PathVariable("depoId") Integer depoId,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("fuelTypeId") Integer fuelTypeId) {
		log.info("Returns List Depot Wise Diesel Stock Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DepotWiseDieselStockReportDto> listdepotWiseDieselStockReportDto = this.service.depotWiseDieselStockReport(depoId,startDate,endDate,fuelTypeId);
		return listdepotWiseDieselStockReportDto;

	}
	
	/**
	 * 9.Advance Payment Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @return List<DepotWiseDieselStockReportDto>
	 */
	@ApiOperation("Advance Payment Report")
	@GetMapping(path = "/advancePaymentReport/{fromDate}/{toDate}/{depoId}")
	public List<DepotWiseDieselStockReportDto> advancePaymentReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("depoId") Integer depoId) {
		log.info("Returns List Advance Payment Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DepotWiseDieselStockReportDto> listadvancePaymentReport = this.service.advancePaymentReport(startDate,endDate,depoId);
		return listadvancePaymentReport;

	}
	
	/**
	 * 10.KMPL Report
	 * @param fromDate
	 * @param toDate
	 * @param busType
	 * @param grouping
	 * @param depoId
	 * @param select
	 * @return List<KMPLReportDto>
	 */
	@ApiOperation("KMPL Report")
	@GetMapping(path = "/kmplReport/{fromDate}/{toDate}/{busType}/{grouping}/{depoId}/{select}")
	public List<KMPLReportDto> kmplReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("busType") Integer busType,@PathVariable("grouping") String grouping,
			@PathVariable("depoId") Integer depoId,@PathVariable("select") String select) {
		log.info("Returns List KMPL Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(grouping.equalsIgnoreCase("Date") && fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
			else
				startDate = new Date();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(grouping.equalsIgnoreCase("Date") && toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
			else
				endDate = new Date();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<KMPLReportDto> listKmplReportDto = this.service.kmplReport(startDate,endDate,busType,grouping,depoId,select);
		return listKmplReportDto;

	}
	
	
	/**
	 * 12.Driver Wise- Bus, Route KMPL Report
	 * @param fromDate
	 * @param toDate
	 * @param driver
	 * @param bus
	 * @param route
	 * @return List<DriverWiseBusRouteKMPLDto>
	 */
	@ApiOperation("Driver Wise- Bus, Route KMPL Report")
	@GetMapping(path = "/driverWiseBusRouteKMPL/{driverId}/{routeId}/{fromDate}/{toDate}")
	public List<DriverWiseBusRouteKMPLDto> driverWiseBusRouteKMPL(@PathVariable("driverId") Integer driverId,
			@PathVariable("routeId") Integer routeId,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Returns List Driver Wise- Bus, Route KMPL");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DriverWiseBusRouteKMPLDto> listDriverWiseBusRouteKMPLDto = this.service.driverWiseBusRouteKMPL(driverId,routeId,startDate,endDate);
		return listDriverWiseBusRouteKMPLDto;

	}
	
	/**
	 * 15.Bus Type Bus Wise Diesel Mobile oil Report
	 * @param fromDate
	 * @param toDate
	 * @param busType
	 * @return List<BusWiseDriverRouteKmplDto>
	 */
	@ApiOperation("Bus Type Bus Wise Diesel Mobile oil Report")
	@GetMapping(path = "/busTypeBusWiseDieselMobileReport/{busNo}/{fromDate}/{toDate}/{busType}")
	public List<BusWiseDriverRouteKmplDto> busTypeBusWiseDieselMobileReport(@PathVariable("busNo") String busNo,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("busType") Integer busType ) {
		log.info("Returns List busTypeBusWiseDieselMobileReport");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusWiseDriverRouteKmplDto> listBusWiseDriverRouteKmplDto = this.service.busTypeBusWiseDieselMobileReport(busNo,startDate,endDate,busType);
		return listBusWiseDriverRouteKmplDto;

	}
	/**
	 * 16.Variation Beyond 4% Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @return List<VariationBeyondReportDto>
	 */
	@ApiOperation("Variation Beyond 4% Report")
	@GetMapping(path = "/variationBeyondReport/{depoId}/{fromDate}/{toDate}")
	public List<VariationBeyondReportDto> variationBeyondReport(@PathVariable("depoId") Integer depoId,@PathVariable("fromDate") String fromDate,@PathVariable("toDate") String toDate) {
		log.info("Returns List Variation Beyond 4% Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<VariationBeyondReportDto> listVariationBeyondReportDto = this.service.variationBeyondReport(depoId,startDate,endDate);
		return listVariationBeyondReportDto;

	}
	/**
	 * 17.Inspection Carried Out Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @return List<InspectionCarriedOutReportDto> 
	 */
	@ApiOperation("Inspection Carried Out Report ")
	@GetMapping(path = "/inspectionCarriedOutReport/{depoId}/{fromDate}/{toDate}")
	public List<InspectionCarriedOutReportDto> inspectionCarriedOutReport(@PathVariable("depoId") Integer depoId,@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Returns List Inspection Carried Out Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<InspectionCarriedOutReportDto> listInspectionCarriedOutReportDto= this.service.inspectionCarriedOutReport(depoId,startDate,endDate);
		return listInspectionCarriedOutReportDto;

	}
	/**
	 * 18.Gross KMS Deposited with Cashier Vs. DPA Report
	 * @param fromDate
	 * @param toDate
	 * @return List<IssuanceReceiptStmntReportDto>
	 */
	@ApiOperation("Gross KMS Deposited with Cashier Vs. DPA Report")
	@GetMapping(path = "/grossKMSDepositedCashierVsDpaReport/{fromDate}/{toDate}")
	public List<IssuanceReceiptStmntReportDto> grossKMSDepositedCashierVsDpaReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Returns List Gross KMS Deposited with Cashier Vs. DPA Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<IssuanceReceiptStmntReportDto> listIssuanceReceiptStmntReportDto = this.service.grossKMSDepositedCashierVsDpaReport(startDate,endDate);
		return listIssuanceReceiptStmntReportDto;

	}
	/**
	 * 19.Diesel Not Issued Report
	 * @param fromDate
	 * @param toDate
	 * @return List<IssuanceReceiptStmntReportDto> 
	 */
	@ApiOperation("Diesel Not Issued Report")
	@GetMapping(path = "/dieselNotIssuedReport/{fromDate}/{toDate}")
	public List<IssuanceReceiptStmntReportDto> dieselNotIssuedReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate) {
		log.info("Returns List Diesel Not Issued Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<IssuanceReceiptStmntReportDto> listDieselNotIssuedReport = this.service.dieselNotIssuedReport(startDate,endDate);
		return listDieselNotIssuedReport;

	}
	/**
	 * 23.Diesel Issue other purpose Report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @return List<DieselIssueOtherPurposeReportDto>
	 */
	@ApiOperation("Diesel Issue other purpose Report")
	@GetMapping(path = "/dieselIssueOtherPurposeReport/{fromDate}/{toDate}/{depoId}")
	public List<DieselIssueOtherPurposeReportDto> dieselIssueOtherPurposeReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("depoId") Integer depoId) {
		log.info("Returns List Diesel Issue other purpose Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DieselIssueOtherPurposeReportDto> listDieselIssueOtherPurposeReportDto = this.service.dieselIssueOtherPurposeReport(startDate,endDate,depoId);
		return listDieselIssueOtherPurposeReportDto;

	}
	/**
	 * 25.Depotwise/State wise for Bus Type Comparison report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @param busType
	 * @return List<DepotStateWiseBusTypeComparReportDto>
	 */
	@ApiOperation("Depotwise/State wise for Bus Type Comparison report")
	@GetMapping(path = "/depotStateWiseBusTypeComparReport/{fromDate}/{toDate}/{depoId}/{busType}")
	public List<DepotStateWiseBusTypeComparReportDto> depotStateWiseBusTypeComparReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("depoId") Integer depoId,@PathVariable("busType") Integer busType) {
		log.info("Returns List DepotStateWiseBusTypeComparReport");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<DepotStateWiseBusTypeComparReportDto> listDepotStateWiseBusTypeComparReportDto = this.service.depotStateWiseBusTypeComparReport(startDate,endDate,depoId,busType);
		return listDepotStateWiseBusTypeComparReportDto;

	}
	/**
	 * 26.Inspections Done Versus Due report
	 * @param fromDate
	 * @param toDate
	 * @param depoId
	 * @param busType
	 * @return List<DepotStateWiseBusTypeComparReportDto>
	 * @throws ParseException 
	 */
	@ApiOperation("Inspections Done Versus Due report")
	@GetMapping(path = "/inspectionDoneVersusDueReport/{monthYear}/{depotCode}")
	public List<InspectionDoneVersusDueReportDto> inspectionDoneVersusDueReport(@PathVariable("monthYear") String monthYear,
		@PathVariable("depotCode") String depotCode) throws ParseException {
		log.info("Returns List Inspection Done Versus Due Report");
		String[] tempMonthYear = monthYear.split("-");
		String month = tempMonthYear[0];
		String year = tempMonthYear[1];
		String fromDate = year+"-"+month+"-01";
		LocalDate lastDayOfMonth = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
			       .with(TemporalAdjusters.lastDayOfMonth());
		System.out.println(lastDayOfMonth);
		String lastDateOfMonth = lastDayOfMonth.toString();
		String[] lastDateOfMonthArray = lastDateOfMonth.split("-");
		String lastDate = lastDateOfMonthArray[2];
		Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastDayOfMonth.toString());
		Date startDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<InspectionDoneVersusDueReportDto> reportDtoList = this.service.inspectionDoneVersusDueReport(lastDate,month,startDate,endDate,depotCode,fromDate);
		return reportDtoList;

	}
	
	/**
	 * 27.Buses Not Getting Diesel Issues on Time report
	 * @param depoId
	 * @return List<DepotStateWiseBusTypeComparReportDto>
	 * @throws ParseException 
	 */
	@ApiOperation("Inspections Done Versus Due report")
	@GetMapping(path = "/busesNotGettingDieselIssueReport/{fromDate}/{toDate}/{depoId}")
	public List<BusesNotGettingDieselIssuedOnTimeReportDto> busesNotGettingDieselIssueReport(@PathVariable("fromDate") String fromDate,
			@PathVariable("toDate") String toDate,@PathVariable("depoId") Integer depoId)  {
		log.info("Returns List Buses Not Getting Diesel Issue Report");
		Date startDate = null;
		Date endDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(toDate != null)
			endDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<BusesNotGettingDieselIssuedOnTimeReportDto> dieselIssuedNotOnTimeList = this.service.busesNotGettingDieselIssueReport(startDate,endDate,depoId);
		return dieselIssuedNotOnTimeList;

	}
	
	/**
	 * 28.Fetch fuel type list
	 * @return List<FuelTypeDto> 
	 */
	@ApiOperation("Fetch Fuel Type list")
	@GetMapping(path = "/fetchFuelTypeList")
	public List<FuelTypeDto> fetchFuelTypeList()  {
		log.info("Returns List fuel type");
		List<FuelTypeDto> fuelTypeDto = this.service.fetchFuelTypeList();
		return fuelTypeDto;

	}
	
	
}
