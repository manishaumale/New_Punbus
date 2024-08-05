package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Entity
@Table(name = "manage_ticket_box_history")
public class TicketBoxManagementEntityHistory extends BaseEntity{

	/**
	 * @author 1000956
	 */
	private static final long serialVersionUID = -8803834607278202848L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticketbox_mgmt_his_id")
	private Integer id;
	
	@Column(name = "ticketbox_mgmt_id")
	private Integer ticketBoxMngId;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class)
	@JoinColumn(name = "transportId")
	private TransportUnitMaster transportUnitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxMaster.class)
	@JoinColumn(name = "ticketBoxId")
	private TicketBoxMaster ticketBoxNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DenominationEntity.class)
	@JoinColumn(name = "denominationId")
	private DenominationEntity denomination;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxManagementParentEntity.class)
    @JoinColumn(name="ticketBoxManagementParent_id")
    private TicketBoxManagementParentEntity ticketBoxManagementParent;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = SubmitEtmTicketBoxEntity.class)
    @JoinColumn(name="submitEtmTicketBoxEntity")
	private SubmitEtmTicketBoxEntity submitEtmTicketBoxEntity;
	
	@Column(name="startingserial_no")
	private Long startingSerialNo;
	
	@Column(name="endingserial_no")
	private Long endingSerialNo;
	
	@Column
	private Long amount;
	
	@Column
	private Long currentLastNo;
	
	
}
