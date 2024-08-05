package com.idms.base.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.Module;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "RolesModuleAccess Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleModuleAccessDto {
	
	private Integer id;

	private ModuleDto module;
	
}
