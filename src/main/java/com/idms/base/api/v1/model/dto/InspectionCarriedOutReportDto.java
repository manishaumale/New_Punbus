package com.idms.base.api.v1.model.dto;

public class InspectionCarriedOutReportDto {

	private String depo;

	private String inspectionDueDate;

	private String statusWithDate;

	private String nextDueDate;

	private String authorityName;

	public String getDepo() {
		return depo;
	}

	public void setDepo(String depo) {
		this.depo = depo;
	}

	public String getInspectionDueDate() {
		return inspectionDueDate;
	}

	public void setInspectionDueDate(String inspectionDueDate) {
		this.inspectionDueDate = inspectionDueDate;
	}

	public String getStatusWithDate() {
		return statusWithDate;
	}

	public void setStatusWithDate(String statusWithDate) {
		this.statusWithDate = statusWithDate;
	}

	public String getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(String nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

}
