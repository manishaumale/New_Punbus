package com.idms.base.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.DocketTyreAssociationDto;
import com.idms.base.api.v1.model.dto.RetreadingDocketDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.dao.entity.RetreadingDocket;
import com.idms.base.dao.entity.TakenOffReason;
import com.idms.base.dao.entity.TyreCondition;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DocketTyreAssociationRepository;
import com.idms.base.dao.repository.RetreadingDocketRepository;
import com.idms.base.dao.repository.TakenOffReasonRepository;
import com.idms.base.dao.repository.TyreConditionRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.service.TyreRetradeService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.TyreCostCalculationUtility;

@Service
public class TyreRetradeServiceImpl implements TyreRetradeService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TyreMasterRepository tyreRepo;
	
	@Autowired
	private DepotMasterRepository depotRepo;
	
	@Autowired
	private RetreadingDocketRepository rdRepo;
	
	@Autowired
	private DocketTyreAssociationRepository dtaRepo;
	
	@Autowired
	private TakenOffReasonRepository toRepo;
	
	@Autowired
	private TyreConditionRepository conRepo;
	
	@Autowired
	TyreCostCalculationUtility tyreCostCalculationUtility;
	
	@Autowired
	TakenOffReasonRepository takenOffRepo;

	@Override
	public List<TyreMasterDto> getAllTyreForResole(String dpCode) {
		List<TyreMasterDto> tyreMasterList = new ArrayList<TyreMasterDto>();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> objList = tyreRepo.getAllTyreForResole(depotMaster.getId());
			for(Object[] o : objList) {
				TyreMasterDto tyreDto = new TyreMasterDto();
				tyreDto.setId(Integer.parseInt(o[0].toString()));
				tyreDto.setTyreNumber(o[1].toString());
				
				TyreMakerDto maker = new TyreMakerDto();
				maker.setName(o[2].toString());
				tyreDto.setTyreMake(maker);
				
				TyreSizeDto size = new TyreSizeDto();
				size.setSize(o[3].toString());
				tyreDto.setTyreSize(size);
				
				//tyreDto.setKmsDone(Double.parseDouble(o[4].toString()));
				
				TyreConditionDto condition = new TyreConditionDto();
				condition.setName(o[5].toString());
				tyreDto.setTyreCondition(condition);
				
				Float totalKmsDone = tyreCostCalculationUtility.calculateTyreTotalKms(tyreDto.getId());
				if(totalKmsDone != null)
				tyreDto.setTotalKilometerDone(totalKmsDone.toString());
				tyreMasterList.add(tyreDto);
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tyreMasterList;
	}

	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> markForRetreading(RetreadingDocketDto docketDto) {
		try {
			
			DepotMaster depotMaster = depotRepo.findByDepotCode(docketDto.getDpCode());
			RetreadingDocket docket = this.mapper.map(docketDto, RetreadingDocket.class);
			docket.setDocketNumber(this.generateDocketNumber(depotMaster.getId()));
			docket.setDepot(depotMaster);
			TakenOffReason toObj = toRepo.findByCode("FRS");
			for(DocketTyreAssociation dta : docket.getTyreLists()) {
				dta.setDocket(docket);
				TyreMaster tyre = tyreRepo.findById(dta.getTyre().getId()).get();
				tyre.setTakenOffReason(toObj);
				tyreRepo.save(tyre);
			}
			rdRepo.save(docket);
			return new ResponseEntity<>(
					new ResponseStatus("Data has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		
	}
	
	public String generateDocketNumber(Integer dpId) {
		String docketNumber = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("ddmmyyy/hhmm");
			docketNumber = "DOC/"+dpId.toString()+"/"+dateFormat.format(new Date());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return docketNumber;
	}

	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> markForCondemnation(RetreadingDocketDto docketDto) {
		Boolean flag = null;
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(docketDto.getDpCode());
			RetreadingDocket docket = this.mapper.map(docketDto, RetreadingDocket.class);

			TakenOffReason toObj = toRepo.findByCode("CN");
			TyreCondition condition = conRepo.findByName("Condemn");
			for (DocketTyreAssociation dta : docket.getTyreLists()) {
				flag = false;
				Float kmsDoneByCondition = tyreCostCalculationUtility
						.calculateTyreKmsByCondition(dta.getTyre().getId());
				if (dta != null && dta.getExpectedLife() != null) {
					if (dta.getExpectedLife() > kmsDoneByCondition) {
						flag = true;
						TakenOffReason reasonAuthentication = takenOffRepo.findByCode("GMAC");
						dta.getTyre().setTakenOffReason(reasonAuthentication);
					}
				}
				TyreMaster tyre = tyreRepo.findById(dta.getTyre().getId()).get();
				tyre.setTyreCondition(condition);
				if(!flag)
				tyre.setTakenOffReason(toObj);
				tyreRepo.save(tyre);
			}

			return new ResponseEntity<>(new ResponseStatus("Tyres marked condemn successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<RetreadingDocketDto> getAllDocket(String dpCode) {
		List<RetreadingDocketDto> list = new ArrayList<>();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> objList =  rdRepo.findAllbyDepotId(depotMaster.getId());
			for(Object[] o : objList) { 
				RetreadingDocketDto obj = new RetreadingDocketDto();
				obj.setId(Integer.parseInt(o[0].toString()));
				obj.setDocketNumber(o[1].toString());
				obj.setTyreCount(Integer.parseInt(o[2].toString()));
				obj.setDate(o[3].toString());
				list.add(obj);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<RetreadingDocketDto> getReceivedDocketList(String dpCode) {
		List<RetreadingDocketDto> list = new ArrayList<>();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> objList =  rdRepo.getReceivedDocketList(depotMaster.getId());
			for(Object[] o : objList) { 
				RetreadingDocketDto obj = new RetreadingDocketDto();
				obj.setId(Integer.parseInt(o[0].toString()));
				obj.setDocketNumber(o[1].toString());
				obj.setTyreCount(Integer.parseInt(o[2].toString()));
				list.add(obj);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<DocketTyreAssociationDto> getTyreListForDocket(Integer docketId) {
		
		List<DocketTyreAssociationDto> list = new ArrayList<>();
		try {
			List<Object[]> objList = dtaRepo.findAllByDocketId(docketId);
			for(Object[] o : objList) {
				DocketTyreAssociationDto dto = new DocketTyreAssociationDto();
				if(o[0] != null)
				dto.setId(Integer.parseInt(o[0].toString()));
				TyreMasterDto tyre = new TyreMasterDto();
				if(o[1] != null)
				tyre.setId(Integer.parseInt(o[1].toString()));
				if(o[2] != null)
				tyre.setTyreNumber(o[2].toString());
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[3] != null)
				size.setSize(o[3].toString());
				tyre.setTyreSize(size);
				
				TyreMakerDto maker = new TyreMakerDto();
				if(o[4] != null)
				maker.setName(o[4].toString());
				tyre.setTyreMake(maker);
				
				Float totalKmsDone = tyreCostCalculationUtility.calculateTyreTotalKms(tyre.getId());
				if(totalKmsDone != null)
				dto.setTotalKilometerDone(totalKmsDone.toString());
				dto.setTyre(tyre);
				dto.setTreaded(true);
				list.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Transactional
	@Override
	public ResponseEntity<ResponseStatus> saveReceivedDocketList(RetreadingDocketDto docketDto) {
		try { 
			RetreadingDocket docket = this.mapper.map(docketDto, RetreadingDocket.class);
			for(DocketTyreAssociation dta : docket.getTyreLists()) {
				TyreCondition condition = conRepo.findByName("Condemn");
				DocketTyreAssociation dtaObj = dtaRepo.findObjectByDocketId(dta.getId());
				if(dta.isTreaded()) {
					TyreMaster tyre = tyreRepo.findById(dtaObj.getTyre().getId()).get();
					dtaObj.setOldConditon(tyre.getTyreCondition());
					tyre.setAvailable(true);
					if(tyre.getTyreCondition().getName().equals("New")) {
						tyre.setTyreCondition(conRepo.findByName("R1"));
					} else if(tyre.getTyreCondition().getName().equals("R1")) {
						tyre.setTyreCondition(conRepo.findByName("R2"));
					} else if(tyre.getTyreCondition().getName().equals("R2")) {
						tyre.setTyreCondition(conRepo.findByName("R3"));
					}
					dtaObj.setNewConditon(tyre.getTyreCondition());
					dtaObj.setExpectedLife(dta.getExpectedLife());
					dtaObj.setTyreCost(dta.getTyreCost());
					dtaObj.setReceived(true);
					dtaObj.setTreaded(dta.isTreaded());
					dtaObj.setRemarks(dta.getRemarks());
					TakenOffReason reasonObj = takenOffRepo.findByCode("DRS");
					tyre.setTakenOffReason(reasonObj);
					tyreRepo.save(tyre);
					dtaObj.setReceivedDate(new Date());
					dtaRepo.save(dtaObj);
				} else {
					TyreMaster tyre = tyreRepo.findById(dta.getTyre().getId()).get();
					dtaObj.setOldConditon(tyre.getTyreCondition());
					dtaObj.setNewConditon(condition);
					dtaObj.setExpectedLife(dta.getExpectedLife());
					dtaObj.setTyreCost(dta.getTyreCost());
					dtaObj.setReceived(true);
					dtaObj.setTreaded(dta.isTreaded());
					dtaObj.setRemarks(dta.getRemarks());
					dtaObj.setReceivedDate(new Date());
					dtaRepo.save(dtaObj);
					TakenOffReason toObj = toRepo.findByCode("CNM");
					tyre.setTakenOffReason(toObj);
					tyre.setTyreCondition(condition);
					tyreRepo.save(tyre);
				}
			}
			List<DocketTyreAssociation> list = dtaRepo.findByDocketIdAndRec(docketDto.getId());
			if(list.size()==0) {
				RetreadingDocket docketObj = rdRepo.findById(docket.getId()).get();
				docketObj.setClosed(true);
				rdRepo.save(docketObj);
			}
			return new ResponseEntity<>(
					new ResponseStatus("Tyres marked received successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<TyreMasterDto> getTyreObjForDocket(Integer docketId) {
		TyreMasterDto dtoObj = null;
		TyreMakerDto makerDto = null;
		TyreSizeDto sizeDto = null;
		TyreConditionDto conditionDto = null;
		List<TyreMasterDto> dtoObjList = new ArrayList<>();
		try {
			List<DocketTyreAssociation> docketList = dtaRepo.findByDocketId(docketId);
			for (DocketTyreAssociation docketObj : docketList) {
				dtoObj = new TyreMasterDto();
				makerDto = new TyreMakerDto();
				sizeDto = new TyreSizeDto();
				conditionDto = new TyreConditionDto();
				TyreMaster tyreMaster = docketObj.getTyre();
				dtoObj.setTyreNumber(tyreMaster.getTyreNumber());
				if(tyreMaster.getTyreTag() != null && tyreMaster.getTyreCondition() != null)
				dtoObj.setTyreTag(tyreMaster.getTyreTag()+"-"+tyreMaster.getTyreCondition().getName());
				makerDto.setName(tyreMaster.getTyreMake().getName());
				dtoObj.setTyreMake(makerDto);
				sizeDto.setSize(tyreMaster.getTyreSize().getSize());
				dtoObj.setTyreSize(sizeDto);
				conditionDto.setName(tyreMaster.getTyreCondition().getName());
				dtoObj.setTyreCondition(conditionDto);
				Double recoveredCost = tyreCostCalculationUtility
						.calculateTyreRecoveredCostByCondition(tyreMaster.getId());
				dtoObj.setRecoveredCost(recoveredCost);
				Float kmsDoneByCondition = tyreCostCalculationUtility.calculateTyreKmsByCondition(tyreMaster.getId());
				if (kmsDoneByCondition != null)
					dtoObj.setKmsDoneByCondition(kmsDoneByCondition.toString());
				dtoObjList.add(dtoObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtoObjList;
	}

}
