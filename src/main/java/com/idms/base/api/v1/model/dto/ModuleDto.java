package com.idms.base.api.v1.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.idms.base.dao.entity.Module;
import com.idms.base.dao.entity.RoleModuleAccess;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@ApiModel(description = "Module Model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleDto extends BaseEntityDto {
	
	private Integer id;
	
	private String moduleName;
	
	private String moduleUrl;
	
	private Integer displayOrder;
	
	private List<ModuleDto> childModule;

}
