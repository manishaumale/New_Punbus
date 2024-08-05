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
@Table(name = "submit_concession_ticket")
@EqualsAndHashCode(callSuper = true)
public class SubmitConcessionTicketEntity extends BaseEntity  {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "c_id")
	private Integer id;
	
	private Integer totalTickets;
	
	private Integer netAmount;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = SubmitEtmTicketBoxEntity.class)
	@JoinColumn(name = "set_id")
	private SubmitEtmTicketBoxEntity submitEtmTicketBoxEntity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConcessionTicketEntity.class)
	@JoinColumn(name = "concession_ticket_id")
	private ConcessionTicketEntity concessionTicketEntity;

}
