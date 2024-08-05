package com.idms.base.dao.entity;

import java.sql.Timestamp;
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

@Data
@Entity
@Table(name = "message_logs")
//@EqualsAndHashCode(callSuper = true)
public class MessageLogs {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	private String message;
	
	private String response;
	
	private Date log_date;
	
	private String phoneNumber;
	
	@Column(name="create_on")
	private Date createdOn;
	
	@Column(name="update_on")
	private Date updatedOn;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = MessageTemplates.class)
    @JoinColumn(name = "message_", nullable = true)
	private MessageTemplates messageId;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FuelTankMaster.class,cascade=CascadeType.MERGE)
    @JoinColumn(name = "fuelTankId", nullable = true)
   private FuelTankMaster fuelTank;
   
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class,cascade=CascadeType.MERGE)
    @JoinColumn(name = "depotId",nullable=true)
   private DepotMaster depot;
	
	
}
