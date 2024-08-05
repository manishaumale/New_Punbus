package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "Credential Model")
@NoArgsConstructor
public class CredentialDto {
	
	private String type;
	
	private String value;
	
	private boolean temporary;

}
