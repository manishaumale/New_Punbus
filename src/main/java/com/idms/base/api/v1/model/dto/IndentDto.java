package com.idms.base.api.v1.model.dto;

import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MasterStatus;
import com.idms.base.dao.entity.TransportUnitMaster;

import lombok.Data;

@Data
public class IndentDto {
	private Integer id;
	private TransportUnitMaster transportUnit;
	private DepotMaster depot;

	private Float itemQuantity;
	private String itemType;
	private String measurementUnitName;
	private String manufacture;
	private String itemSpecification;
	private String itemName;

	private String indentRaisedOn;

	private String targetDateToReceive;

	private Float previoueDemandedQuantity;

	private String previousSuppliedDate;
	private String justification;

	private String indentDate;

	private String indentRaisedBy;

	private String identNumber;

	private MasterStatus indentStatus;

	private String actiondate;
	private String actionBy;

	private Integer itemId;
	private Integer manufactureId;
	private Integer measurementId;
	private Integer specificationId;
	
	private Integer itemNameId;
	
	private Integer indentChildId; 

}
