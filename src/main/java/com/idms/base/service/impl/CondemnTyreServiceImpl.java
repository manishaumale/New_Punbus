package com.idms.base.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AuctionedDocketDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DocketTyreAssociationDto;
import com.idms.base.api.v1.model.dto.RetreadingDocketDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TyreCondemnFormDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.api.v1.model.dto.TyreTypeDto;
import com.idms.base.dao.entity.AucDocketTyreAssociation;
import com.idms.base.dao.entity.AuctionedDocket;
import com.idms.base.dao.entity.BusTyreAssociationHistory;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.TakenOffReason;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.AucDocketTyreAssociationRepository;
import com.idms.base.dao.repository.AuctionedDocketRepository;
import com.idms.base.dao.repository.BusTyreAssociationHistoryRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.TakenOffReasonRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TyreConditionRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.dao.repository.TyreSizeRepository;
import com.idms.base.service.CondemnTyreService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.util.TyreCostCalculationUtility;
import com.idms.base.util.TyreManagementUtility;

@Service
public class CondemnTyreServiceImpl implements CondemnTyreService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private DepotMasterRepository depotRepo;
	
	@Autowired
	TransportUnitRepository transportUnitRepository;
	
	@Autowired
	TyreSizeRepository tsRepo;
	
	@Autowired
	TyreMasterRepository tyreRepo;
	
	@Autowired
	TakenOffReasonRepository torRepo;
	
	@Autowired
	AuctionedDocketRepository adRepo;
	
	@Autowired
	AucDocketTyreAssociationRepository adtaRepo;
	
	@Autowired
	private TyreCostCalculationUtility tyrecostCalUtility;
	
	@Autowired
	private TyreConditionRepository tyreConditionRepo;
	
	@Autowired
	private TyreManagementUtility tyreMgmtUtility;
	
	@Autowired
	private BusTyreAssociationHistoryRepository historyRepository;
	
	
	
	@Override
	public TyreCondemnFormDto getCondemnTyreForm(String dpCode) {
		TyreCondemnFormDto form = new TyreCondemnFormDto();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<TransportDto> TransportDtoList = transportUnitRepository.allTransportMasterByDepot(depotMaster.getId()).stream()
					.map(transport -> new TransportDto(transport.getTransportUnitMaster().getId(), transport.getTransportUnitMaster().getTransportName()))
					.collect(Collectors.toList());
			form.setTransportUnitList(TransportDtoList);
			
			List<TyreSizeDto> sizeDtoList = tsRepo.findAll().stream().map(size -> new TyreSizeDto(size.getId(), size.getSize())).collect(Collectors.toList());
			form.setSizeList(sizeDtoList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return form;
	}

	@Override
	public List<TyreMasterDto> getTyreList(Integer tpId, Integer sizeId, String dpCode) {
		List<TyreMasterDto> tyreList = new ArrayList<>();
		BusTyreAssociationHistory historyObj = null;
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> list = tyreRepo.getAllTyreForCondemnAvail(tpId, sizeId, depotMaster.getId());
			for(Object[] o : list) {
				TyreMasterDto tyre = new TyreMasterDto();
				if(o[0]!=null){
				tyre.setId(Integer.parseInt(o[0].toString()));
				 historyObj = historyRepository.findConditionByTyreId(Integer.parseInt(o[0].toString()));
				}
				if(o[1]!=null)
				tyre.setTyreNumber(o[1].toString());
				if(o[2]!=null)
				//tyre.setTyreTag(o[2].toString());
				tyre.setTyreTag(tyreMgmtUtility.concatConditionWithTag(Integer.parseInt(o[0].toString())));	
				
				TransportDto transportDto= new TransportDto();
				if(o[3]!=null)
				transportDto.setTransportName(o[3].toString());
				tyre.setTransportUnit(transportDto);
				
				TyreMakerDto make = new TyreMakerDto();
				if(o[4]!=null)
				make.setName(o[4].toString());
				tyre.setTyreMake(make);
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[5]!=null)
				size.setSize(o[5].toString());
				tyre.setTyreSize(size);
				if(o[6]!=null)
					tyre.setKmsDone(Double.parseDouble(o[6].toString()));
				else
					tyre.setKmsDone(0d);
				
				TyreConditionDto condition = new TyreConditionDto();
				//if(o[7]!=null)
				//condition.setName(o[7].toString());
				if(historyObj != null){
					condition.setName(historyObj.getTyreCondition().getName());
				tyre.setTyreCondition(condition);
				}
				
			
				DepotMasterDto depo= new DepotMasterDto();
				depo.setDepotCode(o[8].toString());
				tyre.setDepot(depo);
				
               tyre.setTotalRecoveredCost(tyrecostCalUtility.calculateTyreTotalRecoveredCost(Integer.parseInt(o[0].toString())));
				
			  tyre.setTotalKmsDone(Math.round(tyrecostCalUtility.calculateTyreTotalKms(Integer.parseInt(o[0].toString()))));
				
				tyreList.add(tyre);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tyreList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveMarkedForAuction(RetreadingDocketDto docketDto) {
		try {
			TakenOffReason reason = torRepo.findByCode("CNM");
			for(DocketTyreAssociationDto dta : docketDto.getTyreLists()) {
				TyreMaster tyre = tyreRepo.findById(dta.getTyre().getId()).get();
				tyre.setTakenOffReason(reason);
				tyreRepo.save(tyre);
			}
			return new ResponseEntity<>(
					new ResponseStatus("Tyres marked for auction received successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<TyreMasterDto> getCondemnAvailTyreList(Integer tpId, Integer sizeId, String dpCode) {
		List<TyreMasterDto> tyreList = new ArrayList<>();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<Object[]> list = tyreRepo.getCondemnAvailTyreList(tpId, sizeId, depotMaster.getId());
			for(Object[] o : list) {
				TyreMasterDto tyre = new TyreMasterDto();
				if(o[0]!=null)
				tyre.setId(Integer.parseInt(o[0].toString()));
				if(o[1]!=null)
				tyre.setTyreNumber(o[1].toString());
				if(o[2]!=null)
				//tyre.setTyreTag(o[2].toString());
				tyre.setTyreTag(tyreMgmtUtility.concatConditionWithTag(Integer.parseInt(o[0].toString())));	
				TransportDto trans= new TransportDto();
				if(o[3]!=null)
				trans.setTransportName(o[3].toString());
				tyre.setTransportUnit(trans);
				
				TyreMakerDto make = new TyreMakerDto();
				if(o[4]!=null)
				make.setName(o[4].toString());
				tyre.setTyreMake(make);
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[5]!=null)
				size.setSize(o[5].toString());
				tyre.setTyreSize(size);
				if(o[6]!=null)
					
					tyre.setKmsDone(Double.parseDouble(o[6].toString()));
				else
					tyre.setKmsDone(0d);
				
				TyreConditionDto condition = new TyreConditionDto();
				if(o[7]!=null)
				condition.setName(o[7].toString());
				tyre.setTyreCondition(condition);
				
				tyre.setTotalRecoveredCost(tyrecostCalUtility.calculateTyreTotalRecoveredCost(Integer.parseInt(o[0].toString())));
				
				tyre.setTotalKmsDone(Math.round(tyrecostCalUtility.calculateTyreTotalKms(Integer.parseInt(o[0].toString()))));
				
				DepotMasterDto depo= new DepotMasterDto();
				if(o[8]!=null)
				depo.setDepotCode(o[8].toString());
				tyre.setDepot(depo);
				tyreList.add(tyre);
				
				
				
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return tyreList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveAuctionedCondemnTyres(AuctionedDocketDto docketDto) {
		try {
		DepotMaster depotMaster = depotRepo.findByDepotCode(docketDto.getDpcode());
		AuctionedDocket docket = this.mapper.map(docketDto, AuctionedDocket.class);
		docket.setDocketNumber(this.generateDocketNumber(depotMaster.getId()));
		docket.setDepot(depotMaster);
		TakenOffReason toObj = torRepo.findByCode("CNA");
		for(AucDocketTyreAssociation adta : docket.getTyreLists()) {
			adta.setDocket(docket);
			TyreMaster tyre = tyreRepo.findById(adta.getTyre().getId()).get();
			tyre.setTakenOffReason(toObj);
			tyreRepo.save(tyre);
		}
		adRepo.save(docket);
		return new ResponseEntity<>(
				new ResponseStatus("Data saved successfully.", HttpStatus.OK),
				HttpStatus.OK);
		}catch(Exception e) {
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
			docketNumber = "AUC/"+dpId.toString()+"/"+dateFormat.format(new Date());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return docketNumber;
	}

	@Override
	public List<AuctionedDocketDto> getAuctionedDocketList(String dpCode) {
		List<AuctionedDocketDto> list = new ArrayList<>();
		try {
			DepotMaster depotMaster = depotRepo.findByDepotCode(dpCode);
			List<AuctionedDocket> docList = adRepo.findAllByDepotId(depotMaster.getId());
			if(docList.size()>0) {
				for(AuctionedDocket doc : docList) {
					AuctionedDocketDto dto = new AuctionedDocketDto();
					dto.setId(doc.getId());
					dto.setAuctionDate(doc.getAuctionDate());
					dto.setBidAmount(doc.getBidAmount());
					dto.setBidderName(doc.getBidderName());
					dto.setDocketNumber(doc.getDocketNumber());
					dto.setTransport(this.mapper.map(new TransportDto(doc.getTransport().getId(), doc.getTransport().getTransportName()), TransportDto.class));
					list.add(dto);
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<TyreMasterDto> getAuctionedTyreList(Integer docketId) {
		List<TyreMasterDto> tyreList = new ArrayList<>();
		BusTyreAssociationHistory historyObj = null;
		try {
			List<Object[]> list = adtaRepo.findTyreListByDocketId(docketId);
			for(Object[] o : list) {
				TyreMasterDto tyre = new TyreMasterDto();
				if(o[0]!=null){
				tyre.setId(Integer.parseInt(o[0].toString()));
				historyObj = historyRepository.findConditionByTyreId(Integer.parseInt(o[0].toString()));
				}
				if(o[1]!=null)
				tyre.setTyreNumber(o[1].toString());
				if(o[2]!=null)
				//tyre.setTyreTag(o[2].toString());
				tyre.setTyreTag(tyreMgmtUtility.concatConditionWithTag(Integer.parseInt(o[0].toString())));	
				
				TyreMakerDto make = new TyreMakerDto();
				if(o[3]!=null)
				make.setName(o[3].toString());
				tyre.setTyreMake(make);
				
				TyreSizeDto size = new TyreSizeDto();
				if(o[4]!=null)
				size.setSize(o[4].toString());
				tyre.setTyreSize(size);
				
				TyreTypeDto type = new TyreTypeDto();
				if(o[5]!=null)
				type.setName(o[5].toString());
			
                 tyre.setTotalRecoveredCost(tyrecostCalUtility.calculateTyreTotalRecoveredCost(Integer.parseInt(o[0].toString())));
				
				 tyre.setTotalKmsDone(Math.round(tyrecostCalUtility.calculateTyreTotalKms(Integer.parseInt(o[0].toString()))));
			
				TransportDto trans= new TransportDto();
				if(o[6]!=null)
				trans.setTransportName(o[6].toString());
				tyre.setTransportUnit(trans);
				
				DepotMasterDto depo= new DepotMasterDto();
				if(o[7]!=null)
				depo.setDepotCode(o[7].toString());
				tyre.setDepot(depo);
				
				TyreConditionDto tyrecondition= new TyreConditionDto();
				//if(o[9]!=null)
				if(historyObj != null){
				tyrecondition.setName(historyObj.getTyreCondition().getName());
				tyre.setTyreCondition(tyrecondition);
				}
				tyreList.add(tyre);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return tyreList;
	}
	
	
}
