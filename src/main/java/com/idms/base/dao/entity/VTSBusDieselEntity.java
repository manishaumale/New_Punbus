package com.idms.base.dao.entity;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.Data;

@Data  // Lombok control - creates getters/setters, equals, hashCode, toString
@Entity 
@Table(name = "vts_tyre_kms_details_saveresponse")
public class VTSBusDieselEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vts_data_id")
	private Integer id;
	
	@Column(length=7)
	private Timestamp createdOn;
	
	@Column(length=7)
	private Timestamp updatedOn;
	
	@Column(name="bus_reg_no")
	private String bus_reg_no;
	
	private Date date;
	
	@Column(name="tyre_kms_covered")
	private Double kms_covered;
	
}
