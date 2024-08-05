package com.idms.base.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.ETMIntegrationController;
import com.idms.base.api.v1.IssueETMnTicketBoxController;
import com.idms.base.api.v1.model.dto.IssueEtmObjDto;
import com.idms.base.api.v1.model.dto.ResponseMessage;
import com.idms.base.dao.entity.BusStandBookingDtls;
import com.idms.base.dao.entity.ConcessionDtls;
import com.idms.base.dao.entity.ConductorEtmBookingDetails;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.ConductorRotaHistory;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.ETMMaster;
import com.idms.base.dao.entity.ETMStatewiseCollection;
import com.idms.base.dao.entity.EarningFromETM;
import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.dao.entity.EtmTripDtls;
import com.idms.base.dao.entity.EtmTripTotalDtls;
import com.idms.base.dao.entity.ExcelUploadLogs;
import com.idms.base.dao.entity.IssueEtmTicketBoxEntity;
import com.idms.base.dao.entity.PassengerClassification;
import com.idms.base.dao.entity.RouteMaster;
import com.idms.base.dao.entity.TripExpenses;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.ConductorRotaHistoryRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DocumentRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.ETMMasterRepository;
import com.idms.base.dao.repository.EtmTBAssignmentRepository;
import com.idms.base.dao.repository.ExcelUploadLogsRepository;
import com.idms.base.dao.repository.IssueEtmTicketBoxRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class etmExcelUploadServiceImpl {
	@Autowired
	EtmTBAssignmentRepository repository;

	@Autowired
	ConductorMasterRepository conductorRepo;

	@Autowired
	RouteMasterRepository routeRepo;

	@Autowired
	ETMMasterRepository etmMasterRepo;

	@Value("${file.path}")
	private String filePath;

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	ExcelUploadLogsRepository logRepo;

	@Autowired
	ETMIntegrationController etmController;
	
	@Autowired
	IssueEtmTicketBoxRepository issueEtmRepo;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	IssueETMnTicketBoxController issueEtmController;
	
	@Autowired
	DailyRoasterRepository dailyRoasterRepo;
	
	@Autowired
	DriverMasterRepository driverMasterRepo;
	
	@Autowired
	TripMasterRepository tripMasterRepository;
	
	@Autowired
	ConductorRotaHistoryRepository conductorRotaHistoryRepository;
	

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Title", "Description", "Published" };
	static String[] submitEtmDataHEADERs = { "wayBillNo", "inspectDtls", "Date_Time(DD/MM/YYYY hh:mm:ss)", "boxAmt",
			"tollAmt", "concessionAmt", "luggageAmt", "conductorTotalAmt", "miscAmt", "passengerAmt", "passengerCount",
			"addaBookingAmt", "advBookingAmtByEBTM", "totalAmt", "tollTax", "ticketRefund", "busStandFee", "diesel",
			"rate_of_diesel", "repairBill", "miscFee", "total", "totalAdults", "totalChilds","totalTicketIssued","PUNJAB_ticket_amount",
			"HARYANA_ticket_amount", "H.P_ticket_amount", "CHANDIGARH_ticket_amount", "DELHI_ticket_amount",
			"J&K_ticket_amount", "RAJASTHAN_ticket_amount", "UK_ticket_amount", "UP_ticket_amount",
			"passengerCounterBooking", "concessionTicket", "advBooking", "luggageTicket", "policWarrent",
			"trafficInspection", "ticketAmount", "kmpl", "POLICE_concession_ticket_count",
			"POLICE_concession_ticket_amount", "OLD WOMEN_concession_ticket_count", "OLD WOMEN_concession_amount",
			"DISABLE_concession_ticket_count", "DISABLE_concession_ticket_amount", "CANCER_concession_ticket_count",
			"CANCER_concession_ticket_amount", "FFIGHTER_concession_ticket_count", "FFIGHTER_concession_ticket_amount",
			"PRESS_concession_ticket_count", "PRESS_concession_ticket_amount", "PWT_TICKETS_concession_ticket_count",
			"PWT_TICKETS_concession_ticket_amount", "other_consession_ticket_count", "other_concession_ticket_amount",
			"earningPerKmFree", "loadFactor", "totalRemittance", "netAmtToDeposit", "earningPerKM" };

	static String[] etmTripDtlsheaders = { "wayBillNo", "advBookingAmt", "netTripAmt", "distance", "tripEndDateTime",
			"tripStartDateTime", "totalCollection", "routeName", "expenses" };
	static String[] etmStatewiseheaders = { "amount", "stateName", "wayBillNo" };
	static String[] concessionheaders = { "discPercentage", "passType", "tickets", "netAmount", "wayBillNo" };
	static String SHEET = "Tutorials";

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public List<ResponseEntity<ResponseMessage>> excelToTutorials(InputStream is,String dpCode) {
		try {
			DepotMaster depot= depotRepo.findByDepotCode(dpCode);
			List<ResponseEntity<ResponseMessage>> errorLog = new ArrayList<>();
			Workbook workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheet("IssueEtm");
			if(sheet!=null){
			Iterator<Row> rows = sheet.iterator();
			List<EtmTBAssignment> tutorials = new ArrayList<EtmTBAssignment>();
			int rowNumber = 0;
			boolean conductorHeader = true;
			boolean routeHeader = true;
			boolean etmHeader = true;
			boolean waybillHeader = true;
			boolean dateHeader = true;
			boolean conductorNameheader = true;
			boolean routeNameHeader = true;
			boolean busNumberHeader = true;
			boolean tripNameHeader = true;
			boolean roasterStatus=true;
			boolean driverHeader =true;
			boolean tripServiceHeader = true;
			TripMaster tripObj = null;
			while (rows.hasNext()) {
				String waybillNo="waybill ";
				Row currentRow = rows.next();
				ConductorMaster conductorId = null;
				ETMMaster etmMaster = null;
				RouteMaster routeId = null;
				Date date = null;
				EtmTBAssignment waybill = null;
				//String  wayBillStr="";
				DriverMaster driver=null;
				tripObj = null;
				// skip header
				// if (rowNumber == 0) {
				//
				// rowNumber++;
				// continue;
				// }
				Iterator<Cell> cellsInRow = currentRow.iterator();
				EtmTBAssignment tutorial = new EtmTBAssignment();
				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					switch (cellIdx) {
					case 1:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Conductor_Name")) {
								conductorNameheader = true;
							} else {
								conductorNameheader = false;
								ResponseMessage message = new ResponseMessage("Conductor Name header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 2:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Conductor_Code")) {
								conductorHeader = true;
							} else {
								conductorHeader = false;
								ResponseMessage message = new ResponseMessage("Conductor header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (currentCell.getCellType() == CellType.STRING && conductorHeader) {
							conductorId = conductorRepo.findByConductorName(currentCell.getStringCellValue().trim());
						}
						break;
					case 3:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Driver_code")) {
								driverHeader = true;
							} else {
								driverHeader = false;
								ResponseMessage message = new ResponseMessage("Driver header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (currentCell.getCellType() == CellType.STRING && driverHeader) {
							driver = driverMasterRepo.findByDrivercode(currentCell.getStringCellValue().trim());
						}
						break;
					case 7:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Etm_Number")) {
								etmHeader = true;
							} else {
								etmHeader = false;
								ResponseMessage message = new ResponseMessage("etm header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (currentCell.getCellType() == CellType.STRING && etmHeader) {
							//etmMaster = etmMasterRepo.findByEtmNumber(String.valueOf((int)currentCell.getNumericCellValue()));
							etmMaster = etmMasterRepo.findByEtmNumber(currentCell.getStringCellValue().trim());
						}
						break;
					case 5:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Route_Code")) {
								routeHeader = true;
							} else {
								routeHeader = false;
								ResponseMessage message = new ResponseMessage("Route header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (currentCell.getCellType() == CellType.STRING && routeHeader) {
							routeId = routeRepo.findByRouteCode(currentCell.getStringCellValue().trim());
						}
						break;
					case 6:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Route_Name")) {
								routeNameHeader = true;
							} else {
								routeNameHeader = false;
								ResponseMessage message = new ResponseMessage("Route Name header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 4:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Date_Time(DD/MM/YYYY hh:mm:ss)")) {
								dateHeader = true;
							} else {
								dateHeader = false;
								ResponseMessage message = new ResponseMessage("Date time header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (dateHeader && rowNumber >= 1) {
							date = currentCell.getDateCellValue();
						}
						break;
					case 8:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Bus_Number")) {
								busNumberHeader = true;
							} else {
								busNumberHeader = false;
								ResponseMessage message = new ResponseMessage("Busnumber header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 0:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("WayBill_Number")) {
								waybillHeader = true;
							} else {
								waybillHeader = false;
								ResponseMessage message = new ResponseMessage("Waybill header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (waybillHeader && rowNumber >= 1) {
							waybillNo=waybillNo+"- "+ currentCell.getStringCellValue().trim();
							waybill = repository
									.findByWaybillNo(currentCell.getStringCellValue().trim());
							if(waybill!=null){
							ResponseMessage message = new ResponseMessage(
									"Waybill " + waybill.getWayBillNo() + " is already entered! Please Upload Correct File.");
							errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 9:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().trim().equals("Trip_Service_id")) {
								tripServiceHeader = true;
							} else {
								tripServiceHeader = false;
								ResponseMessage message = new ResponseMessage("Trip service id is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							
						}
						if (currentCell.getCellType() == CellType.STRING && tripServiceHeader) {
							tripObj = tripMasterRepository.findByServiceId(currentCell.getStringCellValue().trim());
						}
						break;
					}
					cellIdx++;
				}
				if (conductorId != null && etmMaster != null && routeId != null && date != null && waybill == null) {
					cellsInRow = currentRow.iterator();
					cellIdx = 0;
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");  
					 String excelDate= formatter.format(date); 
					Integer excelhour =(Integer)date.getHours();
					Integer excelmin =(Integer)date.getMinutes();
					String exceltime =(excelhour.toString().length() >1?excelhour.toString():"0" + excelhour.toString()  )+":" + (excelmin.toString().length() > 1?excelmin.toString():"0"+excelmin.toString()) ;
					List<ConductorRotaHistory> DailyRoaster =conductorId!=null && conductorId.getId()!=null ?  dailyRoasterRepo
							.fetchRosterDetailsByCondutorIdAndTrip(conductorId.getId(),tripObj.getId()) : null;
							String rotaDate= formatter.format(DailyRoaster.get(0).getRoaster().getRota().getRotaDate());
							
							if(DailyRoaster.size()==0)
							{
								ResponseMessage message = new ResponseMessage("Roster is not prepared for the day ");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							else if( !DailyRoaster.get(0).getTrip().getTripStartTime().toString().equals(exceltime)  ||  !excelDate.equals(rotaDate) ){
								   
								ResponseMessage message = new ResponseMessage("Roster date & time doesn't matched with excel date & time for conductor :- "+conductorId.getConductorName() + ", and trip :- "+ tripObj.getServiceId() );
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							else if(DailyRoaster.get(0).getIssueEtmTicketBoxEntity() != null)
							{ 
								ResponseMessage message = new ResponseMessage("ETM is already assigned to :- "+conductorId.getConductorName() + " for the trip :- "+ tripObj.getServiceId() );
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							
							else {
							
					while (cellsInRow.hasNext()) {
						Cell currentCell = cellsInRow.next();
						switch (cellIdx) {
						case 2:
							if(roasterStatus){
							roasterStatus=DailyRoaster.get(0).getRoaster().getConductor().getConductorCode().equals(currentCell.getStringCellValue());
							}
							if(!roasterStatus) {
								ResponseMessage message = new ResponseMessage(waybillNo +" conductor data is not matched with roaster data");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							break;
						case 3:
							if(roasterStatus){
							roasterStatus=DailyRoaster.get(0).getRoaster().getDriver().getDriverCode().equals(currentCell.getStringCellValue());
							}
							if(!roasterStatus) {
								ResponseMessage message = new ResponseMessage(waybillNo +" driver data is not matched with roaster data");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							break;
						case 8:
							if(roasterStatus){
								roasterStatus=DailyRoaster.get(0).getRoaster().getBus().getBusRegNumber().equals(currentCell.getStringCellValue());
							}
							if(!roasterStatus) {
								ResponseMessage message = new ResponseMessage(waybillNo +" bus data is not matched with roaster data");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							break;
						case 5:
							if(roasterStatus){
							roasterStatus=DailyRoaster.get(0).getRoaster().getRoute().getRouteCode().equals(currentCell.getStringCellValue());
							}
							if(!roasterStatus) {
								ResponseMessage message = new ResponseMessage(waybillNo +" route data is not matched with roaster data");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
							break;
						}
						cellIdx++;
					}	
							}
				}
				if (conductorId != null && etmMaster != null && routeId != null && date != null && waybill == null && errorLog.size()==0) {
					cellsInRow = currentRow.iterator();
					cellIdx = 0;
					while (cellsInRow.hasNext()) {
						Cell currentCell = cellsInRow.next();
						switch (cellIdx) {
						case 2:
							tutorial.setConductorId(conductorId);
							break;
						case 4:
							tutorial.setEtmAssignmentDate(currentCell.getDateCellValue());
							break;
						case 7:
							tutorial.setEtmId(etmMaster);
							break;
						case 0:
							tutorial.setWayBillNo( currentCell.getStringCellValue());
							break;
						case 5:
							tutorial.setRouteId(routeId);
							break;
						case 9:
							tutorial.setServiceId(currentCell.getStringCellValue());;
							break;	
						}
						cellIdx++;											
						tutorial.setEtmSubmitStatus(false);
						tutorial.setTbSubmitStatus(false);
						tutorials.add(tutorial);
					}
				} else {
					boolean s = false;
					if (conductorHeader && etmHeader && routeHeader && dateHeader && waybillHeader
							&& conductorNameheader && routeNameHeader && busNumberHeader && tripNameHeader) {
						s = true;
					}
					if (rowNumber > 0 && conductorId == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has conductor data wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && etmMaster == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has etm data wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && routeId == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has route data wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && date == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has date value wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && waybill != null && s) {
						ResponseMessage message = new ResponseMessage(
								"waybill " + waybill.getWayBillNo() + " is already entered!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
				}
				rowNumber++;
			}
			workbook.close();
			if(errorLog.size()==0){
			saveAllIssueEtmRecords(tutorials,depot,tripObj);
			}
			}
			else {
				ResponseMessage message = new ResponseMessage("IssueEtm sheet is not found!");
				errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
			}
//			repository.saveAll(tutorials);
			return errorLog;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}

	public void saveAllIssueEtmRecords(List<EtmTBAssignment> tutorials, DepotMaster depot,TripMaster tripObj) {
		ResponseEntity<Object> dto = new ResponseEntity<>(HttpStatus.OK);
		EtmTBAssignment tempObj = new EtmTBAssignment();
		for (EtmTBAssignment record : tutorials) {
			if (!record.getWayBillNo().isEmpty()) {
				IssueEtmTicketBoxEntity issueEtm = issueEtmRepo.findByWayBillNo(record.getWayBillNo());
				if (issueEtm == null) {
					issueEtm = new IssueEtmTicketBoxEntity();
					issueEtm.setConductorMaster(record.getConductorId());
					issueEtm.setCondutorStatus(false);
					issueEtm.setManualEntry(false);
					issueEtm.setEtmMaster(record.getEtmId());
					issueEtm.setWayBillNo(record.getWayBillNo());
					issueEtm.setDepoMaster(depot);
					issueEtmRepo.save(issueEtm);
					record.setIssueEtmId(issueEtm);
				}
				List<ConductorRotaHistory> dailyList = tempObj.getConductorId() != null
						&& tempObj.getConductorId().getId() != null
								? dailyRoasterRepo.fetchRosterDetailsByCondutorIdAndTrip(
										tempObj.getConductorId().getId(), tripObj.getId())
								: null;
				if (dailyList != null && dailyList.size() > 0 && dailyList.get(0).getRoaster() != null) {
					for (ConductorRotaHistory dailyObj : dailyList) {
						if ((dailyObj.getTrip().getTripStartTime().compareTo(tripObj.getTripStartTime()) == 0)) {
							issueEtm.setDailyRoaster(dailyObj.getRoaster());
							IssueEtmTicketBoxEntity issuePersistObj = issueEtmRepo.save(issueEtm);
							if (dailyObj != null) {
								dailyObj.setIssueEtmTicketBoxEntity(issuePersistObj);
								conductorRotaHistoryRepository.save(dailyObj);
							}
						}
					}
				}
			}
			
			tempObj = repository.save(record);
		}
	}

	public List<ResponseEntity<ResponseMessage>> save(MultipartFile file, String type,String dpCode) {
		try {
			boolean fileuploaded = uploadFile(file, type);
			List<ResponseEntity<ResponseMessage>> tutorials = excelToTutorials(file.getInputStream(),dpCode);
			return tutorials;
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	public boolean uploadFile(MultipartFile file, String type) {
		Document uploadDocument = null;
		String pattern = "ddMMyyyy";
		String currentDate = new SimpleDateFormat(pattern).format(new Date());
		boolean status = false;
		try {
			if (file != null && !file.isEmpty()) {
				File dir = new File(filePath + File.separator + "excelFiles" + File.separator + currentDate);
				if (!dir.exists())
					dir.mkdirs();
				uploadDocument = new Document();

				Files.copy(file.getInputStream(), dir.toPath().resolve(file.getOriginalFilename()),
						StandardCopyOption.REPLACE_EXISTING);
				uploadDocument.setContentType(file.getContentType());
				uploadDocument.setDocumentName(file.getOriginalFilename());
				uploadDocument.setDocumentPath(filePath + File.separator + "excelFiles" + File.separator + currentDate
						+ File.separator + file.getOriginalFilename());
				uploadDocument = documentRepository.save(uploadDocument);
				ExcelUploadLogs logs = new ExcelUploadLogs();
				logs.setFileName(uploadDocument.getDocumentName());
				logs.setLogDateTime(uploadDocument.getCreatedOn());
				logs.setType(type);
				logs.setDocumentId(uploadDocument);
				logRepo.save(logs);
				status = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = false;
		}
		return status;
	}	

	public List<ResponseEntity<ResponseMessage>> fetchDataAndSaveSubmitEtmExcelSheet(MultipartFile file)
			throws IOException {
		List<ResponseEntity<ResponseMessage>> errorLog = new ArrayList<>();
		List<ResponseEntity<ResponseMessage>> headerErrorLog = new ArrayList<>();
		Workbook workbook = new XSSFWorkbook(file.getInputStream());
		Sheet sheet = workbook.getSheet("etmData");
		Sheet sheet2 = workbook.getSheet("etmTripDtls");
//		Sheet sheet4 = workbook.getSheet("concessionDtls");
//		Sheet sheet3 = workbook.getSheet("etmStatewiseDtls");
		Iterator<Row> rows = sheet.iterator();
		Iterator<Row> etmTripDtlsRows = sheet2.iterator();
//		Iterator<Row> etmStatewiseDtls = sheet3.iterator();
//		Iterator<Row> concessionDtls = sheet4.iterator();
		int rowIndex = 0;
		boolean status = true;
		boolean sheet2Header = true;
		BusStandBookingDtls BusStandDtls = null;
		EarningFromETM earningETMDtls = null;
		EtmTripTotalDtls etmTripTotalDtls = null;
		TripExpenses tripExpenses = null;
		PassengerClassification passengerClassification = null;
		ConductorEtmBookingDetails conductorEtm = null;
		EtmTBAssignment mainObj = new EtmTBAssignment();
		List<EtmTripDtls> listEtmTripDtls = new ArrayList<>();
		List<ETMStatewiseCollection> listEtmStateWisedtls = new ArrayList<>();
		List<ConcessionDtls> listConcessionDtls = new ArrayList<>();
		ResponseEntity<ResponseStatus> response = new ResponseEntity<ResponseStatus>(HttpStatus.FORBIDDEN);

		if (sheet != null && sheet2 != null) {
			while (rows.hasNext()) {
				mainObj = new EtmTBAssignment();
				Row currentRow = rows.next();

				Iterator<Cell> cellsInRow = currentRow.iterator();

				int cellIdx = 0;
				if (rowIndex == 0) {
					Row sheet2Row = etmTripDtlsRows.next();
//					Row sheet3Row = etmStatewiseDtls.next();
//					Row sheet4Row = concessionDtls.next();
					Iterator<Cell> sheet2cells = sheet2Row.iterator();
//					Iterator<Cell> sheet3cells = sheet3Row.iterator();
//					Iterator<Cell> sheet4cells = sheet4Row.iterator();
					while (cellsInRow.hasNext()) {
						Cell currentCell = cellsInRow.next();
						if (rowIndex == 0 && currentCell.getCellType() == CellType.STRING) {
							status = submitEtmDataHEADERs[cellIdx].contentEquals(currentCell.getStringCellValue());
							if (!status) {
								ResponseMessage message = new ResponseMessage(currentCell.getStringCellValue()
										+ " header is incorrect in sheet " + sheet.getSheetName());
								headerErrorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						cellIdx++;
						
						}
					cellIdx = 0;
					while (sheet2cells.hasNext()) {
						Cell currentCell = sheet2cells.next();
						if (rowIndex == 0 && currentCell.getCellType() == CellType.STRING) {
							status = etmTripDtlsheaders[cellIdx].equals(currentCell.getStringCellValue());
							if (!status) {
								ResponseMessage message = new ResponseMessage(currentCell.getStringCellValue()
										+ " header is incorrect in sheet " + sheet2.getSheetName());
								headerErrorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						cellIdx++;
					}
//					cellIdx = 0;
//					while (sheet3cells.hasNext()) {
//						Cell currentCell = sheet3cells.next();
//						if (rowIndex == 0 && currentCell.getCellType() == CellType.STRING) {
//							status = etmStatewiseheaders[cellIdx].equals(currentCell.getStringCellValue());
//							if (!status) {
//								ResponseMessage message = new ResponseMessage(currentCell.getStringCellValue()
//										+ " header is incorrect in sheet " + sheet3.getSheetName());
//								headerErrorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
//							}
//						}
//						cellIdx++;
//					}
//					cellIdx = 0;
//					while (sheet4cells.hasNext()) {
//						Cell currentCell = sheet4cells.next();
//						if (rowIndex == 0 && currentCell.getCellType() == CellType.STRING) {
//							status = concessionheaders[cellIdx].equals(currentCell.getStringCellValue());
//							if (!status) {
//								ResponseMessage message = new ResponseMessage(currentCell.getStringCellValue()
//										+ " header is incorrect in sheet " + sheet4.getSheetName());
//								headerErrorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
//							}
//						}
//						cellIdx++;
//					}
				}
				cellsInRow = currentRow.iterator();

				cellIdx = 0;
				if (rowIndex > 0 && headerErrorLog.size() == 0) {

					BusStandDtls = new BusStandBookingDtls();
					// EtmTBAssignment etmData = new EtmTBAssignment(null,
					// etmId,
					// currentRow.getCell(15)!=null ?currentRow.getCell(15) :
					// null,
					// ticketBoxId, conductorId, routeId, issueEtmId,
					// etmAssignmentDate, etmSubmitDate, etmSubmitStatus,
					// tbSubmitStatus, associationList, conductorBookingDetails,
					// busStandBookingDtls, tripExpenses,
					// passengerClassification,
					// concessionDtls, earning, etmStatewiseDtls, etmTripDtls,
					// etmTripTotalDtls);
					mainObj.setWayBillNo(
							currentRow.getCell(0) != null && currentRow.getCell(0).getCellType() == CellType.NUMERIC
									? String.valueOf((int) Double.parseDouble(currentRow.getCell(0).toString()))
									: null);
					mainObj.setIsFaulty(currentRow.getCell(63) != null && currentRow.getCell(3).getCellType() == CellType.STRING
									? Boolean.parseBoolean(currentRow.getCell(63).toString())
									: false);
					BusStandDtls.setPassengerCount(
							currentRow.getCell(10) != null && currentRow.getCell(10).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(10).toString()) : null);
					BusStandDtls.setAddaBookingAmt(
							currentRow.getCell(11) != null && currentRow.getCell(11).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(11).toString()) : null);
					BusStandDtls.setAdvBookingAmtByEBTM(
							currentRow.getCell(12) != null && currentRow.getCell(12).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(12).toString()) : null);
					BusStandDtls.setTotalAmt(
							currentRow.getCell(13) != null && currentRow.getCell(13).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(13).toString()) : null);
					
					
					mainObj.setBusStandBookingDtls(BusStandDtls);
					earningETMDtls = new EarningFromETM();
					earningETMDtls.setInspectDtls(
							currentRow.getCell(1) != null && currentRow.getCell(1).getCellType() == CellType.STRING
									? currentRow.getCell(1).toString() : null);
					earningETMDtls.setTotalRemittance(
							currentRow.getCell(60) != null && currentRow.getCell(60).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(60).toString()) : null);
					earningETMDtls.setTotalTicketIssued(
							currentRow.getCell(24) != null && currentRow.getCell(24).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(24).toString()) : null);
					earningETMDtls.setEarningPerKM(
							currentRow.getCell(62) != null && currentRow.getCell(62).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(62).toString()) : null);
					earningETMDtls.setNetAmtToDeposit(
							currentRow.getCell(61) != null && currentRow.getCell(61).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(61).toString()) : null);
					earningETMDtls.setEarningPerKmFree(
							currentRow.getCell(58) != null && currentRow.getCell(58).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(58).toString()) : null);
					earningETMDtls.setLoadFactor(
							currentRow.getCell(59) != null && currentRow.getCell(59).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(59).toString()) : null);
					mainObj.setEarning(earningETMDtls);
										etmTripTotalDtls = new EtmTripTotalDtls();
										etmTripTotalDtls.setTotalAdvBookingAmt(
												currentRow.getCell(11) != null && currentRow.getCell(11).getCellType() == CellType.NUMERIC
														? Double.parseDouble(currentRow.getCell(11).toString()) : null);
										etmTripTotalDtls.setTotalTripAmount(
												currentRow.getCell(12) != null && currentRow.getCell(12).getCellType() == CellType.NUMERIC
														? Double.parseDouble(currentRow.getCell(12).toString()) : null);
										etmTripTotalDtls.setTotalExpenses(
												currentRow.getCell(13) != null && currentRow.getCell(13).getCellType() == CellType.NUMERIC
														? Double.parseDouble(currentRow.getCell(13).toString()) : null);
										etmTripTotalDtls.setTotalDistance(
												currentRow.getCell(14) != null && currentRow.getCell(14).getCellType() == CellType.NUMERIC
														? (int) Double.parseDouble(currentRow.getCell(14).toString()) : null);
										etmTripTotalDtls.setTotalCollection(
												currentRow.getCell(15) != null && currentRow.getCell(15).getCellType() == CellType.NUMERIC
														? Double.parseDouble(currentRow.getCell(15).toString()) : null);
										etmTripTotalDtls.setRecords(
												currentRow.getCell(16) != null && currentRow.getCell(16).getCellType() == CellType.NUMERIC
														? (int) Double.parseDouble(currentRow.getCell(16).toString()) : null);
										mainObj.setEtmTripTotalDtls(etmTripTotalDtls);
										
					tripExpenses = new TripExpenses();
					tripExpenses.setTotal(
							currentRow.getCell(21) != null && currentRow.getCell(21).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(21).toString()) : null);
					tripExpenses.setMiscFee(
							currentRow.getCell(20) != null && currentRow.getCell(20).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(20).toString()) : null);
					tripExpenses.setRepairBill(
							currentRow.getCell(19) != null && currentRow.getCell(19).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(19).toString()) : null);
					tripExpenses.setDiesel(
							currentRow.getCell(17) != null && currentRow.getCell(17).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(17).toString()) : null);
					tripExpenses.setBusStandFee(
							currentRow.getCell(16) != null && currentRow.getCell(16).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(16).toString()) : null);
					tripExpenses.setTollTax(
							currentRow.getCell(14) != null && currentRow.getCell(14).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(14).toString()) : null);
					tripExpenses.setTicketRefund(
							currentRow.getCell(15) != null && currentRow.getCell(15).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(15).toString()) : null);
					mainObj.setTripExpenses(tripExpenses);
					passengerClassification = new PassengerClassification();
					passengerClassification.setTotalAdults(
							currentRow.getCell(22) != null && currentRow.getCell(22).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(22).toString()) : null);
					passengerClassification.setConcessionTicket(
							currentRow.getCell(35) != null && currentRow.getCell(35).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(35).toString()) : 0);
					passengerClassification.setAdvBooking(
							currentRow.getCell(36) != null && currentRow.getCell(36).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(36).toString()) : 0);
					passengerClassification.setTotalChilds(
							currentRow.getCell(23) != null && currentRow.getCell(23).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(23).toString()) : null);
					passengerClassification.setKmpl(
							currentRow.getCell(41) != null && currentRow.getCell(41).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(41).toString()) : null);
					passengerClassification.setTrafficInspection(
							currentRow.getCell(39) != null && currentRow.getCell(39).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(39).toString()) : null);
					passengerClassification.setTicketAmount(
							currentRow.getCell(40) != null && currentRow.getCell(40).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(40).toString()) : null);
					passengerClassification.setLuggageTicket(
							currentRow.getCell(37) != null && currentRow.getCell(37).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(37).toString()) : null);
					passengerClassification.setPassengerCounterBooking(
							currentRow.getCell(34) != null && currentRow.getCell(34).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(34).toString()) : null);
					passengerClassification.setPolicWarrent(
							currentRow.getCell(38) != null && currentRow.getCell(38).getCellType() == CellType.NUMERIC
									? (int) Double.parseDouble(currentRow.getCell(38).toString()) : null);
					mainObj.setPassengerClassification(passengerClassification);
					conductorEtm = new ConductorEtmBookingDetails();
					conductorEtm.setBoxAmt(
							currentRow.getCell(3) != null && currentRow.getCell(3).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(3).toString()) : null);
					conductorEtm.setTollAmt(
							currentRow.getCell(4) != null && currentRow.getCell(4).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(4).toString()) : null);
					conductorEtm.setConcessionAmt(
							currentRow.getCell(5) != null && currentRow.getCell(5).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(5).toString()) : null);
					conductorEtm.setLuggageAmt(
							currentRow.getCell(6) != null && currentRow.getCell(6).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(6).toString()) : null);
					conductorEtm.setTotalAmt(
							currentRow.getCell(7) != null && currentRow.getCell(7).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(7).toString()) : null);
					conductorEtm.setMiscAmt(
							currentRow.getCell(8) != null && currentRow.getCell(8).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(8).toString()) : null);
					conductorEtm.setPassengerAmt(
							currentRow.getCell(9) != null && currentRow.getCell(9).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(9).toString()) : null);
					mainObj.setConductorBookingDetails(conductorEtm);
					sheet2 = workbook.getSheet("etmTripDtls");
					etmTripDtlsRows = sheet2.iterator();
					int etmTripDtlsRowsCount = 0;
					while (etmTripDtlsRows.hasNext()) {
						Row etmTripDtlsRow = etmTripDtlsRows.next();
						Iterator<Cell> etmTripDtlsCellsInRow = etmTripDtlsRow.iterator();
						int etmTripDtlsCell = 0;
//						while (etmTripDtlsCellsInRow.hasNext()) {
//							Cell currentCell = etmTripDtlsCellsInRow.next();
//							if (etmTripDtlsRowsCount == 0 && currentCell.getCellType() == CellType.STRING && status) {
//								sheet2Header = etmTripDtlsheaders[etmTripDtlsCell]
//										.equals(currentCell.getStringCellValue());
//							}
//							etmTripDtlsCell++;
//						}
						if (etmTripDtlsRowsCount > 0) {
							EtmTripDtls etmTripDtlsObj = null;
							if (mainObj.getWayBillNo() != null && !mainObj.getWayBillNo().isEmpty()
									&& mainObj.getWayBillNo().equals(String.valueOf((int) Double.parseDouble(etmTripDtlsRow.getCell(0).toString())))) {
								etmTripDtlsObj = new EtmTripDtls();
								etmTripDtlsObj.setAdvBookingAmt(etmTripDtlsRow.getCell(1) != null
										&& etmTripDtlsRow.getCell(1).getCellType() == CellType.NUMERIC
												? Double.parseDouble(etmTripDtlsRow.getCell(1).toString()) : null);
								etmTripDtlsObj.setNetTripAmt(etmTripDtlsRow.getCell(2) != null
										&& etmTripDtlsRow.getCell(2).getCellType() == CellType.NUMERIC
												? Double.parseDouble(etmTripDtlsRow.getCell(2).toString()) : null);
								etmTripDtlsObj.setDistance(etmTripDtlsRow.getCell(3) != null
										&& etmTripDtlsRow.getCell(3).getCellType() == CellType.NUMERIC
												? (int) Double.parseDouble(etmTripDtlsRow.getCell(3).toString())
												: null);
								etmTripDtlsObj.setTripEndDateTime(etmTripDtlsRow.getCell(4) != null
										? etmTripDtlsRow.getCell(4).toString() : null);
								etmTripDtlsObj.setTripStartDateTime(etmTripDtlsRow.getCell(5) != null
										? etmTripDtlsRow.getCell(5).toString() : null);
								etmTripDtlsObj.setTotalCollection(etmTripDtlsRow.getCell(6) != null
										&& etmTripDtlsRow.getCell(6).getCellType() == CellType.NUMERIC
												? Double.parseDouble(etmTripDtlsRow.getCell(6).toString()) : null);
								etmTripDtlsObj.setRouteName(etmTripDtlsRow.getCell(7) != null
										&& etmTripDtlsRow.getCell(7).getCellType() == CellType.STRING
												? etmTripDtlsRow.getCell(7).toString() : null);
								etmTripDtlsObj.setExpenses(etmTripDtlsRow.getCell(8) != null
										&& etmTripDtlsRow.getCell(8).getCellType() == CellType.NUMERIC
												? Double.parseDouble(etmTripDtlsRow.getCell(8).toString()) : null);
								listEtmTripDtls.add(etmTripDtlsObj);
							}
						}
						etmTripDtlsRowsCount++;
					}
					mainObj.setEtmTripDtls(listEtmTripDtls);

					ETMStatewiseCollection statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(25) != null && currentRow.getCell(25).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(25).toString()) : null);
					statewiseCollection.setStateName("PUNJAB");
					listEtmStateWisedtls.add(statewiseCollection);
					statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(26) != null && currentRow.getCell(26).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(26).toString()) : null);
					statewiseCollection.setStateName("HARYANA");
					listEtmStateWisedtls.add(statewiseCollection);
					statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(27) != null && currentRow.getCell(27).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(27).toString()) : null);
					statewiseCollection.setStateName("H.P");
					listEtmStateWisedtls.add(statewiseCollection);
					statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(28) != null && currentRow.getCell(28).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(28).toString()) : null);
					statewiseCollection.setStateName("CHANDIGARH");
					listEtmStateWisedtls.add(statewiseCollection);
					statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(29) != null && currentRow.getCell(29).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(29).toString()) : null);
					statewiseCollection.setStateName("DELHI");
					listEtmStateWisedtls.add(statewiseCollection);
					statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(30) != null && currentRow.getCell(30).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(30).toString()) : null);
					statewiseCollection.setStateName("J&K");
					listEtmStateWisedtls.add(statewiseCollection);statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(31) != null && currentRow.getCell(31).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(31).toString()) : null);
					statewiseCollection.setStateName("RAJASTHAN");
					listEtmStateWisedtls.add(statewiseCollection);statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(32) != null && currentRow.getCell(32).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(32).toString()) : null);
					statewiseCollection.setStateName("UK");
					listEtmStateWisedtls.add(statewiseCollection);statewiseCollection = new ETMStatewiseCollection();
					statewiseCollection.setAmount(
							currentRow.getCell(33) != null && currentRow.getCell(33).getCellType() == CellType.NUMERIC
									? Double.parseDouble(currentRow.getCell(33).toString()) : null);
					statewiseCollection.setStateName("UP");
					listEtmStateWisedtls.add(statewiseCollection);
