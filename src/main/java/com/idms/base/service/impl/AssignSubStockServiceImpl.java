package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AssignSubStockToDepoDto;
import com.idms.base.api.v1.model.dto.AssignedSubStockFromLoadDto;
import com.idms.base.api.v1.model.dto.DenominationDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DepotTicketStockDto;
import com.idms.base.api.v1.model.dto.MasterStockSerialDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.AssignSubStockToDepoEntity;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.dao.entity.MasterStockTicketDtls;
import com.idms.base.dao.entity.TicketDepotAssignment;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.repository.AssignSubStockToDepoRepository;
import com.idms.base.dao.repository.DenominationRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MasterStockRepository;
import com.idms.base.dao.repository.MasterStockTicketDtlsRepository;
import com.idms.base.dao.repository.TicketDepotAssignmentRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.AssignSubStockService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AssignSubStockServiceImpl implements AssignSubStockService {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	AssignSubStockToDepoRepository assignSubStockRepo;
	
	@Autowired
	DepotMasterRepository depotMasterRepository;
	
	@Autowired
	TransportUnitRepository transportRepo;
	
	@Autowired
	MasterStockTicketDtlsRepository mstdRepo;
	
	@Autowired
	TicketDepotAssignmentRepository tdaRepo;
	
	@Autowired
	DenominationRepository denoRepo;
	
	@Autowired
	MasterStockRepository msRepo;

	@Override
	public String addSubStockDetails(AssignSubStockToDepoDto assignSubStock) {
		log.info("Entered into addSubStockServices services");

		try {

			AssignSubStockToDepoEntity assignSubStocks = modelMapper.map(assignSubStock,
					AssignSubStockToDepoEntity.class);
			AssignSubStockToDepoEntity subStock = assignSubStockRepo.save(assignSubStocks);
			if (subStock.getSubStockId() != null)
				return "Added Substock Details of Depo " + subStock.getDepotName()
						+ " sucessfully with Sub Stock serial ID " + subStock.getSubStockId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AssignSubStockToDepoDto> getAllSubStockDetailsBasedOnDepoCode(String depotCode) {
		log.info("Entered into getAllSubStockServices Services");
		List<AssignSubStockToDepoDto> substockdtls;
		try {
			DepotMaster findByDepotCode = depotMasterRepository.findByDepotCode(depotCode);
			String depotName = findByDepotCode.getDepotName();
			substockdtls = assignSubStockRepo.findByDepotName(depotName).stream()
					.map(stock -> modelMapper.map(stock, AssignSubStockToDepoDto.class)).collect(Collectors.toList());
			return substockdtls;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AssignSubStockToDepoDto findSubStockDtls(Integer subStockId) {

		try {
			AssignSubStockToDepoEntity assignSubStockToDepoEntity = assignSubStockRepo.findById(subStockId).get();
			AssignSubStockToDepoDto map = modelMapper.map(assignSubStockToDepoEntity, AssignSubStockToDepoDto.class);
			return map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<AssignSubStockToDepoDto> findSubStockDtlsOfDepo(String depotName) {
		try {
			List<AssignSubStockToDepoDto> collect = assignSubStockRepo.findByDepotName(depotName).stream()
					.map(stock -> modelMapper.map(stock, AssignSubStockToDepoDto.class)).collect(Collectors.toList());
			return collect;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public AssignedSubStockFromLoadDto fromOnLoadDetails(String tpList) {
		
		List<TransportDto> transportList = new ArrayList<>();
		AssignedSubStockFromLoadDto formLoadDetails = new AssignedSubStockFromLoadDto();
		
		try {

			String[] tpGrp = tpList.split(",");
			transportList = transportRepo.findTPUByGroupIds(tpGrp).stream()
					.map(tpu -> new TransportDto(tpu.getId(), tpu.getTransportName())).collect(Collectors.toList());
			formLoadDetails.setTransportList(transportList);
			
			List<Integer> tpIds = new ArrayList<Integer>();
			for(TransportDto tp : transportList) {
				tpIds.add(tp.getId());
			}

			List<DepotMasterDto> dpList = depotMasterRepository.findAllByTPIds(tpIds).stream().
					map(dp-> new DepotMasterDto(dp.getId(), dp.getDepotName())).collect(Collectors.toList());
			
			formLoadDetails.setDepotList(dpList);

			List<DenominationDto> denominations = denoRepo.findAll().stream()
					.map(denomination -> new DenominationDto(denomination.getId(),
							denomination.getDenomination()))
					.collect(Collectors.toList());
			formLoadDetails.setDenominationList(denominations);

			return formLoadDetails;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MasterStockSerialDto> getTicketBooks(Integer denoId, Integer transportId) {
		List<MasterStockSerialDto> list= new ArrayList<>(); 
		List<Object[]> objList = new ArrayList<>();
		try {
			objList = mstdRepo.findTicketBooksByTrnspAndDeno(denoId, transportId);
			if(objList.size()>0) {
				for(Object[] o: objList) {
					MasterStockSerialDto dto = new MasterStockSerialDto();
					dto.setId(Integer.parseInt(o[0].toString()));
					dto.setTicketBookNo(o[1].toString());
					dto.setStartSrNo(Integer.parseInt(o[2].toString()));
					dto.setEndSrNo(Integer.parseInt(o[3].toString()));
					DenominationDto dDto = new DenominationDto();
					dDto.setId(Integer.parseInt(o[4].toString()));
					dDto.setDenomination(Integer.parseInt(o[5].toString()));
					dto.setDenomination(dDto);
					dto.setSeriesNumber(o[6].toString());
					list.add(dto);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveTicketDepotAssignment(List<MasterStock> masterStockList) {
		try {
			for(MasterStock tda : masterStockList) {
				MasterStock stk = msRepo.findById(tda.getId()).get();
				stk.setAssigned(true);
				DepotMaster depot = depotMasterRepository.getOne(tda.getDepot().getId());
				stk.setDepot(depot);
				msRepo.save(stk);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(
				new ResponseStatus("Ticket bundles has been assigned to depot.", HttpStatus.OK), HttpStatus.OK);
	}

	@Override
	public List<DepotTicketStockDto> getDepotTicketStockList(String tpList) {
		List<DepotTicketStockDto> list = new ArrayList<>();
		List<Object[]> objList = new ArrayList<>();
		List<TransportDto> transportList = new ArrayList<>();
		try {
			String[] tpGrp = tpList.split(",");
			transportList = transportRepo.findTPUByGroupIds(tpGrp).stream()
					.map(tpu -> new TransportDto(tpu.getId(), tpu.getTransportName())).collect(Collectors.toList());
			
			List<Integer> tpIds = new ArrayList<Integer>();
			for(TransportDto tp : transportList) {
				tpIds.add(tp.getId());
			}
			objList = tdaRepo.getDepotTicketStockList(tpIds);
			if(objList!=null) {
				for(Object[] o : objList) {
					DepotTicketStockDto dto = new DepotTicketStockDto();
					
					DepotMasterDto depot = new DepotMasterDto();
					if(o[0]!=null)
					depot.setId(Integer.parseInt(o[0].toString()));
					if(o[1]!=null)
					depot.setDepotName(o[1].toString());
					dto.setDepot(depot);
					
					TransportDto tp = new TransportDto();
					if(o[2]!=null)
					tp.setId(Integer.parseInt(o[2].toString()));
					if(o[3]!=null)
					tp.setTransportName(o[3].toString());
					dto.setTransport(tp);
					
					DenominationDto den = new DenominationDto();
					if(o[4]!=null)
					den.setId(Integer.parseInt(o[4].toString()));
					if(o[5]!=null)
					den.setDenomination(Integer.parseInt(o[5].toString()));
					dto.setDenomination(den);
					
					if(o[6]!=null)
					dto.setTicketBookCount(Integer.parseInt(o[6].toString()));
					if(o[7]!=null)
					dto.setTotalAmount(Integer.parseInt(o[7].toString()));
					if(o[8]!=null)
					dto.setCentralStockId(Integer.parseInt(o[8].toString()));
					if(o[9]!=null)
					dto.setTbCount(Integer.parseInt(o[9].toString()));
					list.add(dto);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<DepotTicketStockDto> getTicketStockByDenomination(Integer denomination,Integer centralStockId ) {
		List<DepotTicketStockDto> list = new ArrayList<>();
		List<Object[]> objList = new ArrayList<>();
		try {
			objList = tdaRepo.getTicketStockByDenomination(denomination,centralStockId);
			if(objList!=null) {
				for(Object[] o : objList) {
					DepotTicketStockDto dto = new DepotTicketStockDto();
					
					DepotMasterDto depot = new DepotMasterDto();
					if(o[0]!=null)
					depot.setId(Integer.parseInt(o[0].toString()));
					if(o[1]!=null)
					depot.setDepotName(o[1].toString());
					dto.setDepot(depot);
					
					TransportDto tp = new TransportDto();
					if(o[2]!=null)
					tp.setId(Integer.parseInt(o[2].toString()));
					if(o[3]!=null)
					tp.setTransportName(o[3].toString());
					dto.setTransport(tp);
					
					DenominationDto den = new DenominationDto();
					if(o[4]!=null)
					den.setId(Integer.parseInt(o[4].toString()));
					if(o[5]!=null)
					den.setDenomination(Integer.parseInt(o[5].toString()));
					dto.setDenomination(den);
							
					if(o[6]!=null)
					dto.setStockDtlsId(Integer.parseInt(o[6].toString()));
					if(o[7]!=null)
					dto.setSeriesNumber(o[7].toString());
					if(o[8]!=null)
					dto.setStartSerialNo(Integer.parseInt(o[8].toString()));
					if(o[9]!=null)
					dto.setEndSerialNo(Integer.parseInt(o[9].toString()));
					if(o[10]!=null)
					dto.setTicketBookNo(Integer.parseInt(o[10].toString()));
					if(o[11]!=null)
					dto.setBundleNumber(o[11].toString());
					if(o[12]!= null)
					dto.setStateTax(o[12].toString());
					if(o[13] != null)
					dto.setStateName(o[13].toString());
					if(o[14] != null)
					dto.setTotalValue(Integer.parseInt(o[14].toString()));
					list.add(dto);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
