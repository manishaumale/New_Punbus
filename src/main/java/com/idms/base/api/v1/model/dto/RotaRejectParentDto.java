package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Rota Reject Parent Dto")
public class RotaRejectParentDto {
	
    private Integer rotaId; 
	
	private List<RotaRejectChildDto> rotaChildList;

}
