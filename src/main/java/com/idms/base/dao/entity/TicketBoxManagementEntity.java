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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Entity
@Table(name = "manage_ticket_box")
public class TicketBoxManagementEntity extends BaseEntity {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticketbox_mgmt_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class)
	@JoinColumn(name = "transportId")
	private TransportUnitMaster transportUnitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxMaster.class)
	@JoinColumn(name = "ticketBoxId")
	private TicketBoxMaster ticketBoxNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DenominationEntity.class)
	@JoinColumn(name = "denominationId", nullable = false)
	private DenominationEntity denomination;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxManagementParentEntity.class)
    @JoinColumn(name="ticketBoxManagementParent_id", nullable=false)
    private TicketBoxManagementParentEntity ticketBoxManagementParent;
	
	@Column(name="startingserial_no")
	private Long startingSerialNo;
	
	@Column(name="endingserial_no")
	private Long endingSerialNo;
	
	private Long amount;
	
	private Long currentLastNo;
	
	private Long currentamount;

	private String bookSeries;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CentralTicketStock.class)
	@JoinColumn(name = "centeralStockId", nullable = true)
	private CentralTicketStock centralTicket;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MasterStockTicketDtls.class)
	@JoinColumn(name = "masterStockId", nullable = true)
	private MasterStockTicketDtls masterStock;

	private Boolean isBookletChecked;
}
