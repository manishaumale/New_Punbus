package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.MasterStock;
import com.idms.base.dao.entity.TicketBoxMaster;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface TicketBoxMasterRepository extends JpaRepository<TicketBoxMaster, Integer> {
	
	@Query(value="select t.ticket_box_id,t.ticket_box_number  from punbus_dev.mst_ticket_box t join punbus_dev.mst_transport m on t.transport_id=m.transport_id and m.transport_name ='Punjab Roadways';",nativeQuery=true)
	List<Object[]> findAllTicketBoxMgmtDtls();

	@Query(value="select * from "+RestConstants.SCHEMA_NAME+"."+"mst_ticket_box where transport_id =?1",
			nativeQuery=true)
	List<Object[]> findTicketBoxMgmtDtlsByTransportId(Integer transportUnitId);

	@Query(value="select * from "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls where denomination_id =?1",nativeQuery=true)
	List<Object[]> getStockTicketDetailsList(Integer id);

	@Query(value="select mstd.* from "+RestConstants.SCHEMA_NAME+"."+"master_stock_ticket_dtls mstd "
			+ "inner join "+RestConstants.SCHEMA_NAME+"."+"mst_tkt_master_stock mtms on mtms.master_stock_id =mstd.stock_id "
			+ " where mtms.centeral_stock_id =?1 and mstd.is_assigned = false ORDER BY 1 asc",nativeQuery=true)
	List<Object[]> getStockTicketDetailsByStockIdList(Integer id);
	
}
