package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class DepotWiseDieselStockReportDto {

	private String depot;
	
	private String issuedDiesel;

	private String availableDieselStock;

	private String valueOfDieselStock;

	private String availableMobilOilStock;

	private String valueOfMobilStock;

	private String advancePaymentInRs;
	
	private String stockDate;

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getAvailableDieselStock() {
		return availableDieselStock;
	}

	public void setAvailableDieselStock(String availableDieselStock) {
		this.availableDieselStock = availableDieselStock;
	}

	public String getValueOfDieselStock() {
		return valueOfDieselStock;
	}

	public void setValueOfDieselStock(String valueOfDieselStock) {
		this.valueOfDieselStock = valueOfDieselStock;
	}

	public String getValueOfMobilStock() {
		return valueOfMobilStock;
	}

	public void setValueOfMobilStock(String valueOfMobilStock) {
		this.valueOfMobilStock = valueOfMobilStock;
	}

	public String getAvailableMobilOilStock() {
		return availableMobilOilStock;
	}

	public void setAvailableMobilOilStock(String availableMobilOilStock) {
		this.availableMobilOilStock = availableMobilOilStock;
	}

	public String getAdvancePaymentInRs() {
		return advancePaymentInRs;
	}

	public void setAdvancePaymentInRs(String advancePaymentInRs) {
		this.advancePaymentInRs = advancePaymentInRs;
	}

}
