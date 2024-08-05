package com.idms.base.api.v1.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Access Model")
public class AccessDto {
	
	private boolean manageGroupMembership;
	
	private boolean view;
	
	private boolean mapRoles;
	
	private boolean impersonate;
	
	private boolean manage;

}
