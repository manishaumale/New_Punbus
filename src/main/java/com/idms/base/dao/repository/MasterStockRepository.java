package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.api.v1.model.dto.MasterStockDto;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface MasterStockRepository extends JpaRepository<MasterStock,Integer> {
	
	@Query(value = "SELECT denomination from  punbus_dev.tbl_denomination", nativeQuery = true)
    List<Integer> listdenominations();
	
	@Query(value ="select type_id,stock_type from punbus_dev.tbl_stock_type ", nativeQuery = true)
	List<Object[]> stockType();
	
	@Query(value = "select tms.stocktypeid, tst.type_id , tst.stock_type,tms.denominationid,tms.bundle_no,"
	+" ((td.denomination)*(ss.end_serial_no-ss.start_serial_no)) as amount"
	+" from punbus_dev.tbl_master_stock tms"
	+" inner join punbus_dev.tbl_stock_type tst"
	+" on tst.type_id= tms.stocktypeid"
	+" inner join punbus_dev. Masterstock_Serialno ss"
	+" on tms.masterstock_id= ss.stock_serial_id "
	+" inner join punbus_dev.tbl_denomination td on td.denomination_id=tms.denominationid" , nativeQuery = true)
	List<Object[]> Masterstockdenomination();
	
	@Query(value = "SELECT  denomination_id,denomination FROM punbus_dev.tbl_denomination td;", nativeQuery = true)
	List<Object[]> findAllDenomination();

	@Query(value = " select mstd.ticket_book_no, mstd.start_serial_no, mstd.end_serial_no, mcts.state_tax, mcts.state_id, ms.state_name, mtms.bundle_number, mcts.series_number "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_central_ticket_stock mcts "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms on mcts.central_stock_id = mtms.centeral_stock_id  "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls mstd on mstd.stock_id=mtms.master_stock_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_state ms on ms.state_id = mcts.state_id "
			+ " where mcts.denomination_id=?2 and mcts.transport_id=?1 and mtms.is_assigned=false"
			+ " order by mtms.master_stock_id ", nativeQuery=true)
	List<Object[]> getMasterStockTicketList(Integer tpId, Integer denoId);
	
}
