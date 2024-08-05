package com.idms.base.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.TyreMasterRepository;

import lombok.extern.log4j.Log4j2;

/**
*
* @author Hemant Makkar
*/
@Component
@Log4j2
public class TyreManagementUtility {
	
	@Autowired
	TyreMasterRepository tyreMasterRepository;
	
	public String concatConditionWithTag(Integer tyreId){
		StringBuffer buffer = new StringBuffer();
		try{
			TyreMaster tyreMaster = tyreMasterRepository.findById(tyreId).get();
			if(tyreMaster.getTyreTag() != null){
		buffer.append(tyreMaster.getTyreTag());
		buffer.append("-");
		buffer.append(tyreMaster.getTyreCondition().getName());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return buffer.toString();
	}

}
