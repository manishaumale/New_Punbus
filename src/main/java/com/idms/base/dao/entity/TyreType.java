package com.idms.base.dao.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "mst_tyre_type")
@EqualsAndHashCode(callSuper = true)
public class TyreType extends BaseEntity {
	
	private static final long serialVersionUID = -2804328746187339721L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tyreTypeId")
    private Integer id;
	
	private String name;
	
	@OneToMany(mappedBy = "tyreType")
	List<MakerTyreDetails> makerDetailList;
	

}
