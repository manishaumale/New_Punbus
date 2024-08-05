package com.idms.base.api.v1.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReviewMasterDto {

	private ReviewAverageDto averages;
	
	private List<ReviewDto> reviewDto;
}
