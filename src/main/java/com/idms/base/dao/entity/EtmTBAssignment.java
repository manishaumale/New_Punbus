package com.idms.base.dao.entity;

import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data  
@Entity 
@Table(name = "etm_assignment")
@EqualsAndHashCode(callSuper = true)
public class EtmTBAssignment extends BaseEntity {
	
	private static final long serialVersionUID = 8199700458933501143L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etmAssignmentId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ETMMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "etmId")
	private ETMMaster etmId;
	
	private String wayBillNo;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "ticketBoxId")
	private TicketBoxMaster ticketBoxId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ConductorMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "conductorId")
	private ConductorMaster conductorId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = RouteMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "routeId")
	private RouteMaster routeId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = IssueEtmTicketBoxEntity.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "iet_id")
	private IssueEtmTicketBoxEntity issueEtmId;
	
	private Date etmAssignmentDate;
	
	private Date etmSubmitDate;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean etmSubmitStatus;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean tbSubmitStatus;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = EtmTBTripsAssociation.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<EtmTBTripsAssociation> associationList;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = ConductorEtmBookingDetails.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	private ConductorEtmBookingDetails conductorBookingDetails;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = BusStandBookingDtls.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	private BusStandBookingDtls busStandBookingDtls;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = TripExpenses.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	private TripExpenses tripExpenses;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = PassengerClassification.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	private PassengerClassification passengerClassification;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ConcessionDtls.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<ConcessionDtls> concessionDtls;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = EarningFromETM.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	private EarningFromETM earning;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ETMStatewiseCollection.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<ETMStatewiseCollection> etmStatewiseDtls;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = EtmTripDtls.class, mappedBy = "assignment", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<EtmTripDtls> etmTripDtls;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = EtmTripTotalDtls.class, mappedBy = "assignment", cascade = CascadeType.MERGE)
	private EtmTripTotalDtls etmTripTotalDtls;
	
	@Transient
	private Boolean isFaulty;
	
	private String serviceId;
	
}
