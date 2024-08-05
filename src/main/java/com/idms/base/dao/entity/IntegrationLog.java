package com.idms.base.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "integration_log")
public class IntegrationLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "type")
	private String type;

	@Column(name = "way")
	private String way;

	@Column(name = "api_text")
	private String apiText;

	@Column(name = "response")
	private String response;

	@Column(name = "log_date_time")
	private Date logDateTime;

	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name ="reg_id")
	private String regId;
}
