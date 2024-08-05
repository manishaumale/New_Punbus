package com.idms.base.api.v1.model.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class AttendanceReportDto {
private String driverCodeOrconductorCode;
private String driverNameorconductorName;
private String fromDate;
private String toDate;
private String reason;
}
