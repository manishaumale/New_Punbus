package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data  
@Entity 
@Table(name = "mst_concession_percentage")
@EqualsAndHashCode(callSuper = true)
public class ConcessionPercentage extends BaseEntity {
	
	private static final long serialVersionUID = 203854578266993762L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
	private String name;

}
