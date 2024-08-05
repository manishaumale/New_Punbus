package com.idms.base.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "mst_enrolment_type")
@EqualsAndHashCode(callSuper = true)
public class EmploymentType extends BaseEntity {
	
	private static final long serialVersionUID = 4993709155093945897L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrolmentId")
    private Integer id;
	
	@Column(length=50)
    private String enrolmentName;
	
	@Column(length=50)
    private String enrolmentCode;

}
