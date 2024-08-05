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
@Table(name = "mst_tyre_change_actions")
@EqualsAndHashCode(callSuper = true)
public class TyreChangeAction extends BaseEntity{
	
	private static final long serialVersionUID = 7396546320225747603L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actionId")
    private Integer id;
	
	private String actionName;
	
	private String actionCode;

}
