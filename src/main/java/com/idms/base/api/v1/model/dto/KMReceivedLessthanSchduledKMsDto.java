package com.idms.base.api.v1.model.dto;

import lombok.Data;

@Data
public class KMReceivedLessthanSchduledKMsDto {

	private String routeName;

	private String scheduledKms;

	private String receivedKMs;

	private String difference;

	private String reason;

	private String nonScheduleKm;
	
	private String vtsKms;

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getScheduledKms() {
		return scheduledKms;
	}

	public void setScheduledKms(String scheduledKms) {
		this.scheduledKms = scheduledKms;
	}

	public String getReceivedKMs() {
		return receivedKMs;
	}

	public void setReceivedKMs(String receivedKMs) {
		this.receivedKMs = receivedKMs;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNonScheduleKm() {
		return nonScheduleKm;
	}

	public void setNonScheduleKm(String nonScheduleKm) {
		this.nonScheduleKm = nonScheduleKm;
	}

}
