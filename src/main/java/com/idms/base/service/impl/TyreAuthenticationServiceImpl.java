package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.PenaltyTypeDto;
import com.idms.base.api.v1.model.dto.RouteMasterDto;
import com.idms.base.api.v1.model.dto.TyreAuthenticationDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyrePositionDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.TakenOffReason;
import com.idms.base.dao.entity.TyreAuthenticationEntity;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MasterStatusRepository;
import com.idms.base.dao.repository.PenaltyTypeRepository;
import com.idms.base.dao.repository.TakenOffReasonRepository;
import com.idms.base.dao.repository.TyreAuthenticationRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.dao.repository.TyrePositionRepository;
import com.idms.base.service.TyreAuthenticationService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.TyreCostCalculationUtility;
import com.idms.base.util.TyreManagementUtility;

@Service
public class TyreAuthenticationServiceImpl implements TyreAuthenticationService {

	@Autowired
	TyreAuthenticationRepository tyreAuth;
	@Autowired
	MasterStatusRepository masterStatusRepository;
	@Autowired
	DepotMasterRepository depo;
	
	@Autowired
	TyreCostCalculationUtility tyreCostCalculationUtility;

	@Autowired
	TyreMasterRepository tyreMasterRepo;
	
	@Autowired
	TakenOffReasonRepository takenOffReasonId;
	
	@Autowired
	TyrePositionRepository tyrePosRepo;
	
	@Autowired
	TyreManagementUtility tyreManagementUtility;
	
	@Autowired
	PenaltyTypeRepository penaltyTypeRepository;
	
