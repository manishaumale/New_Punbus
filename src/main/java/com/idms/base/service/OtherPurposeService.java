package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.OtherPurposeFormLoadDto;
import com.idms.base.dao.entity.DieselIssuedForOtherPurpose;
import com.idms.base.support.persist.ResponseStatus;

public interface OtherPurposeService {

	OtherPurposeFormLoadDto otherPurposeDataOnLoad(String dpCode);

	ResponseEntity<ResponseStatus> saveOtherPurpose(DieselIssuedForOtherPurpose otherPurpose);

	List<DieselIssuedForOtherPurpose> listOfAllOtherPurpose(String dpCode);

}
