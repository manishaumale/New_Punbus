package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.ReviewCommentsHistory;
import com.idms.base.support.rest.RestConstants;

public interface ReviewCommentsRepository extends JpaRepository<ReviewCommentsHistory, Integer> {

	@Query(nativeQuery = true, value = "select * from " + RestConstants.SCHEMA_NAME
			+ ".review_comments_history where depot_id=?1")
	public List<ReviewCommentsHistory> findByDepot(Integer depotId);

	@Query(nativeQuery = true, value = "select * from " + RestConstants.SCHEMA_NAME
			+ ".review_comments_history where (bus_id=?1 or driver_master_id=?3 or route_master_id=?2 or conductor_master_id=?4) order by created_on desc limit 1")
	public ReviewCommentsHistory findByUniqueId(Integer busId, Integer routeId, Integer driverId, Integer conductorId);
	
	
	@Query(nativeQuery = true, value = "select * from " + RestConstants.SCHEMA_NAME
			+ ".review_comments_history where (bus_id=?1 or driver_master_id=?3 or route_master_id=?2 or conductor_master_id=?4) order by created_on desc")
	public List<ReviewCommentsHistory> findByUniqueHistory(Integer busId, Integer routeId, Integer driverId, Integer conductorId);
}
