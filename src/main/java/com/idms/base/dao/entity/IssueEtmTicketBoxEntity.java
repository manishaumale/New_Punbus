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
@Table(name = "issue_etm_ticket_box")
@EqualsAndHashCode(callSuper = true)
public class IssueEtmTicketBoxEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iet_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class)
	@JoinColumn(name = "conductor_Id")
	private ConductorMaster conductorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxMaster.class)
	@JoinColumn(name = "ticketBox_Id")
	private TicketBoxMaster ticketBoxNumber;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ETMMaster.class)
	@JoinColumn(name = "etm_Id")
	private ETMMaster etmMaster;
	
	@Column(name = "waybill_no")
	private String wayBillNo;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DailyRoaster.class)
	@JoinColumn(name = "roaster_Id")
	private DailyRoaster dailyRoaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = OnlineBookingDetails.class)
	@JoinColumn(name = "online_booking_Id")
	private OnlineBookingDetails onlineBookingDetails;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxManagementParentEntity.class)
	@JoinColumn(name = "ticket_box_management_parent_Id")
	private TicketBoxManagementParentEntity ticketBoxManagementParentEntity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
	@JoinColumn(name = "depot_id")
	private DepotMaster depoMaster;
	
	@Column(name="condutor_status")
	private boolean condutorStatus;
	
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = IssueEtmTicketBoxEntityChild.class, mappedBy = "issueEtmTicketBoxEntity", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<IssueEtmTicketBoxEntityChild> issueEtmChildList;
	
	private Boolean manualEntry;
	

}
