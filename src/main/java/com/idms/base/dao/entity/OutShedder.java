package com.idms.base.dao.entity;

import java.time.LocalTime;
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

//@Getter @Setter   // Lombok control - creates getters/setters, equals, hashCode, toString
@Data
@Entity
@Table(name = "out_shedder")
@EqualsAndHashCode(callSuper = true)
public class OutShedder extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "outshedderId")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = BusMaster.class)
    @JoinColumn(name = "busId", nullable = true)
	private BusMaster bus;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Roaster.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "rotaId", nullable = true)
	private Roaster rota;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DailyRoaster.class)
    @JoinColumn(name = "roasterId", nullable = true)
	private DailyRoaster dailyRoaster;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = DepotMaster.class)
    @JoinColumn(name = "depotId", nullable = true)
	private DepotMaster depot;
	
	private Date rotaDate;
	
	private LocalTime inTime;
	
	private LocalTime outTime;
}
