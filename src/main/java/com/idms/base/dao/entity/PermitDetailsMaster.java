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
import javax.persistence.Table;

import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.RouteComplexityDto;
import com.idms.base.api.v1.model.dto.RoutePermitMasterDto;
import com.idms.base.api.v1.model.dto.RouteRotationDto;
import com.idms.base.api.v1.model.dto.RouteTypeDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.StateWiseRouteKmsDto;
import com.idms.base.api.v1.model.dto.TotalNightsDto;
import com.idms.base.api.v1.model.dto.TransportDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_permit_details")
@EqualsAndHashCode(callSuper = true)
public class PermitDetailsMaster extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "permitId")
	private Integer id;

	private String permitNumber;
	
	private String permitIssuingAuthority;
	
	private Date issueDate;
	
	private Date validUpTo;
	
	@Column(name = "permitName", length=20)
	private String permitName;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fromStateId", nullable = false)
	private StateMaster fromState;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "fromCityId", nullable = false)
	private CityMaster fromCity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "toStateId", nullable = false)
	private StateMaster toState;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "toCityId", nullable = false)
	private CityMaster toCity;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyperMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busTypeId")
	private BusTyperMaster busTyperMaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "depotId")
	private DepotMaster depotMaster;
	
	private Integer totalTrips;
	
	private Integer totalKms;
	
	private Integer plainKms;
	
	private Integer hillKms;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ViaInformation.class, mappedBy = "permitDetailsMaster", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<ViaInformation> viaInfoList;
	
	@OneToMany(fetch = FetchType.EAGER, targetEntity = StateWiseKmMaster.class, mappedBy = "permitDetails", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<StateWiseKmMaster> stateWiseKmList;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "documentId")
	private Document document;
	
	private Boolean isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TransportUnitMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "transportId")
	private TransportUnitMaster transport;
	
}
