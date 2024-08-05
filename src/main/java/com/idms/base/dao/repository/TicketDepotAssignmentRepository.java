package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.TicketDepotAssignment;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface TicketDepotAssignmentRepository extends JpaRepository<TicketDepotAssignment, Integer> {

	@Query(value=" select md.depot_id , md.depot_name, mt.transport_id, mt.transport_name, mtd.denomination_id, mtd.denomination, count(mstd.stock_dtls_id), count(mstd.stock_dtls_id)*mtd.denomination*mtbc.tb_count as total_amount, mcts.central_stock_id,mtbc.tb_count "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mtms.depot_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts on mcts.central_stock_id = mtms.centeral_stock_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mcts.transport_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id = mcts.denomination_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls mstd on mstd.stock_id = mtms.master_stock_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_book_count mtbc on mcts.tb_count_id =mtbc.master_stock_id "
			+ " where mt.transport_id in(?1) and mtms.is_assigned = true "
			+ " group by md.depot_id, md.depot_name, mt.transport_id, mt.transport_name, "
			+ " mtd.denomination_id, mtd.denomination, mcts.central_stock_id ,mtbc.tb_count,mtms.centeral_stock_id order by mtms.centeral_stock_id desc ", nativeQuery=true)
	List<Object[]> getDepotTicketStockList(List<Integer> tpList);
	
	@Query(value=" select md.depot_id , md.depot_name, mt.transport_id, mt.transport_name, mtd.denomination_id, mtd.denomination,"
			+ "mstd.stock_dtls_id ,mcts.series_number ,mstd.start_serial_no ,mstd.end_serial_no ,mstd.ticket_book_no,mtms.bundle_number,mcts.state_tax,ms.state_name "
			+ ",mtd.denomination*((mstd.end_serial_no-mstd.start_serial_no)+1)as totalValue "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mtms.depot_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts on mcts.central_stock_id = mtms.centeral_stock_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt on mt.transport_id = mcts.transport_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_denomination mtd on mtd.denomination_id = mcts.denomination_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls mstd on mstd.stock_id = mtms.master_stock_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on ms.state_id =mcts.state_id  "
			+ " where  mtd.denomination =?1 and mcts.central_stock_id =?2  and mtms.is_assigned = true order by mstd.start_serial_no", nativeQuery=true)
	List<Object[]> getTicketStockByDenomination(Integer denomination,Integer centralStockId);

}
