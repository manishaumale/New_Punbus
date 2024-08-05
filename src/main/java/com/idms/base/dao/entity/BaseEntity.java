package com.idms.base.dao.entity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author Hemant Makkar(1000073)
 */
@Data  
@NoArgsConstructor 
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean status;
	
	@Column(length=50)
	private String createdBy;
	
	@Column(length=7)
	private Timestamp createdOn;
	
	@Column(length=40)
	private String createdClientIp;
	
	@Column(length=50)
	private String updatedBy;
	
	@Column(length=7)
	private Timestamp updatedOn;
	
	@Column(length=40)
	private String updatedClientIp;
	
	@PrePersist
    public void prePersist() throws UnknownHostException {
		status = true;
        createdOn = new Timestamp(System.currentTimeMillis());
        createdBy = SecurityContextHolder.getContext().getAuthentication().getName();
        createdClientIp = InetAddress.getLocalHost().toString();
        updatedOn = new Timestamp(System.currentTimeMillis());
        updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        updatedClientIp = InetAddress.getLocalHost().toString();
    }
 
    @PreUpdate
    public void preUpdate() throws UnknownHostException {
        updatedOn = new Timestamp(System.currentTimeMillis());
        updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        updatedClientIp = InetAddress.getLocalHost().toString();
    }
}