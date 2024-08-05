package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.TyreHistoryDto;
import com.idms.base.api.v1.model.dto.TyreRepairDto;
import com.idms.base.dao.entity.BusTyreAssociationHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.dao.entity.TakenOffReason;
import com.idms.base.dao.entity.TyreCondition;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.entity.TyreRepairHistory;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusTyreAssociationHistoryRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DocketTyreAssociationRepository;
import com.idms.base.dao.repository.TakenOffReasonRepository;
import com.idms.base.dao.repository.TyreConditionRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.dao.repository.TyreRepairHistoryRepo;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.TyreCostCalculationUtility;
import com.idms.base.util.TyreManagementUtility;

@Service
public class TyreRepairServiceImpl {

	@Autowired
	TyreMasterRepository tyreMasterRepo;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	TyreRepairHistoryRepo tyreRepairHisRepo;
	
	@Autowired
	BusTyreAssociationHistoryRepository assoRepo;
	
	@Autowired
	TyreConditionRepository tyreConditionRepo;
	
	@Autowired
	BusMasterRepository busMasterRepo;
	
	@Autowired
	TakenOffReasonRepository takenOffReasonId;
	
	@Autowired
	TyreCostCalculationUtility tyreCostUtility;
	
	@Autowired
	TyreManagementUtility tyreMgmtUtility;
	
	@Autowired
	DocketTyreAssociationRepository docketTyreAssoRepo;
	
