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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "master_stock_ticket_dtls")
@EqualsAndHashCode(callSuper = true)
public class MasterStockTicketDtls extends BaseEntity {

	private static final long serialVersionUID = 7450894316726811108L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_dtls_id")
	private Integer id;

	@Column(name = "startSerial_no")
	private Integer startSrNo;

	@Column(name = "endSerial_no")
	private Integer endSrNo;
	
	private String ticketBookNo;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStock.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "stockId", nullable = false)
	private MasterStock masterStock;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DenominationEntity.class)
	@JoinColumn(name = "denominationId", nullable = false)
	private DenominationEntity denomination;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isAssigned;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = TicketDepotAssignment.class, mappedBy = "ticketDtls", cascade = CascadeType.ALL)
	private TicketDepotAssignment ticketDepotAssignment;

}
