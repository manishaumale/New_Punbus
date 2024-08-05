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
@Table(name = "mst_central_ticket_stock")
@EqualsAndHashCode(callSuper = true)
public class CentralTicketStock extends BaseEntity {
	
	private static final long serialVersionUID = 6767070630995843997L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "centralStockId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class)
	@JoinColumn(name = "transportId", nullable = true)
	private TransportUnitMaster transport;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class)
	@JoinColumn(name = "stateId", nullable = true)
	private StateMaster state;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DenominationEntity.class)
	@JoinColumn(name = "denominationId", nullable = true)
	private DenominationEntity denomination;
	
	@Column(name = "stateTax")
	private boolean stateTax;
	
	private String seriesNumber;
	
	@Column(name = "startSerial_no")
	private Integer startSrNo;

	@Column(name = "endSerial_no")
	private Integer endSrNo;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBookCount.class)
	@JoinColumn(name = "tbCountId", nullable = true)
	private TicketBookCount tbCount;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BookPerBundle.class)
	@JoinColumn(name = "bookId", nullable = true)
	private BookPerBundle book;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = MasterStock.class, mappedBy = "centeralStock", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<MasterStock> bundleStockDtls;
	
	

}
