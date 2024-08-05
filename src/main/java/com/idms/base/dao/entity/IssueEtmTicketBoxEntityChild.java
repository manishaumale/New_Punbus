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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "issue_etm_ticket_box_child")
@EqualsAndHashCode(callSuper = true)
public class IssueEtmTicketBoxEntityChild extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iet_child_id")
	private Integer id;
	
	
	@Column(name="startingserial_no")
	private Long startingSerialNo;
	
	@Column(name="endingserial_no")
	private Long endingSerialNo;
	
	private Long amount;
	
	private Long currentLastNo;
	
	@Column(name = "denomination")
	private Integer denomination;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class)
	@JoinColumn(name = "transportId")
	private TransportUnitMaster transportUnitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = IssueEtmTicketBoxEntity.class)
	@JoinColumn(name = "issue_etm_ticket_Box_entity")
	private IssueEtmTicketBoxEntity issueEtmTicketBoxEntity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxManagementEntity.class)
	@JoinColumn(name = "ticket_Box_manage_entity")
	private TicketBoxManagementEntity ticketBoxManagementEntity;
	
	
}

