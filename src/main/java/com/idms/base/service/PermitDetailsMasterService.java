package com.idms.base.service;

import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.idms.base.api.v1.model.dto.PermitDetailsFormDto;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface PermitDetailsMasterService {
	
	List<PermitDetailsMaster> getallPermitDetailsMaster(String dpCode);

	PermitDetailsFormDto permitDetailsMasterLoad(String dpCode);

	ResponseEntity<ResponseStatus> savePermitMaster(PermitDetailsMaster permitMaster, MultipartFile uploadFile);

	ResponseEntity<ResponseStatus> updatePermitMaster(Integer id, MultipartFile uploadFile, Date issueDate, Date validUpTo);

	ResponseEntity<ResponseStatus> updatePermitMasterStatusFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updatePermitMasterIsDeletedFlag(Integer id, Boolean flag);

}
