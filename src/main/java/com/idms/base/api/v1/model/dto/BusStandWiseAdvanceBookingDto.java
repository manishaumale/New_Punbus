package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class BusStandWiseAdvanceBookingDto {

	private String depot;
	private String routeName;

	private String busStandName;
	private String advanceBookingAmount;
	private String cashDepositedAmount;

	private String freeTicket;

	private String concessionTicket;

	private String total;

	private String busType;

	private String busNo;
	
	private String sum;
	
	private String transportUnit;
	private String passType;
	private String discount;
	private String amount;
	private String conductorName;
	private String conductorCode;
	private String transportUnit1;
	private String depot1;
	private String fromDate;
	private String toDate;
	private String busCount;

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getBusStandName() {
		return busStandName;
	}

	public void setBusStandName(String busStandName) {
		this.busStandName = busStandName;
	}

	public String getAdvanceBookingAmount() {
		return advanceBookingAmount;
	}

	public void setAdvanceBookingAmount(String advanceBookingAmount) {
		this.advanceBookingAmount = advanceBookingAmount;
	}

	public String getCashDepositedAmount() {
		return cashDepositedAmount;
	}

	public void setCashDepositedAmount(String cashDepositedAmount) {
		this.cashDepositedAmount = cashDepositedAmount;
	}

	public String getFreeTicket() {
		return freeTicket;
	}

	public void setFreeTicket(String freeTicket) {
		this.freeTicket = freeTicket;
	}

	public String getConcessionTicket() {
		return concessionTicket;
	}

	public void setConcessionTicket(String concessionTicket) {
		this.concessionTicket = concessionTicket;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

}
