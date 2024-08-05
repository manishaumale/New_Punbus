package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.DenominationDto;
import com.idms.base.api.v1.model.dto.ManageTicketBoxDto;
import com.idms.base.api.v1.model.dto.MasterStockDto;
import com.idms.base.api.v1.model.dto.MasterStockSerialDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementDto;
import com.idms.base.api.v1.model.dto.TicketBoxManagementFormDto;
import com.idms.base.api.v1.model.dto.TicketBoxMasterDto;
import com.idms.base.api.v1.model.dto.TicketBoxNumberDto;
import com.idms.base.api.v1.model.dto.TicketManagementBoxParentDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.CentralTicketStock;
import com.idms.base.dao.entity.DenominationEntity;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MasterStockTicketDtls;
import com.idms.base.dao.entity.TicketBoxManagementEntity;
import com.idms.base.dao.entity.TicketBoxManagementParentEntity;
import com.idms.base.dao.entity.TicketBoxMaster;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.repository.CentralTicketStockRepository;
import com.idms.base.dao.repository.DenominationRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MasterStockTicketDtlsRepository;
import com.idms.base.dao.repository.TicketBoxManagementParentRepository;
import com.idms.base.dao.repository.TicketBoxManagementRepository;
import com.idms.base.dao.repository.TicketBoxMasterRepository;
import com.idms.base.dao.repository.TicketBoxRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.TicketBoxManagementService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TicketBoxManagementServiceImpl implements TicketBoxManagementService {

	@Autowired
	DepotMasterRepository depotRepo;

	@Autowired
	TransportUnitRepository tpRepo;

	@Autowired
	DenominationRepository denoRepo;

	@Autowired
	TicketBoxRepository tbRepo;

	@Autowired
	ModelMapper mapper;

	@Autowired
	TicketBoxManagementRepository tbmRepo;

	@Autowired
	TicketBoxMasterRepository tiketboxmasterRepo;
	
	@Autowired
	MasterStockTicketDtlsRepository mstdRepo;
	
	@Autowired
	TicketBoxManagementParentRepository tbmpRepo;

	@Autowired
	private CentralTicketStockRepository cenRepo;
	
	@Override
	public TicketBoxManagementFormDto getTicketBoxMgmtFormLoad(String tpList, String dpCode) {

		TicketBoxManagementFormDto form = new TicketBoxManagementFormDto();
		List<TransportDto> transportList = new ArrayList<>();
		List<TicketBoxMasterDto> ticketBoxList = new ArrayList<>();
		List<DenominationDto> denoList = new ArrayList<>();

		try {

			DepotMaster depot = depotRepo.findByDepotCode(dpCode);

			String[] tpGrp = tpList.split(",");
			transportList = tpRepo.findTPUByGroupIds(tpGrp).stream()
					.map(tpu -> new TransportDto(tpu.getId(), tpu.getTransportName())).collect(Collectors.toList());
			form.setTpList(transportList);

			denoList = denoRepo.findAll().stream().map(d -> new DenominationDto(d.getId(), d.getDenomination()))
					.collect(Collectors.toList());
			form.setDenominationList(denoList);

			// ticketBoxList = tbRepo.findAllBoxWithDepotCode(dpCode);

		} catch (Exception e) {

		}
		return form;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public ResponseEntity<ResponseStatus> addTicketBoxMgmt(List<ManageTicketBoxDto> ticketBoxManagementEntity) {
		log.info("Entered into addTicketBoxMgmt services");
		try {
					ticketBoxManagementEntity.forEach(dto->{
						TicketBoxManagementParentEntity ticketBoxManagement1 = tbmpRepo.findByTicketBoxId(dto.getTicketBoxNumber());
						if (ticketBoxManagement1 != null) {
							List<TicketBoxManagementEntity> boxManagementEntities = new ArrayList<TicketBoxManagementEntity>();
							TicketBoxManagementEntity ticketBoxMgmt = new TicketBoxManagementEntity();
							ticketBoxMgmt.setAmount(dto.getAmount());
							ticketBoxMgmt.setStartingSerialNo(dto.getStartingSerialNo());
							ticketBoxMgmt.setEndingSerialNo(dto.getEndingSerialNo());
							ticketBoxMgmt.setTicketBoxManagementParent(ticketBoxManagement1);
							Optional<TransportUnitMaster> transportUnitMaster = tpRepo.findById(dto.getTransportUnitMaster());
							TransportUnitMaster unitMaster = transportUnitMaster.get();
							ticketBoxMgmt.setTransportUnitMaster(unitMaster);
							Optional<DenominationEntity> denomination = denoRepo.findById(dto.getDenomination());
							DenominationEntity denominationEntity = denomination.get();
							ticketBoxMgmt.setDenomination(denominationEntity);
							
							Optional<CentralTicketStock> centralTicket= cenRepo.findById(dto.getCentralTicket());
							CentralTicketStock centralTicketStockEntity=centralTicket.get();
							ticketBoxMgmt.setCentralTicket(centralTicketStockEntity);
							ticketBoxMgmt.setIsBookletChecked(false);
							Optional<MasterStockTicketDtls> masterStockTicket= mstdRepo.findById(dto.getMasterStock());
							MasterStockTicketDtls masterStockTicketDtlsEntity=masterStockTicket.get();
							ticketBoxMgmt.setMasterStock(masterStockTicketDtlsEntity);
							boxManagementEntities.add(ticketBoxMgmt);
							
							ticketBoxManagement1.setTicketBoxManagementList(boxManagementEntities);
							tbmpRepo.save(ticketBoxManagement1);	

							//MasterStockTicketDtls masterStockTicketDtls = mstdRepo.getMasterStockTicketDtlsByStartSrNo(Integer.parseInt(dto.getStartingSerialNo().toString()),dto.getDenomination());
							MasterStockTicketDtls masterStockTicketDtls = mstdRepo.findById(dto.getMasterStock()).get();
							masterStockTicketDtls.setAssigned(true);
							mstdRepo.save(masterStockTicketDtls); 
							
							
						} else {
							List<TicketBoxManagementEntity> boxManagementEntities1 = new ArrayList<TicketBoxManagementEntity>();
							TicketBoxManagementParentEntity tbmpe = new TicketBoxManagementParentEntity();
							Optional<TicketBoxMaster> ticketBoxMaster = tiketboxmasterRepo.findById(dto.getTicketBoxNumber());
							tbmpe.setTicketBoxNumber(ticketBoxMaster.get());
							TicketBoxManagementEntity ticketBoxMgmt1 = new TicketBoxManagementEntity();
							ticketBoxMgmt1.setAmount(dto.getAmount());
							ticketBoxMgmt1.setStartingSerialNo(dto.getStartingSerialNo());
							ticketBoxMgmt1.setEndingSerialNo(dto.getEndingSerialNo());
							ticketBoxMgmt1.setTicketBoxManagementParent(tbmpe);
							Optional<TransportUnitMaster> transportUnitMaster = tpRepo.findById(dto.getTransportUnitMaster());
							TransportUnitMaster unitMaster = transportUnitMaster.get();
							ticketBoxMgmt1.setTransportUnitMaster(unitMaster);
							Optional<DenominationEntity> denomination = denoRepo.findById(dto.getDenomination());
							DenominationEntity denominationEntity = denomination.get();
							ticketBoxMgmt1.setDenomination(denominationEntity);
							Optional<CentralTicketStock> centralTicket= cenRepo.findById(dto.getCentralTicket());
							CentralTicketStock centralTicketStockEntity=centralTicket.get();
							ticketBoxMgmt1.setIsBookletChecked(false);
							ticketBoxMgmt1.setCentralTicket(centralTicketStockEntity);							
							Optional<MasterStockTicketDtls> masterStockTicketDtls= mstdRepo.findById(dto.getMasterStock());
							MasterStockTicketDtls masterStockTicketDtlsEntity=masterStockTicketDtls.get();
							ticketBoxMgmt1.setMasterStock(masterStockTicketDtlsEntity);
							boxManagementEntities1.add(ticketBoxMgmt1);
							tbmpe.setTicketBoxManagementList(boxManagementEntities1);
							tbmpRepo.save(tbmpe);
							//MasterStockTicketDtls masterStockTicketDtls1 = mstdRepo.getMasterStockTicketDtlsByStartSrNo(Integer.parseInt(dto.getStartingSerialNo().toString()),dto.getDenomination());
							MasterStockTicketDtls masterStockTicketDtls1 = mstdRepo.findById(dto.getMasterStock()).get();
							masterStockTicketDtls1.setAssigned(true);
							mstdRepo.save(masterStockTicketDtls1); 
							
						}
					});
					 
			return new ResponseEntity<>(
							new ResponseStatus("Added TicketBoxManagementdtls sucessfully", HttpStatus.OK),
							HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Error While Saving The Data.",HttpStatus.FORBIDDEN), HttpStatus.OK);
		}

	}

	@Override
	public TicketBoxManagementDto getAllTicketBoxManagementFormLoad(Integer transportUnitId) {
		log.info("Entered into getAllTicketBoxManagementFormLoad Services");
		TicketBoxManagementDto ticketBoxManagementDtos = new TicketBoxManagementDto();
		List<TicketBoxNumberDto> ticketBoxNumbers =new ArrayList<>();
		List<DenominationDto> denoList = new ArrayList<>();
		//ticketBoxManagementDtos.setTransportUnitName(transportUnitName);
		try {

			List<Object[]> ticketboxnumber = tiketboxmasterRepo.findTicketBoxMgmtDtlsByTransportId(transportUnitId);

			for (Object[] o : ticketboxnumber) {
				
				ticketBoxNumbers.add(new TicketBoxNumberDto(Integer.parseInt(o[0].toString()), o[8].toString()));
				
			}
			ticketBoxManagementDtos.setTicketBoxNumber(ticketBoxNumbers);
			
			
			denoList = denoRepo.findAll().stream().map(d -> new DenominationDto(d.getId(), d.getDenomination()))
					.collect(Collectors.toList());
			ticketBoxManagementDtos.setDenomination(denoList);
			return ticketBoxManagementDtos;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MasterStockDto> getStockTicketDetailsList(Integer id) {
		log.info("Entered into getStockTicketDetailsList Services");
		List<MasterStockDto> masterStockDtos = new ArrayList<MasterStockDto>();
		
		try {
			
			//List<Object[]> stocks = tiketboxmasterRepo.getStockTicketDetailsList(id);
			
			List<Object[]> stocks = tiketboxmasterRepo.getStockTicketDetailsByStockIdList(id);
			
			for (Object[] stock : stocks) {
				MasterStockDto dto = new MasterStockDto();
				dto.setId(Integer.parseInt(stock[0].toString()));
				dto.setEndSrNo(Integer.parseInt(stock[8].toString()));
				dto.setStartSrNo(Integer.parseInt(stock[9].toString()));
				dto.setTicketBookNo(Integer.parseInt(stock[10].toString()));
				
				masterStockDtos.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return masterStockDtos;
	}

	@Override
	public List<MasterStockSerialDto> getTicketBooks(Integer denoId, Integer transportId) {
		List<MasterStockSerialDto> list= new ArrayList<>(); 
		List<Object[]> objList = new ArrayList<>();
		try {
			objList = mstdRepo.findTicketBooksByTrnspAndDenoAssined(denoId, transportId);
			if(objList.size()>0) {
				for(Object[] o: objList) {
					MasterStockSerialDto dto = new MasterStockSerialDto();
					dto.setCentralStockId(Integer.parseInt(o[0].toString()));
					dto.setSeriesNumber(o[1].toString());
					/*dto.setId(Integer.parseInt(o[0].toString()));
					dto.setTicketBookNo(o[1].toString());
					dto.setStartSrNo(Integer.parseInt(o[2].toString()));
					dto.setEndSrNo(Integer.parseInt(o[3].toString()));
					DenominationDto dDto = new DenominationDto();
					dDto.setId(Integer.parseInt(o[4].toString()));
					dDto.setDenomination(Integer.parseInt(o[5].toString()));
					dto.setDenomination(dDto);*/
					list.add(dto);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<TicketManagementBoxParentDto> getTicketDetailsList() {
		List<TicketBoxManagementParentEntity> list = new ArrayList<>();
		List<TicketManagementBoxParentDto> parentDtos = new ArrayList<>();
		try {
			 list = tbmpRepo.findAllTicketBoxes(true);
			for(TicketBoxManagementParentEntity entity: list ){
				TicketManagementBoxParentDto dto = new TicketManagementBoxParentDto();
				Long totalValue = (long) 0;
				List<TicketBoxManagementDto> boxManagementDtos = new ArrayList<>();
				for(TicketBoxManagementEntity entity2: entity.getTicketBoxManagementList()){
					TicketBoxManagementDto boxManagementDto = new TicketBoxManagementDto();
					
					if(entity2.getIsBookletChecked()==false){
					boxManagementDto.setId(entity2.getId());
					
					boxManagementDto.setAmount(entity2.getCurrentLastNo()== null ? entity2.getAmount():entity2.getCurrentamount());
					
					boxManagementDto.setStartingSerialNo(null != entity2.getCurrentLastNo() ?entity2.getCurrentLastNo():entity2.getStartingSerialNo());

					boxManagementDto.setEndingSerialNo(entity2.getEndingSerialNo());
					boxManagementDto.setDeno(entity2.getDenomination().getDenomination());
					boxManagementDto.setTransportUnitMaster(entity2.getTransportUnitMaster().getTransportName());
					//boxManagementDto.setTicketBox(entity2.getTicketBoxNumber());
					totalValue = totalValue + boxManagementDto.getAmount();
					
					if(entity2.getCentralTicket().getId()!=null){
					Optional<CentralTicketStock> centralTicket= cenRepo.findById(entity2.getCentralTicket().getId());
					CentralTicketStock centralTicketStockEntity=centralTicket.get();
					boxManagementDto.setSeriesNumber(centralTicketStockEntity.getSeriesNumber());
					}else{
						boxManagementDto.setSeriesNumber("");
					}
					boxManagementDtos.add(boxManagementDto);
					}
				}
				dto.setId(entity.getId());
				dto.setTicketBoxManagementList(boxManagementDtos);
				TicketBoxMaster tbm = tbRepo.findById(entity.getTicketBoxNumber().getId()).get();
				if (tbm!=null) {
					TicketBoxMasterDto dtoo = new TicketBoxMasterDto();
					dtoo.setTicketBoxNumber(tbm.getTicketBoxNumber());
					dto.setTransportUnitMaster(tbm.getTransportUnitMaster().getTransportName());
					dto.setTicketBox(dtoo);	
				}
				/*if(boxManagementDtos.size() > 0)
				dto.setTransportUnitMaster(boxManagementDtos.get(0).getTransportUnitMaster());*/
				dto.setTotalValue(totalValue);
				
				
				parentDtos.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parentDtos;
	}

}
