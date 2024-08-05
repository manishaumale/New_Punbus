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
@Table(name = "mst_concession_ticket")
@EqualsAndHashCode(callSuper = true)
public class ConcessionTicketEntity extends BaseEntity  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "concession_ticket_id")
	private Integer id;
	
	@Column(name = "pass_type")
	private String passType;
	
	@Column(name = "discount")
	private String discount;
}
