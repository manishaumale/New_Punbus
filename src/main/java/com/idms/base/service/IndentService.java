package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.IndentDto;
import com.idms.base.api.v1.model.dto.ItemFormDto;
import com.idms.base.dao.entity.Indent;
import com.idms.base.dao.entity.IndentChildEntity;
import com.idms.base.dao.entity.ItemTypeMaster;
import com.idms.base.support.persist.ResponseStatus;

@Service
public interface IndentService {

	List<ItemTypeMaster> getAllItemMasterList();

	List<ItemFormDto> getAllItemFormLoad(Integer id);

	ResponseEntity<ResponseStatus> saveIndentDetails(Indent indent);
	
	ResponseEntity<ResponseStatus> saveIndentChildDetails(List<IndentChildEntity> indentChildEntity,Integer indentId);

	List<IndentDto> getIndentDetails(String depoCode);

	List<IndentDto> getAllItemDetailsById(Integer id);
    
	ResponseEntity<ResponseStatus> deleteIndentStatusFlag(Integer id);
	
	ResponseEntity<ResponseStatus> updateIndentStatus(Integer id);
	
   IndentDto getPrevousDemandQuantityAndPreviousSupplyDate(Integer itemid,Integer manufactureid,Integer measurementId,Integer specificationId);
   
   ResponseEntity<ResponseStatus> deleteIndentChildStatusFlag(Integer id);
   
	ResponseEntity<ResponseStatus> updateIndentChildQuantity(Float itemQuantity,Integer id);
	
}
