package com.idms.base.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.idms.base.dao.entity.FuelType;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.FuelTypeRepository;
import com.idms.base.dao.repository.TankAndCapacityRepository;
import com.idms.base.service.TankAndCapacityService;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class TankAndCapacityServiceImpl implements  TankAndCapacityService{
	
	@Autowired 
	private ModelMapper mapper;
	
	@Autowired
	TankAndCapacityRepository tankAndCapacityRepository;
	
	@Autowired
	DepotMasterRepository depotRepo;
	 
	@Autowired
	FuelTypeRepository fuelTypeRepository;
	@Override
	public List<DepotMasterDto> findAllDepot() {
		log.info("Entering into findAllDepot service");
		List<DepotMasterDto> depoList = new ArrayList<>();
		try {
			List<Object[]> depo= tankAndCapacityRepository.findAllDepot();
			for(Object[] o: depo) {
				DepotMasterDto dto = new DepotMasterDto();
				dto.setId(Integer.parseInt(o[0].toString()));
				dto.setDepotName(o[1].toString());
				
				depoList.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return depoList;
	}

	@Override
	public List<TotalTankAndCapacityDto> listOfTankCapacityReport(Integer dpcode,Date date) {
		log.info("Entering into listOfTankCapacityReport service");
		List<TotalTankAndCapacityDto> totalTankAndCapacityDto = new ArrayList<>();
		try {
			List<Object[]> depo= tankAndCapacityRepository.listOfTankCapacityReport(dpcode,date);
			for(Object[] o: depo) {
				TotalTankAndCapacityDto dto = new TotalTankAndCapacityDto();
				dto.setDepot(o[0].toString());
				dto.setTotalTanks(o[1].toString());
				dto.setTotalCapacity(o[3].toString());
				dto.setTankCapacity(o[2].toString());
				//dto.setSum(Float.parseFloat(o[2].toString()));
				totalTankAndCapacityDto.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return totalTankAndCapacityDto;
	}


	@Override
	public List<ExplosiveLicenseReportDto> listOfexplosiveReport(Integer dpcode,Date date) {
		List<ExplosiveLicenseReportDto> explosiveLicenseReportDto = new ArrayList<ExplosiveLicenseReportDto>();
		try {
			List<Object[]> depo= tankAndCapacityRepository.listOfexplosiveReport(dpcode,date);
			for(Object[] o: depo) {
				ExplosiveLicenseReportDto dto = new ExplosiveLicenseReportDto();
				dto.setDepotName(o[0].toString());
				dto.setExplosiveLicenseValidity(o[1].toString());
				dto.setExplosive_licence_id(Integer.parseInt(o[2].toString()));
				dto.setLicenseDetails(o[3].toString());
				dto.setDocument_path(o[4].toString());
				explosiveLicenseReportDto.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return explosiveLicenseReportDto;
	}
	
	@Override
	public List<DispensingUnitReportDto> listOfDispensingReport( Integer dpcode) {
		List<DispensingUnitReportDto> dispensingUnitReportDto = new ArrayList<>();
		try {
			List<Object[]> depo= tankAndCapacityRepository.listOfDispensingReport(dpcode);
			for(Object[] o: depo) {
				DispensingUnitReportDto dto = new DispensingUnitReportDto();
				dto.setDepotName(o[0].toString());
				dto.setDispensingUnitName(o[1].toString());
				dto.setDisUnitTypeName(o[2].toString());
				dto.setLastOperatedDate(o[3].toString());
				dispensingUnitReportDto.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dispensingUnitReportDto;
	}
	
	@Override
	public List<KmplComparisonReportDto> listOfKMPLComparisonReport(Date fromDate, Date toDate,Integer routeId) {
		List<KmplComparisonReportDto> kmplComparisonReportDto = new ArrayList<>();
		try {
			List<Object[]> depo= tankAndCapacityRepository.listOfKMPLComparisonReport(fromDate,toDate,routeId);
			for(Object[] o: depo) {
				KmplComparisonReportDto dto = new KmplComparisonReportDto();
				if(o[0]!=null){
					dto.setRouteId(o[0].toString());
					}
		        //dto.setRouteCode(o[0].toString());
				//dto.setRouteNo(o[0].toString());
				if(o[1]!=null){
				dto.setRouteName(o[1].toString());
				}
				if(o[2]!=null){
				dto.setRouteCategory(o[2].toString());
				}
				if(o[3]!=null){
				dto.setBusRegistrationNumber(o[3].toString());
				}
				if(o[4]!=null){
				dto.setScheduledKMS(o[4].toString());
				}
				if(o[5] != null){
				dto.setKmplScheduledKms(o[5].toString());
				}
				if(o[6]!= null){
				dto.setVtsKms(o[6].toString());
				}
				if(o[7]!= null){
				dto.setKmsAsPerVtsKms(o[7].toString());
				}
				
				kmplComparisonReportDto.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return kmplComparisonReportDto;
	}

	@Override
	public List<FuelDispenserWiseReport> listOfFuelDispenserReport(Integer depotId,Date fromDate, Date toDate) {
		List<FuelDispenserWiseReport> fuelDispenserWiseReport = new ArrayList<>();
		try {
			//DepotMaster depot = depotRepo.findByDepotCode(dpcode);
			List<Object[]> depo= tankAndCapacityRepository.listOfFuelDispenserReport(depotId,fromDate,toDate);
			for(Object[] o: depo) {
				FuelDispenserWiseReport dto = new FuelDispenserWiseReport();
				dto.setDepotName(o[0].toString());
				dto.setDisUnitCode(o[1].toString());
				dto.setStartingReading(o[3].toString());
				dto.setLasReading(o[4].toString());
				dto.setFuelIssuanceDate(o[5].toString());
				dto.setTotal(o[6].toString());
				dto.setBusRegNumber(o[2].toString());
				dto.setTransUnit(o[7].toString());
				fuelDispenserWiseReport.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return fuelDispenserWiseReport;
	}

	@Override
	public List<SupplyReceivedReportDto> supplyReceivedReport(Integer depoId,Date from,Date to) {
		List<SupplyReceivedReportDto> supplyReceivedReportDto = new ArrayList<SupplyReceivedReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.supplyReceivedReport(depoId,from,to);
			for (Object[] objects : supply) {
				SupplyReceivedReportDto supply1= new SupplyReceivedReportDto();
				
				if (objects[0]!=null) {
					supply1.setDepotName(objects[0].toString());
				}
				if (objects[1]!=null) {
					supply1.setDateOfSupplyReceived(objects[1].toString());
				}
				if (objects[2]!=null) {
					supply1.setQuantityReceived(objects[2].toString());
				}
				if (objects[3]!=null) {
					supply1.setRateLiterReceived(objects[3].toString());
				}
				if (objects[4]!=null) {
					supply1.setInvoiceNo(objects[4].toString());
				}
				if (objects[5]!=null) {
					supply1.setSupplyReceivetime(objects[5].toString());
			    }
								
				supplyReceivedReportDto.add(supply1);
			}
			
		} catch (Exception e) {
			log.info("supplyReceivedReportServiceImpl " + e);
		}
		return supplyReceivedReportDto;
	}

	@Override
	public List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmpl( String busNo,Date startDate, Date endDate) {
		List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmplDtoList = new ArrayList<BusWiseDriverRouteKmplDto>();
		try {
			if(busNo.equalsIgnoreCase("0"))
				busNo="";
			List<Object[]> bus = tankAndCapacityRepository.busWiseDriverRouteKmpl(startDate,endDate,busNo);
			for (Object[] objects : bus) {
				BusWiseDriverRouteKmplDto busWise= new BusWiseDriverRouteKmplDto();
				busWise.setBusNo(objects[0].toString());
			//	busWise.setMobileNo(objects[1].toString());
				busWise.setDriverName(objects[1].toString());
				busWise.setRouteName(objects[2].toString());
				busWise.setKmpl(objects[3].toString());
				busWiseDriverRouteKmplDtoList.add(busWise);
			}
			
		} catch (Exception e) {
			log.info("supplyReceivedReportServiceImpl " + e);
		}
		return busWiseDriverRouteKmplDtoList;
	}

	@Override
	public List<BusWiseDriverRouteKmplDto> routeWiseBusDriverKmpl(Date startDate, Date endDate,Integer driverId) {
		List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmplDtoList = new ArrayList<BusWiseDriverRouteKmplDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.routeWiseBusDriverKmpl(startDate,endDate,driverId);
			for (Object[] objects : supply) {
				BusWiseDriverRouteKmplDto busWise= new BusWiseDriverRouteKmplDto();
				busWise.setRouteName(objects[0].toString());
				//busWise.setMobileNo(objects[1].toString());
				busWise.setDriverName(objects[1].toString());
				busWise.setBusNo(objects[2].toString());
				busWise.setKmpl(objects[3].toString());
				busWiseDriverRouteKmplDtoList.add(busWise);
			}
			
		} catch (Exception e) {
			log.info("supplyReceivedReportServiceImpl " + e);
		}
		return busWiseDriverRouteKmplDtoList;
	}

	@Override
	public List<BusWiseDriverRouteKmplDto> busTypeBusWiseDieselIssuanceReport(String bus,Date startDate, Date endDate,String busType) {
		List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmplDtoList = new ArrayList<BusWiseDriverRouteKmplDto>();
		try {
			if(bus.equalsIgnoreCase("0"))
				bus="";
			List<Object[]> supply = tankAndCapacityRepository.busTypeBusWiseDieselIssuanceReport(bus,startDate,endDate,busType);
			for (Object[] objects : supply) {
				BusWiseDriverRouteKmplDto busWise= new BusWiseDriverRouteKmplDto();
				//busWise.setBusTypeId(objects[0].toString());
				//busWise.setBusId(objects[1].toString());
				busWise.setBusType(objects[0].toString());
				busWise.setBusNo(objects[1].toString());
				busWise.setCategory(objects[2].toString());
				busWise.setDiselIssued(objects[3].toString());
				busWise.setIssuanceDate(objects[4].toString());
				busWiseDriverRouteKmplDtoList.add(busWise);
			}
			
		} catch (Exception e) {
			log.info("supplyReceivedReportServiceImpl " + e);
		}
		return busWiseDriverRouteKmplDtoList;
	}

	@Override
	public List<IssuanceReceiptStmntReportDto> issuanceReceiptStmntReport(Date startDate, Date endDate) {
		List<IssuanceReceiptStmntReportDto> issuanceReceiptStmntReportDtoList = new ArrayList<IssuanceReceiptStmntReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.issuanceReceiptStmntReport(startDate,endDate);
			for (Object[] objects : supply) {
				IssuanceReceiptStmntReportDto supply1= new IssuanceReceiptStmntReportDto();
				if(objects[0]!=null)
				supply1.setRouteCode(objects[0].toString());
				//supply1.setRouteNo(objects[0].toString());
				if(objects[1]!=null)
				supply1.setRouteName(objects[1].toString());
				if(objects[2]!=null)
				supply1.setBusNumber(objects[2].toString());
				if(objects[3]!=null)
				supply1.setDieselIssuedInLtr(objects[3].toString());
				if(objects[4]!=null)
				supply1.setValueInRs(objects[4].toString());
				if(objects[5]!=null)
				supply1.setIssuanceDate(objects[5].toString());
				if(objects[6]!=null)
				supply1.setReceiptInRs(objects[6].toString());
				if(objects[7]!=null)
				supply1.setVTSKms(objects[7].toString());
				if(objects[8]!=null)
				supply1.setTransportunit(objects[8].toString());
				if(objects[9]!=null)
					supply1.setSchKMs(objects[9].toString());
				issuanceReceiptStmntReportDtoList.add(supply1);
			}
			
		} catch (Exception e) {
			log.info("supplyReceivedReportServiceImpl " + e);
		}
		return issuanceReceiptStmntReportDtoList;
	}

	@Override
	public List<IssuanceReceiptStmntReportDto> deadKMSReport(Integer routeId,String fromDate) {
		List<IssuanceReceiptStmntReportDto> issuanceReceiptStmntReportDtoList = new ArrayList<IssuanceReceiptStmntReportDto>();
		Date startDate = null;
		try {
			if(fromDate != null)
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			List<Object[]> supply = tankAndCapacityRepository.deadKMSReport(routeId,startDate);
			for (Object[] objects : supply) {
				IssuanceReceiptStmntReportDto supply1= new IssuanceReceiptStmntReportDto();
				if(objects[0]!=null)
				supply1.setRouteId(objects[0].toString());
				if(objects[1]!=null)
				supply1.setRouteName(objects[1].toString());
				if(objects[2]!=null)
				supply1.setRouteDate(objects[2].toString());
				if(objects[3]!=null)
					supply1.setDeadKms(objects[3].toString());
				if(objects[4]!=null)
					supply1.setExtraKMs(objects[4].toString());
				issuanceReceiptStmntReportDtoList.add(supply1);
			}
			
		} catch (Exception e) {
			log.info("supplyReceivedReportServiceImpl " + e);
		}
		return issuanceReceiptStmntReportDtoList;
	}

	@Override
	public List<BusWiseDriverRouteKmplDto> bestWorstDriverReport(Date startDate, Date endDate,Integer depoId,Double startRange,Double endRange) {
		List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmplDtoList = new ArrayList<BusWiseDriverRouteKmplDto>();
		try {
			DecimalFormat df=new DecimalFormat("0.00");
			List<Object[]> supply = tankAndCapacityRepository.bestWorstDriverReport(startDate,endDate,depoId,startRange,endRange);
			for (Object[] objects : supply) {
				BusWiseDriverRouteKmplDto supply1= new BusWiseDriverRouteKmplDto();
				//supply1.setMobileNo(objects[0].toString());
				
				supply1.setDriverName(objects[0].toString());
				supply1.setKmpl(objects[1].toString());	
				supply1.setRouteName(df.format(Double.parseDouble(objects[2].toString())));
				busWiseDriverRouteKmplDtoList.add(supply1);
			}
			
		} catch (Exception e) {
			log.info("bestWorstDriverReportServiceImpl " + e);
		}
		return busWiseDriverRouteKmplDtoList;
	}

	@Override
	public List<DueDateCleaningReportDto> dueDateCleaningReport(Integer depoId,Date date) {
		List<DueDateCleaningReportDto> dueDateCleaningReportDto = new ArrayList<DueDateCleaningReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.dueDateCleaningReport(depoId,date);
			for (Object[] objects : supply) {
				DueDateCleaningReportDto supply1= new DueDateCleaningReportDto();
				supply1.setDepotName(objects[0].toString());
				supply1.setDueDateCleaning(objects[1].toString());
				supply1.setTankName(objects[2].toString());
				
				
				dueDateCleaningReportDto.add(supply1);
			}
			
		} catch (Exception e) {
			log.info("bestWorstDriverReportServiceImpl " + e);
		}
		return dueDateCleaningReportDto;
	}

	@Override
	public List<InspectionDueDateReportDto> inspectionDueDateReport(Integer depoId,Date date) {
		List<InspectionDueDateReportDto> inspectionDueDateReportDto = new ArrayList<InspectionDueDateReportDto>();
		
			List<Object[]> supply = tankAndCapacityRepository.inspectionDueDateReport(depoId,date);
			for (Object[] objects : supply) {
				InspectionDueDateReportDto inspectionDueDateReportDtoObj= new InspectionDueDateReportDto();
				inspectionDueDateReportDtoObj.setDepotName(objects[0].toString());
				inspectionDueDateReportDtoObj.setInspectionDate(objects[1].toString());
				inspectionDueDateReportDtoObj.setNextDueDate(objects[2].toString());
				inspectionDueDateReportDtoObj.setStatus(objects[3].toString());
				inspectionDueDateReportDto.add(inspectionDueDateReportDtoObj);
			//InspectionDueDateReportDto inspectionDueDateReportDtoObj= new InspectionDueDateReportDto();
			/*inspectionDueDateReportDtoObj.setDepotName("DP-JAL-1");
			inspectionDueDateReportDtoObj.setInspectionDate("6-9-2021");
			inspectionDueDateReportDtoObj.setNextDueDate("10-10-2022");
			inspectionDueDateReportDtoObj.setStatus("ok");
			inspectionDueDateReportDto.add(inspectionDueDateReportDtoObj);*/
			}
		return inspectionDueDateReportDto;
	}

	@Override
	public List<DepotWiseDieselStockReportDto> depotWiseDieselStockReport(Integer depoId ,Date startDate, Date endDate,Integer fuelTypeId) {
		List<DepotWiseDieselStockReportDto> depotWiseDieselStockReportDto = new ArrayList<DepotWiseDieselStockReportDto>();
		try {
			
			FuelType typeObejct=fuelTypeRepository.findById(fuelTypeId).get();
			if(typeObejct.getShortCode().equals("Diesel")){
				List<Object[]> supply = tankAndCapacityRepository.depotWiseDieselStockReport(depoId,startDate,endDate);
				for (Object[] objects : supply) {
					DepotWiseDieselStockReportDto depotWiseDieselStockReportDtoObj= new DepotWiseDieselStockReportDto();
					if(objects[0] != null)
					depotWiseDieselStockReportDtoObj.setDepot(objects[0].toString());
					if(objects[1] != null)
					depotWiseDieselStockReportDtoObj.setStockDate(objects[1].toString());
					if(objects[2] != null)
					depotWiseDieselStockReportDtoObj.setAvailableDieselStock(objects[2].toString());
					if(objects[3] != null)
					depotWiseDieselStockReportDtoObj.setValueOfDieselStock(objects[3].toString());
					depotWiseDieselStockReportDto.add(depotWiseDieselStockReportDtoObj);
				}
			}else if(typeObejct.getShortCode().equals("Mobil Oil")){
				List<Object[]> supply = tankAndCapacityRepository.depotWiseMobilOilStockReport(depoId,startDate,endDate);
				for (Object[] objects : supply) {
					DepotWiseDieselStockReportDto depotWiseDieselStockReportDtoObj= new DepotWiseDieselStockReportDto();
					if(objects[0] != null)
					depotWiseDieselStockReportDtoObj.setDepot(objects[0].toString());
					if(objects[1] != null)
					depotWiseDieselStockReportDtoObj.setStockDate(objects[1].toString());
					if(objects[2] != null)
					depotWiseDieselStockReportDtoObj.setAvailableDieselStock(objects[2].toString());
					if(objects[3] != null)
					depotWiseDieselStockReportDtoObj.setValueOfDieselStock(objects[3].toString());
					depotWiseDieselStockReportDto.add(depotWiseDieselStockReportDtoObj);
				}
				
			}else if(typeObejct.getShortCode().equals("Ad Blue")){
				List<Object[]> supply = tankAndCapacityRepository.depotWiseAdBlueStockReport(depoId,startDate,endDate);
				for (Object[] objects : supply) {
					DepotWiseDieselStockReportDto depotWiseDieselStockReportDtoObj= new DepotWiseDieselStockReportDto();
					if(objects[0] != null)
					depotWiseDieselStockReportDtoObj.setDepot(objects[0].toString());
					if(objects[1] != null)
					depotWiseDieselStockReportDtoObj.setStockDate(objects[1].toString());
					if(objects[2] != null)
					depotWiseDieselStockReportDtoObj.setAvailableDieselStock(objects[2].toString());
					if(objects[3] != null)
					depotWiseDieselStockReportDtoObj.setValueOfDieselStock(objects[3].toString());
					depotWiseDieselStockReportDto.add(depotWiseDieselStockReportDtoObj);
				}
				
			}
					
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return depotWiseDieselStockReportDto;
	}

	@Override
	public List<DepotWiseDieselStockReportDto> advancePaymentReport(Date startDate, Date endDate, Integer depoId) {
		List<DepotWiseDieselStockReportDto> depotWiseDieselStockReportDto = new ArrayList<DepotWiseDieselStockReportDto>();
		try {
			/*List<Object[]> supply = tankAndCapacityRepository.advancePaymentReport(startDate,endDate,depoId);
			for (Object[] objects : supply) {
				DepotWiseDieselStockReportDto depotWiseDieselStockReportDtoObj= new DepotWiseDieselStockReportDto();
				depotWiseDieselStockReportDtoObj.setDepot(objects[0].toString());
				depotWiseDieselStockReportDtoObj.setAdvancePaymentInRs(objects[1].toString());
				depotWiseDieselStockReportDto.add(depotWiseDieselStockReportDtoObj);*/
			
			
		} catch (Exception e) {
			//log.info("TankAndCapacityServiceImpl " + e);
		}
		return depotWiseDieselStockReportDto;
	}

	@Override
	public List<KMPLReportDto> kmplReport(Date startDate, Date endDate, Integer busType, String grouping,
			Integer depoId, String select) {
		List<KMPLReportDto> kMPLReportDto = new ArrayList<KMPLReportDto>();
		List<Object[]> supply = null;
		try {
			if(select.equalsIgnoreCase("Driver-wise,Route-wise,Bus-wise")){
			supply = tankAndCapacityRepository.driverwiseRoutewiseBuswiseKmplReport(startDate,endDate,busType,grouping,depoId,select);
			for (Object[] objects : supply) {
				KMPLReportDto kMPLReport= new KMPLReportDto();
				if(objects[2] != null)
				kMPLReport.setBusRegNo(objects[2].toString());
				if(objects[0] != null)
				kMPLReport.setDriverName(objects[0].toString());
				if(objects[1] != null)
				kMPLReport.setRouteName(objects[1].toString());
				if(objects[3] != null)
				kMPLReport.setDepotName(objects[3].toString());
				if(objects[4] != null)
				kMPLReport.setBusType(objects[4].toString());
				if(objects[5] != null)
				kMPLReport.setGrouping(objects[5].toString());
				if(objects[6] != null)
				kMPLReport.setRotation(objects[6].toString());
				if(objects[7] != null)
				kMPLReport.setKmpl(objects[7].toString());
				if(objects[8] != null)
				kMPLReport.setStartDate(objects[8].toString());
				if(objects[9] != null)
				kMPLReport.setEndDate(objects[9].toString());
				if(objects[10] != null)
					kMPLReport.setRefuelDate(objects[10].toString());
				if(objects[11] != null)
					kMPLReport.setVtsKMPL(objects[11].toString());
				
				kMPLReportDto.add(kMPLReport);
			}
			} else if(select.equalsIgnoreCase("Driver-wise,Bus-wise,Route-wise")){
				supply = tankAndCapacityRepository.driverwiseBuswiseRoutewiseKmplReport(startDate,endDate,busType,grouping,depoId,select);
				for (Object[] objects : supply) {
					KMPLReportDto kMPLReport= new KMPLReportDto();
					if(objects[1] != null)
					kMPLReport.setBusRegNo(objects[1].toString());
					if(objects[0] != null)
					kMPLReport.setDriverName(objects[0].toString());
					if(objects[2] != null)
					kMPLReport.setRouteName(objects[2].toString());
					if(objects[3] != null)
					kMPLReport.setDepotName(objects[3].toString());
					if(objects[4] != null)
					kMPLReport.setBusType(objects[4].toString());
					if(objects[5] != null)
					kMPLReport.setGrouping(objects[5].toString());
					if(objects[6] != null)
					kMPLReport.setRotation(objects[6].toString());
					if(objects[7] != null)
					kMPLReport.setKmpl(objects[7].toString());
					if(objects[8] != null)
					kMPLReport.setStartDate(objects[8].toString());
					if(objects[9] != null)
					kMPLReport.setEndDate(objects[9].toString());
					if(objects[10] != null)
						kMPLReport.setRefuelDate(objects[10].toString());
					if(objects[11] != null)
						kMPLReport.setVtsKMPL(objects[11].toString());

					kMPLReportDto.add(kMPLReport);
				}
			}else if(select.equalsIgnoreCase("Bus-wise,Driver-wise,Route-wise")){
				supply = tankAndCapacityRepository.buswiseDriverwiseRoutewiseKmplReport(startDate,endDate,busType,grouping,depoId,select);
				for (Object[] objects : supply) {
					KMPLReportDto kMPLReport= new KMPLReportDto();
					if(objects[0] != null)
					kMPLReport.setBusRegNo(objects[0].toString());
					if(objects[1] != null)
					kMPLReport.setDriverName(objects[1].toString());
					if(objects[2] != null)
					kMPLReport.setRouteName(objects[2].toString());
					if(objects[3] != null)
						kMPLReport.setDepotName(objects[3].toString());
						if(objects[4] != null)
						kMPLReport.setBusType(objects[4].toString());
						if(objects[5] != null)
						kMPLReport.setGrouping(objects[5].toString());
						if(objects[6] != null)
						kMPLReport.setRotation(objects[6].toString());
						if(objects[7] != null)
						kMPLReport.setKmpl(objects[7].toString());
						if(objects[8] != null)
						kMPLReport.setStartDate(objects[8].toString());
						if(objects[9] != null)
						kMPLReport.setEndDate(objects[9].toString());
						if(objects[10] != null)
							kMPLReport.setRefuelDate(objects[10].toString());
						if(objects[11] != null)
							kMPLReport.setVtsKMPL(objects[11].toString());
						
					kMPLReportDto.add(kMPLReport);
				}
			} else if(select.equalsIgnoreCase("Bus-wise,Route-wise,Driver-wise")){
				supply = tankAndCapacityRepository.buswiseRoutewiseDriverwisekmplReport(startDate,endDate,busType,grouping,depoId,select);
				for (Object[] objects : supply) {
					KMPLReportDto kMPLReport= new KMPLReportDto();
					if(objects[0] != null)
					kMPLReport.setBusRegNo(objects[0].toString());
					if(objects[2] != null)
					kMPLReport.setDriverName(objects[2].toString());
					if(objects[1] != null)
					kMPLReport.setRouteName(objects[1].toString());
					if(objects[3] != null)
						kMPLReport.setDepotName(objects[3].toString());
						if(objects[4] != null)
						kMPLReport.setBusType(objects[4].toString());
						if(objects[5] != null)
						kMPLReport.setGrouping(objects[5].toString());
						if(objects[6] != null)
						kMPLReport.setRotation(objects[6].toString());
						if(objects[7] != null)
						kMPLReport.setKmpl(objects[7].toString());
						if(objects[8] != null)
						kMPLReport.setStartDate(objects[8].toString());
						if(objects[9] != null)
						kMPLReport.setEndDate(objects[9].toString());
						if(objects[10] != null)
							kMPLReport.setRefuelDate(objects[10].toString());
						if(objects[11] != null)
							kMPLReport.setVtsKMPL(objects[11].toString());
						
					kMPLReportDto.add(kMPLReport);
				}
			}else if(select.equalsIgnoreCase("Route-wise,Driver-wise,Bus-wise")){
				supply = tankAndCapacityRepository.routewiseDriverwiseBuswiseKmplReport(startDate,endDate,busType,grouping,depoId,select);
				for (Object[] objects : supply) {
					KMPLReportDto kMPLReport= new KMPLReportDto();
					if(objects[2] != null)
					kMPLReport.setBusRegNo(objects[2].toString());
					if(objects[1] != null)
					kMPLReport.setDriverName(objects[1].toString());
					if(objects[0] != null)
					kMPLReport.setRouteName(objects[0].toString());
					if(objects[3] != null)
						kMPLReport.setDepotName(objects[3].toString());
						if(objects[4] != null)
						kMPLReport.setBusType(objects[4].toString());
						if(objects[5] != null)
						kMPLReport.setGrouping(objects[5].toString());
						if(objects[6] != null)
						kMPLReport.setRotation(objects[6].toString());
						if(objects[7] != null)
						kMPLReport.setKmpl(objects[7].toString());
						if(objects[8] != null)
						kMPLReport.setStartDate(objects[8].toString());
						if(objects[9] != null)
						kMPLReport.setEndDate(objects[9].toString());
						if(objects[10] != null)
							kMPLReport.setRefuelDate(objects[10].toString());
						if(objects[11] != null)
							kMPLReport.setVtsKMPL(objects[11].toString());
						
					kMPLReportDto.add(kMPLReport);
				}
			}else{
				supply = tankAndCapacityRepository.routewiseBuswiseDriverwiseKmplReport(startDate,endDate,busType,grouping,depoId,select);
				for (Object[] objects : supply) {
					KMPLReportDto kMPLReport= new KMPLReportDto();
					if(objects[2] != null)
					kMPLReport.setBusRegNo(objects[2].toString());
					if(objects[1] != null)
					kMPLReport.setDriverName(objects[1].toString());
					if(objects[0] != null)
					kMPLReport.setRouteName(objects[0].toString());
					if(objects[3] != null)
						kMPLReport.setDepotName(objects[3].toString());
						if(objects[4] != null)
						kMPLReport.setBusType(objects[4].toString());
						if(objects[5] != null)
						kMPLReport.setGrouping(objects[5].toString());
						if(objects[6] != null)
						kMPLReport.setRotation(objects[6].toString());
						if(objects[7] != null)
						kMPLReport.setKmpl(objects[7].toString());
						if(objects[8] != null)
						kMPLReport.setStartDate(objects[8].toString());
						if(objects[9] != null)
						kMPLReport.setEndDate(objects[9].toString());
						if(objects[10] != null)
							kMPLReport.setRefuelDate(objects[10].toString());
						if(objects[11] != null)
							kMPLReport.setVtsKMPL(objects[11].toString());
						
					kMPLReportDto.add(kMPLReport);
				}
			}
			
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return kMPLReportDto;
		
	}

	@Override
	public List<DriverWiseBusRouteKMPLDto> driverWiseBusRouteKMPL(Integer driverId,Integer routeId,Date startDate,Date endDate) 
	{
		List<DriverWiseBusRouteKMPLDto> driverWiseBusRouteKMPLDto = new ArrayList<DriverWiseBusRouteKMPLDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.driverWiseBusRouteKMPL(startDate,endDate,driverId,routeId);
			for (Object[] objects : supply) {
				DriverWiseBusRouteKMPLDto driverWiseBusRouteKMPL= new DriverWiseBusRouteKMPLDto();
				driverWiseBusRouteKMPL.setDrivername(objects[0].toString());
				driverWiseBusRouteKMPL.setBusNo(objects[1].toString());
				driverWiseBusRouteKMPL.setRouteName(objects[2].toString());
				driverWiseBusRouteKMPL.setTotalKms(objects[3].toString());
				//driverWiseBusRouteKMPL.setVts(objects[3].toString());
				driverWiseBusRouteKMPL.setIssuedDiesel(objects[4].toString());
				driverWiseBusRouteKMPL.setKMPL(objects[5].toString());
				
				//driverWiseBusRouteKMPL.setDeadBus(objects[7].toString());
				driverWiseBusRouteKMPL.setRefueldate(objects[6].toString());
				driverWiseBusRouteKMPLDto.add(driverWiseBusRouteKMPL);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return driverWiseBusRouteKMPLDto;
	}

	@Override
	public List<BusWiseDriverRouteKmplDto> busTypeBusWiseDieselMobileReport(String busNo,Date startDate, Date endDate,
			Integer busType) {
		List<BusWiseDriverRouteKmplDto> busWiseDriverRouteKmplDtoList = new ArrayList<BusWiseDriverRouteKmplDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.busTypeBusWiseDieselMobileOilReport(busNo,startDate,endDate,busType);
			for (Object[] objects : supply) {
				BusWiseDriverRouteKmplDto busWiseDriverRouteKmplDto= new BusWiseDriverRouteKmplDto();
				//busWiseDriverRouteKmplDto.setBusTypeId(objects[0].toString());
				//busWiseDriverRouteKmplDto.setBusId(objects[1].toString());
				busWiseDriverRouteKmplDto.setBusType(objects[0].toString());
				busWiseDriverRouteKmplDto.setBusNo(objects[1].toString());
				busWiseDriverRouteKmplDto.setCategory(objects[2].toString());
				//busWiseDriverRouteKmplDto.setQuantity(objects[5].toString());
				busWiseDriverRouteKmplDto.setMobileOilIssue(objects[5].toString());
				busWiseDriverRouteKmplDto.setDriverName(objects[3].toString());
				busWiseDriverRouteKmplDto.setDriverNo(objects[4].toString());
				busWiseDriverRouteKmplDto.setIssuanceDate(objects[6].toString());
				busWiseDriverRouteKmplDtoList.add(busWiseDriverRouteKmplDto);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return busWiseDriverRouteKmplDtoList;
	}

	@Override
	public List<VariationBeyondReportDto> variationBeyondReport(Integer depoId,Date fromDate,Date toDate) {
		List<VariationBeyondReportDto> variationBeyondReportDtoList = new ArrayList<VariationBeyondReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.variationBeyondReport(depoId,fromDate,toDate);
			for (Object[] objects : supply) {
				VariationBeyondReportDto variationBeyondReportDto= new VariationBeyondReportDto();
				variationBeyondReportDto.setDepotName(objects[0].toString());
				variationBeyondReportDto.setVariants(objects[1].toString());
				variationBeyondReportDto.setTankName(objects[2].toString());
				variationBeyondReportDto.setCreatedOn(objects[3].toString());
				//variationBeyondReportDto.setTank2(objects[3].toString());
				variationBeyondReportDtoList.add(variationBeyondReportDto);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return variationBeyondReportDtoList;
	}

	@Override
	public List<InspectionCarriedOutReportDto> inspectionCarriedOutReport(Integer depoId,Date startDate, Date endDate) {
		List<InspectionCarriedOutReportDto> inspectionCarriedOutReportDtoList = new ArrayList<InspectionCarriedOutReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.inspectionCarriedOutReport(depoId,startDate,endDate);
			for (Object[] objects : supply) {
				InspectionCarriedOutReportDto inspectionCarriedOutReportDto= new InspectionCarriedOutReportDto();
				if(objects[0] != null)
				inspectionCarriedOutReportDto.setDepo(objects[0].toString());
				if(objects[1] != null)
				inspectionCarriedOutReportDto.setInspectionDueDate(objects[1].toString());
				if(objects[2] != null)
				inspectionCarriedOutReportDto.setStatusWithDate(objects[2].toString());
				if(objects[3] != null)
				inspectionCarriedOutReportDto.setNextDueDate(objects[3].toString());
				if(objects[4] != null)
				inspectionCarriedOutReportDto.setAuthorityName(objects[4].toString());
				inspectionCarriedOutReportDtoList.add(inspectionCarriedOutReportDto);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return inspectionCarriedOutReportDtoList;
	}

	@Override
	public List<IssuanceReceiptStmntReportDto> grossKMSDepositedCashierVsDpaReport(Date startDate, Date endDate) {
		List<IssuanceReceiptStmntReportDto> issuanceReceiptStmntReportDtoList = new ArrayList<IssuanceReceiptStmntReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.grossKMSDepositedCashierVsDpaReport(startDate,endDate);
			for (Object[] objects : supply) {
				IssuanceReceiptStmntReportDto issuanceReceiptStmntReportDto= new IssuanceReceiptStmntReportDto();
				issuanceReceiptStmntReportDto.setBusUnitType(objects[0].toString());
				issuanceReceiptStmntReportDto.setBusNumber(objects[1].toString());
				issuanceReceiptStmntReportDto.setBusType(objects[2].toString());				
				issuanceReceiptStmntReportDto.setRouteDate(objects[3].toString());
				issuanceReceiptStmntReportDto.setDpaKm(objects[4].toString());
				issuanceReceiptStmntReportDto.setCashierKm(objects[5].toString());
				issuanceReceiptStmntReportDtoList.add(issuanceReceiptStmntReportDto);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return issuanceReceiptStmntReportDtoList;
	}

	@Override
	public List<IssuanceReceiptStmntReportDto> dieselNotIssuedReport(Date startDate, Date endDate) {
		List<IssuanceReceiptStmntReportDto> issuanceReceiptStmntReportDtoList = new ArrayList<IssuanceReceiptStmntReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.dieselNotIssuedReport(startDate,endDate);
			for (Object[] objects : supply) {
				IssuanceReceiptStmntReportDto issuanceReceiptStmntReportDto= new IssuanceReceiptStmntReportDto();
				//issuanceReceiptStmntReportDto.setTime(objects[0].toString());
				if(objects[0]!=null)
				issuanceReceiptStmntReportDto.setBusType(objects[0].toString());
				if(objects[1]!=null)
				issuanceReceiptStmntReportDto.setBusNumber(objects[1].toString());
				if(objects[2]!=null)
				issuanceReceiptStmntReportDto.setRouteName(objects[2].toString());
				if(objects[3]!=null)
				issuanceReceiptStmntReportDto.setRouteTime(objects[3].toString());
				if(objects[4]!=null)
				issuanceReceiptStmntReportDto.setRouteDate(objects[4].toString());
				issuanceReceiptStmntReportDtoList.add(issuanceReceiptStmntReportDto);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return issuanceReceiptStmntReportDtoList;
	}

	@Override
	public List<DieselIssueOtherPurposeReportDto> dieselIssueOtherPurposeReport(Date startDate, Date endDate,Integer depoId) {
		List<DieselIssueOtherPurposeReportDto> dieselIssueOtherPurposeReportList = new ArrayList<DieselIssueOtherPurposeReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.dieselIssueOtherPurposeReport(startDate,endDate,depoId);
			for (Object[] objects : supply) {
				DieselIssueOtherPurposeReportDto dieselIssueOtherPurposeReportDto= new DieselIssueOtherPurposeReportDto();
				if(objects[0]!=null)
				dieselIssueOtherPurposeReportDto.setDepotName(objects[0].toString());
				if(objects[1]!=null)
				dieselIssueOtherPurposeReportDto.setFuelType(objects[1].toString());
				if(objects[2]!=null)
					dieselIssueOtherPurposeReportDto.setIssueDiselInLtr(objects[2].toString());
				if(objects[3]!=null)
					dieselIssueOtherPurposeReportDto.setIsssueTo(objects[3].toString());
				if(objects[4]!=null)
				dieselIssueOtherPurposeReportDto.setValueOfStockMobil(objects[4].toString());
				if(objects[5]!=null)
					dieselIssueOtherPurposeReportDto.setIssueDate(objects[5].toString());
				if(objects[6]!=null)
				dieselIssueOtherPurposeReportDto.setRemark(objects[6].toString());
								
				dieselIssueOtherPurposeReportList.add(dieselIssueOtherPurposeReportDto);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return dieselIssueOtherPurposeReportList;
	}

	@Override
	public List<DepotStateWiseBusTypeComparReportDto> depotStateWiseBusTypeComparReport(Date startDate, Date endDate,
			Integer depoId, Integer busType) {
		List<DepotStateWiseBusTypeComparReportDto> depotStateWiseBusTypeComparReportDtoList = new ArrayList<DepotStateWiseBusTypeComparReportDto>();
		try {
			List<Object[]> supply = tankAndCapacityRepository.depotStateWiseBusTypeComparReport(startDate,endDate,depoId,busType);
			for (Object[] objects : supply) {
				DepotStateWiseBusTypeComparReportDto depotStateWiseBusTypeComparReportDto= new DepotStateWiseBusTypeComparReportDto();
				depotStateWiseBusTypeComparReportDto.setDate(objects[0].toString());
				depotStateWiseBusTypeComparReportDto.setDiesel(objects[1].toString());
				depotStateWiseBusTypeComparReportDto.setKmGross(objects[2].toString());
				depotStateWiseBusTypeComparReportDto.setKmpl(objects[3].toString());
				depotStateWiseBusTypeComparReportDto.setmOil(objects[4].toString());
				depotStateWiseBusTypeComparReportDto.setmOil1000kmps(objects[5].toString());
				//depotStateWiseBusTypeComparReportDto.setDateOfPreviousRoute(objects[6].toString());
				//depotStateWiseBusTypeComparReportDto.setDateOfPreviousYear(objects[7].toString());
				depotStateWiseBusTypeComparReportDtoList.add(depotStateWiseBusTypeComparReportDto);
			}
			
		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return depotStateWiseBusTypeComparReportDtoList;
	}

	@Override
	public List<InspectionDoneVersusDueReportDto> inspectionDoneVersusDueReport(String lastDate, String month,Date startDate, Date endDate,
			String depotCode,String fromDate) {
		List<InspectionDoneVersusDueReportDto> inspectionList = new ArrayList<>();
		InspectionDoneVersusDueReportDto inspectionObj = null;
		try{
			Integer monthInNo = Integer.parseInt(lastDate); 
			Integer dueCount = monthInNo/7;
			Integer dueCountWM = monthInNo/3;
			Integer depoId = depotRepo.getIdByDepoName(depotCode);
			List<Object[]> currentInspectionList = tankAndCapacityRepository.inspectionDoneVersusDueReport(depoId, startDate, endDate);
			for(Object[] object : currentInspectionList){
				inspectionObj = new InspectionDoneVersusDueReportDto();
				if(object[2] != null && object[2].equals("GM")){
					if(object[0] != null ){
						inspectionObj.setUserName(object[0].toString());
					}if(object[1] != null ){
						inspectionObj.setRoleName(object[1].toString());
					}if(object[3] != null ){
						inspectionObj.setDepotName(object[3].toString());
					}if(object[4] != null ){
						inspectionObj.setDoneInspection(object[4].toString());
					}if(fromDate != null ){
						LocalDate ld = LocalDate.parse(fromDate);
						inspectionObj.setMonthName(ld.getMonth().toString());
					}
					if(dueCount != null)
					inspectionObj.setDueInspection(dueCount.toString());
					inspectionList.add(inspectionObj);
				}else if(object[2] != null && object[2].equals("SO")){
					if(object[0] != null ){
						inspectionObj.setUserName(object[0].toString());
					}if(object[1] != null ){
						inspectionObj.setRoleName(object[1].toString());
					}if(object[3] != null ){
						inspectionObj.setDepotName(object[3].toString());
					}if(object[4] != null ){
						inspectionObj.setDoneInspection(object[4].toString());
					}if(fromDate != null ){
						LocalDate ld = LocalDate.parse(fromDate);
						inspectionObj.setMonthName(ld.getMonth().toString());
					}
					if(dueCount != null)
					inspectionObj.setDueInspection(lastDate);
					inspectionList.add(inspectionObj);
				}else if(object[2] != null && object[2].equals("WM")){
					if(object[0] != null ){
						inspectionObj.setUserName(object[0].toString());
					}if(object[1] != null ){
						inspectionObj.setRoleName(object[1].toString());
					}if(object[3] != null ){
						inspectionObj.setDepotName(object[3].toString());
					}if(object[4] != null ){
						inspectionObj.setDoneInspection(object[4].toString());
					}if(fromDate != null ){
						LocalDate ld = LocalDate.parse(fromDate);
						inspectionObj.setMonthName(ld.getMonth().toString());
					}
					if(dueCount != null)
					inspectionObj.setDueInspection(dueCountWM.toString());
					inspectionList.add(inspectionObj);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return inspectionList;
	}

	@Override
	public List<BusesNotGettingDieselIssuedOnTimeReportDto> busesNotGettingDieselIssueReport(Date startDate,
			Date endDate, Integer depoId) {
		List<BusesNotGettingDieselIssuedOnTimeReportDto> dtoList = new ArrayList<BusesNotGettingDieselIssuedOnTimeReportDto>();
		BusesNotGettingDieselIssuedOnTimeReportDto dtoObj = null;
		try {
			List<Object[]> notGettingDieselList = tankAndCapacityRepository.busesNotGettingDieselIssueReport(startDate,
					endDate, depoId);
			for (Object[] object : notGettingDieselList) {
				dtoObj = new BusesNotGettingDieselIssuedOnTimeReportDto();
				if (object[0] != null) {
					dtoObj.setDepotName(object[0].toString());
				}
				
//				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//				Timestamp date = formatter.parse(object[3].toString());
//
//				Timestamp timestamp = new Timestamp(date.getTime());
				
				//String dateTime = object[3].toString();

		        Timestamp refuled = Timestamp.valueOf(object[3].toString());
		        Timestamp sche = Timestamp.valueOf(object[2].toString());
				
		        long milliseconds = refuled.getTime() - sche.getTime();
				Integer seconds = (int) milliseconds / 1000;

				Integer hours = seconds / 3600;
				Integer minutes = (seconds % 3600) / 60;
				seconds = (seconds % 3600) % 60;
				String h;
				String m;
				String s;
				
              if (hours<10)
              {
            	  h="0"+ hours.toString();
              }
              else
              {
            	  h=hours.toString();
              }
              if(minutes<10)
              {
            	  m="0"+minutes.toString();
              }
              else
              {
            	  m=minutes.toString();
              }
              if(seconds<10)
              {
            	  s="0"+seconds.toString();
              }
              else
              {
            	  s=seconds.toString();
              }
				
				if (object[1] != null) {
					dtoObj.setBusNo(object[1].toString());
				}
				if (object[2] != null) {
					dtoObj.setScheduledDateAndTime(object[2].toString());
				}if (object[3] != null) {
					dtoObj.setRefueledDateAndTime(object[3].toString());
				}if (object[4] != null) {
					dtoObj.setDuration(h+":"+m+":"+s);
				}
				dtoList.add(dtoObj);
			}

		} catch (Exception e) {
			log.info("TankAndCapacityServiceImpl " + e);
		}
		return dtoList;
	}

	@Override
	public List<FuelTypeDto> fetchFuelTypeList() {
		List<FuelTypeDto> fuelTypeList = fuelTypeRepository.findAllByStatus(true).stream()
				.map(fuelType -> new FuelTypeDto(fuelType.getId(), fuelType.getFuelTypeName())).collect(Collectors.toList());
		return fuelTypeList;
	}

}
