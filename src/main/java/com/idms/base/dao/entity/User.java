package com.idms.base.dao.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
	
	private static final long serialVersionUID = 2245753358108667595L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer pk;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Role.class)
    @JoinColumn(name = "roleId", nullable = false)
	private Role role;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
    @JoinColumn(name = "depotId", nullable = true)
	private DepotMaster depot;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = UserGroups.class, mappedBy = "user", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<UserGroups> tpGroups;
}
