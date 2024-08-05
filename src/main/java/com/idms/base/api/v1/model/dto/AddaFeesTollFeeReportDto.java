package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class AddaFeesTollFeeReportDto {
	
	private String depotName;
	
	private String routeName;
	
	private String addaFees;
	
	private String tollFees;
	
	private String totalFees;
	private String transportUnit;
	private String date;
	
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getAddaFees() {
		return addaFees;
	}
	public void setAddaFees(String addaFees) {
		this.addaFees = addaFees;
	}
	public String getTollFees() {
		return tollFees;
	}
	public void setTollFees(String tollFees) {
		this.tollFees = tollFees;
	}
	public String getTotalFees() {
		return totalFees;
	}
	public void setTotalFees(String totalFees) {
		this.totalFees = totalFees;
	}
	
	
	

}
