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

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_tyre_size")
@EqualsAndHashCode(callSuper = true)
public class TyreSize extends BaseEntity {
	
	private static final long serialVersionUID = 6600776167756163048L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tyreSizeId")
    private Integer id;
	
	private String size;
	
	@OneToMany(mappedBy = "tyreSize")
	List<MakerTyreDetails> makerDetailList;

}
