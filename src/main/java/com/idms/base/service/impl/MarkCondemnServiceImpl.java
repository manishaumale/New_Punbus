package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.MarkCondemndto;
import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MarkCondemn;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MarkCondemnRepository;
import com.idms.base.service.MarkCondemnService;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class MarkCondemnServiceImpl implements MarkCondemnService {

	@Autowired
	MarkCondemnRepository markCodemnRepo;
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	DepotMasterRepository depo;

	@Override
	public ResponseEntity<ResponseStatus> addmarkCondemnDtls(MarkCondemn markCondemn,String depoCode) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(markCondemn.getCondemnDate()); 
		c.add(Calendar.DATE, 0);
		Date conDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
			Optional<DepotMaster> findById = depo.findById( findByDepotCode.getId());
			markCondemn.setDepotId(findById.get());
			conDate = sdf.parse(sdf.format(c.getTime()));
			markCondemn.setCondemnDate(conDate);
			MarkCondemn markCodemn1 = markCodemnRepo.save(markCondemn);

			if (markCodemn1.getId() != null) {
				return new ResponseEntity<>(new ResponseStatus("MarkCodemn persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}

		return null;
	}

	@Override
	public List<MarkCondemndto> getMarkCondemnDtls(String depoCode) {

		List<MarkCondemndto> markCondemndto = new ArrayList<>();

		MarkCondemndto markcodemn = null;
		try {

			DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);

			List<Object[]> codemndtls = markCodemnRepo.findBydepoId(findByDepotCode.getId());

			for (Object[] ob : codemndtls) {
				markcodemn = new MarkCondemndto();
				if(ob[0]!=null)
				{
				markcodemn.setBusregisternumbers(ob[0].toString());
				}
				else
				{
					markcodemn.setBusregisternumbers("");	
				}
				if(ob[1]!=null)
				{
				markcodemn.setRemarks(ob[1].toString());
				}
				else
				{
					markcodemn.setRemarks("");
				}
				if(ob[2]!=null)
				{
				markcodemn.setBusId(ob[2].toString());
				}
				else
				{
					markcodemn.setBusId("");	
				}
				if(ob[3]!=null)
				{
				markcodemn.setCreatedOn(ob[3].toString());
				}
				else
				{
					markcodemn.setCreatedOn("");	
				}
				if(ob[4]!=null)
				{
				markcodemn.setConDate(ob[4].toString());
				}
				else
				{
					markcodemn.setConDate("");	
				}
				markCondemndto.add(markcodemn);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return markCondemndto;

	}

	@Override
	public List<MarkSpareBusDetailsDto> getAllBusDetails(String depoCode) {
		
		List<MarkSpareBusDetailsDto> markcodemn= new ArrayList<>();
		DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
		List<Object[]> busDetails = markCodemnRepo.getBusDetails(findByDepotCode.getId());
		for(Object[] ob: busDetails)
		{
			MarkSpareBusDetailsDto markCodemn = new MarkSpareBusDetailsDto();
			if(ob[0]!=null)
			markCodemn.setBusId(Integer.parseInt(ob[0].toString()));
			if(ob[1]!=null)
			markCodemn.setBusregisterNumbers(ob[1].toString());
			markcodemn.add(markCodemn);
			
		}
		
		return markcodemn;
	}

}
