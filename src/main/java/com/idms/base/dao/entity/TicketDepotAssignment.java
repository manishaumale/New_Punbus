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
@Table(name = "ticket_depot_assignment")
@EqualsAndHashCode(callSuper = true)
public class TicketDepotAssignment extends BaseEntity {	
	
	private static final long serialVersionUID = -2349661597116716486L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "assignment_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depot;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportId")
	private TransportUnitMaster transport;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStockTicketDtls.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "ticketDtlsId")
	private MasterStockTicketDtls ticketDtls;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isAssigned;

}
