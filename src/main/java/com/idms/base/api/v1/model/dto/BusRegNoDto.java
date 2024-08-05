package com.idms.base.api.v1.model.dto;

public class BusRegNoDto {
	
	private String busNo;
	
	private Integer busId;
	
	private String transportName;

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public Integer getBusId() {
		return busId;
	}

	public void setBusId(Integer busId) {
		this.busId = busId;
	}
	
	
	

}
