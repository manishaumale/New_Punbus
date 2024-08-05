package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class FuelDispenWiseFuelIssuReportResModel {

	private String depot;

	private String state;

	private String denomination;

	private String startingSrNo;

	private String endSerialNo;

	private String noBook;

	private String noOfBooks;
	private String valueInRs;
	private String seriesNumber;
	
	private String recievedDate;
	private String transportUnit;

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getStartingSrNo() {
		return startingSrNo;
	}

	public void setStartingSrNo(String startingSrNo) {
		this.startingSrNo = startingSrNo;
	}

	public String getEndSerialNo() {
		return endSerialNo;
	}

	public void setEndSerialNo(String endSerialNo) {
		this.endSerialNo = endSerialNo;
	}

	public String getNoBook() {
		return noBook;
	}

	public void setNoBook(String noBook) {
		this.noBook = noBook;
	}

	public String getValueInRs() {
		return valueInRs;
	}

	public void setValueInRs(String valueInRs) {
		this.valueInRs = valueInRs;
	}

	public String getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(String recievedDate) {
		this.recievedDate = recievedDate;
	}
	
	

}
