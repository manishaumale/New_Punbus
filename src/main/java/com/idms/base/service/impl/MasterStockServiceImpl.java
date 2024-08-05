package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.BookPerBundleDto;
import com.idms.base.api.v1.model.dto.CentralTicketStockDto;
import com.idms.base.api.v1.model.dto.DenominationDto;
import com.idms.base.api.v1.model.dto.MasterFormListDataDto;
import com.idms.base.api.v1.model.dto.MasterStockDenominationDTO;
import com.idms.base.api.v1.model.dto.MasterStockDto;
import com.idms.base.api.v1.model.dto.MasterStockSerialDto;
import com.idms.base.api.v1.model.dto.MasterStockfrmLoadDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.TicketBookCountDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.CentralTicketStock;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.dao.entity.MasterStockTicketDtls;
import com.idms.base.dao.repository.BookPerBundleRepository;
import com.idms.base.dao.repository.CentralTicketStockRepository;
import com.idms.base.dao.repository.DenominationRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MasterStockRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.TicketBookCountRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.MasterStockService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MasterStockServiceImpl implements MasterStockService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private TransportUnitRepository transportunitrepository;

	@Autowired
	private DenominationRepository denoRepo;

	@Autowired
	private StateMasterRepository stateRepo;

	@Autowired
	private MasterStockRepository masterStockRepo;
	
	@Autowired
	private TicketBookCountRepository tbcRepo;
	
	@Autowired
	private CentralTicketStockRepository cenRepo;
	
	@Autowired
	private BookPerBundleRepository bpbRepo;

	@Override
	public MasterStockfrmLoadDto masterFormOnLoad(String tpGroups) {

		log.info("Entering into masterformload service");
		MasterStockfrmLoadDto loadDto = new MasterStockfrmLoadDto();
		List<TransportDto> tpList = new ArrayList<>();
		try {

			String[] tpGrp = tpGroups.split(",");
			tpList = transportunitrepository.findTPUByGroupIds(tpGrp).stream()
					.map(tpu -> new TransportDto(tpu.getId(), tpu.getTransportName())).collect(Collectors.toList());
			loadDto.setTransportunitlist(tpList);

			List<DenominationDto> denominations = denoRepo.findAll().stream()
					.map(den -> new DenominationDto(den.getId(), den.getDenomination())).collect(Collectors.toList());
			loadDto.setDenominations(denominations);

			List<StateDto> stateMasterList = stateRepo.taxstates().stream()
					.map(state -> new StateDto(state.getId(), state.getStateName())).collect(Collectors.toList());
			loadDto.setStatelist(stateMasterList);
			
			List<TicketBookCountDto> tbcList =  tbcRepo.findAll().stream()
					.map(tbc -> new TicketBookCountDto(tbc.getId(), tbc.getTbCount())).collect(Collectors.toList());
			loadDto.setTicketCountList(tbcList);
			
			List<BookPerBundleDto> bpbList = bpbRepo.findAll().stream()
					.map(bpb -> new BookPerBundleDto(bpb.getId(), bpb.getTbCount())).collect(Collectors.toList());
			loadDto.setBpbList(bpbList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadDto;
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStatus> saveCentralStock(CentralTicketStock centralTicketStock) {
		log.info("Entering into saveMasterStock service");
		try {
			List<CentralTicketStock> cTS = cenRepo.findBySrNo(centralTicketStock.getSeriesNumber(), centralTicketStock.getDenomination().getId());
			if(cTS!=null && cTS.size()>0) {
				for(CentralTicketStock cts : cTS) {
					if(centralTicketStock.getStartSrNo()>cts.getStartSrNo() && centralTicketStock.getStartSrNo()< cts.getEndSrNo()) {
						return new ResponseEntity<>(new ResponseStatus("Entered series numbers are already in within system.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					} 
					if(centralTicketStock.getStartSrNo()<cts.getStartSrNo() && centralTicketStock.getEndSrNo()>cts.getStartSrNo()) {
						return new ResponseEntity<>(new ResponseStatus("Entered series numbers are already in within system.", HttpStatus.FORBIDDEN), HttpStatus.OK);
					}
				}
			}
			
			int startSrNo = centralTicketStock.getStartSrNo();
			int endSrNo = centralTicketStock.getEndSrNo();
			int bundleTicketCount = centralTicketStock.getBook().getTbCount() * centralTicketStock.getTbCount().getTbCount();
			int ticketBookCount = centralTicketStock.getTbCount().getTbCount(); 
			int totalBundles = ((endSrNo - startSrNo) + 1) / bundleTicketCount;
			int totalBooks = ((endSrNo - startSrNo) + 1) / centralTicketStock.getTbCount().getTbCount();
			
			List<MasterStock> msList = new ArrayList<>();
			for(int i=0; i<totalBundles; i++) {
				
				List<MasterStockTicketDtls> dtlsList = new ArrayList<>();
				
				MasterStock ms = new MasterStock();
				ms.setStartSrNo(startSrNo);
				ms.setEndSrNo(startSrNo + (bundleTicketCount - 1));
				if(ms.getEndSrNo() != null && ms.getEndSrNo().toString().length() >= 5)
				ms.setBundleNumber(centralTicketStock.getSeriesNumber()+"-"+ms.getStartSrNo()+"-"+ms.getEndSrNo().toString().substring(ms.getEndSrNo().toString().length()-5));
				else
					ms.setBundleNumber(centralTicketStock.getSeriesNumber()+"-"+ms.getStartSrNo()+"-"+ms.getEndSrNo().toString().substring(ms.getEndSrNo().toString().length()-2));
				ms.setCenteralStock(centralTicketStock);
				int stNo = ms.getStartSrNo();
				for(int j=0; j<centralTicketStock.getBook().getTbCount(); j++) {
					MasterStockTicketDtls obj = new MasterStockTicketDtls();
					obj.setStartSrNo(stNo);
					obj.setEndSrNo(stNo + (ticketBookCount-1));
					obj.setDenomination(centralTicketStock.getDenomination());
					obj.setMasterStock(ms);
					obj.setTicketBookNo(stNo+"");
					stNo = stNo + ticketBookCount;
					dtlsList.add(obj);
				}
				ms.setStockSerialNoDetails(dtlsList);
				startSrNo = startSrNo + bundleTicketCount;
				msList.add(ms);
			}
			centralTicketStock.setBundleStockDtls(msList);
			cenRepo.save(centralTicketStock);
			return new ResponseEntity<>(new ResponseStatus("Ticket stock has been persisted successfully.", HttpStatus.OK), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
	}

	@Override
	public List<CentralTicketStockDto> getMasterStockListUpdated(String tpGroup) {
		List<CentralTicketStockDto> list = new ArrayList<>();
		List<Object[]> objList = new ArrayList<>();
		List<TransportDto> transportList = new ArrayList<>();
		try {
			String[] tpGrp = tpGroup.split(",");
			transportList = transportunitrepository.findTPUByGroupIds(tpGrp).stream()
					.map(tpu -> new TransportDto(tpu.getId(), tpu.getTransportName())).collect(Collectors.toList());
			
			List<Integer> tpIds = new ArrayList<Integer>();
			for(TransportDto tp : transportList) {
				tpIds.add(tp.getId());
			}
			objList = cenRepo.getMasterStockListUpdated(tpIds);
			for(Object[] o : objList) {

				CentralTicketStockDto dto = new CentralTicketStockDto();
				
				TransportDto tp = new TransportDto();
				tp.setId(Integer.parseInt(o[0].toString()));
				tp.setTransportName(o[1].toString());
				dto.setTransport(tp);
				
				DenominationDto deno = new DenominationDto();
				deno.setId(Integer.parseInt(o[2].toString()));
				deno.setDenomination(Integer.parseInt(o[3].toString()));
				dto.setDenomination(deno);
				
				dto.setSeriesNumber(o[4].toString());
				
				dto.setTicketCount(Integer.parseInt(o[5].toString()));
				dto.setTicketTotalAmount(Integer.parseInt(o[6].toString()));
				
				list.add(dto);
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<MasterStockDto> getMasterStockTicketList(Integer tpId, Integer denoId) {
		List<MasterStockDto> list = new ArrayList<>();
		List<Object[]> objList = new ArrayList<>();
		try {
			objList = masterStockRepo.getMasterStockTicketList(tpId, denoId);
			for(Object[] o : objList) {
				MasterStockDto dto = new MasterStockDto();
				List<MasterStockSerialDto> mList = new ArrayList<>();
				MasterStockSerialDto mDto = new MasterStockSerialDto();
				mDto.setTicketBookNo(o[0].toString());
				mDto.setStartSrNo(Integer.parseInt(o[1].toString()));
				mDto.setEndSrNo(Integer.parseInt(o[2].toString()));
				mList.add(mDto);
				dto.setStockSerialNoDetails(mList);
				dto.setStateTax(Boolean.parseBoolean(o[3].toString()));
				dto.setBundleNumber(o[6].toString());
				dto.setSeriesNumber(o[7].toString());
				StateDto state = new StateDto();
				if(o[4]!=null) {
					state.setId(Integer.parseInt(o[4].toString()));
					state.setStateName(o[5].toString());
				} else {
					state.setStateName("-");
				}
				dto.setState(state);
				list.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
