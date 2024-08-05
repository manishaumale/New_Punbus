package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.dao.entity.SubmitEtmTicketBoxEntity;
import com.idms.base.support.rest.RestConstants;

public interface SubmitEtmTicketBoxRepository extends JpaRepository<SubmitEtmTicketBoxEntity,Integer>   {
	
	
	@Query("SELECT submit FROM SubmitEtmTicketBoxEntity submit WHERE submit.issueEtmTicketBoxEntity.depoMaster.id =?1 order by id desc")
	List<SubmitEtmTicketBoxEntity> getAllSubmitEtmByDepot(Integer depotId);
	
	@Query(value="select sum(current_amount) from "
			+ " (select (case when current_amount  is null then amount  else  current_amount end) "
			+ " from "+RestConstants.SCHEMA_NAME+"."+" submit_etm_ticket_box_child where submit_etm_ticket_box =?1)amount",nativeQuery=true)
    Object[] fetchAmountSumBySubmitEtmId(Integer submitEtmId);
	
	@Query(value=" SELECT b from SubmitEtmTicketBoxEntity b where b.wayBillNo = ?1  ")
	List<SubmitEtmTicketBoxEntity> findByWaybillNo(String wayBillNo);
	
	
 
	@Query(value="select distinct ass.depot_nm,ass.sub_stock_id from "+RestConstants.SCHEMA_NAME+"."+" assign_sub_stocks ass",nativeQuery=true)
	List<Object[]> getAllDepoName();
	
	@Query(value="select distinct ass.denomination from "+RestConstants.SCHEMA_NAME+"."+" assign_sub_stocks ass where ass.depot_nm=?1",nativeQuery=true)
	List<Object[]> getAllDenoName(String string);
	
	
	@Query(value="select sum(t.total_amount)total_amount from (select count(mstd.stock_dtls_id)*mtd.denomination*mtbc.tb_count as total_amount "
			+ "from "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms  "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mtms.depot_id  "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts on mcts.central_stock_id = mtms.centeral_stock_id "
			+ "left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mcts.transport_id  "
			+ "left join  "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id = mcts.denomination_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls mstd on mstd.stock_id = mtms.master_stock_id  "
			+ " left join  "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_book_count mtbc on mcts.tb_count_id =mtbc.master_stock_id "
			+ " where mtms.depot_id =?1 and mtd.denomination_id =?2 group by  mtd.denomination_id, mtd.denomination, "
			+ "mcts.central_stock_id ,mtbc.tb_count)t ",nativeQuery=true)
	    public Long getTotalAmountBydenomination(Integer depoId,Integer denominationId);
	
	
	@Query(value="select dwl.min_value from  "+RestConstants.SCHEMA_NAME+"."+"depotwise_denomination_limit dwl where dwl.depot_id =?1 "
			+ "and dwl.demonination_id=?2 ",nativeQuery=true)
	public Long getMinValueOfTicket(Integer depoId,Integer denominationId);
	
	@Query(value="select ass.sub_stock_id from "+RestConstants.SCHEMA_NAME+"."+"assign_sub_stocks ass where denomination =?2 and ass.depot_nm=?1",nativeQuery=true)
	Integer getAssignSubStockId(String depocode,Integer denominationId);
	
}
