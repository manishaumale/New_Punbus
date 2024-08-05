package com.idms.base.dao.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.TicketBoxManagementEntity;
import com.idms.base.support.rest.RestConstants;

public interface TicketBoxManagementRepository extends JpaRepository<TicketBoxManagementEntity,Integer>{

	@Query("SELECT tb FROM TicketBoxManagementEntity tb WHERE ticketBoxNumber.id =?1")
	List<TicketBoxManagementEntity> getTicketBoxNumberList(Integer id);
	
	@Query("SELECT tb FROM TicketBoxManagementEntity tb WHERE tb.ticketBoxManagementParent.id =?1")
	List<TicketBoxManagementEntity> findByParentId(Integer id);
	
	@Modifying
    @Query(value = "Update TicketBoxManagementEntity  set currentAmount=?1,currentLastNo=?2 ,updatedBy=?3, updatedOn=?4,isBookletChecked=?5 where id =?6")
	int updateTicketBoxData(Long amount, Long currentLastNo,String updatedBy, Timestamp updatedOn,Boolean isBookletChecked, Integer id);
	
	 @Query(value="select sum(amount) from "+RestConstants.SCHEMA_NAME+"."+" manage_ticket_box where ticket_box_management_parent_id =?1",nativeQuery=true)
     Object[] fetchAmountSumByIssueEtmParentId(Integer issueParentEtmId);


}
