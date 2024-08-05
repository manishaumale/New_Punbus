package com.idms.base.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.ReadingMasterDto;
import com.idms.base.api.v1.model.dto.RecieveDieselFormLoadDto;
import com.idms.base.api.v1.model.dto.TankCurrentValAndCapacityDto;
import com.idms.base.dao.entity.RecieveDieselSupplyMaster;
import com.idms.base.support.persist.ResponseStatus;

public interface RecieveDieselSupplyService {

	ResponseEntity<ResponseStatus> saveRecieveDieselMaster(RecieveDieselSupplyMaster recieveDieselSupplyMaster);

	List<RecieveDieselSupplyMaster> listOfAllRecieveDieselMaster();

	RecieveDieselFormLoadDto recieveDieselFormOnLoad(String depotCode);

	ResponseEntity<ResponseStatus> updatedeleteReceiveDieselMasterIsDeletedFlag(Integer id, Boolean flag);

	ResponseEntity<ResponseStatus> updateReceiveDieselMasterStatusFlag(Integer id, Boolean flag);

	TankCurrentValAndCapacityDto fetchCurrValAndCapacity(Integer tankId);

	List<ReadingMasterDto> readingByFuelTank(Integer tankId);

	RecieveDieselFormLoadDto fetchFuelTankListAndDu(Integer transportId);

	ResponseEntity<ResponseStatus> validateOnchangeOfReadingMaster(Integer tankId, Integer readingId);

}
