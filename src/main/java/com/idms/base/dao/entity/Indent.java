package com.idms.base.dao.entity;

import java.util.Date;
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

@Entity
@Table(name = "mst_indent")
@EqualsAndHashCode(callSuper = true)
@Data
public class Indent extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "indent_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportUnit")
	private TransportUnitMaster transportUnit;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depot")
	private DepotMaster depot;
	
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = IndentChildEntity.class,mappedBy="indentObj",cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<IndentChildEntity> indentchild;
	
	private Date indentDate;

	private String indentRaisedBy;

	private String identNumber;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStatus.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "indentstatus")
	private MasterStatus masterstatus;
	
	private Boolean isDeleted;

	private Date actiondate;
	private String actionBy;
	
	private Integer count;

}
