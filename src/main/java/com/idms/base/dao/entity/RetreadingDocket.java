package com.idms.base.dao.entity;

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
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "retreading_docket_details")
@EqualsAndHashCode(callSuper = true)
public class RetreadingDocket extends BaseEntity {
	
	private static final long serialVersionUID = 7984522347387841947L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docketId")
    private Integer id;
	
	private String docketNumber;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = DocketTyreAssociation.class, mappedBy = "docket", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<DocketTyreAssociation> tyreLists;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isClosed;
	
	@Transient
	private String dpCode;
	
	@Transient
	private Integer tyreCount;
	
	

}
