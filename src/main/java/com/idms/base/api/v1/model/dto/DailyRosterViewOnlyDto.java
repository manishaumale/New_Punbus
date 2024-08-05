package com.idms.base.api.v1.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class DailyRosterViewOnlyDto extends BaseEntityDto {
	
	
	private String routeName;
	
	private String busNumber;
	
	private String busType;
	
	private String driverNo;
	
	private String scheduledKms;
	
	private String deadKms;
	
	private String plainKms;
	
	private String hillKms;
	
	private String grossKms;
	
	private String vtsKms;
	
	private String kmplAsPerGross;
	
	private String kmplAsPerVTS;
	
	private String kmplAsPerScheduled;
	
	private Integer routeId;
	
	private Integer busId;
	
	private Float dieselFromOutside;
	
	private String dieselIssued;
	
	private Integer tripId;
	
	private Integer rotaId;
	
	private Integer totalActualKms;
	
}
