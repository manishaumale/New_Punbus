package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class SupplyReceivedReportDto {

	private String depotName;
	private String quantityReceived;
	private String dateOfSupplyReceived;
	private String supplyReceived;
	private String invoiceNo;
	private String rateLiterReceived;
	private String placeOfDispatch;

	private String journeyStartTime;

	private String supplyReceivetime;

}
