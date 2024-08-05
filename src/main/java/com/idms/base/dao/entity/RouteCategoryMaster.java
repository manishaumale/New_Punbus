package com.idms.base.dao.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "mst_route_category")
@EqualsAndHashCode(callSuper = true)
public class RouteCategoryMaster extends BaseEntity {

    /**
	 * @author Hemant Makkar
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routeCategoryId")
    private Integer id;
    
    private String routeCategoryName;

}
