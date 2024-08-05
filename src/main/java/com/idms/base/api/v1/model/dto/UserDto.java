package com.idms.base.api.v1.model.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.Role;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "User Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

	private String id;
	
	private Timestamp createdTimestamp;
	
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private boolean enabled;
	
	private boolean totp;
	
	private boolean emailVerified;
	
	private Integer notBefore;
	
	private Object disableableCredentialTypes[];
	
	private Object requiredActions[];
	
	private AttributeDto attributes;
	
	private AccessDto access;
	
	private String groups[];
	
	private CredentialDto credentials[];
	
	private DepotMasterDto depot;
	
	private Role role;
	
	
}
