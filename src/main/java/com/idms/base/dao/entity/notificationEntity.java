package com.idms.base.dao.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="notification_log")
@Data
public class notificationEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	private String description;
	
	private Boolean closed;
	
	private String module;
	
	private String alertType;
	
	private Date createdDate;
	
	private Integer moduleId;
	
	private Boolean status;
	
	private Boolean isRead;
	
	private Date readDate;
	
	private String readBy;
	
	private Integer depotId;
	
	private String displayId;
	
}
