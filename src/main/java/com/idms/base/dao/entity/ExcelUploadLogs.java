package com.idms.base.dao.entity;

import java.util.Date;

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

@Data   // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "excel_upload_logs")
@EqualsAndHashCode(callSuper = true)
public class ExcelUploadLogs extends BaseEntity{
	private static final long serialVersionUID = -2626487385871583636L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id; 
	
	private String fileName;
	
	private String type;
	
	private Date logDateTime;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Document.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "documentId")
	private Document documentId;
}
