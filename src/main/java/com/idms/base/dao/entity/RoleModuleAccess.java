package com.idms.base.dao.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity 
@Table(name = "mst_role_module_access")
@EqualsAndHashCode(callSuper = true)
public class RoleModuleAccess extends BaseEntity {
	
	
	private static final long serialVersionUID = 7189013968901621462L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moduleAccessId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Role.class)
	@JoinColumn(name = "role_fk", nullable = false)
	private Role role;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Module.class)
	@JoinColumn(name = "module_fk", nullable = false)
	private Module module;

}
