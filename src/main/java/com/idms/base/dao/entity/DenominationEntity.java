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
@Table(name = "mst_ticket_denomination")
@EqualsAndHashCode(callSuper = true)
public class DenominationEntity extends BaseEntity {
	
	private static final long serialVersionUID = 8846939694998664154L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "denomination_id")
	private Integer id;
	
	@Column(name = "denomination")
	private Integer denomination;

}
