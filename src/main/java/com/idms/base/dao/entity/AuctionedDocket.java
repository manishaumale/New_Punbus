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
import javax.persistence.Transient;
import javax.transaction.Transactional;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "auctioned_docket_details")
@EqualsAndHashCode(callSuper = true)
public class AuctionedDocket extends BaseEntity {
	
	private static final long serialVersionUID = -1617225283283343752L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docketId")
    private Integer id;
	
	private String docketNumber;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = AucDocketTyreAssociation.class, mappedBy = "docket", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<AucDocketTyreAssociation> tyreLists;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportId")
	private TransportUnitMaster transport;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	private Date auctionDate;
	
	private String bidderName;
	
	private Double bidAmount;
	
	@Transient
	private String dpCode;

}
