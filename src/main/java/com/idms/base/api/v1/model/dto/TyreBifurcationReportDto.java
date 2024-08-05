
package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Tyre Bifurcation Report")
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TyreBifurcationReportDto {
	
	
	@ApiModelProperty(notes = "Condemn Without Resole")
	private String  condemnWithout;
	
	@ApiModelProperty(notes = "Tyre Resoled Once")
	private String  tyreResoledOnce;
	
	
	@ApiModelProperty(notes = "Tyre Resoled Twice")
	private String  tyreResoledTwice;
	
	@ApiModelProperty(notes = "Tyre Resoled Thrice")
	private String  tyreResoledThrice;
	
	@ApiModelProperty(notes = "Total Tyres")
	private String  total;
	
}


