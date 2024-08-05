package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.RetreadingDocket;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface RetreadingDocketRepository extends JpaRepository<RetreadingDocket, Integer>{

	@Query(value="select rdd.docket_id, docket_number, count(dta.dta_id) as tyreCount,to_date(to_char(rdd.created_on , 'YYYY/MM/DD'), 'YYYY/MM/DD') "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"retreading_docket_details rdd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta on dta.docket_id = rdd.docket_id"
			+ " where rdd.depot_id = ?1"
			+ " group by rdd.docket_id, rdd.docket_number,rdd.created_on order by rdd.docket_id desc", nativeQuery=true)
	List<Object[]> findAllbyDepotId(Integer id);

	@Query(value="select rdd.docket_id, docket_number, count(dta.dta_id) as tyreCount"
			+ " from "+RestConstants.SCHEMA_NAME+"."+"retreading_docket_details rdd "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta on dta.docket_id = rdd.docket_id"
			+ " where rdd.depot_id = ?1 and rdd.is_closed=false "
			+ " group by rdd.docket_id, rdd.docket_number order by rdd.docket_id desc", nativeQuery=true)
	List<Object[]> getReceivedDocketList(Integer id);

}
