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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "mst_tkt_master_stock")
@EqualsAndHashCode(callSuper = true)
public class MasterStock extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "masterStockId")
	private Integer id;

	private String bundleNumber;
	
	@Column(name = "startSerial_no")
	private Integer startSrNo;

	@Column(name = "endSerial_no")
	private Integer endSrNo; 

	@OneToMany(fetch = FetchType.LAZY, targetEntity = MasterStockTicketDtls.class, mappedBy = "masterStock", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<MasterStockTicketDtls> stockSerialNoDetails;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CentralTicketStock.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "centeralStockId", nullable = false)
	private CentralTicketStock centeralStock;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isAssigned;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
}
