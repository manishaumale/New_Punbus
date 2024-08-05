package com.idms.base.dao.entity;

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
@Table(name = "mst_takenoff_reason")
@EqualsAndHashCode(callSuper = true)
public class TakenOffReason extends BaseEntity {
	
	private static final long serialVersionUID = -1569189582490843030L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "takenOffReasonId")
    private Integer id;
    
    @Column(length=50)
    private String reasonName;
    
    private String reasonCode;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = TakenOffReason.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "parentId")
    private TakenOffReason parent;

}
