package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "RefreshTokenDto Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefreshTokenDto {
	
	String access_token;
	
	Integer refresh_expires_in;
	
	String refresh_token;
	
	String token_type;
	
	@JsonProperty("not-before-policy")
	String not_before_policy;
	
	String session_state;
	
	String scope;
	
	Integer expires_in;

}
