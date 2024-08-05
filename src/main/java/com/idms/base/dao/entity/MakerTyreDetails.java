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
@Table(name = "mst_maker_tyre_details")
@EqualsAndHashCode(callSuper = true)
public class MakerTyreDetails extends BaseEntity {
	
	private static final long serialVersionUID = -7219402313906922524L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tyreMakerId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaker.class)
	@JoinColumn(name = "makerId")
	private TyreMaker maker;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreType.class)
	@JoinColumn(name = "tyreTypeId")
	private TyreType tyreType;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreSize.class)
	@JoinColumn(name = "tyreSizeId")
	private TyreSize tyreSize;
	
	private Integer expectedLife;
	
	private double tyreCost; 

}
