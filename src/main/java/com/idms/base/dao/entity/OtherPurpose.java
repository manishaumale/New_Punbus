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
@Table(name = "mst_other_purpose")
@EqualsAndHashCode(callSuper = true)
public class OtherPurpose extends BaseEntity {
	
	private static final long serialVersionUID = 5796809069926845650L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otherPurposeId")
    private Integer id;
	
	private String purposeName;
	
	private String shortCode;
}
