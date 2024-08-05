package com.idms.base.dao.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity 
@Table(name = "mst_module")
@EqualsAndHashCode(callSuper = true)
public class Module extends BaseEntity {
	
	
	private static final long serialVersionUID = 5360362734488820464L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moduleId")
    private Integer id;
	
	private String moduleName;
	
	private String moduleUrl;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Module.class)
	@JoinColumn(name = "parentId", nullable = true)
	private Module parentId;
	
	private Integer displayOrder;
	
	@OneToMany(mappedBy = "module")
	private List<RoleModuleAccess> modules;
	
	@Transient
	private List<Module> childModule;
	
	
}
