package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.IssueEtmTicketBoxEntity;
import com.idms.base.support.rest.RestConstants;

public interface IssueEtmTicketBoxRepository extends JpaRepository<IssueEtmTicketBoxEntity,Integer>  {

	@Query("SELECT tb FROM IssueEtmTicketBoxEntity tb WHERE tb.conductorMaster.id =?1 and tb.condutorStatus=false ORDER BY tb.updatedOn DESC")
	IssueEtmTicketBoxEntity getConductorId(Integer conductorId);

	@Query("from IssueEtmTicketBoxEntity iet WHERE iet.depoMaster.id=?1 order by id desc")
	List<IssueEtmTicketBoxEntity> getRecordByDepotId(Integer id);
	
	@Query("SELECT tb.conductorMaster.id FROM IssueEtmTicketBoxEntity tb WHERE tb.condutorStatus=false")
	List<Integer> getRecordForAvailableConductor();

	@Query("SELECT tb.etmMaster.id FROM IssueEtmTicketBoxEntity tb WHERE tb.condutorStatus=false")
	List<Integer> getRecordForAvailableEtm();

	@Query("SELECT tb.ticketBoxNumber.id FROM IssueEtmTicketBoxEntity tb WHERE tb.condutorStatus=true")
	List<Integer> getRecordForAvailableTicket();
	

    @Query(value="select sum(amount) from "+RestConstants.SCHEMA_NAME+"."+" issue_etm_ticket_box_child where issue_etm_ticket_box_entity =?1",nativeQuery=true)
     Object[] fetchAmountSumByIssueEtmId(Integer issueEtmId);
    
    @Query("SELECT tb FROM IssueEtmTicketBoxEntity tb WHERE tb.conductorMaster.id =?1 ")
	List<IssueEtmTicketBoxEntity> getAllByConductorId(Integer conductorId);
    
    @Query("SELECT tb FROM IssueEtmTicketBoxEntity tb WHERE tb.etmMaster.id =?1 ")
   	List<IssueEtmTicketBoxEntity> getAllByEtmId(Integer etmId);

    @Query(nativeQuery=true,value="SELECT x.* FROM "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box x WHERE waybill_no =?1")
	IssueEtmTicketBoxEntity findByWayBillNo(String wayBillNo);
    
    @Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+"."+"issue_etm_ticket_box where roaster_id=?1 order by created_on desc limit 1")
    IssueEtmTicketBoxEntity findByRoasterId(Integer id);
    
}