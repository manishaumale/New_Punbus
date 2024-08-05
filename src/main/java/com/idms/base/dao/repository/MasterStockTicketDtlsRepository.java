package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.MasterStockTicketDtls;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface MasterStockTicketDtlsRepository extends JpaRepository<MasterStockTicketDtls, Integer>{

	@Query(value=" select mtms.master_stock_id, mtms.bundle_number, mtms.start_serial_no, mtms.end_serial_no, mtd.denomination_id, mtd.denomination,mcts.series_number from "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms on mtms.centeral_stock_id = mcts.central_stock_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id =mcts.denomination_id "
			+ " where mcts.denomination_id = ?1 and mcts.transport_id =?2 and mtms.is_assigned =false"
			+ " order by mtms.master_stock_id  ", nativeQuery=true)
	List<Object[]> findTicketBooksByTrnspAndDeno(Integer denoId, Integer transportId);

	/*@Query(value=" select mtms.master_stock_id, mcts.series_number, mtms.start_serial_no, mtms.end_serial_no, mtd.denomination_id, mtd.denomination from "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms on mtms.centeral_stock_id = mcts.central_stock_id "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id =mcts.denomination_id "
			+ " where mcts.denomination_id = ?1 and mcts.transport_id =?2 and mtms.is_assigned =true"
			+ " order by mtms.master_stock_id  ", nativeQuery=true)*/
	@Query(value=" select mcts.central_stock_id ,mcts.series_number from "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts "
			+ " where mcts.denomination_id = ?1 and mcts.transport_id =?2 ", nativeQuery=true)
	List<Object[]> findTicketBooksByTrnspAndDenoAssined(Integer denoId, Integer transportId);
	
	@Query("SELECT ms FROM MasterStockTicketDtls ms WHERE startSrNo =?1 AND denomination.id =?2")
	MasterStockTicketDtls getMasterStockTicketDtlsByStartSrNo(Integer startSrNo,Integer denominationId);

}
