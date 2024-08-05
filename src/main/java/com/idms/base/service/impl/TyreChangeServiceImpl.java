package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusTyreAssociationDto;
import com.idms.base.api.v1.model.dto.TakenOffReasonDto;
import com.idms.base.api.v1.model.dto.TotalPlainHillKmsCalculationDto;
import com.idms.base.api.v1.model.dto.TyreChangeActionDto;
import com.idms.base.api.v1.model.dto.TyreChangeFormDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyrePositionDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.api.v1.model.dto.TyreTypeDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusTyreAssociation;
import com.idms.base.dao.entity.BusTyreAssociationHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.dao.entity.TakenOffReason;
import com.idms.base.dao.entity.TyreChangeAction;
import com.idms.base.dao.entity.TyreCondition;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusRefuelingMasterRepository;
import com.idms.base.dao.repository.BusTyreAssociationHistoryRepository;
import com.idms.base.dao.repository.BusTyreAssociationRepository;
import com.idms.base.dao.repository.BusTyreTypeSizeMappingRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DocketTyreAssociationRepository;
import com.idms.base.dao.repository.TakenOffReasonRepository;
import com.idms.base.dao.repository.TyreChangeActionRepository;
import com.idms.base.dao.repository.TyreConditionRepository;
import com.idms.base.dao.repository.TyreMakersRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.dao.repository.TyrePositionRepository;
import com.idms.base.dao.repository.TyreSizeRepository;
import com.idms.base.dao.repository.TyreTypeRepository;
import com.idms.base.service.TyreChangeService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.TyreCostCalculationUtility;
import com.idms.base.util.TyreManagementUtility;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TyreChangeServiceImpl implements TyreChangeService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	BusMasterRepository busMasterRepo;
	
	@Autowired
	TyreChangeActionRepository tyreChangeActRepo;
	
	@Autowired
	TakenOffReasonRepository takenOffRepo;
	
	@Autowired
	TyrePositionRepository posRepo;
	
	@Autowired
	BusTyreAssociationRepository btaRepo;
	
	@Autowired
	TyreMasterRepository tyreMstRepo;
	
	@Autowired
	BusTyreTypeSizeMappingRepository bttsRepo;
	
	@Autowired
	BusTyreAssociationHistoryRepository btaHistoryRepo;
	
	@Autowired
	TyreConditionRepository tcRepo;
	
	@Autowired
	BusRefuelingMasterRepository busRefuelingMasterRepository;
	
	@Autowired
	TyreCostCalculationUtility tyreCostCalculationUtility;
	
	@Autowired
	TyreTypeRepository tyreTypeRepo;
	
	@Autowired
	TyreSizeRepository tyreSizeRepo;
	
	@Autowired
	TyreMakersRepository tyreMakerRepo;
	
	@Autowired
	DocketTyreAssociationRepository docketRepository;
	
	@Autowired
	TyreManagementUtility tyreManagementUtility;
	

	@Override
	public List<BusMaster> getAllBusesForTyreChange(String dpCode) {
		List<BusMaster> list = new ArrayList<BusMaster>();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> objList = busMasterRepo.getAllBusesForTyreChange(depotMaster.getId());
			for(Object[] o : objList) {
				BusMaster bus = new BusMaster();
				bus.setId(Integer.parseInt(o[0].toString()));
				bus.setBusRegNumber(o[1].toString());
				list.add(bus);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public TyreChangeFormDto getTyreChangeForm(Integer busId) {
		TyreChangeFormDto tyreChangeForm = new TyreChangeFormDto();
		StringBuffer sb = null;
		try {
			
			BusMaster bus = busMasterRepo.findById(busId).get();
			
			List<TyreChangeActionDto> actions = tyreChangeActRepo.findAll().stream()
					.map(act -> this.mapper.map(act, TyreChangeActionDto.class)).collect(Collectors.toList());
			
			tyreChangeForm.setTyreChangeActions(actions);
			
			List<TakenOffReasonDto> reasons = takenOffRepo.findAllWithParentNull().stream()
					.map(reason -> this.mapper.map(reason, TakenOffReasonDto.class)).collect(Collectors.toList());
			tyreChangeForm.setTakenOffReasons(reasons);
			
			List<TyrePositionDto> tps = posRepo.findAllByTyreCount(bus.getTyreCount().getId()).stream()
					.map(pos -> new TyrePositionDto(pos.getId(),pos.getName())).collect(Collectors.toList());
			tyreChangeForm.setPositions(tps);
			
			
			List<TyreTypeDto> tyreTypeList = tyreTypeRepo.findAllByStatus(true).stream()
					.map(tyreType -> new TyreTypeDto(tyreType.getId(),tyreType.getName())).collect(Collectors.toList());
			
			if(tyreTypeList!=null && tyreTypeList.size()>0) {
				tyreChangeForm.setTypeList(tyreTypeList);
			}
			
			List<TyreSizeDto> tyreSizeList = tyreSizeRepo.findAllByStatus(true).stream()
					.map(tyreSize -> new TyreSizeDto(tyreSize.getId(),tyreSize.getSize())).collect(Collectors.toList());
			
			if(tyreSizeList!=null && tyreSizeList.size()>0) {
				tyreChangeForm.setSizeList(tyreSizeList);
			}
			
			List<TyreMakerDto> tyreMakerList = tyreMakerRepo.findAllByStatus(true).stream()
					.map(tyreMaker -> new TyreMakerDto(tyreMaker.getId(),tyreMaker.getName())).collect(Collectors.toList());
			if(tyreMakerList!=null && tyreMakerList.size()>0) {
				tyreChangeForm.setMakeList(tyreMakerList);
			}
			
			List<BusTyreAssociationDto> listBTA = new ArrayList<>();
			List<Object[]> list = busMasterRepo.getBusTyreAssocitionForTyreChange(bus.getId());
			for(Object[] o : list) {
				BusTyreAssociationDto bta = new BusTyreAssociationDto();
				bta.setId(Integer.parseInt(o[0].toString()));
				TyreMasterDto tyre = new TyreMasterDto();
				if(o[1]!=null) {
					tyre.setTyreNumber(o[1].toString());
				} else {
					tyre.setTyreNumber("");
				}
				if(o[12] != null){
					Integer[] busesIds = btaRepo.findAllBusesByTyreId(Integer.parseInt(o[12].toString()));
					if(busesIds.length > 0){
						Integer currentMileage = busRefuelingMasterRepository.findMileageBusDetails(busesIds);
						bta.setCurrentMileage(currentMileage);
					}else{
						bta.setCurrentMileage(0);
					}
						}
				if(o[12]!=null) {
					tyre.setId(Integer.parseInt(o[12].toString()));
				}
				TotalPlainHillKmsCalculationDto dtoObj = tyreCostCalculationUtility.calculateTyreKmsByConditionForPlainAndHillKms(tyre.getId());
				sb = new StringBuffer();
				if(dtoObj.getTotKms() != null)
				sb.append(dtoObj.getTotKms());
				else
					sb.append("0");
				sb.append(" ");
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
				bta.setAllKmsinCondition(sb.toString());
				TyreMakerDto maker = new TyreMakerDto();
				if(o[2]!=null) {
					maker.setName(o[2].toString());
				} else {
					maker.setName("");
				}
				if(o[8]!=null) {
					maker.setId(Integer.parseInt(o[8].toString()));
				} else {
					maker.setId(null);
				}
				tyre.setTyreMake(maker);
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[3]!=null) {
					size.setSize(o[3].toString());
				} else {
					size.setSize("");
				}
				if(o[9]!=null) {
					size.setId(Integer.parseInt(o[9].toString()));
				} else {
					size.setId(null);
				}
				
				tyre.setTyreSize(size);
				bta.setTyre(tyre);
				if(o[4]!=null)
					bta.setKmsDone(Double.parseDouble(o[4].toString()));
				else
					bta.setKmsDone(0.0);

				TyreConditionDto condition = new TyreConditionDto();
				if(o[5]!=null) {
					condition.setName(o[5].toString());
				} else {
					condition.setName("");
				}
				if(o[10]!=null) {
					condition.setId(Integer.parseInt(o[10].toString()));
				} else {
					condition.setId(null);
				}
				bta.setTyreCondition(condition);
				
				if(o[6]!=null) {
					bta.setInstallDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o[6].toString()));
				} else {
					bta.setInstallDate(null);
				}
				
				TyrePositionDto position = new TyrePositionDto();
				if(o[7]!=null) {
					position.setName(o[7].toString());
				} else {
					position.setName("");
				}
				if(o[11]!=null) {
					position.setId(Integer.parseInt(o[11].toString()));
				} else {
					position.setId(null);
				}
				if(o[15]!=null) {
					bta.setTyreTag(tyreManagementUtility.concatConditionWithTag(tyre.getId()));
				}
				
				Double recoveredCost = tyreCostCalculationUtility.calculateTyreRecoveredCostByCondition(tyre.getId());
				bta.setRecoveredCost(recoveredCost);
				Float kmsDoneByCondition = tyreCostCalculationUtility.calculateTyreKmsByCondition(tyre.getId());
				if(kmsDoneByCondition != null)
				bta.setKmsDoneByCondition(kmsDoneByCondition.toString());
				Float totalKmsDone = tyreCostCalculationUtility.calculateTyreTotalKms(tyre.getId());
				if(totalKmsDone != null)
				bta.setTotalKilometerDone(totalKmsDone.toString());
				Float kmsDoneOnCurrent = tyreCostCalculationUtility.calculateTotalKmsInCurretBus(tyre.getId(),bus.getId());
				if(kmsDoneOnCurrent != null)
				bta.setKilometerDoneOnBus(kmsDoneOnCurrent.toString());
				/*bta.setKmsDoneOnThisBus(0);
				bta.setKmsInCondition(0);
				bta.setTotalKmsDone(0);*/
				bta.setTyrePosition(position);
				listBTA.add(bta);
				
				
				if(o[13]!=null) {
					tyre.setTyreCost(Double.parseDouble(o[13].toString()));
				} else {
					tyre.setTyreCost(0);
				}
				
				if(o[14]!=null) {
					tyre.setExpectedLife(o[14].toString());
				}
				
				
			}
			
			
			tyreChangeForm.setAssocitionsList(listBTA);
			
			List<Integer> typeList = bttsRepo.getTyreTypeList(bus.getBusType().getId());
			List<Object[]> tyreList = tyreMstRepo.getTyreListNotAssignedByDepot(bus.getDepot().getId(), typeList);
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
				
				if(o[5]!=null) {
					sizeDto.setId(Integer.parseInt(o[5].toString()));
				} else {
					sizeDto.setId(null);
				}
				
				tyreDto.setTyreSize(sizeDto);
				
				TyreConditionDto condition = new TyreConditionDto();
				if(o[3]!=null) {
					condition.setName(o[3].toString());
				} else {
					condition.setName("");
				}
				
				if(o[6]!=null) {
					condition.setId(Integer.parseInt(o[6].toString()));
				} else {
					condition.setId(null);
				}
				tyreDto.setTyreCondition(condition);
				
				TyreMakerDto maker = new TyreMakerDto();
				if(o[4]!=null) {
					maker.setName(o[4].toString());
				} else {
					maker.setName("");
				}
				
				if(o[7]!=null) {
					maker.setId(Integer.parseInt(o[7].toString()));
				} else {
					maker.setId(null);
				}
				
				tyreDto.setTyreMake(maker);
				
				
				
				tyreDtoList.add(tyreDto);
			}
			
			tyreChangeForm.setTyreList(tyreDtoList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tyreChangeForm;
	}

	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> saveTyreChangeInfo(BusMasterDto bus) {
		log.info("Entering into saveTyreChangeInfo service");
		try {
			for(BusTyreAssociationDto bta : bus.getTyreLists()) {
				BusTyreAssociation btaObj = null;
				btaObj = btaRepo.findById(bta.getId()).get();
				if(btaObj.getBus().getId().equals(bus.getId())) {
					
				} else {
					return new ResponseEntity<>(new ResponseStatus("Wrong tyre association.", HttpStatus.FORBIDDEN),HttpStatus.OK);
				}
			}
			String expectedLife = null;
			Boolean flag = null;
			for(BusTyreAssociationDto bta : bus.getTyreLists()) {
				flag = false;
				TakenOffReason reasonAuthentication = null;
				BusTyreAssociationHistory history = new BusTyreAssociationHistory();
				BusTyreAssociation btaObj = btaRepo.findById(bta.getId()).get();
				TyreChangeAction action = tyreChangeActRepo.findById(bta.getChangeAction().getId()).get();
				if(action.getActionCode().equals("TO")) {
					TakenOffReason reason = takenOffRepo.findById(bta.getTakenOffReason().getId()).get();
					if(reason.getReasonCode().equals("CN") ||  reason.getReasonCode().equals("RS")) {
						try{
						Float kmsDoneByCondition = tyreCostCalculationUtility.calculateTyreKmsByCondition(btaObj.getTyre().getId());
						if (btaObj.getTyre().getTyreCondition() != null && btaObj.getTyre().getTyreCondition().getName().equals("New")) {
							expectedLife = btaObj.getTyre().getExpectedLife();
						} else {
							DocketTyreAssociation docketObj = docketRepository
									.findByConditionIdAndTyreId(btaObj.getTyre().getTyreCondition().getId(), btaObj.getTyre().getId());
							if (docketObj != null && docketObj.getExpectedLife() != null)
								expectedLife = docketObj.getExpectedLife().toString();
						}
						if (expectedLife != null && Float.parseFloat(expectedLife) > kmsDoneByCondition) {
							flag = true;
							if(reason.getReasonCode().equals("CN")){
							 reasonAuthentication = takenOffRepo.findByCode("GMAC");
							}else if(reason.getReasonCode().equals("RS")){
								 reasonAuthentication = takenOffRepo.findByCode("GMAR");
								}
							btaObj.getTyre().setTakenOffReason(reasonAuthentication);
							history.setReason(reasonAuthentication);
						}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					history.setBus(btaObj.getBus());
					history.setTyre(btaObj.getTyre());
					history.setInstallDate(btaObj.getInstallDate());
					history.setTyrePosition(btaObj.getTyrePosition());
					history.setTyreCondition(btaObj.getTyreCondition());
					history.setKmsDone(btaObj.getKmsDone());
					history.setRemovalDate(new Date());
					history.setBusFitted(btaObj.getBusFitted());
					if(!flag)
					history.setReason(this.mapper.map(bta.getTakenOffReason(), TakenOffReason.class));
					history.setRemarks(bta.getRemarks());
					btaHistoryRepo.save(history);
					
					//TakenOffReason reason = takenOffRepo.findById(bta.getTakenOffReason().getId()).get();
					TyreMaster tyre = btaObj.getTyre();
					tyre.setAvailable(false);
					if(!flag)
					tyre.setTakenOffReason(reason);
					tyre.setTyrePosition(null);
					if(reason.getReasonCode().equals("CN")) {
						TyreCondition tc = tcRepo.findByName("Condemn");
						tyre.setTyreCondition(tc);
					}
					tyreMstRepo.save(tyre);
					
				} 
				
				if(action.getActionCode().equals("TR")) {
					BusTyreAssociation oldBtaObj = btaRepo.findById(bta.getOldId()).get();
					history.setBus(oldBtaObj.getBus());
					history.setTyre(oldBtaObj.getTyre());
					history.setInstallDate(oldBtaObj.getInstallDate());
					history.setTyrePosition(oldBtaObj.getTyrePosition());
					history.setTyreCondition(oldBtaObj.getTyreCondition());
					history.setKmsDone(oldBtaObj.getKmsDone());
					history.setRemovalDate(new Date());
					history.setBusFitted(oldBtaObj.getBusFitted());
					history.setRemarks(bta.getRemarks());
					btaHistoryRepo.save(history);
					
					TyreMaster tyre = new TyreMaster();
					tyre.setId(bta.getTyre().getId());
					TyreMaster tyreMaster = tyreMstRepo.findById(tyre.getId()).get();
					btaObj.setTyre(tyre);
					btaObj.setKmsDone(0d);
					btaObj.setInstallDate(new Date());
					btaObj.setTyreCondition(tyreMaster.getTyreCondition());
					btaRepo.save(btaObj);
					tyreMaster.setTyrePosition(btaObj.getTyrePosition());
					tyreMstRepo.save(tyreMaster);
					
				}
				
				if(action.getActionCode().equals("NF")) {
					TyreMaster tyre = new TyreMaster();
					tyre.setId(bta.getTyre().getId());
					TyreMaster tyreMaster = tyreMstRepo.findById(tyre.getId()).get();
					btaObj.setTyre(tyre);
					btaObj.setKmsDone(0d);
					btaObj.setInstallDate(new Date());
					btaObj.setTyreCondition(tyreMaster.getTyreCondition());
					btaRepo.save(btaObj);
					tyreMaster.setTyrePosition(btaObj.getTyrePosition());
					tyreMstRepo.save(tyreMaster);
				}
			}
			
			return new ResponseEntity<>(
					new ResponseStatus("Tyre change data has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		
	}

	

}