	@Override
	public ResponseEntity<ResponseStatus> saveTyreAuthentication(String depoCode,TyreAuthenticationEntity tyreAuthenticationEntity) {
		try {

			DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
			Optional<DepotMaster> findById = depo.findById(findByDepotCode.getId());			
			
			//update the reason code of RS in mst_tyre
			java.util.Optional<TyreMaster> tyremaster = tyreMasterRepo.findById(tyreAuthenticationEntity.getTyre().getId());
			TakenOffReason reasonRS = takenOffReasonId.findById(tyreAuthenticationEntity.getTakenOffReason().getId()).get();
			TakenOffReason reason =null;
			if(reasonRS.getReasonCode().equals("GMAR")){
				 reason = takenOffReasonId.findByCode("RS");
				tyremaster.get().setTakenOffReason(reason);
			}else if(reasonRS.getReasonCode().equals("GMAC")){
				 reason = takenOffReasonId.findByCode("CN");
				tyremaster.get().setTakenOffReason(reason);
			}
			tyreMasterRepo.save(tyremaster.get());
			tyreAuthenticationEntity.setDepot(findById.get());
            if(tyreAuthenticationEntity.getDriver() != null && tyreAuthenticationEntity.getDriver().getId() !=null){
				
			}else{
				tyreAuthenticationEntity.setDriver(null);
			}
            if(tyreAuthenticationEntity.getConductor() != null && tyreAuthenticationEntity.getConductor().getId() !=null){
				
			}else{
				tyreAuthenticationEntity.setConductor(null);
			}
			tyreAuth.save(tyreAuthenticationEntity);

				return new ResponseEntity<>(
						new ResponseStatus("Tyre Authentication Saved Successfully.", HttpStatus.OK), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	

	@Override
	@Transactional
	public List<TyreAuthenticationDto> getTyreAuthenticationDetails(String depoCode) {
		
		List<TyreAuthenticationDto> tyre = new ArrayList<TyreAuthenticationDto>();
		try
		{
	DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
			
			TyreMasterDto tyremaster=null;
			TyreConditionDto tyreCondition=null;
			TyreMakerDto tyremaker=null;
			TyreSizeDto tyreSize=null;
			TyrePositionDto tyrePosition=null;
			List<Object[]> tyreAuthenticationdetails = tyreAuth.findByTyreAuthenticationdetails(findByDepotCode.getId());
			for(Object[]ob:tyreAuthenticationdetails)
			{
				tyremaster= new TyreMasterDto();
				tyreCondition= new TyreConditionDto();
				tyremaker= new TyreMakerDto();
				tyreSize =new TyreSizeDto();
				tyrePosition= new TyrePositionDto();
				TyreAuthenticationDto tyreAuth= new TyreAuthenticationDto();
				
				if(ob[0]!=null)
					tyremaster.setTyreTag(tyreManagementUtility.concatConditionWithTag(Integer.parseInt(ob[11].toString())));
				tyreAuth.setTyre(tyremaster);
				
				if(ob[1]!=null)
					tyremaster.setTyreNumber(ob[1].toString());	 
				tyreAuth.setTyre(tyremaster);
				
				if(ob[2]!=null)
					tyremaker.setName(ob[2].toString());
				if(ob[15]!=null)
					tyremaker.setId(Integer.parseInt(ob[15].toString()));
				tyreAuth.setTyremake(tyremaker);
				
				if(ob[3]!=null)
					tyreSize.setSize(ob[3].toString());
				if(ob[16]!=null)
					tyreSize.setId(Integer.parseInt(ob[16].toString()));
				tyreAuth.setTyreSize(tyreSize);
				
				if(ob[4]!=null)
					tyreCondition.setName(ob[4].toString());
				if(ob[14]!=null)
					tyreCondition.setId(Integer.parseInt(ob[14].toString()));
				tyreAuth.setTyreCondition(tyreCondition);;
				
				if(ob[5]!=null)
					tyreAuth.setTyreRecoveredCost(Double.parseDouble(ob[5].toString()));
				
				if(ob[6]!=null)					
				tyreAuth.setTyreInstallationDate(ob[6].toString());
				
				if(ob[7]!=null)
					tyreAuth.setKmsDoneBus(Float.parseFloat(ob[7].toString()));
				
				if(ob[8]!=null)
				tyreAuth.setKmsInCondition(Float.parseFloat(ob[8].toString()));
				
				if(ob[9]!=null)
				tyreAuth.setTotalKmsDone(Float.parseFloat(ob[9].toString()));
				
				if(ob[10]!=null)
					tyrePosition.setName(ob[10].toString());
				if(ob[13]!=null)
					tyrePosition.setId(Integer.parseInt(ob[13].toString()));
				tyreAuth.setTyrePosition(tyrePosition);
				
				tyre.add(tyreAuth);
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tyre;
	}



	@Override
	public List<TyreAuthenticationDto> fetchTyreAuthenticationDetails(String depoCode) {
		
		List<TyreAuthenticationDto> tyreAuthDto = new ArrayList<TyreAuthenticationDto>();
		try
		{
			BusMasterDto busmaster=null;
			TyreMasterDto tyremaster=null;
			TyreConditionDto tyreCondition=null;
			TyreMakerDto tyremaker=null;
			TyreSizeDto tyreSize=null;
			TyrePositionDto tyrePosition=null;
			RouteMasterDto routeMaster=null;
			DriverMasterDto driverMaster=null;
			ConductorMasterDto conductorMaster=null;
			
			DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
			List<Object[]> tyreauthenticationdetails = tyreAuth.findbydepoId(findByDepotCode.getId());
			TyreAuthenticationDto  tyreAuth= null;
			
			for(Object[]ob:tyreauthenticationdetails)
			{
				busmaster= new BusMasterDto();
				tyremaster= new TyreMasterDto();
				tyreCondition= new TyreConditionDto();
				tyremaker= new TyreMakerDto();
				tyreSize =new TyreSizeDto();
				tyrePosition=new TyrePositionDto();
				driverMaster=new DriverMasterDto();
				routeMaster= new RouteMasterDto();
				conductorMaster= new ConductorMasterDto();
				 tyreAuth= new TyreAuthenticationDto();

				if(ob[0]!=null)
					tyremaster.setId(Integer.parseInt(ob[0].toString()));
				
				if(ob[1]!=null)
					tyremaster.setTyreTag(tyreManagementUtility.concatConditionWithTag(Integer.parseInt(ob[0].toString())));
				tyreAuth.setTyre(tyremaster);
				
				if(ob[2]!=null)
					tyremaster.setTyreNumber(ob[2].toString());	 
				tyreAuth.setTyre(tyremaster);
				
				if(ob[3]!=null)
					tyremaker.setName(ob[3].toString());
				if(ob[24]!=null)
					tyremaker.setId(Integer.parseInt(ob[24].toString()));
				tyreAuth.setTyremake(tyremaker);
				
				if(ob[4]!=null)
					tyreSize.setSize(ob[4].toString());
				if(ob[25]!=null)
					tyreSize.setId(Integer.parseInt(ob[25].toString()));
				tyreAuth.setTyreSize(tyreSize);
				
				if(ob[5]!=null)
					tyreCondition.setName(ob[5].toString());
				if(ob[23]!=null)
					tyreCondition.setId(Integer.parseInt(ob[23].toString()));
				tyreAuth.setTyreCondition(tyreCondition);;
				
				
				if(ob[6]!=null)
					tyrePosition.setName(ob[6].toString());
				if(ob[22]!=null)
					tyrePosition.setId(Integer.parseInt(ob[22].toString()));
					tyreAuth.setTyrePosition(tyrePosition);
				
				if(ob[7]!=null)
					busmaster.setBusRegNumber(ob[7].toString());
					tyreAuth.setBusMaster(busmaster);
				
				if(ob[8]!=null)		
				tyreAuth.setTransportName(ob[8].toString());
				
				if(ob[9]!=null)
					tyreAuth.setTyreInstallationDate(ob[9].toString());
				
				if(ob[10]!=null)
				tyreAuth.setRemarks(ob[10].toString());
				
				if(ob[11]!=null)
				tyreAuth.setTakenoffby(ob[11].toString());
				
				if(ob[12]!=null)
					tyreAuth.setBusId(Integer.parseInt(ob[12].toString()));
				
				Double recoveredCost = tyreCostCalculationUtility.calculateTyreRecoveredCostByCondition(Integer.parseInt(ob[0].toString()));
				tyreAuth.setTyreRecoveredCost(recoveredCost);
				
				Float kmsDoneByCondition = tyreCostCalculationUtility.calculateTyreKmsByCondition(Integer.parseInt(ob[0].toString()));
				if(kmsDoneByCondition != null)					
				tyreAuth.setKmsInCondition(kmsDoneByCondition);
				
				Float totalKmsDone = tyreCostCalculationUtility.calculateTyreTotalKms(Integer.parseInt(ob[0].toString()));
				
				if(totalKmsDone != null)
				tyreAuth.setTotalKmsDone(totalKmsDone);
				
				Float kmsDoneOnCurrent = tyreCostCalculationUtility.calculateTotalKmsInCurretBus(Integer.parseInt(ob[0].toString()),Integer.parseInt(ob[12].toString()));
				
				if(kmsDoneOnCurrent != null)
					tyreAuth.setKmsDoneBus(kmsDoneOnCurrent);
								
				if(ob[13]!=null)
					tyreAuth.setReasonName(ob[13].toString());
				
				if(ob[14]!=null)
					tyreAuth.setReasonCode(ob[14].toString());
				
				if(ob[15]!=null)
					routeMaster.setRouteName(ob[15].toString());
				tyreAuth.setRoute(routeMaster);
				
				if(ob[16]!=null)
					routeMaster.setRouteId(ob[16].toString());
				tyreAuth.setRoute(routeMaster);
				
				if(ob[17]!=null)
					driverMaster.setDriverName(ob[17].toString());
				tyreAuth.setDriver(driverMaster);
				
				if(ob[18]!=null)
					driverMaster.setId(Integer.parseInt(ob[18].toString()));
				tyreAuth.setDriver(driverMaster);
				
				if(ob[19]!=null)
					conductorMaster.setId(Integer.parseInt(ob[19].toString()));
				tyreAuth.setConductor(conductorMaster);
				
				if(ob[20]!=null)
					conductorMaster.setConductorName(ob[20].toString());
				tyreAuth.setConductor(conductorMaster);
				
				if(ob[21]!=null)
				tyreAuth.setReasonId(Integer.parseInt(ob[21].toString()));
				
				tyreAuthDto.add(tyreAuth);
				
			}
			
			List<PenaltyTypeDto> penaltyType = penaltyTypeRepository.findAllPenaltyType().stream().map(
					penaltyTypeDto -> new PenaltyTypeDto(penaltyTypeDto.getId(), penaltyTypeDto.getName()))
					.collect(Collectors.toList());

			if (penaltyType != null && penaltyType.size() > 0)
				tyreAuth.setPenaltyTypeList(penaltyType);
				
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tyreAuthDto;
	}

}