//					sheet3 = workbook.getSheet("etmStatewiseDtls");
//					etmStatewiseDtls = sheet3.iterator();
//					int etmStatewiseDtlsCount = 0;
//					while (etmStatewiseDtls.hasNext()) {
//						Row etmStatewiseDtlsRow = etmStatewiseDtls.next();
//						if (etmStatewiseDtlsCount > 0) {
//							ETMStatewiseCollection statewiseCollection = null;
//							if (mainObj.getWayBillNo() != null && !mainObj.getWayBillNo().isEmpty()
//									&& mainObj.getWayBillNo().equals(String.valueOf(
//											(int) Double.parseDouble(etmStatewiseDtlsRow.getCell(2).toString())))) {
//								statewiseCollection = new ETMStatewiseCollection();
//								statewiseCollection.setAmount(etmStatewiseDtlsRow.getCell(0) != null
//										&& etmStatewiseDtlsRow.getCell(0).getCellType() == CellType.NUMERIC
//												? Double.parseDouble(etmStatewiseDtlsRow.getCell(0).toString()) : null);
//								statewiseCollection.setStateName(etmStatewiseDtlsRow.getCell(1) != null
//										&& etmStatewiseDtlsRow.getCell(1).getCellType() == CellType.STRING
//												? etmStatewiseDtlsRow.getCell(1).toString() : null);
//								listEtmStateWisedtls.add(statewiseCollection);
//							}
//						}
//						etmStatewiseDtlsCount++;
//					}
					mainObj.setEtmStatewiseDtls(listEtmStateWisedtls);
					
					
					ConcessionDtls concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("POLICE");
					concession.setTickets(currentRow.getCell(42) != null && currentRow.getCell(42).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(42).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(43) != null && currentRow.getCell(43).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(43).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("OLD_WOMEN");
					concession.setTickets(currentRow.getCell(44) != null && currentRow.getCell(44).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(44).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(45) != null && currentRow.getCell(45).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(45).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("DISABLE");
					concession.setTickets(currentRow.getCell(46) != null && currentRow.getCell(46).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(46).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(47) != null && currentRow.getCell(47).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(47).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("CANCER");
					concession.setTickets(currentRow.getCell(48) != null && currentRow.getCell(48).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(48).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(49) != null && currentRow.getCell(49).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(49).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("FFIGHTER");
					concession.setTickets(currentRow.getCell(50) != null && currentRow.getCell(50).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(50).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(51) != null && currentRow.getCell(51).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(51).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("PRESS");
					concession.setTickets(currentRow.getCell(52) != null && currentRow.getCell(52).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(52).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(53) != null && currentRow.getCell(53).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(53).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("PWT_TICKETS");
					concession.setTickets(currentRow.getCell(54) != null && currentRow.getCell(54).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(54).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(55) != null && currentRow.getCell(55).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(55).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					concession.setDiscPercentage(100);
					concession.setPassType("OTHER");
					concession.setTickets(currentRow.getCell(56) != null && currentRow.getCell(56).getCellType() == CellType.NUMERIC
							? (int)Double.parseDouble(currentRow.getCell(56).toString()) : 0);
					concession.setNetAmount(currentRow.getCell(57) != null && currentRow.getCell(57).getCellType() == CellType.NUMERIC
							? Double.parseDouble(currentRow.getCell(57).toString()) : 0);
					listConcessionDtls.add(concession);
					concession = new ConcessionDtls();
					mainObj.setConcessionDtls(listConcessionDtls);

//					sheet4 = workbook.getSheet("concessionDtls");
//					concessionDtls = sheet4.iterator();
//					int concessionDtlsCount = 0;
//					while (concessionDtls.hasNext()) {
//						Row concessionDtlsRow = concessionDtls.next();
//						if (concessionDtlsCount > 0) {
//							ConcessionDtls concession = null;
//							if (mainObj.getWayBillNo() != null && !mainObj.getWayBillNo().isEmpty()
//									&& mainObj.getWayBillNo().equals(String.valueOf(
//											(int) Double.parseDouble(concessionDtlsRow.getCell(4).toString())))) {
//								concession = new ConcessionDtls();
//								concession.setDiscPercentage(concessionDtlsRow.getCell(0) != null
//										&& concessionDtlsRow.getCell(0).getCellType() == CellType.NUMERIC
//												? (int) Double.parseDouble(concessionDtlsRow.getCell(0).toString())
//												: null);
//								concession.setPassType(concessionDtlsRow.getCell(1) != null
//										&& concessionDtlsRow.getCell(1).getCellType() == CellType.STRING
//												? concessionDtlsRow.getCell(1).toString() : null);
//								concession.setTickets(concessionDtlsRow.getCell(2) != null
//										&& concessionDtlsRow.getCell(2).getCellType() == CellType.NUMERIC
//												? (int) Double.parseDouble(concessionDtlsRow.getCell(2).toString())
//												: null);
//								concession.setNetAmount(concessionDtlsRow.getCell(3) != null
//										&& concessionDtlsRow.getCell(3).getCellType() == CellType.NUMERIC
//												? Double.parseDouble(concessionDtlsRow.getCell(3).toString()) : null);
//								listConcessionDtls.add(concession);
//							}
//						}
//						concessionDtlsCount++;
//					}
					mainObj.setConcessionDtls(listConcessionDtls);
					
					response = etmController.submitETMData(mainObj);
				}

				if (rowIndex > 0 && response.getStatusCode().equals(HttpStatus.OK)
						&& response.getBody().getStatus() == 403) {
					ResponseMessage message = new ResponseMessage("waybill " +
							mainObj.getWayBillNo() + " " + response.getBody().getMessage());
					errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
				}
				if (rowIndex > 0 && response.getStatusCode().equals(HttpStatus.OK)
						&& response.getBody().getStatus() == 200) {
					ResponseMessage message = new ResponseMessage(
							mainObj.getWayBillNo() + " " + response.getBody().getMessage());
					errorLog.add(new ResponseEntity<>(message, HttpStatus.OK));
				}
				rowIndex++;

			}
		}

		workbook.close();
		errorLog.addAll(headerErrorLog);
		boolean uploadStatus=errorLog.size()==0?uploadFile(file, "SubmitEtm"):false;
		return errorLog;

	}

	public List<IssueEtmObjDto> returnIssueEtmObj(InputStream is) {
		try {
			List<IssueEtmObjDto> objs = new ArrayList<>();
			List<ResponseEntity<ResponseMessage>> errorLog = new ArrayList<>();
			Workbook workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheet("IssueEtm");
			Iterator<Row> rows = sheet.iterator();
			// List<EtmTBAssignment> tutorials = new
			// ArrayList<EtmTBAssignment>();
			int rowNumber = 0;
			boolean conductorHeader = true;
			boolean routeHeader = true;
			boolean etmHeader = true;
			boolean waybillHeader = true;
			boolean dateHeader = true;
			boolean conductorNameheader = true;
			boolean routeNameHeader = true;
			boolean busNumberHeader = true;
			boolean tripNameHeader = true;
			while (rows.hasNext()) {
				IssueEtmObjDto dto = new IssueEtmObjDto();
				Row currentRow = rows.next();
				ConductorMaster conductorId = null;
				ETMMaster etmMaster = null;
				RouteMaster routeId = null;
				Date date = null;
				EtmTBAssignment waybill = null;

				// skip header
				// if (rowNumber == 0) {
				//
				// rowNumber++;
				// continue;
				// }
				Iterator<Cell> cellsInRow = currentRow.iterator();
				EtmTBAssignment tutorial = new EtmTBAssignment();
				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					switch (cellIdx) {
					case 0:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Conductor_Name")) {
								conductorNameheader = true;
							} else {
								conductorNameheader = false;
								ResponseMessage message = new ResponseMessage("Conductor Name header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 1:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Conductor_Code")) {
								conductorHeader = true;
							} else {
								conductorHeader = false;
								ResponseMessage message = new ResponseMessage("Conductor header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (currentCell.getCellType() == CellType.STRING && conductorHeader) {
							conductorId = conductorRepo.findByConductorName(currentCell.getStringCellValue());
						}
						break;
					case 5:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Etm_Number")) {
								etmHeader = true;
							} else {
								etmHeader = false;
								ResponseMessage message = new ResponseMessage("etm header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (currentCell.getCellType() == CellType.NUMERIC && etmHeader) {
							etmMaster = etmMasterRepo.findByEtmNumber(String.valueOf((int) currentCell.getNumericCellValue()));
						}
						break;
					case 3:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Route_Code")) {
								routeHeader = true;
							} else {
								routeHeader = false;
								ResponseMessage message = new ResponseMessage("Route header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (currentCell.getCellType() == CellType.STRING && routeHeader) {
							routeId = routeRepo.findByRouteCode(currentCell.getStringCellValue());
						}
						break;
					case 4:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Route_Name")) {
								routeNameHeader = true;
							} else {
								routeNameHeader = false;
								ResponseMessage message = new ResponseMessage("Route Name header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 2:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Date_Time(DD/MM/YYYY hh:mm:ss)")) {
								dateHeader = true;
							} else {
								dateHeader = false;
								ResponseMessage message = new ResponseMessage("Date time header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (dateHeader && rowNumber >= 1) {
							date = currentCell.getDateCellValue();
						}
						break;
					case 6:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Bus_Number")) {
								busNumberHeader = true;
							} else {
								busNumberHeader = false;
								ResponseMessage message = new ResponseMessage("Busnumber header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 7:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("Trip_Name")) {
								tripNameHeader = true;
							} else {
								tripNameHeader = false;
								ResponseMessage message = new ResponseMessage("trip name header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						break;
					case 8:
						if (rowNumber == 0) {
							if (currentCell.getStringCellValue().equals("WayBill_Number")) {
								waybillHeader = true;
							} else {
								waybillHeader = false;
								ResponseMessage message = new ResponseMessage("Waybill header is wrong!");
								errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
							}
						}
						if (waybillHeader && rowNumber >= 1) {
							waybill = repository
									.findByWaybillNo(String.valueOf((int) currentCell.getNumericCellValue()));
						}
						break;
					}
					cellIdx++;
				}

				if (conductorId != null && etmMaster != null && routeId != null && date != null && waybill == null) {
					cellsInRow = currentRow.iterator();
					cellIdx = 0;
					while (cellsInRow.hasNext()) {
						Cell currentCell = cellsInRow.next();
						switch (cellIdx) {
						case 1:
							tutorial.setConductorId(conductorId);
							dto.setConductorCode(conductorId.getConductorCode());
							dto.setConductorName(conductorId.getConductorName());
							dto.setConductorId(conductorId.getId());
							break;
						case 2:
							tutorial.setEtmAssignmentDate(currentCell.getDateCellValue());
							break;
						case 5:
							tutorial.setEtmId(etmMaster);
							dto.setEtmNumber(etmMaster.getEtmNumber());
							dto.setEtmId(etmMaster.getId());
							break;
						case 8:
							tutorial.setWayBillNo(String.valueOf((int) currentCell.getNumericCellValue()));
							dto.setWayBill(String.valueOf((int) currentCell.getNumericCellValue()));
							break;
						case 3:
							tutorial.setRouteId(routeId);
							dto.setRouteName(routeId.getRouteName());
							dto.setRouteCode(routeId.getRouteCode());
							break;
						}
						cellIdx++;
						tutorial.setEtmSubmitStatus(false);
						tutorial.setTbSubmitStatus(false);
						// tutorials.add(tutorial);

					}
					objs.add(dto);
				} else {
					boolean s = false;
					if (conductorHeader && etmHeader && routeHeader && dateHeader && waybillHeader
							&& conductorNameheader && routeNameHeader && busNumberHeader && tripNameHeader) {
						s = true;
					}
					if (rowNumber > 0 && conductorId == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has conductor data wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && etmMaster == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has etm data wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && routeId == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has route data wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && date == null && s) {
						ResponseMessage message = new ResponseMessage(
								"Row " + currentRow.getRowNum() + " has date value wrong!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
					if (rowNumber > 0 && waybill != null && s) {
						ResponseMessage message = new ResponseMessage(
								"waybill " + waybill.getWayBillNo() + " is already entered!");
						errorLog.add(new ResponseEntity<>(message, HttpStatus.FORBIDDEN));
					}
				}
				rowNumber++;

			}
			workbook.close();
			// repository.saveAll(tutorials);
			return objs;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}

	}
}
