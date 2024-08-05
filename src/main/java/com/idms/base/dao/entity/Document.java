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
@Table(name = "mst_document")
@EqualsAndHashCode(callSuper = true)
public class Document extends BaseEntity {

    private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentId")
    private Integer id;
	
	@Column(length=50)
    private String documentName;
    
    @Column(length=500)
    private String documentPath;
    
    private String contentType;
    
}
