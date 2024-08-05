package com.idms.base.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareConductorDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareDriverdetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MarkSpareEntity;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MarkSpareRepository;
import com.idms.base.service.MarkSpareService;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class MarkSpareServiceImpl implements MarkSpareService {

	@Autowired
	MarkSpareRepository markSpareRepo;

	@Autowired
	DepotMasterRepository depo;

	@Override
	public ResponseEntity<ResponseStatus> addMarkSpareDetails(MarkSpareEntity markSpareEntity,String depoCode) {

		try {
			Date fromDate = null;
			Date toDate  = null;
			DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
			Optional<DepotMaster> findById = depo.findById( findByDepotCode.getId());
			markSpareEntity.setDepot(findById.get());
			Calendar c = Calendar.getInstance(); 
			c.setTime(markSpareEntity.getFromDate()); 
			c.add(Calendar.DATE, 0);
			Calendar c1 = Calendar.getInstance(); 
			c1.setTime(markSpareEntity.getToDate()); 
			c1.add(Calendar.DATE, 0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				fromDate = sdf.parse(sdf.format(c.getTime()));
				toDate = sdf.parse(sdf.format(c1.getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			markSpareEntity.setFromDate(fromDate);
			markSpareEntity.setToDate(toDate);
			MarkSpareEntity markSpare = markSpareRepo.save(markSpareEntity);

			if (markSpare.getId() != null) {
				return new ResponseEntity<>(
						new ResponseStatus("MarkSpare has been persisted successfully.", HttpStatus.OK), HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}

		return null;
	}

	@Override
	public List<MarkSpareDto> getMarkSpareDetails(String depoCode) {

		List<MarkSpareDto> markSpare = new ArrayList<>();

		try {

			DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
			List<Object[]> markspare = markSpareRepo.findbydepoId(findByDepotCode.getId());
			//SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
			for (Object[] ob : markspare) {
				MarkSpareDto ms = new MarkSpareDto();
				if (ob[0] != null) {
					ms.setBusregisterNumbers(ob[0].toString());
				} else {
					ms.setBusregisterNumbers("");
				}
				if (ob[1] != null) {
					ms.setRemarks(ob[1].toString());
				} else {
					ms.setRemarks("");
				}
				if (ob[2] != null) {
					ms.setDriverName(ob[2].toString());
				} else {
					ms.setDriverName("");
				}

				if (ob[3] != null) {
					ms.setConductorName(ob[3].toString());
				} else {
					ms.setConductorName("");

				}
				if (ob[4] != null) {
					ms.setFromDate(ob[4].toString());
				} else {
					ms.setFromDate(null);

				}

				if (ob[5] != null) {
					ms.setToDate(ob[5].toString());
				} else {
					ms.setToDate(null);

				}

				markSpare.add(ms);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return markSpare;
	}

	@Override
	public List<MarkSpareBusDetailsDto> getAllBusRegisterNumbers(String depoCode) {

		List<MarkSpareBusDetailsDto> markSpare = new ArrayList<>();
		MarkSpareBusDetailsDto markspare = null;

		Date date = new Date();
		DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
		List<Object[]> allBusDetails = markSpareRepo.getAllBusDetails(findByDepotCode.getId(), date);
		for (Object[] ob : allBusDetails) {
			markspare = new MarkSpareBusDetailsDto();
			if (ob[0] != null)
				markspare.setBusId(Integer.parseInt(ob[0].toString()));
			if (ob[1] != null)

				markspare.setBusregisterNumbers(ob[1].toString());

			markSpare.add(markspare);
		}
		return markSpare;
	}

	@Override
	public List<MarkSpareDriverdetailsDto> getAllDriverNames(String depoCode) {
		List<MarkSpareDriverdetailsDto> markSpare = new ArrayList<>();
		Date date = new Date();
		MarkSpareDriverdetailsDto markspare = null;
		DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
		List<Object[]> allDriverDetails = markSpareRepo.getAllDriverDetails(findByDepotCode.getId(), date);

		for (Object[] ob : allDriverDetails) {
			markspare = new MarkSpareDriverdetailsDto();
			if (ob[0] != null)
				markspare.setDriverId(Integer.parseInt(ob[0].toString()));
			if (ob[1] != null)

				markspare.setDriverName(ob[1].toString());

			markSpare.add(markspare);
		}
		return markSpare;
	}

	@Override
	public List<MarkSpareConductorDetailsDto> getAllConductorNames(String depoCode) {
		List<MarkSpareConductorDetailsDto> markSpare = new ArrayList<>();
		Date date = new Date();
		DepotMaster findByDepotCode = depo.findByDepotCode(depoCode);
		MarkSpareConductorDetailsDto markspare = null;
		List<Object[]> allConductorDetails = markSpareRepo.getAllConductorDetails(findByDepotCode.getId(),date);

		for (Object[] ob : allConductorDetails) {
			markspare = new MarkSpareConductorDetailsDto();
			if (ob[0] != null)
				markspare.setConductorId(Integer.parseInt(ob[0].toString()));
			if (ob[1] != null)

				markspare.setConductorName(ob[1].toString());

			markSpare.add(markspare);
		}
		return markSpare;
	}

}
