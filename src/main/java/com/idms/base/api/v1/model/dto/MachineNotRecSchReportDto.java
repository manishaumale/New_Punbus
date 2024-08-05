package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class MachineNotRecSchReportDto {

	private String depot;

	private String machineNumber;

	private String scheduleArrival;

	private String actualArrived;

	private String lateInHours;

	private String machineFaulty;

	private String ticketBox;

	private Integer numberFrequency;
	private String transUnit;
	private String transUnit1;
	private String etmCount;
	private String fromDate;
	private String toDate;

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getMachineNumber() {
		return machineNumber;
	}

	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}

	public String getScheduleArrival() {
		return scheduleArrival;
	}

	public void setScheduleArrival(String scheduleArrival) {
		this.scheduleArrival = scheduleArrival;
	}

	public String getActualArrived() {
		return actualArrived;
	}

	public void setActualArrived(String actualArrived) {
		this.actualArrived = actualArrived;
	}

	public String getLateInHours() {
		return lateInHours;
	}

	public void setLateInHours(String lateInHours) {
		this.lateInHours = lateInHours;
	}

	public String getMachineFaulty() {
		return machineFaulty;
	}

	public void setMachineFaulty(String machineFaulty) {
		this.machineFaulty = machineFaulty;
	}

	public String getTicketBox() {
		return ticketBox;
	}

	public void setTicketBox(String ticketBox) {
		this.ticketBox = ticketBox;
	}

	public Integer getNumberFrequency() {
		return numberFrequency;
	}

	public void setNumberFrequency(Integer numberFrequency) {
		this.numberFrequency = numberFrequency;
	}

}
