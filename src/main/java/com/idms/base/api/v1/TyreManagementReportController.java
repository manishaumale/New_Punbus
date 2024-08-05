package com.idms.base.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.idms.base.service.TyreMgtReportService;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/tyreReport")
@Log4j2
public class TyreManagementReportController {
	
	@Autowired
	private ModelMapper mapper;
	
	
	@Autowired
	TyreMgtReportService service;
	
	//1
	@ApiOperation("Returns Depot Wise Tyre Summary Report")
	@GetMapping(path = "/depotWiseTyreSummaryReport/{depotId}/{fromdate}/{todate}")
	public List<DepotWiseTyreReportDto> depotWiseTyreSummaryReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromdate") String fromdate,@PathVariable("todate") String todate) {
		log.info("Enter into depotWiseTyreSummaryReport service");
		List<DepotWiseTyreReportDto> reportData = this.service.depotWiseTyreSummaryReport(depotId,fromdate,todate);
		return reportData;
	}
	//2
	@ApiOperation("Returns Total Buses And New Tyre Report")
	@GetMapping(path = "/getTotalBusesTyreReport/{depotId}/{fromDate}/{toDate}")
	public List<TotalBusesTyreReportByDepotDto> getTotalBusesTyreReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into getTotalBusesTyreReport service");
		List<TotalBusesTyreReportByDepotDto> reportData = this.service.getTotalBusesTyreReport(depotId,fromDate,toDate);
		return reportData;
	}
	//4
	@ApiOperation("Bus wise Tyre Inventory Report")
	@GetMapping(path = "/busWiseTyreInventoryReport/{busId}/{fromDate}/{toDate}")
	public List<BusWiseTyreInventoryReportDto> busWiseTyreInventoryReport(@PathVariable("busId") Integer busId,@PathVariable("fromDate") String fromDate,@PathVariable("toDate") String toDate) {
		log.info("Enter into busWiseTyreInventoryReport service");
		 List<BusWiseTyreInventoryReportDto> busWiseTyreInventoryReport = this.service.busWiseTyreInventoryReport(busId,fromDate,toDate);
		return busWiseTyreInventoryReport;
	}
	//7
	@ApiOperation("Expected Tyre Life Report")
	@GetMapping(path = "/expectedTyreLifeReport/{depotId}/{fromDate}/{toDate}")
	public List<ExpectedTyreLifeReport> expectedTyreLifeReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into expectedTyreLifeReport service");
		 List<ExpectedTyreLifeReport>reportData = this.service.expectedTyreLifeReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//11
	@ApiOperation("Returns Depot Wise Taken Off Report")
	@GetMapping(path = "/depotWiseTakenOffReport/{depotId}/{fromDate}/{toDate}")
	public List<DepotWiseTakenOffReportDto> depotWiseTakenOffReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into depotWiseTyreSummaryReport service");
		List<DepotWiseTakenOffReportDto> reportData = this.service.depotWiseTakenOffReport(depotId,fromDate,toDate);
		return reportData;
	}
	//12
	@ApiOperation("Returns Tyre TakenOff Not Refitted Report")
	@GetMapping(path = "/tyreTakenOffNotRefittedReport/{depotId}/{fromDate}/{toDate}")
	public List<TyreTakenOffNotRefittedDto> tyreTakenOffNotRefittedReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into depotWiseTyreSummaryReport service");
		List<TyreTakenOffNotRefittedDto> reportData = this.service.tyreTakenOffNotRefittedReport(depotId,fromDate,toDate);
		return reportData;
	}
	//13
	@ApiOperation("Returns New Tyre Issued Report")
	@GetMapping(path = "/newTyreIssuedReport/{depotId}/{fromDate}/{toDate}")
	public List<NewAndResoledTyreIssueDto> newTyreIssuedReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into depotWiseTyreSummaryReport service");
		List<NewAndResoledTyreIssueDto> reportData = this.service.newTyreIssuedReport(depotId,fromDate,toDate);
		return reportData;
	}
	//14
	@ApiOperation("Returns Resole Tyre Issued Report")
	@GetMapping(path = "/resoleTyreIssuedReport/{depotId}/{fromDate}/{toDate}")
	public List<NewAndResoledTyreIssueDto> resoleTyreIssuedReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into depotWiseTyreSummaryReport service");
		List<NewAndResoledTyreIssueDto> reportData = this.service.resoleTyreIssuedReport(depotId,fromDate,toDate);
		return reportData;
	}
	//16
	@ApiOperation("Returns Mileage Achieved Report")
	@GetMapping(path = "/mileageAchievedReport/{fromDate}/{toDate}")
	public List<MileageAchievedReportDto> mileageAchievedReport(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into depotWiseTyreSummaryReport service");
		List<MileageAchievedReportDto> reportData = this.service.mileageAchievedReport(fromDate,toDate);
		return reportData;
	}
	//3
	@ApiOperation("Current Tyre Status Report")
	@GetMapping(path = "/currentTyreStatusReport/{tyreId}/{fromDate}/{toDate}")
	public List<CurrentTyreStatusReportDto> currentTyreStatusReport(@PathVariable("tyreId") Integer tyreId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into currentTyreStatusReport service");
		List<CurrentTyreStatusReportDto> reportData = this.service.currentTyreStatusReport(tyreId,fromDate,toDate);
		return reportData;
	}
	//5
	@ApiOperation("Tyre Due For Inspection Report")
	@GetMapping(path = "/tyreDueForInspection/{depotId}/{Date}")
	public List<TyreDueForInspectionDto> tyreDueForInspection(@PathVariable("depotId") Integer depotId,@PathVariable("Date") String Date) {
		log.info("Enter into tyreDueForInspection service");
		List<TyreDueForInspectionDto> reportData = this.service.tyreDueForInspection(depotId,Date);
		return reportData;
	}
	//6
	@ApiOperation("Completion Of Expected Tyre Life Report")
	@GetMapping(path = "/completionOfExpectedTyreReport/{depotId}/{fromDate}/{toDate}")
	public List<CompletionOfExpectedTyreReportDto> completionOfExpectedTyreReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into completionOfExpectedTyreReport service");
		List<CompletionOfExpectedTyreReportDto> reportData = this.service.completionOfExpectedTyreReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//8
	@ApiOperation("Tyre Taken Off Before Expected Life Report")
	@GetMapping(path = "/tyreTakenOffBeforeExpectedLife/{depotId}/{fromDate}/{toDate}")
	public List<TyreTakenOffBeforeExpectedLife> tyreTakenOffBeforeExpectedLife(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyreTakenOffBeforeExpectedLife service");
		List<TyreTakenOffBeforeExpectedLife> reportData = this.service.tyreTakenOffBeforeExpectedLife(depotId,fromDate,toDate);
		return reportData;
	}
	
	//9
	@ApiOperation("New Tyre Issued For Rear Report")
	@GetMapping(path = "/newTyreIssuedForRearReport/{depotId}/{fromDate}/{toDate}")
	public List<NewTyreIssuedForRearReportDto> newTyreIssuedForRearReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into newTyreIssuedForRearReport service");
		List<NewTyreIssuedForRearReportDto> reportData = this.service.newTyreIssuedForRearReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//10
	@ApiOperation("Tyre Condemn Report")
	@GetMapping(path = "/tyreCondemnReport/{depotId}/{fromDate}/{toDate}")
	public List<TyreCondemnReport> tyreCondemnReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyreCondemnReport service");
		List<TyreCondemnReport> reportData = this.service.tyreCondemnReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//15
	@ApiOperation("Stock Of New And Resole Tyre Report")
	@GetMapping(path = "/tyreReportInStock/{fromDate}/{toDate}")
	public List<TyreReportInStockDto> tyreReportInStock(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyreReportInStock service");
		List<TyreReportInStockDto> reportData = this.service.tyreReportInStock(fromDate,toDate);
		return reportData;
	}
	
	//18
	@ApiOperation("Resole Tyre Mileage Report")
	@GetMapping(path = "/resoleTyreMileageReport/{depotId}/{fromDate}/{toDate}")
	public List<ResoleTyreMileageReport> resoleTyreMileageReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into resoleTyreMileageReport service");
		List<ResoleTyreMileageReport> reportData = this.service.resoleTyreMileageReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//19
	@ApiOperation("Tyre Sent For Retreading Report")
	@GetMapping(path = "/tyreSentForRetreadingReport/{depotId}/{fromDate}/{toDate}")
	public List<TyreSentForRetreadingReport> tyreSentForRetreadingReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyreSentForRetreadingReport service");
		List<TyreSentForRetreadingReport> reportData = this.service.tyreSentForRetreadingReport(depotId,fromDate,toDate);
		return reportData;
	}
	//20
	@ApiOperation("Tyre Not Sent For Retreading Report")
	@GetMapping(path = "/tyreNotSentForRetreadingReport/{depotId}/{fromDate}/{toDate}")
	public List<TyreNotSentForRetreadingReport> tyreNotSentForRetreadingReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyreNotSentForRetreadingReport service");
		List<TyreNotSentForRetreadingReport> reportData = this.service.tyreNotSentForRetreadingReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//21
	@ApiOperation("Number of Tyres Retreading Report")
	@GetMapping(path = "/countOfRetreadingTyresReport/{depotId}/{fromDate}/{toDate}")
	public List<CountOfRetreadingTyresReport> countOfRetreadingTyresReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into countOfRetreadingTyresReport service");
		List<CountOfRetreadingTyresReport> reportData = this.service.countOfRetreadingTyresReport(depotId,fromDate,toDate);
		return reportData;
	}
	//22
	@ApiOperation("Tyre Bifurcation Report")
	@GetMapping(path = "/tyreBifurcationReport/{depotId}/{fromDate}/{toDate}")
	public List<TyreBifurcationReportDto> tyreBifurcationReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyreBifurcationReport service");
		List<TyreBifurcationReportDto> reportData = this.service.tyreBifurcationReport(depotId,fromDate,toDate);
		return reportData;
	}
	//23
	@ApiOperation("Tyre Auction Report")
	@GetMapping(path = "/tyresAuctionReport/{fromDate}/{toDate}")
	public List<TyreAuctionReportDto> tyresAuctionReport(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyresAuctionReport service");
		List<TyreAuctionReportDto> reportData = this.service.tyresAuctionReport(fromDate,toDate);
		return reportData;
	}
	
	//24
	@ApiOperation("Tyres lying for Auction Report")
	@GetMapping(path = "/tyresLyingForAuctionReport/{depotId}/{fromDate}/{toDate}")
	public List<TyresLyingForAuctionReport> tyresLyingForAuctionReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyresLyingForAuctionReport service");
		List<TyresLyingForAuctionReport> reportData = this.service.tyresLyingForAuctionReport(depotId,fromDate,toDate);
		return reportData;
	}
	//17
	@ApiOperation("Condemn Tyre  Report")
	@GetMapping(path = "/condemnTyreReport/{depotId}/{fromDate}/{toDate}")
	public List<CondemnTyreReportDto> condemnTyreReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into condemnTyreReport service");
		List<CondemnTyreReportDto> reportData = this.service.condemnTyreReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//25
	@ApiOperation("New Tyre and Resole Tyre Quantity Report In RS")
	@GetMapping(path = "/newTyreAndResoleTyreQuantityReport/{fromDate}/{toDate}")
	public List<NewTyreAndResoleTyreQuantityReportDto> newTyreAndResoleTyreQuantityReport(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into newTyreAndResoleTyreQuantityReport service");
		List<NewTyreAndResoleTyreQuantityReportDto> reportData = this.service.newTyreAndResoleTyreQuantityReport(fromDate,toDate);
		return reportData;
	}
	

	//26
	@ApiOperation("Tyre Not Auction Report")
	@GetMapping(path = "/tyreNotAuctionReport/{depotId}/{fromDate}/{toDate}")
	public List<TyreNotAuctionReportDto> tyreNotAuctionReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
		log.info("Enter into tyreNotAuctionReport service");
		List<TyreNotAuctionReportDto> reportData = this.service.tyreNotAuctionReport(depotId,fromDate,toDate);
		return reportData;
	}
	
	//27	 
	 @ApiOperation("New Tyre and Resole Tyre Mileage Report")
	 @GetMapping(path = "/newTyreAndResoleTyreMileageReport/{fromDate}/{toDate}")
	 public List<NewTyreAndResoleTyreMileageReportDto> newTyreAndResoleTyreMileageReport(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
			log.info("Enter into newTyreAndResoleTyreMileageReport service");
			List<NewTyreAndResoleTyreMileageReportDto> reportData = this.service.newTyreAndResoleTyreMileageReport(fromDate,toDate);
			return reportData;
		}
	 
	 //28 	 
	 @ApiOperation("Make Wise Comparison Report")
	 @GetMapping(path = "/makeWiseComparisonReport/{depotId}/{tyreSizeId}/{fromDate}/{toDate}/{makeId}")
	 public List<MakeWiseComparisonReportDto> makeWiseComparisonReport(@PathVariable("depotId") Integer depotId, @PathVariable("tyreSizeId") Integer tyreSizeId,
			 @PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate, @PathVariable("makeId") Integer makeId) {
			log.info("Enter into makeWiseComparisonReport service");
			List<MakeWiseComparisonReportDto> reportData = this.service.makeWiseComparisonReport(depotId,tyreSizeId,fromDate,toDate,makeId);
			return reportData;
		}
	 
	//29	 
    @ApiOperation("Tyre Current Mileage Report")
	@GetMapping(path = "/tyreCurrentMileageReport/{fromDate}/{toDate}")
	public List<TyreCurrentMileageReportDto> tyreCurrentMileageReport(@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
				log.info("Enter into newTyreAndResoleTyreMileageReport service");
				List<TyreCurrentMileageReportDto> reportData = this.service.tyreCurrentMileageReport(fromDate,toDate);
				return reportData;
	}
    
  //30
  	@ApiOperation("Tyre Due For Retreding Report")
  	@GetMapping(path = "/tyreDueForRetredingReport/{depotId}/{fromDate}/{toDate}")
  	public List<TyreDueForRetredingReportDto> tyreDueForRetredingReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
  		log.info("Enter into tyreNotAuctionReport service");
  		List<TyreDueForRetredingReportDto> reportData = this.service.tyreDueForRetredingReport(depotId,fromDate,toDate);
  		return reportData;
  	}
	
  //31
  	@ApiOperation("Tyre Not Taken Off After Completed Mileage Report")
  	@GetMapping(path = "/tyreNotTakenOffAfterCompletedMileageReport/{depotId}/{fromDate}/{toDate}")
  	public List<TyreNotTakenOffAfterCompletedMileageReportDto> tyreNotTakenOffAfterCompletedMileageReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
  		log.info("Enter into tyreNotAuctionReport service");
  		List<TyreNotTakenOffAfterCompletedMileageReportDto> reportData = this.service.tyreNotTakenOffAfterCompletedMileageReport(depotId,fromDate,toDate);
  		return reportData;
  	}
  	
  //32
  	@ApiOperation("Condemn Mileage Report")
  	@GetMapping(path = "/condemnMileageReport/{depotId}/{fromDate}/{toDate}")
  	public List<TyreNotTakenOffAfterCompletedMileageReportDto> condemnMileageReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
  		log.info("Enter into tyreNotAuctionReport service");
  		List<TyreNotTakenOffAfterCompletedMileageReportDto> reportData = this.service.condemnMileageReport(depotId,fromDate,toDate);
  		return reportData;
  	}
  //33
  	@ApiOperation("Depot Wise Tyre Consumption Report")
  	@GetMapping(path = "/depotWiseTyreConsumptionReport/{depotId}/{fromDate}/{toDate}")
  	public List<DepotWiseTyreConsumptionReportDto> depotWiseTyreConsumptionReport(@PathVariable("depotId") Integer depotId,@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate) {
  		log.info("Enter into tyreNotAuctionReport service");
  		List<DepotWiseTyreConsumptionReportDto> reportData = this.service.depotWiseTyreConsumptionReport(depotId,fromDate,toDate);
  		return reportData;
  	}

	
	@GetMapping(path="/getTyreChangeData/{id}")
	public TyreIssueSlipListDto getTyreIssueSlip(@PathVariable Integer id) {
		return service.getTyreChangeReplacementData(id);
	}
		
	 //33
  	@ApiOperation("Depot Wise Tyre Consumption Report")
  	@GetMapping(path = "/tyreMakerList")
  	public List<TyreMakerDto> tyreMakerList() {
  		log.info("Enter into tyreMakerList service");
  		List<TyreMakerDto> reportData = this.service.tyreMakerList();
  		return reportData;
  	}

}
