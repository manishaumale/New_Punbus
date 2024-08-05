package com.idms.base.dao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Entity
@Table(name = "manage_ticket_box_parent")
public class TicketBoxManagementParentEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticketbox_mgmt_id_parent")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = TicketBoxMaster.class)
	@JoinColumn(name = "ticketBoxId")
	private TicketBoxMaster ticketBoxNumber;
	
	@OneToMany(mappedBy="ticketBoxManagementParent",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<TicketBoxManagementEntity> ticketBoxManagementList;

	

}
