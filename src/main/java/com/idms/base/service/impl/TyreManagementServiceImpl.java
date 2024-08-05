package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusTyreAssoChildWrapper;
import com.idms.base.api.v1.model.dto.BusTyreAssoParentWrapper;
import com.idms.base.api.v1.model.dto.BusTyreAssociationDto;
import com.idms.base.api.v1.model.dto.BusTyreAssociationForm;
import com.idms.base.api.v1.model.dto.BusTyreAssociationHistoryDto;
import com.idms.base.api.v1.model.dto.BusTyreAssociationList;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.MakerTyreDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.api.v1.model.dto.OldTyreMasterDto;
import com.idms.base.api.v1.model.dto.TakenOffReasonDto;
import com.idms.base.api.v1.model.dto.TotalPlainHillKmsCalculationDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyreMasterFormDto;
import com.idms.base.api.v1.model.dto.TyreMgmtDto;
import com.idms.base.api.v1.model.dto.TyrePositionDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.api.v1.model.dto.TyreTypeDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusTyreAssociation;
import com.idms.base.dao.entity.BusTyreAssociationHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MakerTyreDetails;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TyreCondition;
import com.idms.base.dao.entity.TyreMaker;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.entity.TyrePosition;
import com.idms.base.dao.entity.TyreSize;
import com.idms.base.dao.entity.TyreType;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusRefuelingMasterRepository;
import com.idms.base.dao.repository.BusTyreAssociationHistoryRepository;
import com.idms.base.dao.repository.BusTyreAssociationRepository;
import com.idms.base.dao.repository.BusTyreTypeSizeMappingRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MakerTyreDetailsRepository;
import com.idms.base.dao.repository.TakenOffReasonRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TyreConditionRepository;
import com.idms.base.dao.repository.TyreMakersRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.dao.repository.TyrePositionRepository;
import com.idms.base.dao.repository.TyreSizeRepository;
import com.idms.base.dao.repository.TyreTypeRepository;
import com.idms.base.service.TyreManagementService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.TyreCostCalculationUtility;
import com.idms.base.util.TyreManagementUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TyreManagementServiceImpl implements TyreManagementService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	TransportUnitRepository transRepo;
	
	@Autowired
	TyreTypeRepository tyreTypeRepo;
	
	@Autowired
	TyreSizeRepository tyreSizeRepo;
	
	@Autowired
	TyreMakersRepository tyreMakerRepo;
	
	@Autowired
	TyreMasterRepository tyreMstRepo;
	
	@Autowired
	TyrePositionRepository tyrePosRepo;
	
	@Autowired
	TyreConditionRepository tyreConditionRepo;
	
	@Autowired
	MakerTyreDetailsRepository mtdRepo;
	
	@Autowired
	BusTyreAssociationHistoryRepository btahRepo;
	
	@Autowired
	BusMasterRepository busRepo;
	
	@Autowired
	BusTyreAssociationRepository btaRepo;
	
	@Autowired
	BusTyreTypeSizeMappingRepository bttsRepo;
	
	@Autowired
	BusRefuelingMasterRepository busRufelingMstRepo;
	
	@Autowired
	TakenOffReasonRepository takenOffReasonRepo;
	
	@Autowired
	TyreCostCalculationUtility tyreCostCalculationUtility;
	
	@Autowired
	TyreManagementUtility tyreManagementUtility;
	

	@Override
	public TyreMasterFormDto getTyreMasterForm(String dpCode) {
		log.info("Enter into getTyreMasterForm of service TyreManagementServiceImpl");
		TyreMasterFormDto form = new TyreMasterFormDto();
		try {
			
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			
			
			
			List<TransportDto> tpList = transRepo.allTransportMasterByDepot(depotMaster.getId()).stream()
					.map(transport -> new TransportDto(transport.getTransportUnitMaster().getId(), transport.getTransportUnitMaster().getTransportName()))
					.collect(Collectors.toList());
			
			form.setTpList(tpList);
			form.setDepotId(depotMaster.getId());
			
			List<TyreTypeDto> tyreTypeList = tyreTypeRepo.findAllByStatus(true).stream()
					.map(tyreType -> new TyreTypeDto(tyreType.getId(),tyreType.getName())).collect(Collectors.toList());
			
			if(tyreTypeList!=null && tyreTypeList.size()>0) {
				form.setTyreTypeList(tyreTypeList);
			}
			
			List<TyreSizeDto> tyreSizeList = tyreSizeRepo.findAllByStatus(true).stream()
					.map(tyreSize -> new TyreSizeDto(tyreSize.getId(),tyreSize.getSize())).collect(Collectors.toList());
			
			if(tyreSizeList!=null && tyreSizeList.size()>0) {
				form.setTyreSizeList(tyreSizeList);
			}
			
			List<TyreMakerDto> tyreMakerList = tyreMakerRepo.findAllByStatus(true).stream()
					.map(tyreMaker -> new TyreMakerDto(tyreMaker.getId(),tyreMaker.getName())).collect(Collectors.toList());
			if(tyreMakerList!=null && tyreMakerList.size()>0) {
				form.setTyreMakerList(tyreMakerList);
			}
			
			List<TyrePositionDto> tyrePositionList = tyrePosRepo.findAllByStatus(true).stream()
					.map(tyrePos -> new TyrePositionDto(tyrePos.getId(),tyrePos.getName())).collect(Collectors.toList());
			if(tyrePositionList!=null && tyrePositionList.size()>0) {
				form.setTyrePositionList(tyrePositionList);
			}
			
			List<TyreConditionDto> tyreConditionList = tyreConditionRepo.findAllByStatus(true).stream()
					.map(tyreCondition -> new TyreConditionDto(tyreCondition.getId(),tyreCondition.getName())).collect(Collectors.toList());
			
			if(tyreConditionList!=null && tyreConditionList.size()>0) {
				form.setTyreConditionList(tyreConditionList);
				for(int i=0;i<tyreConditionList.size();i++){
					if(tyreConditionList.get(i).getName().equals("New")){
						 tyreConditionList.remove(i);
					}
				}
			}
			
			List<BusMasterDto> busList = busRepo.findAllBusesByDepot(dpCode).stream()
					.map(bus -> new BusMasterDto(bus.getId(),bus.getBusRegNumber())).collect(Collectors.toList());
			
			if(busList!=null && busList.size()>0) {
				form.setBusList(busList);
				
				}	
			List<TakenOffReasonDto> takenOffReasonList = takenOffReasonRepo.findAllWithParentNull().stream()
					.map(reason -> this.mapper.map(reason, TakenOffReasonDto.class)).collect(Collectors.toList());
					
					if(takenOffReasonList!=null && takenOffReasonList.size()>0) {
						form.setTakenOffReasonList(takenOffReasonList);
						
						}
					
					DepotMasterDto dto = null;
					List<DepotMasterDto> dtoList = new ArrayList<>(); 
					List<Object[]> depotList = depotRepo.findByDepocodeAndId();
					for(Object[] depotObj : depotList){
						dto = new DepotMasterDto();
						if(depotObj[0] != null){
							dto.setId(Integer.parseInt(depotObj[0].toString()));
						}if(depotObj[1] != null){
							dto.setDepotCode(depotObj[1].toString());
						}
						dtoList.add(dto);
					}
					form.setDepotList(dtoList);
					
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return form;
	}

	@Override
	public List<TyreMasterDto> getAllTyreListByDepot(String dpCode) {
		List<TyreMasterDto> tyreList = new ArrayList<>();;
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> list = tyreMstRepo.findAllByDepot(depotMaster.getId());
			for(Object[] o : list) {
				TyreMasterDto tyre = new TyreMasterDto();
				if(o[0]!=null)
				tyre.setId(Integer.parseInt(o[0].toString()));
				TransportDto transport = new TransportDto();
				if(o[1]!=null)
				transport.setTransportName(o[1].toString());
				tyre.setTransportUnit(transport);
				
				
				TyreTypeDto type = new TyreTypeDto();
				if(o[2]!=null)
				type.setName(o[2].toString());
				
				tyre.setTyreType(type);
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[3]!=null)
				size.setSize(o[3].toString());
				
				tyre.setTyreSize(size);
				if(o[4]!=null)
				tyre.setTyreNumber(o[4].toString());
				
				TyreMakerDto maker = new TyreMakerDto();
				if(o[5]!=null)
				maker.setName(o[5].toString());
				tyre.setTyreMake(maker);
				if(o[6]!=null)
				tyre.setExpectedLife(o[6].toString());
				if(o[7]!=null)
				tyre.setTyreCost(Double.parseDouble(o[7].toString()));
				
				if(o[8]!=null)
				{
					tyre.setOldMileage(Float.parseFloat(o[8].toString()));
				}
				else
				{
					tyre.setOldMileage(Float.parseFloat("0"));
				}
				if(o[9]!=null)
					tyre.setPositionName(o[9].toString());
				if(o[10]!=null)
					tyre.setBusRegistrationNo(o[10].toString());
				if(o[11]!=null && o[16]!=null)
					tyre.setTyreTag(tyreManagementUtility.concatConditionWithTag(tyre.getId()));
				if(o[12]!=null)
					tyre.setInvoiceNumber(o[12].toString());
				if(o[13]!=null)
					tyre.setInvoiceDateView(o[13].toString());
				if(o[14]!=null)
					tyre.setTyrePurchaseDateView(o[14].toString());
				if(o[15]!=null){
					DepotMaster depotObj = depotRepo.findById(Integer.parseInt(o[15].toString())).get();
					tyre.setSourceOfOrigin(depotObj.getDepotName());
				}
				tyreList.add(tyre);
			}
			
			} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		Integer tyreId = null;
		int i = 0;
		DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
		List<Object[]> tyreMileageDtls = tyreMstRepo.getTyreCurrentMileageDtls(depotMaster.getId());
		TyreMasterDto tyre1 = new TyreMasterDto();
		for(TyreMasterDto tyreObj : tyreList){
		for(Object[] ob:tyreMileageDtls)
		{
			if(ob[0] != null)
			 tyreId = Integer.parseInt(ob[0].toString());
			if(tyreId != null && tyreObj.getId().equals(tyreId)){
				if(ob[1]!=null)
				tyreList.get(i).setCurrentMileage(ob[1].toString());
				break;
			
			}
		}
		i++;
		}
		
		Integer tyreId1 = null;
		int i1= 0;
		DepotMaster depotMaster1 = depotRepo.findByDepotCode(dpCode);
		List<Object[]> plainAndHillKms = tyreMstRepo.getPlainAndHillKms(depotMaster1.getId());
		TyreMasterDto tyre2 = new TyreMasterDto();
		for(TyreMasterDto tyreObj : tyreList){
		for(Object[] ob:plainAndHillKms)
			{
				if(ob[0] != null)
				 tyreId1 = Integer.parseInt(ob[0].toString());
				if(tyreId1 != null && tyreObj.getId().equals(tyreId1)){
					if(ob[1]!=null)
					tyreList.get(i1).setHillKms(Integer.parseInt(ob[1].toString()));
					else
					tyreList.get(i1).setHillKms(0);	
					if(ob[2]!=null)
					tyreList.get(i1).setPlainKms(Integer.parseInt(ob[2].toString()));
					else
					tyreList.get(i1).setPlainKms(0);
					break;
				
				}//if
		
		}//for
			i1++;
		}//for
		return tyreList;
	}

	@Override
	public List<TyreType> getTyreTypeList(Integer makerId) {
		List<TyreType> tyreTypeList= new ArrayList<>();;
		try {
			tyreTypeList = tyreTypeRepo.findAllByTyreMakerId(makerId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tyreTypeList;
	}

	@Override
	public List<TyreSize> getTyreSizeList(Integer tyreMakerId, Integer tyreTypeId) {
		List<TyreSize> tyreSizeList = new ArrayList<>();
		try {
			tyreSizeList = tyreSizeRepo.findAllByTyreTypeId(tyreMakerId, tyreTypeId);
		} catch(Exception e) {
			
		}
		return tyreSizeList;
	}

	@Override
	public MakerTyreDetailsDto getTyreCostMilage(Integer tyreMakerId, Integer tyreTypeId, Integer tyreSizeId) {
		MakerTyreDetailsDto details = null;
		try {
			MakerTyreDetails det = mtdRepo.getTyreCostMilage(tyreMakerId, tyreTypeId, tyreSizeId);
			details = this.mapper.map(det,MakerTyreDetailsDto.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return details;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveTyreDetails(TyreMaster tyreMaster) {
		log.info("Entering into saveTyreDetails service");
		Calendar c = Calendar.getInstance(); 
		DepotMaster depot = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date invoiceDate = null;
		try {
			invoiceDate = sdf.parse(sdf.format(c.getTime()));
			DepotMaster tempDepot = depotRepo.findByDepotCode(tyreMaster.getDepot().getDepotCode());
			//String tyreTagAuto = generateTyreTag(tempDepot.getDepotName());
			TyreCondition tyreCondition = tyreConditionRepo.findByName("New");
			String tyreTagAuto  = fetchTyreTag(tempDepot.getDepotName(),tyreCondition.getId());
			//String tyreTagAuto = tyreMasterDto.getUseTyreID();
			tyreMaster.setTyreTag(tyreTagAuto);	
			if(tyreMaster.getInvoiceDate() != null){
			c.setTime(tyreMaster.getInvoiceDate()); 
			c.add(Calendar.DATE, 0);
			tyreMaster.setInvoiceDate(invoiceDate);
			}
			if(tyreMaster.getInvoiceNumber() != null){
				tyreMaster.setInvoiceNumber(tyreMaster.getInvoiceNumber());
			}
			tyreMaster.setAvailable(true);
			tyreMaster.setDepot(tempDepot);
			depot = new DepotMaster();
			depot.setId(tyreMaster.getSourceOfOriginTyre().getId());
			tyreMaster.setSourceOfOriginTyre(depot);
			tyreMaster.setTyreCondition(tyreCondition);
			tyreMaster.setOldTyre(false);
			tyreMstRepo.save(tyreMaster);
			
			/*if(tyreMaster.getBusHistory()!=null && tyreMaster.getBusHistory().size()>0) {
				for(BusTyreAssociationHistory btah:tyreMaster.getBusHistory()) {
					btah.setTyre(tyreMaster);
					btahRepo.save(btah);
				}
			}*/
			
			return new ResponseEntity<>(
					new ResponseStatus("Tyre master has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<BusTyreAssociationList> getAllBusTyreAssociationList(String dpCode) {
		List<BusTyreAssociationList> associationList = new ArrayList<>();
		TransportUnitMaster tuMaster = null;
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> list = busRepo.getBusTyreAssociationList(depotMaster.getId());
			for(Object[] o : list) {
				BusTyreAssociationList association = new BusTyreAssociationList();
				tuMaster = new TransportUnitMaster();
				if(o[0] !=null)
				association.setBusId(Integer.parseInt(o[0].toString()));
				if(o[1] !=null)
				association.setBusNumber(o[1].toString());
				if(o[2] !=null)
				association.setBusTypeName(o[2].toString());
				if(o[3] !=null)
				association.setReqTyreCount(Integer.parseInt(o[3].toString()));
				if(o[4] !=null)
				association.setCurTyreCount(Integer.parseInt(o[4].toString()));
				if(o[5] !=null)
				tuMaster.setId(Integer.parseInt(o[5].toString()));
				if(o[6] !=null)
				tuMaster.setTransportName(o[6].toString());
				association.setTransportUnit(tuMaster);
                if(o[4]!=null){
					
					association.setCurrentCountWithSpare(Integer.parseInt(o[4].toString()));
				}
				if(o[7] !=null && o[4]!=null){
					
					association.setCurTyreCount(Integer.parseInt(o[4].toString())-1);
				}
				associationList.add(association);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return associationList;
	}

	@Override
	public BusTyreAssociationForm busTyreAssociationFormLoad(Integer busId) {
		BusTyreAssociationForm form = new BusTyreAssociationForm();
		try {
			List<BusTyreAssociationDto> bta = new ArrayList<>();
			List<Object[]> list = btaRepo.findAllByBusId(busId);
			for(Object[] o: list) {
				
				BusTyreAssociationDto obj = new BusTyreAssociationDto();
				
				BusMasterDto bmDto = new BusMasterDto();
				if(o[0]!=null)
					bmDto.setBusRegNumber(o[0].toString());
				else 
					bmDto.setBusRegNumber("");
				
				obj.setBus(bmDto);
				
				TyreMasterDto tmDto = new TyreMasterDto();
				if(o[1]!=null)
					tmDto.setTyreNumber(o[1].toString());
				else
					tmDto.setTyreNumber("");
				
				obj.setTyre(tmDto);
				
				TyrePositionDto tyrePosition = new TyrePositionDto();
				if(o[2]!=null)
					tyrePosition.setName(o[2].toString());
				else
					tyrePosition.setName("");
				
				obj.setTyrePosition(tyrePosition);
				
				if(o[3]!=null)
					obj.setInstallDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o[3].toString()));
				else
					obj.setInstallDate(null);
				
				if(o[4]!=null)
					obj.setBusFitted(Boolean.parseBoolean(o[4].toString()));
				else
					obj.setBusFitted(false);
				
				if(o[5]!=null)
					obj.setKmsDone(Double.parseDouble(o[5].toString()));
				else
					obj.setKmsDone(0.0);
				if(o[6]!=null)
					obj.setSizeName(o[6].toString());
				if(o[7]!=null)
					obj.setMakerName(o[7].toString());	
				if(o[8]!=null)
					obj.setTyreType(o[8].toString());
				if(o[9]!=null)
					obj.setTyreTag(o[9].toString());
				if(o[10]!=null){
				Double recoveredCost = tyreCostCalculationUtility.calculateTyreRecoveredCostByCondition(Integer.parseInt(o[10].toString()));
				obj.setRecoveredCost(recoveredCost);
				Float kmsDoneByCondition = tyreCostCalculationUtility.calculateTyreKmsByCondition(Integer.parseInt(o[10].toString()));
				if(kmsDoneByCondition != null)
				obj.setKmsDoneByCondition(kmsDoneByCondition.toString());
				Float totalKmsDone = tyreCostCalculationUtility.calculateTyreTotalKms(Integer.parseInt(o[10].toString()));
				if(totalKmsDone != null)
				obj.setTotalKilometerDone(totalKmsDone.toString());
				Float kmsDoneOnCurrent = tyreCostCalculationUtility.calculateTotalKmsInCurretBus(Integer.parseInt(o[10].toString()),Integer.parseInt(o[11].toString()));
				if(kmsDoneOnCurrent != null)
				obj.setKilometerDoneOnBus(kmsDoneOnCurrent.toString());
				}
				
				bta.add(obj);
			}
			form.setBusTyreAssociation(bta);
			BusMaster bus = busRepo.findById(busId).get();
			List<Integer> typeList = bttsRepo.getTyreTypeList(bus.getBusType().getId());
			List<Object[]> tyreList = tyreMstRepo.getTyreListNotAssignedByDepot(bus.getDepot().getId(),typeList);
			List<TyreMasterDto> tyreDtoList = new ArrayList<>();
			for(Object[] o: tyreList) {
				TyreMasterDto tyreDto = new TyreMasterDto();
				
				tyreDto.setId(Integer.parseInt(o[0].toString()));
				
				tyreDto.setTyreNumber(o[1].toString());
				TyreSizeDto sizeDto = new TyreSizeDto();
				if(o[2]!=null) {
					sizeDto.setSize(o[2].toString());
				} else {
					sizeDto.setSize("");
				}
				tyreDto.setTyreSize(sizeDto);
				TyreMakerDto maker = new TyreMakerDto();
				if(o[3]!=null) {
					maker.setName(o[3].toString());
				} else {
					maker.setName("");
				}
				tyreDto.setTyreMake(maker);
				
				TyreConditionDto condition = new TyreConditionDto();
				if(o[4]!=null) {
					condition.setName(o[4].toString());
				} else {
					condition.setName("");
				}
				tyreDto.setTyreCondition(condition);
				tyreDtoList.add(tyreDto);
			}
			form.setTyreList(tyreDtoList);
			List<Object[]> tyrePositionList = tyrePosRepo.getRemaingPositionList(busId, bus.getTyreCount().getId());
			List<TyrePositionDto> tyrePositionDtoList = new ArrayList<>();
			for(Object[] o: tyrePositionList) {
				TyrePositionDto tyrePosDto = new TyrePositionDto(Integer.parseInt(o[0].toString()), o[1].toString());
				tyrePositionDtoList.add(tyrePosDto);
			}
			form.setTyrePositions(tyrePositionDtoList);
			form.setBusId(busId);
			
			List<TyreTypeDto> tyreTypeList = tyreTypeRepo.findAllByStatus(true).stream()
					.map(tyreType -> new TyreTypeDto(tyreType.getId(),tyreType.getName())).collect(Collectors.toList());
			
			if(tyreTypeList!=null && tyreTypeList.size()>0) {
				form.setTyreTypeList(tyreTypeList);
			}
			
			List<TyreSizeDto> tyreSizeList = tyreSizeRepo.findAllByStatus(true).stream()
					.map(tyreSize -> new TyreSizeDto(tyreSize.getId(),tyreSize.getSize())).collect(Collectors.toList());
			
			if(tyreSizeList!=null && tyreSizeList.size()>0) {
				form.setTyreSizeList(tyreSizeList);
			}
			
			List<TyreMakerDto> tyreMakerList = tyreMakerRepo.findAllByStatus(true).stream()
					.map(tyreMaker -> new TyreMakerDto(tyreMaker.getId(),tyreMaker.getName())).collect(Collectors.toList());
			if(tyreMakerList!=null && tyreMakerList.size()>0) {
				form.setTyreMakerList(tyreMakerList);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return form;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveBusTyreAssociations(BusMaster busMaster) {
		log.info("Entering into saveTyreDetails service");
		try {
			
			BusMaster busMst = busRepo.findById(busMaster.getId()).get();
			
			if(busMaster.getTyreLists()!=null && busMaster.getTyreLists().size()>0) {
				for(BusTyreAssociation bta:busMaster.getTyreLists()) {
					TyreMaster tyre = tyreMstRepo.findById(bta.getTyre().getId()).get();
					tyre.setTyrePosition(bta.getTyrePosition());
					int i = tyreMstRepo.updateTyrePosition(bta.getTyrePosition().getId(),tyre.getId());
					//tyreMstRepo.save(tyre);
					bta.setTyreCondition(tyre.getTyreCondition());
					bta.setBus(busMst);
					btaRepo.save(bta);
				}
				return new ResponseEntity<>(
						new ResponseStatus("Bus Tyre Association has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new ResponseStatus("Atleast one association should be there.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public TyreMasterDto getTyreDetails(Integer tyreId) {
		TyreMasterDto dto = new TyreMasterDto();
		try {
			TyreMaster tyre = tyreMstRepo.findById(tyreId).get();
			dto.setId(tyre.getId());
			dto.setTyreNumber(tyre.getTyreNumber());
			dto.setTransportUnit(this.mapper.map(tyre.getTransportUnit(), TransportDto.class));
			dto.setTyreSize(this.mapper.map(tyre.getTyreSize(), TyreSizeDto.class));
			dto.setTyreMake(this.mapper.map(tyre.getTyreMake(), TyreMakerDto.class));
			dto.setTyreCondition(this.mapper.map(tyre.getTyreCondition(), TyreConditionDto.class));
			dto.setTyreType(this.mapper.map(tyre.getTyreType(), TyreTypeDto.class));
			dto.setDepot(this.mapper.map(new DepotMasterDto(tyre.getDepot().getId(), tyre.getDepot().getDepotName()), DepotMasterDto.class));
			dto.setTyreCost(tyre.getTyreCost());
			dto.setExpectedLife(tyre.getExpectedLife());
			dto.setBusFitted(tyre.isBusFitted());
			/*if(tyre.getUseTyreID()!=null)
			{
				dto.setUseTyreID(tyre.getUseTyreID());
				}
			else
			{
				dto.setUseTyreID("0");
			}*/
			
			if(tyre.getSourceOfOriginTyre()!=null)
			{
				dto.setSourceOfOrigin(tyre.getSourceOfOriginTyre().getDepotName());	
			}
			else
			{
				dto.setSourceOfOrigin("");
			}
		 
			
			BusTyreAssociation bta = tyre.getBus();
			if(bta!=null) {
				BusTyreAssociationDto btaDto = new BusTyreAssociationDto();
				btaDto.setBus(this.mapper.map(new BusMasterDto(bta.getBus().getId(), bta.getBus().getBusRegNumber()), BusMasterDto.class));
				btaDto.setBusFitted(true);
				btaDto.setTyrePosition(this.mapper.map(new TyrePositionDto(bta.getTyrePosition().getId(), bta.getTyrePosition().getName()), TyrePositionDto.class));
				btaDto.setInstallDate(bta.getInstallDate());
				btaDto.setKmsDone(bta.getKmsDone());
				dto.setBus(btaDto);
			} else {
				BusTyreAssociationDto btaDto = new BusTyreAssociationDto();
				dto.setBus(btaDto);
			}
			List<BusTyreAssociationHistoryDto> hList = new ArrayList<>();
			if(tyre.getBusHistory().size()>0) {
				for(BusTyreAssociationHistory btah: tyre.getBusHistory()) {
					BusTyreAssociationHistoryDto o = new BusTyreAssociationHistoryDto();
					o.setBus(this.mapper.map(new BusMasterDto(btah.getBus().getId(), btah.getBus().getBusRegNumber()), BusMasterDto.class));
					o.setInstallDate(btah.getInstallDate());
					o.setBusFitted(btah.getBusFitted());
					o.setKmsDone(btah.getKmsDone());
					o.setRemovalDate(btah.getRemovalDate());
					o.setTyrePosition(this.mapper.map(new TyrePositionDto(btah.getTyrePosition().getId(), btah.getTyrePosition().getName()), TyrePositionDto.class));
					o.setTyreCondition(this.mapper.map(btah.getTyreCondition(), TyreConditionDto.class));
					if(btah.getReason()!=null)
						o.setReason(this.mapper.map(btah.getReason() , TakenOffReasonDto.class));
					else {
						TakenOffReasonDto reason = new TakenOffReasonDto();
						reason.setReasonName("");
						o.setReason(reason);
					}
						
					o.setRemarks(btah.getRemarks());
					if(btah.getHillKms()!=null)
					{
					o.setHillKms(btah.getHillKms());
					}
					else
					{
						o.setHillKms(0);
					}
					if(btah.getPlainKms()!=null)
					{
					o.setPlainKms(btah.getPlainKms());
					}
					else
					{
						o.setPlainKms(0);
					}
					
					hList.add(o);
				}
			}
			dto.setBusHistory(hList);
			
            /*List<TyreMaster> Omileage = tyreMstRepo.findAll();
			
			List<Float> oldMileageDetails = Omileage.stream().map(entity->entity.getOldMileage()).collect(Collectors.toList());
			int sum = oldMileageDetails.stream().mapToInt(i -> i.intValue()).sum();
			
			List<BusRefuelingMaster> Cmileage = busRufelingMstRepo.findAll();
			
			 List<Float> currentmileage = Cmileage.stream().map(entity->entity.getKmplAsperActualKms()).collect(Collectors.toList());
			 int sum2 = currentmileage.stream().mapToInt(i -> i.intValue()).sum();
			
			Integer mileage = sum + sum2;
			
			dto.setMileage(mileage);
			*/
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public List<TyreConditionDto> getTyreConditionList() {
		List<TyreConditionDto> tyreConditionList = tyreConditionRepo.findAllByStatus(true).stream()
				.map(tyreCondition -> new TyreConditionDto(tyreCondition.getId(),tyreCondition.getName())).collect(Collectors.toList());
		return tyreConditionList;
	}

	@Override
	public List<TyreMasterDto> getAllTyresByStatus() {
		List<TyreMasterDto> tyreList = tyreMstRepo.findAllByStatus(true).stream()
				.map(tyre -> new TyreMasterDto(tyre.getId(),tyre.getTyreNumber())).collect(Collectors.toList());
		return tyreList;
	}

	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateTyreDtls(String fromdate,String todate,Float oldMileage,Integer Id)
	{

		
		try {
			Integer i = tyreMstRepo.update( fromdate,todate, oldMileage,Id);
			if ( i == 1)
				return new ResponseEntity<>(new ResponseStatus("Status has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		
		
	}

	@Override
	public TyreMasterDto getuseTyreId(String dpCode, Integer tyreConditionId) {
		
       TyreMasterDto t= new TyreMasterDto();
		
		DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
		String depotName = depotMaster.getDepotName();
		
		String depotName1 = depotName.replaceAll("-", "");
	 
		String substring = depotName1.substring(depotName1.length()-4);
		
		Date date = new Date();
		SimpleDateFormat DateFor = new SimpleDateFormat("yyyyMM");
		String format = DateFor.format(date);
		

		if(tyreConditionId==1)
		{
			t.setUseTyreID(substring+format+"N"+"0001");
		}
		else if(tyreConditionId==2)
		{
			t.setUseTyreID(substring+format+"R1"+"0001");
		}
	  else if(tyreConditionId==3)
		{
		  t.setUseTyreID(substring+format+"R2"+"0001");
		}
	  else if(tyreConditionId==4)
		{
		  t.setUseTyreID(substring+format+"R3"+"0001");
		}
	  else if(tyreConditionId==5)
		{
		  t.setUseTyreID(substring+format+"C"+"0001");
		}
		
		return t;
	}

	@Override
	public TyreMasterFormDto getNumbers(String dpCode, Integer tyreConditionId)
	{ 
		TyreMasterFormDto tyrenumb= new TyreMasterFormDto();
		List<TyreMgmtDto > tn= new ArrayList<>();
		DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
		Integer id = depotMaster.getId();
		
		if(tyreConditionId==1)
		{
			return null;
			
		}
		else
		{
		
		 List<Object[]> tyreNumber = tyreMstRepo.getTyreNumber(id,tyreConditionId);
		 
		 for(Object[] ob:tyreNumber)
		 {
			 TyreMgmtDto  t = new TyreMgmtDto ();
			 t.setTyreNumber(ob[0].toString());
			 tn.add(t);
		 }
		 
		 tyrenumb.setTyreNumber(tn);
		
		return tyrenumb;
		}
	}
	private String generateTyreTag(String depotName){
		StringBuffer tyreTag = new StringBuffer();
		String[] depotArray = null;
		String depotCode = "";
		String depot = null;
		Date date = new Date();
		String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
		try {
			DepotMaster tempDepot = depotRepo.findByDepotCode(depotName);
		    depot = tempDepot.getDepotName();
		    tyreTag.append("TAG");
			if(depot.contains("-")){
			 depotArray = depot.split("-");
			 depotCode = depotArray[1].substring(0, 3);
			}
			else{
				depotCode = depot.substring(0,3);
			}
			tyreTag.append(depotCode);
			String[] dateArray = modifiedDate.split("-");
			tyreTag.append(dateArray[0]);
			tyreTag.append(dateArray[1]);
			tyreTag.append(dateArray[2]);
			Integer tyreId = tyreMstRepo.getMaxTyreId();
			if(tyreId != null)
				tyreTag.append(tyreId.toString());
			else
				tyreTag.append("00");
		}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return tyreTag.toString();
	}

	@Override
	public ResponseEntity<ResponseStatus> saveTyreDetailsAndAssociations(BusTyreAssoParentWrapper parentDto) {
		log.info("Entering into saveTyreDetailsAndAssociations service");
		TyreMaster tyreMaster = null;
		BusTyreAssociation busTyreAssociation = null;
		Calendar c = Calendar.getInstance(); 
		Calendar c1 = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date invoiceDate = null;
		Date purchaseDate = null;
		DepotMaster sourceOfOrigin = null;
		try{
			for(BusTyreAssoChildWrapper childDto : parentDto.getChildDto()){
				tyreMaster = new TyreMaster();
				busTyreAssociation = new BusTyreAssociation();
				tyreMaster.setBusFitted(true);
				tyreMaster.setExpectedLife(childDto.getExpectedLife());
				if(childDto.getInvoiceDate() != null){
					c.setTime(parentDto.getInvoiceDate()); 
					c.add(Calendar.DATE, 0);
					invoiceDate = sdf.parse(sdf.format(c.getTime()));
					tyreMaster.setInvoiceDate(invoiceDate);
				}
				sourceOfOrigin = new DepotMaster();
				sourceOfOrigin.setId(childDto.getSourceOfOriginTyre().getId());
				tyreMaster.setSourceOfOriginTyre(sourceOfOrigin);
				if(childDto.getTyreCost() != null)
				tyreMaster.setTyreCost(Double.parseDouble(childDto.getTyreCost()));
				if(childDto.getInvoiceNumber() != null)
				tyreMaster.setInvoiceNumber(childDto.getInvoiceNumber());
				TransportUnitMaster transport =  transRepo.findById(parentDto.getTransportUnit().getId()).get();
				tyreMaster.setTransportUnit(transport);
				TyreMaker tyreMaker = tyreMakerRepo.findById(childDto.getTyreMake().getId()).get();
				tyreMaster.setTyreMake(tyreMaker);
				tyreMaster.setTyreNumber(childDto.getTyreNumber());
				TyreCondition tyreCondition = tyreConditionRepo.findByName("New");
				tyreMaster.setTyreCondition(tyreCondition);
				TyreSize tyreSize = tyreSizeRepo.findById(childDto.getTyreSize().getId()).get();
				tyreMaster.setTyreSize(tyreSize);
				TyreType tyreType = tyreTypeRepo.findById(childDto.getTyreType().getId()).get();
				tyreMaster.setTyreType(tyreType);
				DepotMaster tempDepot = depotRepo.findByDepotCode(parentDto.getDepot().getDepotCode());
				//String tyreTagAuto = generateTyreTag(tempDepot.getDepotName());
				String tyreTagAuto = fetchTyreTag(tempDepot.getDepotName(),tyreCondition.getId()); 
				tyreMaster.setTyreTag(tyreTagAuto);	
				tyreMaster.setDepot(tempDepot);
				if(childDto.getPurchaseDate() != null){
					c1.setTime(childDto.getPurchaseDate()); 
					c1.add(Calendar.DATE, 0);
					purchaseDate = sdf.parse(sdf.format(c1.getTime()));
				}
				tyreMaster.setTyrePurchaseDate(purchaseDate);
				tyreMaster.setOldTyre(false);
				tyreMaster.setAvailable(true);
				TyrePosition tyrePos = tyrePosRepo.findById(childDto.getTyrePosition().getId()).get();
				tyreMaster.setTyrePosition(tyrePos);
				TyreMaster tempMaster = tyreMstRepo.save(tyreMaster);
				busTyreAssociation.setTyre(tempMaster);
				busTyreAssociation.setTyrePosition(tyrePos);
				BusMaster busMaster = busRepo.findById(parentDto.getBusMaster().getId()).get();
				busTyreAssociation.setBus(busMaster);
				busTyreAssociation.setTyreCondition(tyreCondition);
				busTyreAssociation.setInstallDate(purchaseDate);
				btaRepo.save(busTyreAssociation);
			}
			return new ResponseEntity<>(
					new ResponseStatus("Tyre master has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<ResponseStatus> saveOldTyreUnfitted(TyreMaster tyreMaster) {
		Map<String,Integer> tempMap = new HashMap<>();
		Integer tyreConditionId = null;
		log.info("Entering into saveOldTyreUnfitted service");
		DepotMaster depot = null;
		try {
			depot = new DepotMaster(); 
			depot.setId(tyreMaster.getSourceOfOriginTyre().getId());
			tyreMaster.setSourceOfOriginTyre(depot);
			if(tyreMaster.getBusHistory()!=null && tyreMaster.getBusHistory().size()>0) {
				for(BusTyreAssociationHistory btah:tyreMaster.getBusHistory()) {
					tempMap.put(btah.getTyreCondition().getName(),btah.getTyreCondition().getId());
					//btahRepo.save(btah);
					
				}
			}
			if(tempMap.containsKey("Condemn")){
				tyreConditionId = tempMap.get("Condemn");
			}
			else if(tempMap.containsKey("R3")){
				tyreConditionId = tempMap.get("R3");
			}else if(tempMap.containsKey("R2")){
				tyreConditionId = tempMap.get("R2");
			}else if(tempMap.containsKey("R1")){
				tyreConditionId = tempMap.get("R1");
			}
			
			tyreMaster.setOldTyre(true);
			
			TyreCondition tyreCondition = tyreConditionRepo.findById(tyreConditionId).get();			
			tyreMaster.setTyreCondition(tyreCondition);
			
			DepotMaster tempDepot = depotRepo.findByDepotCode(tyreMaster.getDepot().getDepotCode());
			//String tyreTagAuto = generateTyreTag(tempDepot.getDepotName());
			String tyreTagAuto = fetchTyreTag(tempDepot.getDepotName(),tyreConditionId); 
			tyreMaster.setTyreTag(tyreTagAuto);	
			tyreMaster.setDepot(tempDepot);
			tyreMaster.setAvailable(true);
			TyreMaster savedTyre = tyreMstRepo.save(tyreMaster);
			if(tyreMaster.getBusHistory()!=null && tyreMaster.getBusHistory().size()>0) {
				for(BusTyreAssociationHistory btah:tyreMaster.getBusHistory()) {
					btah.setBusFitted(savedTyre.isBusFitted());
					btah.setTyre(savedTyre);
					btahRepo.save(btah);
					
				}
			}
			return new ResponseEntity<>(
					new ResponseStatus("Tyre master has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}
	
	@Override
	public List<MarkSpareBusDetailsDto> getAllBusDetails(Integer transportId) {
		
		List<MarkSpareBusDetailsDto> markcodemn= new ArrayList<>();
		//List<BusMaster> busDetails = busRepo.findAllByTransportId(transportId);
		List<Object[]> busDetails =  busRepo.getAllBusesByTransportId(transportId);
		for(Object[] obj : busDetails)
		{
			MarkSpareBusDetailsDto markCodemn = new MarkSpareBusDetailsDto();
			if(obj[0] != null)
			markCodemn.setBusId(Integer.parseInt(obj[0].toString()));
			if(obj[1] !=null)
			markCodemn.setBusregisterNumbers(obj[1].toString());
			markcodemn.add(markCodemn);
			
		}
		
		return markcodemn;
	}
	
	private String fetchTyreTag(String depot, Integer tyreConditionId) {
		StringBuffer tyreTag = new StringBuffer();
		TyreMasterDto t = new TyreMasterDto();
		String[] depotArray = null;
		String depotCode = "";

		if (depot.contains("-")) {
			depotArray = depot.split("-");
			depotCode = depotArray[1].substring(0, 2);
		} else {
			depotCode = depot.substring(0, 3);
		}
		Integer tyreId = tyreMstRepo.getCounterTyreId();
		if(tyreId != null){
			if(tyreId.toString().length() == 1)
				tyreTag.append("000");
			else if(tyreId.toString().length() == 2)
				tyreTag.append("00");
			else if(tyreId.toString().length() == 3)
				tyreTag.append("0");
			tyreId = tyreId + 1 ;
			tyreTag.append(tyreId.toString());
		}
		else
			tyreTag.append("0001");
		
		Calendar cal = Calendar.getInstance();
		String sDate = (new SimpleDateFormat("MMM").format(cal.getTime())).toUpperCase();

		Date date = new Date();
		SimpleDateFormat DateFor = new SimpleDateFormat("yyyy");
		String format = DateFor.format(date);

		//if (tyreConditionId == 1) {
			t.setUseTyreID(depotCode + sDate + format + tyreTag);
		/*} else if (tyreConditionId == 2) {
			t.setUseTyreID(depotCode + sDate + format + tyreTag );
		} else if (tyreConditionId == 3) {
			t.setUseTyreID(depotCode + sDate + format + tyreTag );
		} else if (tyreConditionId == 4) {
			t.setUseTyreID(depotCode + sDate + format + tyreTag );
		} else if (tyreConditionId == 5) {
			t.setUseTyreID(depotCode + sDate + format + tyreTag );
		}*/

		return t.getUseTyreID();
	}
	
	@Override
	public List<OldTyreMasterDto> getAllOldTyreListByDepot(String dpCode) {
		List<OldTyreMasterDto> tyreList = new ArrayList<>();;
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> list = tyreMstRepo.findAllOldTyreByDepot(depotMaster.getId());
			for(Object[] o : list) {
				OldTyreMasterDto tyre = new OldTyreMasterDto();
				
				if(o[0]!=null)					
				tyre.setId(Integer.parseInt(o[0].toString()));
				
				TransportDto transport = new TransportDto();
				if(o[1]!=null)
				transport.setTransportName(o[1].toString());
				tyre.setTransportUnit(transport);
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[2]!=null)
				size.setSize(o[2].toString());
				tyre.setTyreSize(size);
				
				TyreMakerDto maker = new TyreMakerDto();
				if(o[3]!=null)
				maker.setName(o[3].toString());
				tyre.setTyreMake(maker);
				
				TyreTypeDto type = new TyreTypeDto();
				if(o[4]!=null)
				type.setName(o[4].toString());				
				tyre.setTyreType(type);
						
				if(o[5]!=null)
				tyre.setTyreNumber(o[5].toString());
							
				if(o[6]!=null)
				tyre.setInvoiceNumber(o[6].toString());
					
				if(o[7]!=null)
				tyre.setInvoiceDate(o[7].toString());
				
				if(o[8]!=null)
				tyre.setTyreCost(Double.parseDouble(o[8].toString()));
				
				if(o[9]!=null)
				tyre.setTyrePurchaseDate(o[9].toString());
								
				if(o[10]!=null)
				tyre.setExpectedLife(o[10].toString());
				
				if(o[11]!=null)
				tyre.setTyreTag(o[11].toString());		
				
				if(o[12]!=null)
				tyre.setSourceOfOriginTyre(o[12].toString());
				
				if(o[13]!=null)
				tyre.setKmsRunTillDate(Integer.parseInt(o[13].toString()));
				
				if(o[14]!=null)
					tyre.setOldTyre(Boolean.parseBoolean(o[14].toString()));
				
				if(o[15]!=null)
					tyre.setReasonName(o[15].toString());
				
				TyrePositionDto tyrePosition = new TyrePositionDto();
				if(o[16]!=null)
					tyrePosition.setName(o[16].toString());
				tyre.setTyrePosition(tyrePosition);
				
				tyreList.add(tyre);
			}
			
			} 
		catch(Exception e) {
			e.printStackTrace();
		}
			
		
		return tyreList;
	}

	@Override
	public BusMasterDto getPurchaseDateByBus(Integer busId) {
		BusMasterDto busMasterDto = new BusMasterDto();
		try{
		BusMaster busMaster = busRepo.findById(busId).get();
		busMasterDto.setChessisPurDate(busMaster.getChessisPurDate());
		List<Object[]> tyrePositionList = tyrePosRepo.getRemaingPositionList(busId, busMaster.getTyreCount().getId());
		List<TyrePositionDto> tyrePositionDtoList = new ArrayList<>();
		for(Object[] o: tyrePositionList) {
			TyrePositionDto tyrePosDto = new TyrePositionDto(Integer.parseInt(o[0].toString()), o[1].toString());
			tyrePositionDtoList.add(tyrePosDto);
		}
		busMasterDto.setTyrePositions(tyrePositionDtoList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return busMasterDto;
	}

	@Override
	public List<TyreMasterDto> getTyreListBySizeMakeAndType(Integer sizeId, Integer makeId, Integer typeId,Integer tyrePositionId) {
		log.info("Entering into saveOldTyreUnfitted service");
		TyreMasterDto tyreDto = null;
		List<TyreMasterDto> tyreDtoList = new ArrayList<>();
		try{
			List<Object[]> tyreList = tyreMstRepo.getTyreListBySizeMakeAndType(sizeId,makeId,typeId);
			TyrePosition positionObj = tyrePosRepo.findById(tyrePositionId).get();
			for(Object[] objArray : tyreList){
				tyreDto = new TyreMasterDto();
				if(positionObj.getName().contains("Front") && objArray[4].toString().equals("New")){
				tyreDto.setId(Integer.parseInt(objArray[0].toString()));
				if(objArray[1] != null)
				tyreDto.setTyreNumber(objArray[1].toString());
				if(objArray[2] != null)
				tyreDto.setTyreTag(tyreManagementUtility.concatConditionWithTag(tyreDto.getId()));
				if(objArray[3] != null)
				tyreDto.setKmsDone(Double.parseDouble(objArray[3].toString()));
				if(objArray[4] != null)
					tyreDto.setConditionName(objArray[4].toString());
				Double recoveredCost = tyreCostCalculationUtility.calculateTyreRecoveredCostByCondition(tyreDto.getId());
				tyreDto.setRecoveredCost(recoveredCost);
				Float kmsDoneByCondition = tyreCostCalculationUtility.calculateTyreKmsByCondition(tyreDto.getId());
				tyreDto.setKmsDoneOnThisBus(0);
				if(kmsDoneByCondition != null)
				tyreDto.setKmsDoneByCondition(kmsDoneByCondition.toString());
				tyreDto.setTotalKmsDone(0);
				tyreDto.setKilometerDoneOnBus("0.0");
				Float totalKmsDone = tyreCostCalculationUtility.calculateTyreTotalKms(tyreDto.getId());
				if(totalKmsDone != null)
					tyreDto.setTotalKilometerDone(totalKmsDone.toString());
				TotalPlainHillKmsCalculationDto dtoObj = tyreCostCalculationUtility.calculateTyreKmsByConditionForPlainAndHillKms(tyreDto.getId());
				StringBuffer sb = new StringBuffer();
				if(dtoObj.getTotKms() != null)
				sb.append(dtoObj.getTotKms());
				else
					sb.append("0");	
				sb.append("(");
				if(dtoObj.getPlainKms() != null)
				sb.append(dtoObj.getPlainKms());
				else
					sb.append("0");
				sb.append(" ");
				sb.append("+");
				sb.append(" ");
				if(dtoObj.getHillKms() != null)
				sb.append(dtoObj.getHillKms());
				else
					sb.append("0");	
				sb.append(")");
				tyreDto.setAllKmsinCondition(sb.toString());
				tyreDtoList.add(tyreDto);
				}else if(!positionObj.getName().contains("Front") && (!(objArray[4].toString().equals("New")))){
					tyreDto.setId(Integer.parseInt(objArray[0].toString()));
					if(objArray[1] != null)
					tyreDto.setTyreNumber(objArray[1].toString());
					if(objArray[2] != null)
					tyreDto.setTyreTag(tyreManagementUtility.concatConditionWithTag(tyreDto.getId()));
					if(objArray[3] != null)
					tyreDto.setKmsDone(Double.parseDouble(objArray[3].toString()));
					if(objArray[4] != null)
						tyreDto.setConditionName(objArray[4].toString());
					Double recoveredCost = tyreCostCalculationUtility.calculateTyreRecoveredCostByCondition(tyreDto.getId());
					tyreDto.setRecoveredCost(recoveredCost);
					Float kmsDoneByCondition = tyreCostCalculationUtility.calculateTyreKmsByCondition(tyreDto.getId());
					tyreDto.setKmsDoneOnThisBus(0);
					if(kmsDoneByCondition != null)
					tyreDto.setKmsDoneByCondition(kmsDoneByCondition.toString());
					tyreDto.setTotalKmsDone(0);
					Float totalKmsDone = tyreCostCalculationUtility.calculateTyreTotalKms(tyreDto.getId());
					if(totalKmsDone != null)
						tyreDto.setTotalKilometerDone(totalKmsDone.toString());
					TotalPlainHillKmsCalculationDto dtoObj = tyreCostCalculationUtility.calculateTyreKmsByConditionForPlainAndHillKms(tyreDto.getId());
					StringBuffer sb = new StringBuffer();
					if(dtoObj.getTotKms() != null)
					sb.append(dtoObj.getTotKms());
					else
						sb.append("0");	
					sb.append("(");
					if(dtoObj.getPlainKms() != null)
					sb.append(dtoObj.getPlainKms());
					else
						sb.append("0");
					sb.append(" ");
					sb.append("+");
					sb.append(" ");
					if(dtoObj.getHillKms() != null)
					sb.append(dtoObj.getHillKms());
					else
						sb.append("0");	
					sb.append(")");
					tyreDto.setAllKmsinCondition(sb.toString());
					tyreDtoList.add(tyreDto);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tyreDtoList;
	}
	


@Override
	public OldTyreMasterDto getOldTyreByTyreId(Integer tyreId) {
		OldTyreMasterDto tyre = new OldTyreMasterDto();
		try {
			List<Object[]> list = tyreMstRepo.getOldTyreByTyreId(tyreId);
			for(Object[] o : list) {				
				
				if(o[0]!=null)					
				tyre.setId(Integer.parseInt(o[0].toString()));
				
				TransportDto transport = new TransportDto();
				if(o[1]!=null)
				transport.setTransportName(o[1].toString());
				tyre.setTransportUnit(transport);
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[2]!=null)
				size.setSize(o[2].toString());
				tyre.setTyreSize(size);
				
				TyreMakerDto maker = new TyreMakerDto();
				if(o[3]!=null)
				maker.setName(o[3].toString());
				tyre.setTyreMake(maker);
				
				TyreTypeDto type = new TyreTypeDto();
				if(o[4]!=null)
				type.setName(o[4].toString());				
				tyre.setTyreType(type);
						
				if(o[5]!=null)
				tyre.setTyreNumber(o[5].toString());
							
				if(o[6]!=null)
				tyre.setInvoiceNumber(o[6].toString());
					
				if(o[7]!=null)
				tyre.setInvoiceDate(o[7].toString());
				
				if(o[8]!=null)
				tyre.setTyreCost(Double.parseDouble(o[8].toString()));
				
				if(o[9]!=null)
				tyre.setTyrePurchaseDate(o[9].toString());
								
				if(o[10]!=null)
				tyre.setExpectedLife(o[10].toString());
				
				if(o[11]!=null)
				tyre.setTyreTag(o[11].toString());		
				
				if(o[12]!=null)
				tyre.setSourceOfOriginTyre(o[12].toString());
				
				if(o[13]!=null)
				tyre.setKmsRunTillDate(Integer.parseInt(o[13].toString()));
				
				if(o[14]!=null)
					tyre.setBusRegNumber(o[14].toString());
				
				if(o[15]!=null)
					tyre.setConditionName(o[15].toString());
				
				if(o[16]!=null)
					tyre.setTakenOffDate(o[16].toString());
				
				if(o[17]!=null)
					tyre.setInstallDate(o[17].toString());
				
				if(o[18]!=null)
					tyre.setKmsTakenOff(o[18].toString());
				
				if(o[19]!=null)
					tyre.setKmsInstalled(o[19].toString());
				
				if(o[20]!=null)
					tyre.setTotalMileageTakenOff(o[20].toString());
				
				if(o[21]!=null)
					tyre.setReasonName(o[21].toString());
				
				if(o[22]!=null)
					tyre.setOldTyre(Boolean.parseBoolean(o[22].toString()));
				
				TyrePositionDto tyrePosition = new TyrePositionDto();
				if(o[23]!=null)
					tyrePosition.setName(o[23].toString());
				tyre.setTyrePosition(tyrePosition);
			}
			
			} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return tyre;
	}

@Override
public ResponseEntity<ResponseStatus> validateDuplicateByTyreNo(String tyreNo) {
	try{
		List<TyreMaster> tyreList = tyreMstRepo.validateByTyreNo(tyreNo);
		if (tyreList != null && tyreList.size() > 0) {

			return new ResponseEntity<>(
					new ResponseStatus("Tyre number already exists, please enter other tyre no.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);

		} else {
			return new ResponseEntity<>(new ResponseStatus("", HttpStatus.OK), HttpStatus.OK);
		}

	}catch(Exception e){
		e.printStackTrace();
	}
	return null;
}
	
	}
