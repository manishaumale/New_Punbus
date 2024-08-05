package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.dao.entity.DipReadingMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface DipReadingMasterService {

	ResponseEntity<ResponseStatus> saveDipReadingMaster(DipReadingMaster dipReadingMaster);

	List<DipReadingMaster> listOfAllDipReadingMaster();

}
