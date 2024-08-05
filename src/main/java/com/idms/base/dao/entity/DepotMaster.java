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
import javax.persistence.Transient;

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
@Table(name = "mst_depot")
@EqualsAndHashCode(callSuper = true)
public class DepotMaster extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "depotId")
	private Integer id;

	@Column(length = 50)
	private String depotName;
	
	@Column(length = 500)
	private String depotAddress;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = StateMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "stateId", nullable = false)
	private StateMaster state;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "cityId", nullable = false)
	private CityMaster city;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = DepotTransportUnit.class, mappedBy = "depotMaster", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<DepotTransportUnit> depotTransportList;

	@Column(length = 10, unique = true)
	private String depotCode;
	
	@Column(length = 8)
	private String pinCode;
	
	@Column(length = 50)
	private String fuelLevel;
	
	@Transient
	private List<TransportDto> tranposrtList;

}
