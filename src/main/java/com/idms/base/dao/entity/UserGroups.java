package com.idms.base.dao.entity;

import java.sql.Timestamp;

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
@Table(name = "user_groups")
@EqualsAndHashCode(callSuper = true)
public class UserGroups extends BaseEntity {
	
	private static final long serialVersionUID = 1393581180781471693L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "transportId", nullable = false)
	private TransportUnitMaster transportUnit;

}
