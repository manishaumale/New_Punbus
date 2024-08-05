package com.idms.base.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.VTSTyreKmsDetailsDto;
import com.idms.base.dao.entity.VTSBusDieselEntity;
import com.idms.base.dao.repository.VTSBusDieselRepository;
import com.idms.base.service.VTSTyreKmsDetailsService;
import com.idms.base.util.VTS_Util;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class VTSTyreKmsDetailsServiceImpl implements VTSTyreKmsDetailsService{

	@Autowired 
	VTSBusDieselRepository vtsBusDieselRepository;
	
	@Autowired
	VTS_Util vtsUtil;
	
	@Override
	public List<VTSTyreKmsDetailsDto> getVTSTyreKmsDetailsDto(String busNumber, String date) {
		// TODO Auto-generated method stub
		log.info("Enter  getVTSTyreKmsDetailsDto()  into VTSTyreKmsDetailsServiceImpl");
		List<VTSTyreKmsDetailsDto> list = new ArrayList<VTSTyreKmsDetailsDto>();
		List<VTSBusDieselEntity> list1 = null;
		Date searchDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (busNumber == null || busNumber.equalsIgnoreCase("0")) {
				busNumber = "";
			}
			if (date == null  || date.equalsIgnoreCase("0")) {
				date = "";
			} else {
				searchDate = sdf.parse(date);
			}

			if (!busNumber.isEmpty() && !date.isEmpty()) {
				list1 = vtsBusDieselRepository.findAllByBusnoAndDate(busNumber, searchDate);
				if (list1.size() > 0) {
					list = setEntityListDataIntoDto(list1);
				} else {
					list1 = getTyreKmsDetailsFromVTS(busNumber, sdf.format(searchDate));
					list = setEntityListDataIntoDto(list1);				
				}
			} else if (!busNumber.isEmpty()) {
				list1 = vtsBusDieselRepository.findAllByBusno(busNumber);
				if (list1.size() > 0) {
					list = setEntityListDataIntoDto(list1);
				} else {
					list1 = getTyreKmsDetailsFromVTS(busNumber, sdf.format(searchDate));
					list = setEntityListDataIntoDto(list1);
				
				}
			} else if (!date.isEmpty()) {
				list1 = vtsBusDieselRepository.findAllByDate(searchDate);
				if (list1.size() > 0) {
					list = setEntityListDataIntoDto(list1);
				} else {
					list1 = getTyreKmsDetailsFromVTS(busNumber, sdf.format(sdf.parse(date)));
					list = setEntityListDataIntoDto(list1);
				
				}
			} else {
				list1 = vtsBusDieselRepository.findAll();
				if (list1.size() > 0) {
					list = setEntityListDataIntoDto(list1);
				} else {
					list1 = getTyreKmsDetailsFromVTS(busNumber, sdf.format(sdf.parse(date)));
					list = setEntityListDataIntoDto(list1);
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<VTSBusDieselEntity> getTyreKmsDetailsFromVTS(String busNumber, String date) {

		VTSBusDieselEntity vTSBusDieselEntity = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<VTSBusDieselEntity> list = new ArrayList<VTSBusDieselEntity>();
		try {
			String response = vtsUtil.VTS_tyre_wise_kms_api(busNumber, date);
			JSONObject jsonObj = new JSONObject(response);
			if (jsonObj.has("output")) {
				JSONArray root = jsonObj.getJSONArray("output");
				if (root.length() > 0) {
					for (int i = 0; i < root.length(); i++) {
						JSONObject jsonObj2 = root.getJSONObject(i);
						vTSBusDieselEntity = new VTSBusDieselEntity();
						vTSBusDieselEntity.setBus_reg_no(jsonObj2.getString("reg_no"));
						vTSBusDieselEntity.setDate(sdf.parse(jsonObj2.getString("date")));
						vTSBusDieselEntity.setKms_covered(Double.valueOf(jsonObj2.getString("kms_covered")));
						vTSBusDieselEntity.setCreatedOn(new Timestamp(System.currentTimeMillis()));
						vTSBusDieselEntity.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
						list.add(vTSBusDieselEntity);
						vtsBusDieselRepository.save(vTSBusDieselEntity);
					}
				} else {
					log.info("No data found from VTS");
				}
			} else {
				log.info("Invalid input");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<VTSTyreKmsDetailsDto> setEntityListDataIntoDto(List<VTSBusDieselEntity> list) {
		List<VTSTyreKmsDetailsDto> dtoList = new ArrayList<VTSTyreKmsDetailsDto>();
		VTSTyreKmsDetailsDto vtsTyreKmsDetailsDto = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (VTSBusDieselEntity entity : list) {
			vtsTyreKmsDetailsDto = new VTSTyreKmsDetailsDto();
			vtsTyreKmsDetailsDto.setBus_no(entity.getBus_reg_no());
			vtsTyreKmsDetailsDto.setDate(sdf.format(entity.getDate()));
			vtsTyreKmsDetailsDto.setKms_covered(java.lang.String.valueOf(entity.getKms_covered()));
			dtoList.add(vtsTyreKmsDetailsDto);
		}
		return dtoList;
	}

}