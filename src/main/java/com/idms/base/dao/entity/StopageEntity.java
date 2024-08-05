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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "mst_stopage")
@EqualsAndHashCode(callSuper = true)
public class StopageEntity extends BaseEntity {
	
	private static final long serialVersionUID = -3393142639825651688L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stopageId")
    private Integer id;
	
	private String stopageName;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = CityMaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "cityId", nullable = true)
	private CityMaster city;
	
}
