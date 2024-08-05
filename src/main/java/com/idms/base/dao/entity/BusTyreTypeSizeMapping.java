package com.idms.base.dao.entity;

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
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "mst_bus_tyre_type_size_mapping")
@EqualsAndHashCode(callSuper = true)
public class BusTyreTypeSizeMapping extends BaseEntity {
	
	private static final long serialVersionUID = -645701344775403630L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusTyperMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "busTypeId")
	private BusTyperMaster busType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreType.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreTypeId")
	private TyreType tyreType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreSize.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreSizeId")
	private TyreSize tyreSize;

	
}
