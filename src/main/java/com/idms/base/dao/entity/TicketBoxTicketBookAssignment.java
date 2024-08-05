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
@Table(name = "ticket_box_ticket_book_asgmt")
@EqualsAndHashCode(callSuper = true)
public class TicketBoxTicketBookAssignment extends BaseEntity {
	
	private static final long serialVersionUID = 6746657687920235101L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "ticketBoxId")
	private TicketBoxMaster ticketBox;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketDepotAssignment.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "ticketDepotAsgmtId")
	private TicketDepotAssignment ticketDepotAsgmt;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStockTicketDtls.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "ticketBookDtlsId")
	private MasterStockTicketDtls ticketBookDtls;
	
}
