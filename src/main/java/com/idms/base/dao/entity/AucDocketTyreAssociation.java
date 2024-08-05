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

import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.dao.entity.RetreadingDocket;
import com.idms.base.dao.entity.TyreMaster;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data   
@Entity 
@Table(name = "auc_docket_tyre_association")
@EqualsAndHashCode(callSuper = true)
public class AucDocketTyreAssociation extends BaseEntity {
	
	private static final long serialVersionUID = -1446487590820478507L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adtaId")
    private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = AuctionedDocket.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "docketId")
	private AuctionedDocket docket;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TyreMaster.class, cascade = CascadeType.MERGE)
	@JoinColumn(name = "tyreId")
	private TyreMaster tyre;

}
