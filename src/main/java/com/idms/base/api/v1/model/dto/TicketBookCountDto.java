package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "TicketBookCountDto Model")
public class TicketBookCountDto {
	
	private Integer id;
	
	private Integer tbCount;
	
	public TicketBookCountDto(Integer id, Integer tbCount) {
		super();
		this.id=id;
		this.tbCount=tbCount;
	}

}
