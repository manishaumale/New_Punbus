package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.TicketBoxManagementParentEntity;

public interface TicketBoxManagementParentRepository extends JpaRepository<TicketBoxManagementParentEntity,Integer> {

	@Query("from TicketBoxManagementParentEntity tb WHERE tb.ticketBoxNumber.id =?1")
	TicketBoxManagementParentEntity findByTicketBoxId(Integer id);
	
	@Query("select tb from TicketBoxManagementParentEntity tb WHERE tb.status =?1 order by tb.updatedOn desc")
	List<TicketBoxManagementParentEntity> findAllTicketBoxes(Boolean status);

}
