package com.idms.base.master.serviceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.AdBlueUsedDto;
import com.idms.base.api.v1.model.dto.AddBlueDrumMasterDto;
import com.idms.base.api.v1.model.dto.BusDieselCorrectionDto;
import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusRefuelingFormLoadDto;
import com.idms.base.api.v1.model.dto.BusRefuelingListDto;
import com.idms.base.api.v1.model.dto.BusRefuelingListDtoParent;
import com.idms.base.api.v1.model.dto.DUReadingHistoryDto;
import com.idms.base.api.v1.model.dto.DailyRosterViewOnlyDto;
import com.idms.base.api.v1.model.dto.DispensingUnitHelperDto;
import com.idms.base.api.v1.model.dto.DispensingUnitMasterDto;
import com.idms.base.api.v1.model.dto.DocumentDto;
import com.idms.base.api.v1.model.dto.FuelSourceMasterDto;
import com.idms.base.api.v1.model.dto.FuelTakenOutSideDto;
import com.idms.base.api.v1.model.dto.FuelTankMasterDto;
import com.idms.base.api.v1.model.dto.MobilOilDrumMasterDto;
import com.idms.base.api.v1.model.dto.MobilOilUsedDto;
import com.idms.base.api.v1.model.dto.RefuelingViewDto;
import com.idms.base.dao.entity.AddBlueDrumMaster;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusRefuelingMaster;
import com.idms.base.dao.entity.BusReturnReason;
import com.idms.base.dao.entity.BusRotaHistory;
import com.idms.base.dao.entity.BusTyreAssociation;
import com.idms.base.dao.entity.DUReadingHistory;
import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DispensingUnitMaster;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.IntegrationLog;
import com.idms.base.dao.entity.MobilOilDrumMaster;
import com.idms.base.dao.entity.Roaster;
import com.idms.base.dao.entity.StateWiseRouteKms;
import com.idms.base.dao.repository.AdBlueUsedRepository;
import com.idms.base.dao.repository.AddBlueDrumMasterRepository;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusRefuelingMasterRepository;
import com.idms.base.dao.repository.BusReturnReasonRepository;
import com.idms.base.dao.repository.BusRotaHistoryRepository;
import com.idms.base.dao.repository.BusTyreAssociationRepository;
import com.idms.base.dao.repository.DUReadingHistoryRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DispensingUnitRepository;
import com.idms.base.dao.repository.DocumentRepository;
import com.idms.base.dao.repository.FuelSourceMasterRepository;
import com.idms.base.dao.repository.FuelTakenOutSideRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.IntegrationLogRepository;
import com.idms.base.dao.repository.MobilOilDrumMasterRepository;
import com.idms.base.dao.repository.MobilOilUsedRepository;
import com.idms.base.dao.repository.RecieveDieselSupplyMasterRepository;
import com.idms.base.dao.repository.RoasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.BusRefuelingMasterService;
import com.idms.base.service.DispensingUnitMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.VTS_Util;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BusRefuelingMasterServiceImpl implements BusRefuelingMasterService{
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	DispensingUnitRepository dispensingRepository;
	
	@Autowired
	FuelTankMasterRepository fuelTankRepository;
	
	@Autowired
	BusMasterRepository busMasterRepository;
	
	@Autowired
	FuelSourceMasterRepository fuelSourceRepository;
	
	@Autowired
	DailyRoasterRepository dailyRoasterRepository;
	
	@Autowired
	BusRefuelingMasterRepository busRefuelingMasterRepository;
	
	@Autowired
	DocumentRepository documentRepository;
	
	@Autowired
	DepotMasterRepository depotRepository;
	
	@Autowired
	DispensingUnitMasterService dispensingUnitMasterService;
	
	@Autowired
	BusRotaHistoryRepository busRotaHistoryRepo;
	
	@Autowired
	BusReturnReasonRepository busReturnRepo;
	
	@Autowired
	DUReadingHistoryRepository duReadingHistoryRepo;
	
	@Autowired
	FuelTakenOutSideRepository fuelTakenRepo;
	
	@Autowired
	BusTyreAssociationRepository btaRepo;
	
	@Autowired
	VTS_Util vtsUtil;
	
	@Autowired
	IntegrationLogRepository integrationRepository;
	
	HttpStatus status= HttpStatus.FORBIDDEN;
	
	@Autowired
	RoasterRepository roasterRepository;
	

	@Value("${file.path}")
	private String filePath;
	
	@Autowired
	private MobilOilDrumMasterRepository mobilOilDrumMasterRepository;
	
	@Autowired
	private AddBlueDrumMasterRepository addBlueDrumMasterRepository;
	
	@Autowired
	RecieveDieselSupplyMasterRepository recieveDieselSupplyMasterRepository;
	
	@Autowired
	TransportUnitRepository transportUnitRepository;
	
	@Autowired
	private MobilOilUsedRepository mobilOilUsedRepository;

	@Autowired
	private AdBlueUsedRepository adBlueUsedRepository;
	
	
	@Override
	public ResponseEntity<Object> busRefuelingMasterOnLoad(String depotCode) {
	log.info("Entering into busRefuelingMasterOnLoad service");
	BusRefuelingFormLoadDto busRefuelingFormLoadDto = new BusRefuelingFormLoadDto();
	List<MobilOilDrumMasterDto> mobilList = new ArrayList<>();
	List<AddBlueDrumMasterDto> addBlueList = new ArrayList<>();
	Integer currentValue = 0;
	try {
		if(depotCode.equals(null) || depotCode.equals(" ")) {
			busRefuelingFormLoadDto = new BusRefuelingFormLoadDto();
			return new ResponseEntity<>(new ResponseStatus(
					"Depot code cannot be null",
					HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
		DepotMaster depotMaster = depotRepository.findByDepotCode(depotCode);
		if(depotMaster != null) {
		List<DispensingUnitMasterDto> dispensingList = dispensingRepository.findAllByStatusAndDepotId(true,depotMaster.getId()).stream()
				.map(dispensing -> new DispensingUnitMasterDto(dispensing.getId(), dispensing.getDisUnitName()))
				.collect(Collectors.toList());
		if (dispensingList != null && dispensingList.size() > 0)
			busRefuelingFormLoadDto.setDispensingList(dispensingList);
		
		List<FuelTankMasterDto> fuelTankList = fuelTankRepository.findAllByStatusAndDepotId(true,depotMaster.getId()).stream()
				.map(fuelTank -> new FuelTankMasterDto(fuelTank.getId(), fuelTank.getCurrentValue() != null ? 
						fuelTank.getTankName()+"("+fuelTank.getCurrentValue()+")" : 
						fuelTank.getTankName()+"("+currentValue+")",fuelTank.getCurrentValue(),fuelTank.getCurrentPrice() != null ? fuelTank.getCurrentPrice() : 0))
				.collect(Collectors.toList());
		if (fuelTankList != null && fuelTankList.size() > 0)
			busRefuelingFormLoadDto.setFuelTankList(fuelTankList);
		}
		
		List<BusMasterDto> busList = busMasterRepository.findAllBusesByDepotAndStatus(depotCode).stream()
				.map(busObj -> new BusMasterDto(busObj.getId(), busObj.getBusRegNumber()))
				.collect(Collectors.toList());
		if (busList != null && busList.size() > 0)
			busRefuelingFormLoadDto.setBusMasterList(busList);
		
		List<FuelSourceMasterDto> fuelSourceList = fuelSourceRepository.findAllByStatus(true).stream()
				.map(fuelSource -> new FuelSourceMasterDto(fuelSource.getId(), fuelSource.getName()))
				.collect(Collectors.toList());
		if (fuelSourceList != null && fuelSourceList.size() > 0)
			busRefuelingFormLoadDto.setFuelSourceList(fuelSourceList);
		
		List<BusReturnReason> reasonList = busReturnRepo.findAll();
		if(reasonList != null && reasonList.size()>0) {
			busRefuelingFormLoadDto.setBusReturnReasonList(reasonList);
		}
		List<MobilOilDrumMaster> mobilOilDrumList = mobilOilDrumMasterRepository.findAllDrumsByDepot(true,false,depotCode);
		MobilOilDrumMasterDto dtoObj = null;
		for(MobilOilDrumMaster mobilObj : mobilOilDrumList){
			dtoObj = new MobilOilDrumMasterDto();
			dtoObj.setId(mobilObj.getId());
			dtoObj.setNameOfDrum(mobilObj.getNameOfDrum());
			Float mobilOilUsed = mobilOilUsedRepository.toalAddBlueUsedFromDrum(mobilObj.getId());
			if (mobilOilUsed != null && mobilObj.getTotalCapacity() != null) {
				Float availableStock = mobilObj.getTotalCapacity() - mobilOilUsed;
				String avialStock = availableStock.toString();
				dtoObj.setAvailableStock(avialStock);
			} else if (mobilOilUsed == null && mobilObj.getTotalCapacity() != null) {
				dtoObj.setAvailableStock(mobilObj.getTotalCapacity().toString());
			}
			mobilList.add(dtoObj);
		}
		if(mobilList.size() > 0){
			busRefuelingFormLoadDto.setDrumList(mobilList);
		}
		
		List<AddBlueDrumMaster> mobilOilDrumListTemp = addBlueDrumMasterRepository.findAllDrumsByDepot(true,false,depotCode);
		AddBlueDrumMasterDto tempDtoObj = null;
		for(AddBlueDrumMaster mobilObj : mobilOilDrumListTemp){
			tempDtoObj = new AddBlueDrumMasterDto();
			tempDtoObj.setId(mobilObj.getId());
			tempDtoObj.setNameOfDrum(mobilObj.getNameOfDrum());
			Float addBlueUsed = adBlueUsedRepository.toalMobilUsedFromDrum(mobilObj.getId());
			if (addBlueUsed != null && mobilObj.getTotalCapacity() != null) {
				Float availableStock = mobilObj.getTotalCapacity() - addBlueUsed;
				String avialStock = availableStock.toString();
				tempDtoObj.setAvailableStock(avialStock);
			} else if (addBlueUsed == null && mobilObj.getTotalCapacity() != null) {
				tempDtoObj.setAvailableStock(mobilObj.getTotalCapacity().toString());
			}
			addBlueList.add(tempDtoObj);
		}
		if(addBlueList.size() > 0){
			busRefuelingFormLoadDto.setAddBlueDrumList(addBlueList);
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return new ResponseEntity<>(busRefuelingFormLoadDto, HttpStatus.OK);
	}


	@Override
	public DailyRosterViewOnlyDto fetchRosterDetailsByBusId(Integer busId,Integer reasonId) {
		log.info("Entering into fetchRosterDetailsByBusId service");
		DailyRosterViewOnlyDto dto = new DailyRosterViewOnlyDto();
		List<DailyRoaster> dailyRoasterList = null;
		DailyRoaster roaster = null;
		int schKms = 0;
		int schDeadKms = 0;
		int plainKms = 0;
		int hillKms = 0;
		int completedRoute = 0;
		
		String vtsKm="";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			 Date date1 =new SimpleDateFormat("dd/MM/yyyy").parse(formatter.format(new Date()));  
			dailyRoasterList = dailyRoasterRepository.fetchRosterDetailsByBusId(busId,date1);
			Date today = new Date();
			BusRotaHistory brh = null;
			Integer currentScheduledValue = null;
			Integer oldScheduledValue = null;
			Integer rotationValue = null;
			int i = 0;
			if (dailyRoasterList != null && dailyRoasterList.size() > 0) {
				if (dailyRoasterList.size() > 0) {
					for (DailyRoaster daily : dailyRoasterList) {
						if (daily.getBusRota() != null && daily.getBusRota().size() > 0) {
							brh = daily.getBusRota().get(i);
							roaster = daily;
							dto.setRotaId(roaster.getRota().getId());
							i++;
							break;
						}
					}	
					if(brh != null && brh.getReturnDate()!=null && (today.after(brh.getReturnDate()) || formatter.format(brh.getReturnDate()) == (formatter.format(today)))) {	
						dto.setBusNumber(roaster.getBus().getBusRegNumber());
						dto.setBusType(roaster.getBus().getBusType().getBusTypeName());
						dto.setDeadKms(roaster.getTrip().getDeadKms().toString());
						if (roaster.getRoute() != null && roaster.getRoute().getTripType() != null
								&& roaster.getRoute().getTripType().getTripValue() != null) {
							rotationValue = roaster.getRoute().getTripType().getTripValue();
						} else
							rotationValue = 1;
						if (roaster.getRoute() != null && roaster.getRoute().getScheduledKms() != null) {
							oldScheduledValue = roaster.getRoute().getScheduledKms();
						}
						currentScheduledValue = rotationValue * oldScheduledValue;
						dto.setScheduledKms(oldScheduledValue.toString());
						dto.setDriverNo(roaster.getDriver().getDriverCode());
						dto.setRouteId(roaster.getRoute().getId());
						dto.setRouteName(roaster.getRoute().getRouteName());
						dto.setBusId(roaster.getBus().getId());					
						dto.setDeadKms(roaster.getRoute().getDeadKms().toString());	
						dto.setTripId(brh.getTrip().getId());
						completedRoute = dailyRoasterList.size()/2;
						Optional<BusReturnReason> reason = busReturnRepo.findById(reasonId);
						Integer dieselIssued=	reasonId==1 ?  busRefuelingMasterRepository.findByBusId(busId,roaster.getRoute().getId()) : null;
						dto.setDieselIssued(dieselIssued!=null ? String.valueOf(dieselIssued) : "0");
						vtsKm=reason.get().getReason().equals("Trip Completed") ? vtsDieselDistance(busId) : "Error";
						if (vtsKm.contentEquals("Error")) {
							dto.setVtsKms("0");
						} else {
							dto.setVtsKms(vtsKm);
						}
						for(DailyRoaster rota  : dailyRoasterList) {
							schKms = schKms + rota.getTrip().getScheduledKms();
						}
//						dto.setScheduledKms(schKms+"");
						
						dto.setGrossKms(roaster.getRoute().getScheduledKms()+roaster.getRoute().getDeadKms()+"");
						Integer totalActualKms = roaster.getRoute().getScheduledKms()+roaster.getRoute().getDeadKms();
						if(totalActualKms != null)
						dto.setTotalActualKms((totalActualKms * rotationValue));
						for(StateWiseRouteKms swkm : roaster.getRoute().getStateWiseRouteKms()) {
							plainKms = plainKms + swkm.getPlainKms();
							hillKms = hillKms + swkm.getHillKms();
						}
						
//						plainKms = plainKms * completedRoute;
//						hillKms = hillKms * completedRoute;
						
						dto.setPlainKms(plainKms+"");
						dto.setHillKms(hillKms+"");
						
					  }
					}
				//}
				
			} else {
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveBusRefuelingMaster(BusRefuelingMaster busRefuelingMaster, MultipartFile uploadFile,MultipartFile mobilOilBillFile, MultipartFile redBlueBillFile) {
		log.info("Entering into saveBusRefuelingMaster service");
		String pattern = "ddMMyyyy";
		String currentDate =new SimpleDateFormat(pattern).format(new Date());
		Document uploadDocument = null;
		Integer rotaId = null;
		try {
			String status = this.validateBusRefueling(busRefuelingMaster);

			if(status.equals("success")) {
				if (busRefuelingMaster.getId() == null) {
					List<DailyRoaster> rotaList = dailyRoasterRepository
							.fetchRosterDetailsByBusIdandTripId(busRefuelingMaster.getBusMaster().getId(),busRefuelingMaster.getTripId(),busRefuelingMaster.getRotaId());
					if (uploadFile != null && !uploadFile.isEmpty()) {
						File dir = new File(filePath + File.separator + "refueling" + File.separator + currentDate);
						log.info(dir.toPath());
						if (!dir.exists())
							dir.mkdirs();
						uploadDocument = new Document();
						Files.copy(uploadFile.getInputStream(), dir.toPath().resolve(uploadFile.getOriginalFilename()),
								StandardCopyOption.REPLACE_EXISTING);
						uploadDocument.setContentType(uploadFile.getContentType());
						uploadDocument.setDocumentName(uploadFile.getOriginalFilename());
						uploadDocument.setDocumentPath(
								filePath + File.separator + "refueling" + File.separator + currentDate+File.separator+uploadDocument.getDocumentName());
						uploadDocument = documentRepository.save(uploadDocument);
					}
					if (uploadDocument != null && uploadDocument.getId() != null) {
						busRefuelingMaster.getFuelTakenOutSide().setBillDocument(uploadDocument);
					}
					
					if (mobilOilBillFile != null && !mobilOilBillFile.isEmpty()) {
						File dir = new File(filePath + File.separator + "mobilOilBillFile" + File.separator + currentDate);
						log.info(dir.toPath());
						if (!dir.exists())
							dir.mkdirs();
						uploadDocument = new Document();
						Files.copy(mobilOilBillFile.getInputStream(), dir.toPath().resolve(mobilOilBillFile.getOriginalFilename()),
								StandardCopyOption.REPLACE_EXISTING);
						uploadDocument.setContentType(mobilOilBillFile.getContentType());
						uploadDocument.setDocumentName(mobilOilBillFile.getOriginalFilename());
						uploadDocument.setDocumentPath(
								filePath + File.separator + "mobilOilBillFile" + File.separator + currentDate+File.separator+uploadDocument.getDocumentName());
						uploadDocument = documentRepository.save(uploadDocument);
					}
					if (uploadDocument != null && uploadDocument.getId() != null && busRefuelingMaster.getMobilOilUsed() != null) {
						busRefuelingMaster.getMobilOilUsed().setMobilOilDocument(uploadDocument);
					}
					
					if(busRefuelingMaster.getMobilOilUsed() != null && busRefuelingMaster.getMobilOilUsed().getMobilOilDrumMaster() != null 
							&& busRefuelingMaster.getMobilOilUsed().getMobilOilDrumMaster().getId() == null){
						busRefuelingMaster.getMobilOilUsed().setMobilOilDrumMaster(null);
					}
					if (redBlueBillFile != null && !redBlueBillFile.isEmpty()) {
						File dir = new File(filePath + File.separator + "redBlueBillFile" + File.separator + currentDate);
						log.info(dir.toPath());
						if (!dir.exists())
							dir.mkdirs();
						uploadDocument = new Document();
						Files.copy(redBlueBillFile.getInputStream(), dir.toPath().resolve(redBlueBillFile.getOriginalFilename()),
								StandardCopyOption.REPLACE_EXISTING);
						uploadDocument.setContentType(redBlueBillFile.getContentType());
						uploadDocument.setDocumentName(redBlueBillFile.getOriginalFilename());
						uploadDocument.setDocumentPath(
								filePath + File.separator + "redBlueBillFile" + File.separator + currentDate+File.separator+uploadDocument.getDocumentName());
						uploadDocument = documentRepository.save(uploadDocument);
					}
					if (uploadDocument != null && uploadDocument.getId() != null && busRefuelingMaster.getAdBlueUsed() != null) {
						busRefuelingMaster.getAdBlueUsed().setAddBlueDocument(uploadDocument);
					}
					
					DepotMaster dp = depotRepository.findByDepotCode(busRefuelingMaster.getDpCode());
					busRefuelingMaster.setDepot(dp);
					if(busRefuelingMaster.getAdBlueUsed() != null && busRefuelingMaster.getAdBlueUsed().getAddBlueDrumMaster() != null && busRefuelingMaster.getAdBlueUsed().getAddBlueDrumMaster().getId() == null){
						busRefuelingMaster.getAdBlueUsed().setAddBlueDrumMaster(null);
					}
					 Roaster roasterObj = roasterRepository.findById(busRefuelingMaster.getRotaId()).get();
					 busRefuelingMaster.setRota(roasterObj);
					 busRefuelingMasterRepository.save(busRefuelingMaster);
					/*List<DailyRoaster> roasterList = dailyRoasterRepository.findAllByRefuellingId(refuelObj.getId());
					if(roasterList.size() > 0){
						rotaId = roasterList.get(0).getRota().getId();
					    Roaster roasterObj = roasterRepository.findById(rotaId).get(); 
					    refuelObj.setRota(roasterObj);
					    busRefuelingMasterRepository.save(refuelObj);
					}*/
					
					if (busRefuelingMaster.getDispensingUnitMaster() != null) {
						DUReadingHistory duHistory = new DUReadingHistory();
						duHistory.setDispensingUnit(busRefuelingMaster.getDispensingUnitMaster());
						DispensingUnitMaster du = dispensingRepository.getOne(busRefuelingMaster.getDispensingUnitMaster().getId());
						duHistory.setDuStartReading(du.getCurrentReading());
						duHistory.setDuEndReading(busRefuelingMaster.getCurrentReading());
						duHistory.setRefueling(busRefuelingMaster);
						duHistory.setIssuedDiesel(busRefuelingMaster.getIssuedDiesel());
						duHistory.setFuelTank(busRefuelingMaster.getFuelTankMaster());
						duReadingHistoryRepo.save(duHistory);
						dispensingUnitMasterService.updateCurrentReading(busRefuelingMaster.getCurrentReading(),busRefuelingMaster.getDispensingUnitMaster().getId());
					}
					
					FuelTankMaster fuelTank = fuelTankRepository.findById(busRefuelingMaster.getFuelTankMaster().getId()).get();
					fuelTank.setCurrentValue(fuelTank.getCurrentValue()-busRefuelingMaster.getIssuedDiesel());
					fuelTankRepository.save(fuelTank);
					if (busRefuelingMaster.getReason().getId() == 1) {
						for (DailyRoaster rota : rotaList) {
							rota.setTripStatus(true);
							rota.setRefueling(busRefuelingMaster);
							dailyRoasterRepository.save(rota);
						}
					}
					
					BusMaster bus = busMasterRepository.findById(busRefuelingMaster.getBusMaster().getId()).get();
					List<BusTyreAssociation> btaList = bus.getTyreLists();
					for(BusTyreAssociation bta : btaList) {
						if(bta.getKmsDone()!=null)
							bta.setKmsDone(bta.getKmsDone()+busRefuelingMaster.getGrossKms());
						else
							bta.setKmsDone(Double.parseDouble(busRefuelingMaster.getGrossKms().toString()));
					}					
					return vtsValidation(new ResponseStatus("Bus Refueling master has been persisted successfully.", HttpStatus.OK), busRefuelingMaster);
//					return new ResponseEntity<>(new ResponseStatus("Bus Refueling master has been persisted successfully.", HttpStatus.OK),HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new ResponseStatus(status, HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
			}
			} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),HttpStatus.OK);
		}
		return null;
	}
	
	public ResponseEntity<ResponseStatus> vtsValidation(ResponseStatus response,BusRefuelingMaster busRefuelingMaster) throws JSONException {
		String msg="Bus Refueling master has been persisted successfully.";
		HttpStatus e = busRefuelingMaster.getReason().getId()==2 ? HttpStatus.OK : HttpStatus.FORBIDDEN;
		if(busRefuelingMaster.getReason().getId()==1){
		if (response.getStatus().equals(200)) {
			busRefuelingMaster.setId(busRefuelingMaster.getId());
			busRefuelingMaster.setIssuedDiesel(busRefuelingMaster.getIssuedDiesel());
			boolean status = vtsDieselApi(busRefuelingMaster.getBusMaster().getId(), busRefuelingMaster.getIssuedDiesel().toString());
			if(status) {
				msg="Data is persisted & vts data sent successfull";
				e= HttpStatus.OK;
			}
			else { 
				msg = "Data is persisted but vts data sent un-successfull";
				e= HttpStatus.FORBIDDEN;
			}
		}
		}
		return new ResponseEntity<>(new ResponseStatus(msg,e),HttpStatus.OK);
	}

	public String validateBusRefueling(BusRefuelingMaster busRefueling) {
		String status = "";
		if(busRefueling.getBusMaster()==null || busRefueling.getBusMaster().getId()==null) {
			return "Bus id is mandatory.";
		} else if(busRefueling.getReason()==null || busRefueling.getReason().getId() == null) {
			return "Bus return reason is mandatory.";
		} else if(busRefueling.getDispensingUnitMaster()==null || busRefueling.getDispensingUnitMaster().getId() == null) {
			return "Dispensing Unit is mandatory.";
		} else if(busRefueling.getFuelTankMaster()==null || busRefueling.getFuelTankMaster().getId() == null) {
			return "Fuel Tank is mandatory.";
		} else if(busRefueling.getIssuedDiesel()==null || busRefueling.getIssuedDiesel() <=0) {
			return "Diesel Issue is mandatory and should be a positive value.";
		} else {
			return "success";
		}
	}

	@Override
	public DispensingUnitMasterDto fetchDispensingDataByDuId(Integer dispensingId) {
		DispensingUnitMasterDto dto = new DispensingUnitMasterDto();
		try {
		Optional<DispensingUnitMaster> duMaster = dispensingRepository.findById(dispensingId);
		if(duMaster.isPresent()) {
			dto.setInitialReading(duMaster.get().getInitialReading());
			dto.setCurrentReading(duMaster.get().getCurrentReading());
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}


	@Override
	public List<BusRefuelingMaster> getAllBusRefuelingMaster() {
		log.info("Entering into getAllBusRefuelingMaster service");
		List<BusRefuelingMaster> busRefuelingMasterList = null;
		try {
			busRefuelingMasterList = busRefuelingMasterRepository.findAll();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return busRefuelingMasterList;
	}
	
	public BusRefuelingListDtoParent getBusRefuelingListByDepot(String dpCode) {
		List<BusRefuelingListDto> refuelingList = new ArrayList<>();
		BusRefuelingListDto obj = null;
		Integer rotaId = null;
		Integer dieselIssued = null;
		BusRefuelingListDtoParent parentDto = null;
		try {
			DepotMaster depot = depotRepository.findByDepotCode(dpCode);
			List<Object[]> list = busRefuelingMasterRepository.findAllBusRefuelingByDepot(depot.getId());
			for(Object[] o : list) {
				obj = new BusRefuelingListDto();
				obj.setId(Integer.parseInt(o[0].toString()));
				BusRefuelingMaster busRefuelingObj = busRefuelingMasterRepository.findById(Integer.parseInt(o[0].toString())).get();
				List<DailyRoaster> roasterList = dailyRoasterRepository.findAllByRefuellingId(Integer.parseInt(o[0].toString()));
				if(roasterList.size() > 0)
					rotaId = roasterList.get(0).getRota().getId();
				try{
				 dieselIssued = busRefuelingObj.getReason().getId() == 1
						? busRefuelingMasterRepository.findSumByBusDetails(busRefuelingObj.getBusMaster().getId(),
								busRefuelingObj.getRouteMaster().getId(), busRefuelingObj.getTripId(),rotaId)
						: 0;
				}catch(Exception e){
					dieselIssued = 0;
					e.printStackTrace();
				}
				obj.setBusNumber(o[1].toString());	
				obj.setDieselIssued(busRefuelingObj.getReason().getId() == 1 ? dieselIssued : Float.parseFloat(o[2].toString()));
				if(o[3]!=null)
					obj.setDieselFromOutside(Float.parseFloat(o[3].toString()));
				else
					obj.setDieselFromOutside(0F);
				obj.setDieselIssuDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o[4].toString()));
				obj.setGrossKms(Integer.parseInt(o[5].toString()));
				obj.setKmplAsGrossKms(Float.parseFloat(o[6].toString()));
				obj.setTotalActualKms(Float.parseFloat(o[7].toString()));
				obj.setKmplAsTotalKms(Float.parseFloat(o[8].toString()));
				obj.setRouteName(o[9].toString());
				obj.setVtsKms(o[10].toString());
				obj.setKmplAsPerVts(o[11].toString());
				obj.setScheduledKms(o[12].toString());
				obj.setKmplAsPerScheduled(o[13].toString());
				refuelingList.add(obj);
			}
			DispensingUnitHelperDto duObj = null;
			List<DispensingUnitHelperDto> duObjList = new ArrayList<>();
			List<Object[]> helperList = duReadingHistoryRepo.findAllDuDataByDepot(depot.getId());
			for (Object[] arrayObj : helperList) {
				duObj = new DispensingUnitHelperDto();
				if (arrayObj[0] != null)
					duObj.setDispensingUnitName(arrayObj[0].toString());
				if (arrayObj[1] != null)
					duObj.setDuCurrentReading(arrayObj[1].toString());
				else
					duObj.setDuCurrentReading("0");
				if (arrayObj[2] != null)
					duObj.setDuOpeningReading(arrayObj[2].toString());
				else
					duObj.setDuOpeningReading("0");
				duObjList.add(duObj);
			}
			parentDto = new BusRefuelingListDtoParent();
			parentDto.setBusRefuelingList(refuelingList);
			parentDto.setDuObjList(duObjList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return parentDto;
	}
	
	public RefuelingViewDto getBusRefuelingById(Integer id) {
		RefuelingViewDto refueling = new RefuelingViewDto(); 
		FuelTakenOutSideDto fuelTaken = new FuelTakenOutSideDto();
		FuelSourceMasterDto fsm = new FuelSourceMasterDto();
		DocumentDto doc = new DocumentDto();
		Integer rotaId = null;
		Integer dieselIssued = null;
		try {
			BusRefuelingMaster busRefuelingObj = busRefuelingMasterRepository.findById(id).get();
			List<Object[]> objList = busRefuelingObj.getReason().getId() == 1
					? busRefuelingMasterRepository.findRefuelingById(id)
					: busRefuelingMasterRepository.findRefuelingByIdMidTrip(id);
					if(busRefuelingObj.getDailyRota().size() > 0)
						rotaId = busRefuelingObj.getDailyRota().get(0).getRota().getId();
					try{
			 dieselIssued = busRefuelingObj.getReason().getId() == 1
					? busRefuelingMasterRepository.findSumByBusDetails(busRefuelingObj.getBusMaster().getId(),
							busRefuelingObj.getRouteMaster().getId(), busRefuelingObj.getTripId(),rotaId)
					: 0;
					}catch(Exception e){
						dieselIssued = 0;
						e.printStackTrace();
					}
			for (Object[] obj : objList) {
				refueling = new RefuelingViewDto();
				refueling.setReturnReason(obj[0].toString());
				refueling.setBusNumber(obj[1].toString());
				refueling.setDuName(obj[2].toString());
				refueling.setFuelTankName(obj[3].toString());
				refueling.setDieselIssued(busRefuelingObj.getReason().getId() == 1 ? Float.parseFloat(String.valueOf(dieselIssued)) : Float.parseFloat(obj[4].toString()));
				if(obj[5] != null)
				refueling.setDieselFromOutside(Float.parseFloat(obj[5].toString()));
				refueling.setRouteName(obj[6].toString());
				refueling.setBusType(obj[7].toString());
				refueling.setDriverName(obj[8]!=null ? obj[8].toString() : " ");
				refueling.setScheduledKms(Integer.parseInt(obj[9].toString()));
				refueling.setDeadKms(Integer.parseInt(obj[10].toString()));
				refueling.setPlainKms(Integer.parseInt(obj[11].toString()));
				refueling.setHillKms(Integer.parseInt(obj[12].toString()));
				refueling.setKmplAsSchKms(Float.parseFloat(obj[13].toString()));
				refueling.setKmplAsGrossKms(Float.parseFloat(obj[14].toString()));
				refueling.setKmplAsActualKms(Float.parseFloat(obj[15].toString()));
				refueling.setKmplAsVtsKms(Float.parseFloat(obj[16].toString()));
				if(obj[17]!=null)
					refueling.setExtraDeadReason(obj[17].toString());
				else
					refueling.setExtraDeadReason("");
				if(obj[18]!=null)
					refueling.setRemarks(obj[18].toString());
				else
					refueling.setRemarks("");
				if(obj[19]!=null)
					refueling.setExtraDeadKms(obj[19].toString());
				else
					refueling.setExtraDeadKms("0");
				if(obj[20]!=null)
					refueling.setTotalActualKms(Integer.parseInt(obj[20].toString()));
				else
					refueling.setTotalActualKms(0);
				if(obj[21]!=null)
					refueling.setGrossKms(Integer.parseInt(obj[21].toString()));
				else
					refueling.setGrossKms(0);
				if(obj[22]!=null && Float.parseFloat(obj[22].toString())>0) {
					
					fuelTaken.setQuantity(obj[22]!=null ? Float.parseFloat(obj[22].toString()) : null);
					fsm.setName(obj[23]!=null ? obj[23].toString() : null );
					fuelTaken.setBillNo(obj[24]!=null ? obj[24].toString() : null);
					fuelTaken.setFuelTakenDate(obj[25]!=null  ? new SimpleDateFormat("yyyy-MM-dd HH:MM").parse(obj[25].toString()) : null);
					fuelTaken.setAmount(obj[26]!=null ? Float.parseFloat(obj[26].toString()) : null);
					
					doc.setId(obj[27]!=null ? Integer.parseInt(obj[27].toString()) : null);
					doc.setDocumentName(obj[28]!=null ? obj[28].toString() : null);
				}
				fuelTaken.setBillDocument(doc);
				fuelTaken.setFuelSourceMaster(fsm);
				refueling.setFuelFromOutside(fuelTaken);
				MobilOilUsedDto mobilOil = new MobilOilUsedDto();
				if(obj[29]!=null) {
					
					mobilOil.setQuantity(Float.parseFloat(obj[29].toString()));
					
				}
				else 
				{
					mobilOil.setQuantity(0.0f);
				}
                if(obj[34]!=null) {
					
					mobilOil.setAmount(Float.parseFloat(obj[34].toString()));
					
				}
				else 
				{
					mobilOil.setAmount(0.0f);
				}
                if(obj[35]!=null) {
					
					mobilOil.setIsOutSide(Boolean.parseBoolean(obj[35].toString()));
					
				}
				if(obj[32]!=null)
					mobilOil.setMobilOilDrumName(obj[32].toString());
				DocumentDto mobilOilDoc = new DocumentDto();
                if(obj[38]!=null) 
                	mobilOilDoc.setId(Integer.parseInt(obj[38].toString()));
                if(obj[39]!=null) 
                	mobilOilDoc.setDocumentName(obj[39].toString());
                mobilOil.setMobilOilDocument(mobilOilDoc);
                
				refueling.setMobileOilUsed(mobilOil);
				AdBlueUsedDto adBlu = new AdBlueUsedDto();
				if(obj[30]!=null) {
					
					adBlu.setQuantity(Float.parseFloat(obj[30].toString()));
					
				}
				else
				{
					adBlu.setQuantity(0.0f);
				}
				if(obj[31]!=null)
					refueling.setVtsKms(Float.parseFloat(obj[31].toString()));
				else
					refueling.setVtsKms(0.0f);
				if(obj[33]!=null)
					adBlu.setAddBlueDrumName(obj[33].toString());
                if(obj[36]!=null) {
					
                	adBlu.setAmount(Float.parseFloat(obj[36].toString()));
					
				}
				else 
				{
					adBlu.setAmount(0.0f);
				}
                if(obj[37]!=null) {
					
                	adBlu.setIsOutSide(Boolean.parseBoolean(obj[37].toString()));
					
				}
                DocumentDto addBlueDoc = new DocumentDto();
                if(obj[40]!=null) 
                	addBlueDoc.setId(Integer.parseInt(obj[40].toString()));
                if(obj[41]!=null) 
                	addBlueDoc.setDocumentName(obj[41].toString());
                adBlu.setAddBlueDocument(addBlueDoc);
				refueling.setAdBluUsed(adBlu);
				List<DUReadingHistoryDto> readings = duReadingHistoryRepo.findByRefuelingId(id).stream()
						.map(reading -> new DUReadingHistoryDto(reading.getId(), reading.getDuStartReading(), reading.getDuEndReading()))
						.collect(Collectors.toList());
				refueling.setReadings(readings);
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return refueling;
	}
	
	private String vtsDieselDistance(Integer busRegId) throws JSONException {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		
		String busNumber = busMasterRepository.findById(busRegId).get().getBusRegNumber();
		
		List<IntegrationLog> log = integrationRepository.findByRegIdOrderByDesc(busNumber);
		
		String date = log!=null && log.size()>0  ? sdf.format(log.get(0).getLogDateTime()) : sdf.format(new Date());
		String response = vtsUtil.VTS_diesel_distance_api(busNumber, date);

		JSONObject jsonObj = new JSONObject(response);

		JSONArray root = jsonObj.getJSONArray("output");
		String status = "";
		for (int i = 0; i < root.length(); i++) {
			JSONObject jsonObj2 = root.getJSONObject(i);

			try {
				status = jsonObj2.getString("distance_covered");
			} catch (Exception e) {
				status = jsonObj2.getString("status");
			}
		}
		return status;

	}
	
	private boolean vtsDieselApi(Integer busRegid, String diesel) throws JSONException {
		
		String busNumber = busMasterRepository.findById(busRegid).get().getBusRegNumber();
		
//		List<IntegrationLog> log = integrationRepository.findByRegIdOrderByDesc(busNumber);
		
		String response = vtsUtil.VTS_diesel_api(busNumber, new Date(), diesel);
		JSONObject jsonObj = new JSONObject(response);

		JSONArray root = jsonObj.getJSONArray("output");
		
		String status = "";
		
		for (int i = 0; i < root.length(); i++) {
			JSONObject jsonObj2 = root.getJSONObject(i);

			try {
				status = jsonObj2.getString("status");
			} catch (Exception e) {
				System.out.println("Error occured while retriving vts data , please check the vts api.");
			}
		}
		boolean out = false;
		if (status.equals("ERROR")) {
			out = false;
		}
		if (status.equals("SUCCESS")) {
			out = true;
		}
		return out;
	}


	public ResponseEntity<BusDieselCorrectionDto> getBusfuelingCorrectionData(Integer busId, String refuelingDate)
			throws ParseException {
		// TODO Auto-generated method stub
		BusDieselCorrectionDto output = new BusDieselCorrectionDto();
		try {
			List<BusRefuelingMaster> busMasterRecords = busRefuelingMasterRepository.findByIdAndRefuelingDate(busId,
					refuelingDate);

			if (busMasterRecords.size() > 0) {
				output.setBusrefuelingId(busMasterRecords.get(0).getId());
				output.setBusId(busId);
				output.setBusRefuelingDate(refuelingDate);
				output.setDieselTaken(busMasterRecords.get(0).getIssuedDiesel() != null
						? String.valueOf(busMasterRecords.get(0).getIssuedDiesel()) : "0");
				output.setDieselTank(busMasterRecords.get(0).getFuelTankMaster().getTankName());
				output.setDispensingUnit(busMasterRecords.get(0).getDispensingUnitMaster().getDisUnitName());
			}
			status = HttpStatus.OK;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("Exception occured while fetching data");
		}
		return new ResponseEntity<BusDieselCorrectionDto>(output, status);
	}

	public ResponseEntity<Boolean> postBusfuelingCorrectionData(BusDieselCorrectionDto input) {

		Optional<BusRefuelingMaster> masterRecord = busRefuelingMasterRepository.findById(input.getBusrefuelingId());
		masterRecord.get().setIssuedDiesel(Float.parseFloat(input.getCrtDieselTaken()));
		masterRecord.get().setDiesel_corrected(true);
		boolean saveStatus = false;
		long timeDiff = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			timeDiff = timeDiff(sdf.format(new Date()), sdf.format(masterRecord.get().getCreatedOn()));
			if (timeDiff <= 2) {
				busRefuelingMasterRepository.save(masterRecord.get());
				saveStatus = true;
				status = HttpStatus.OK;
			}
		} catch (ParseException e1) {
			log.info("Exception occured while saving data!");
			saveStatus = false;
		}
		return new ResponseEntity<>(saveStatus, status);

	}

	private long timeDiff(String s1, String s2) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = sdf.parse(s1);
		Date d2 = sdf.parse(s2);
		long diff = d.getTime() - d2.getTime();
		long diffInDays = diff / (24 * 60 * 60 * 1000);
		return diffInDays;

	}


	@Override
	public List<BusDieselCorrectionDto> getAllBusfuelingCorrectionData() {
		List<BusDieselCorrectionDto> output = new ArrayList<>();
		try {
			List<BusRefuelingMaster> busMasterRecords = busRefuelingMasterRepository.findAllDieselCorectedValues();
			for(BusRefuelingMaster mst : busMasterRecords) {
				BusDieselCorrectionDto tempObj = new BusDieselCorrectionDto();
				tempObj.setBusrefuelingId(mst.getId());
				tempObj.setBusId(mst.getBusMaster().getId());
				tempObj.setBusRefuelingDate(mst.getCreatedOn().toString());
				tempObj.setDieselTaken(mst.getIssuedDiesel() != null
						? String.valueOf(mst.getIssuedDiesel()) : "0");
				tempObj.setDieselTank(mst.getFuelTankMaster().getTankName());
				tempObj.setDispensingUnit(mst.getDispensingUnitMaster().getDisUnitName());
				output.add(tempObj);
			}

	} catch (Exception e) {
		log.info("Error occured while fetching the data!");
	}
		return output;
}


	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> deleteBusRefuellingByStatusFlag(Integer id) {
		log.info("Entering into deleteBusRefuellingByStatusFlag service");
		try {
			int i = busRefuelingMasterRepository.deleteBusRefuellingByStatusFlag(true,id);
			if (i == 1)
				return new ResponseEntity<>(
						new ResponseStatus("Bus Refuelling has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.FORBIDDEN);
		}
	}
	
	
}
