package com.idms.base.service;

import com.idms.base.dao.entity.VTSData;
import com.idms.base.support.persist.ResponseStatus;

public interface VTSDataService {
	
	ResponseStatus saveVTSData(VTSData vtsData);
	
}
