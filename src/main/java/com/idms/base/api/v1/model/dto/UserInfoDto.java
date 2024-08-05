package com.idms.base.api.v1.model.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "User Model")
public class UserInfoDto {

	String sub;

	List<String> roles;

	List<String> groups;

	String preferred_username;

	boolean email_verified;

	String depot;

	String token;

	String refreshToken;
	
	List<ModuleDto> modules;

	String name;

	String given_name;
	
	String family_name;
	
	String email;

}
