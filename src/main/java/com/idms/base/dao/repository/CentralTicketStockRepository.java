package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.CentralTicketStock;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface CentralTicketStockRepository extends JpaRepository<CentralTicketStock, Integer>{

	@Query(value="SELECT c from CentralTicketStock c where c.seriesNumber=?1 and c.denomination.id=?2 ")
	List<CentralTicketStock> findBySrNo(String serieslNumber, Integer integer);

	@Query(value = "Select s from CentralTicketStock s where s.transport.id in ?1 ")
	List<CentralTicketStock> getMasterStockList(List<Integer> li);
	
	@Query(value=" select mt.transport_id, mt.transport_name, mtd.denomination_id , mtd.denomination, mcts.series_number, count(mstd.stock_dtls_id) as books, count(mstd.stock_dtls_id)*100*mtd.denomination as totalamount "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mcts.transport_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id = mcts.denomination_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms on mtms.centeral_stock_id = mcts.central_stock_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls mstd on mstd.stock_id=mtms.master_stock_id "
			+ " where mtms.is_assigned=false and mcts.transport_id in (?1)"
			+ " group by mt.transport_id, mt.transport_name, mtd.denomination_id , mtd.denomination, mcts.series_number,mcts.central_stock_id order by  mcts.central_stock_id desc", nativeQuery=true)
	List<Object[]> getMasterStockListUpdated(List<Integer> tpIds);

}
 