	public List<TyreRepairDto> getRepairTyres(String depotCode) {
		List<TyreRepairDto> t = new ArrayList<>();
		List<Object[]> tyres = new ArrayList<>();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(depotCode);
			tyres = tyreMasterRepo.findAllRepairTyres(depot.getId());
			for(Object a[]:tyres){
				
				TyreRepairDto temp = new  TyreRepairDto();
				BusTyreAssociationHistory his = assoRepo.findTyreRepairRecord(Integer.parseInt(a[0].toString()));
				temp.setId(a[0]!=null ? Integer.parseInt(a[0].toString()) : null);
				temp.setTyreNumber(a[1]!=null ? a[1].toString() : " ");
				temp.setTyreTag(tyreMgmtUtility.concatConditionWithTag(Integer.parseInt(a[0].toString())));
				temp.setAvailable(a[3]!=null ? Boolean.parseBoolean(a[3].toString()) : null);
				TyreCondition condition =a[4]!=null ?  tyreConditionRepo.findById(Integer.parseInt(a[4].toString())).get() : null;
				temp.setDepot(depot.getDepotName());
				temp.setTyreCondition(condition.getName());

				if(condition.getName().equals("New")) {
					temp.setExpectedLife(a[5]!=null ? a[5].toString() :" ");
				} else {
					DocketTyreAssociation docket = docketTyreAssoRepo.findObjectByTyreId(Integer.parseInt(a[0].toString()));
					temp.setExpectedLife(docket!=null && docket.getExpectedLife()!=null ? docket.getExpectedLife().toString() :" ");
				}
//				BusMaster busMaster = a[5]!=null ?  busMasterRepo.findById(Integer.parseInt(a[5].toString())).get() :null;
				temp.setBusRegNumber(his!=null ? his.getBus().getBusRegNumber() : " ");
				temp.setDate(his!=null ? his.getRemovalDate() : null);
				temp.setTotalKmsDone(tyreCostUtility.calculateTyreTotalKms(Integer.parseInt(a[0].toString())));
				t.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public ResponseEntity<ResponseStatus> updateTyre(Integer id,String depotCode,String Code,String remarks) {
		try {
			java.util.Optional<TyreMaster> tyremaster = tyreMasterRepo.findById(id);
			DocketTyreAssociation docket = tyremaster.get().getTyreCondition().getName().equals("New") ? docketTyreAssoRepo.findObjectByTyreId(id) : null;
			Float expectedKms= tyremaster.get().getTyreCondition().getName().equals("New") ? Float.parseFloat(tyremaster.get().getExpectedLife()) : docket!=null ? docket.getExpectedLife() : 0.0f;	
			Float actualKms = tyreCostUtility.calculateTyreKmsByCondition(id);
			DepotMaster depot = depotRepo.findByDepotCode(depotCode);					
			if(tyremaster.isPresent() && !tyremaster.get().isAvailable() && Code.equals("Repair")){
				tyremaster.get().setAvailable(true);
				BusTyreAssociationHistory his = assoRepo.findTyreRepairRecord(id);
				if(his!=null){
					TakenOffReason reason = takenOffReasonId.findByCode("DRP");
					TyreRepairHistory tyreHistory = new TyreRepairHistory();
					tyreHistory.setAssosiation(his);
					tyreHistory.setDepot(depot);
					tyreHistory.setTyre(tyremaster.get());
					tyreHistory.setRemarks(remarks);
					tyremaster.get().setTakenOffReason(reason);
					tyreMasterRepo.save(tyremaster.get());
					tyreRepairHisRepo.save(tyreHistory);
					return new ResponseEntity<>(
							new ResponseStatus("Tyre has been marked repair successfully", HttpStatus.OK),
							HttpStatus.OK);
				}
				return new ResponseEntity<>(
						new ResponseStatus("Association is not found", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if(tyremaster.isPresent() && Code.equals("Condemn")) {
				if(actualKms!=null && expectedKms>actualKms) {
					BusTyreAssociationHistory his = getAssosiationRecord(id);
					if(his!=null){
					TakenOffReason reason = takenOffReasonId.findByCode("GMAC");
//					TyreCondition condition = tyreConditionRepo.findByName("Condemn"); 	
					TyreRepairHistory tyreHistory = new TyreRepairHistory();
					tyreHistory.setAssosiation(his);
					tyreHistory.setDepot(depot);
					tyreHistory.setTyre(tyremaster.get());
					tyreHistory.setRemarks(remarks);
					tyremaster.get().setTakenOffReason(reason);
					his.setReason(reason);
					assoRepo.save(his);
					tyreMasterRepo.save(tyremaster.get());
					tyreRepairHisRepo.save(tyreHistory);
					return new ResponseEntity<>(
							new ResponseStatus("Tyre has been marked to GM for tyre condemn approval", HttpStatus.OK),
							HttpStatus.OK);
					} else {
						return new ResponseEntity<>(
								new ResponseStatus("Association is not found", HttpStatus.FORBIDDEN),
								HttpStatus.OK);
					}
				} else {
				BusTyreAssociationHistory his = getAssosiationRecord(id);
				if(his!=null){
				TakenOffReason reason = takenOffReasonId.findByCode("CN");
//				TyreCondition condition = tyreConditionRepo.findByName("Condemn");
				TyreRepairHistory tyreHistory = new TyreRepairHistory();
				tyreHistory.setAssosiation(his);
				tyreHistory.setDepot(depot);
				tyreHistory.setTyre(tyremaster.get());
				tyreHistory.setRemarks(remarks);
				tyremaster.get().setTakenOffReason(reason);
				his.setReason(reason);
				assoRepo.save(his);
				tyreMasterRepo.save(tyremaster.get());
				tyreRepairHisRepo.save(tyreHistory);
				return new ResponseEntity<>(
						new ResponseStatus("Tyre has been marked condemn", HttpStatus.OK),
						HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							new ResponseStatus("Association is not found", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				}
			}else if(tyremaster.isPresent() && Code.equals("Resole")) {
				if(actualKms!=null && expectedKms>actualKms) {
					BusTyreAssociationHistory his = getAssosiationRecord(id);
					if(his!=null){
					TakenOffReason reason = takenOffReasonId.findByCode("GMAR");
//					TyreCondition condition = tyreConditionRepo.findByName("R1"); 
					TyreRepairHistory tyreHistory = new TyreRepairHistory();
					tyreHistory.setAssosiation(his);
					tyreHistory.setDepot(depot);
					tyreHistory.setTyre(tyremaster.get());
					tyreHistory.setRemarks(remarks);
					tyremaster.get().setTakenOffReason(reason);
					his.setReason(reason);
					assoRepo.save(his);
					tyreMasterRepo.save(tyremaster.get());
					tyreRepairHisRepo.save(tyreHistory);
					return new ResponseEntity<>(
							new ResponseStatus("Tyre has been marked to GM for tyre resole approval", HttpStatus.OK),
							HttpStatus.OK);
					} else {
						return new ResponseEntity<>(
								new ResponseStatus("Association is not found", HttpStatus.FORBIDDEN),
								HttpStatus.OK);
					}
				} else { 
				BusTyreAssociationHistory his = getAssosiationRecord(id);
				if(his!=null){
				TakenOffReason reason = takenOffReasonId.findByCode("RS");
//				TyreCondition condition = tyreConditionRepo.findByName("R1"); 
				TyreRepairHistory tyreHistory = new TyreRepairHistory();
				tyreHistory.setAssosiation(his);
				tyreHistory.setDepot(depot);
				tyreHistory.setTyre(tyremaster.get());
				tyreHistory.setRemarks(remarks);
				tyremaster.get().setTakenOffReason(reason);
				his.setReason(reason);
				assoRepo.save(his);
				tyreMasterRepo.save(tyremaster.get());
				tyreRepairHisRepo.save(tyreHistory);
				return new ResponseEntity<>(
						new ResponseStatus("Tyre has been marked Resole", HttpStatus.OK),
						HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							new ResponseStatus("Association is not found", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
			}
			}
			return new ResponseEntity<>(
					new ResponseStatus("tyre is not present", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("something went wrong", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		
	}
	private BusTyreAssociationHistory getAssosiationRecord (Integer id) {
		try {
			if(id!=null) {
				BusTyreAssociationHistory his = assoRepo.findTyreRepairRecord(id);
				if(his!=null){ 
					return his;
				} else {
					return null;
				}
				 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;	
	}
	
	public List<TyreHistoryDto> getRepairedTyres(String depotCode) {
		List<TyreHistoryDto> out = new ArrayList<>();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(depotCode);		
			List<TyreRepairHistory>obj = tyreRepairHisRepo.findAllByDepot(depot.getId());
			for(TyreRepairHistory a : obj){
				TyreHistoryDto dto = new TyreHistoryDto();
				dto.setRepairDate(a.getCreatedOn());
				dto.setTyreNumber(a.getTyre().getTyreNumber());
				dto.setTyreId(a.getTyre().getId());
				dto.setTyrePosition(a.getTyre()!=null && a.getTyre().getTyrePosition()!=null ? a.getTyre().getTyrePosition().getName(): "");
				dto.setTotalKmsDone(String.valueOf(a.getTyre().getKmsDone()));
				dto.setKmsDoneInBus("0");
				dto.setKmsInCondition(tyreCostUtility.calculateTyreKmsByCondition(a.getTyre().getId()));
				dto.setTyreCondition(a.getTyre().getTyreCondition().getName());
				dto.setTyreRecoverCost(tyreCostUtility.calculateTyreRecoveredCostByCondition(a.getTyre().getId()));
				out.add(dto);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return out;
	}
	
}
