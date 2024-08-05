package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
@ApiModel(description = "Total Nights Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalNightsDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */

	
	@ApiModelProperty(notes = "Total Nights Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
    @ApiModelProperty(notes = "Night Detail")
    private String nightDetail;

	public TotalNightsDto(Integer id, String nightDetail) {
		super();
		this.id = id;
		this.nightDetail = nightDetail;
	}
}