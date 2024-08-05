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
@Table(name = "submit_etm_ticket_box")
@EqualsAndHashCode(callSuper = true)
public class SubmitEtmTicketBoxEntity extends BaseEntity  {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "set_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = IssueEtmTicketBoxEntity.class)
	@JoinColumn(name = "iet_id")
	private IssueEtmTicketBoxEntity issueEtmTicketBoxEntity;
	
	@OneToMany(mappedBy="submitEtmTicketBoxEntity",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<SubmitConcessionTicketEntity> concessionTicketList;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = SubmitEtmTicketBoxChild.class, mappedBy = "submitEtmTicketBox", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<SubmitEtmTicketBoxChild> submitEtmChildList;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = TicketBoxManagementEntityHistory.class, mappedBy = "submitEtmTicketBoxEntity", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<TicketBoxManagementEntityHistory> ticketBoxHistoryList;
	
	@Column(name = "manualEntry", columnDefinition = "boolean default false")
	private Boolean manualEntry;
	
	private String wayBillNo;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = EtmTBAssignment.class)
	@JoinColumn(name = "etm_assignment_id")
	private EtmTBAssignment etmAssignmentId;
	
	private Boolean isFaulty;
	
}
