package com.idms.base.api.v1.model.dto;

import com.idms.base.dao.entity.ItemNameMaster;
import com.idms.base.dao.entity.ItemTypeMaster;
import com.idms.base.dao.entity.ManufactureEntity;
import com.idms.base.dao.entity.MeasurementEntity;

import lombok.Data;

@Data
public class SpecificationDto {

	private Integer id;

	private String name;

	private ItemTypeMaster itemType;

	private ItemNameMaster itemNameMaster;

	private MeasurementEntity measurementEntity;

	private ManufactureEntity manufactureEntity;
	

	

}
