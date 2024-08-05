package com.idms.base.api.v1.model.dto;

import com.idms.base.dao.entity.ItemNameMaster;
import com.idms.base.dao.entity.ItemTypeMaster;

import lombok.Data;

@Data
public class MeasurementDto {
	private Integer id;

	private String name;

	private ItemTypeMaster itemType;

	private ItemNameMaster itemNameMaster;
	
	
	
}
