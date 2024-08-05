package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "All Rota List View Dto")
public class AllRotaListViewDto {
	private Integer scheduledKms;
	private List<DailyRoasterAutoDto> autoList;
	private List<DailyRoasterAutoDto> specialList;
	private List<DailyRoasterAutoDto> manualList;
	private List<MarkSpareListDto> spareList;
}
