package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class KMPLTicketReportDto {

	private String conductorName;

	private String conductorNo;

	private String routeName;

	private String amountOnPaperTicketUsed;

	private String lateInHours;
	
	private String depoName;
	private String date;
	private String transportUnit;
	private String actualTime;
	private String ScheduledTime;
	

	public String getConductorName() {
		return conductorName;
	}

	public void setConductorName(String conductorName) {
		this.conductorName = conductorName;
	}

	public String getConductorNo() {
		return conductorNo;
	}

	public void setConductorNo(String conductorNo) {
		this.conductorNo = conductorNo;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getAmountOnPaperTicketUsed() {
		return amountOnPaperTicketUsed;
	}

	public void setAmountOnPaperTicketUsed(String amountOnPaperTicketUsed) {
		this.amountOnPaperTicketUsed = amountOnPaperTicketUsed;
	}

	public String getLateInHours() {
		return lateInHours;
	}

	public void setLateInHours(String lateInHours) {
		this.lateInHours = lateInHours;
	}

}
