package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity 
@Table(name = "mst_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = -5367432237942934875L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Integer id;
	
	@Column(name = "roleNames")
    private String name;
	
	@Column(length=50)
    private String roleCode;
	
	@Column(length=200)
	private String description;
	
	private String ids;
	
//	@OneToMany(mappedBy = "role")
//	@EqualsAndHashCode.Exclude
//	private List<RoleModuleAccess> modules;
}
