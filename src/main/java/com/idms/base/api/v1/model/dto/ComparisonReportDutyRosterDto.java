package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class ComparisonReportDutyRosterDto {
     
	
	private String transportName;
	private String depotName;
	private String dutyRosterKMs;
	private String dpaKms;
	private String busKms;
	private String driverKms;
	private String conductorKms;
	private String routeKms;
	private String routeName;
	private String busRegisterNumber;
	private String tripCount;
	private String driverName;
	private String conductorName;
	

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getDutyRosterKMs() {
		return dutyRosterKMs;
	}

	public void setDutyRosterKMs(String dutyRosterKMs) {
		this.dutyRosterKMs = dutyRosterKMs;
	}

	public String getDpaKms() {
		return dpaKms;
	}

	public void setDpaKms(String dpaKms) {
		this.dpaKms = dpaKms;
	}

	public String getBusKms() {
		return busKms;
	}

	public void setBusKms(String busKms) {
		this.busKms = busKms;
	}

	public String getDriverKms() {
		return driverKms;
	}

	public void setDriverKms(String driverKms) {
		this.driverKms = driverKms;
	}

	public String getConductorKms() {
		return conductorKms;
	}

	public void setConductorKms(String conductorKms) {
		this.conductorKms = conductorKms;
	}

	public String getRouteKms() {
		return routeKms;
	}

	public void setRouteKms(String routeKms) {
		this.routeKms = routeKms;
	}

}
