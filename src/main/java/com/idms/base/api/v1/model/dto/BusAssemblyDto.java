package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Bus Assembly Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusAssemblyDto extends BaseEntityDto{

    /**
	 * @author Hemant Makkar
	 */
	
	@ApiModelProperty(notes = "Bus Assembly Id", accessMode = AccessMode.READ_ONLY)
    private Integer id;
    
	@ApiModelProperty(notes = "Bus Part Model")
	private PartsMasterDto part;
	
    private String maker;
	
	private String partNumber;
	
	@ApiModelProperty(notes = "Bus Master Model")
	@JsonIgnore
	private BusMasterDto bus;
    

}
