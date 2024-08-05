package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTripMaster;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TripMasterRepository extends JpaRepository<TripMaster, Integer> , QuerydslPredicateExecutor<TripMaster>, QuerydslBinderCustomizer<QTripMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QTripMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<TripMaster> findAllByStatus(boolean status);
	
	List<TripMaster> findAll();
	
	List<TripMaster> findAllByIsDeleted(boolean IsDeleted);
	
	@Query(value = "SELECT p from TripMaster p where p.routeMaster.depotMaster.depotCode = ?1 and  p.isDeleted=false order by p.id desc,p.tripStartTime ,p.updatedOn desc  ")
	List<TripMaster> findAllByDepot(String dpCode);
	
	@Modifying
    @Query(value = "Update TripMaster  set status=?1 where id=?2 ")
	int updateTripMasterStatusFlag(Boolean flag, Integer id);
	
	@Modifying
    @Query(value = "Update TripMaster  set isDeleted=?1 where id=?2 ")
	int updateTripMasterIsDeletedFlag(Boolean flag, Integer id);
	
	@Query(value = "SELECT p from TripMaster p where p.routeMaster.id = ?1 and p.isDeleted=false and status=true")
	List<TripMaster> findByRouteId(Integer id);
	
	@Query(value = "SELECT trip_id FROM "+ RestConstants.SCHEMA_NAME + ".mst_trip ORDER BY trip_id DESC LIMIT 1", nativeQuery = true)
	int fetchMaxId();
	
	//@Query(value = "SELECT t from TripMaster t where t.permitDetailsMaster.id = ?1")
	//List<TripMaster> findByPermitId(Integer id);
	
	@Query(value = "SELECT t from TripMaster t where t.routeMaster.id in ?1")
	List<TripMaster> findByAllRouteIdsId(List<Integer> id);

	@Query(value = "SELECT t from TripMaster t where t.tripCode=?1 ")
	TripMaster findByTripCode(String tripCode);
	
	@Query(value = "SELECT p from TripMaster p where p.routeMaster.depotMaster.id = ?1 and p.routeMaster.transport.id=?2 and p.isDeleted=false order by p.tripStartTime ")
	List<TripMaster> findByRouteType(Integer depotId, Integer tpId);
	
	@Query(value = " from TripMaster as p inner join p.routeMaster as route  where p.routeMaster.depotMaster.id = ?1 and p.routeMaster.transport.id=?2 "
			+ " and p.isDeleted=false and p.status=true and not exists(select ar.trip.id from AuthorizeRoute as ar where ar.trip.id = p.id and ar.isDeleted = false ) "
			+ " order by p.tripStartTime")
	List<TripMaster> findAllTripsByAvailableRoutes(Integer depotId, Integer tpId, Date rotaDate);

	@Query(value = " from TripMaster p where p.routeMaster.id=?1 and p.isDeleted=false and p.status=true")
	List<TripMaster> findAllByRouteId(Integer routeId);
	
	@Query(value = " from TripMaster as p inner join p.routeMaster as route  where p.routeMaster.depotMaster.id = ?1 and p.routeMaster.transport.id=?2 "
			+ " and p.routeMaster.id in ?3 and  p.isDeleted=false and p.status=true and not exists(select ar.trip.id from AuthorizeRoute as ar where ar.trip.id = p.id and ar.isDeleted = false ) "
			+ "order by p.tripStartTime")
	List<TripMaster> findAllTripsBySelectedRoutes(Integer depotId, Integer tpId,Integer[] selectedIds,Date date);
	
	@Query(value = "select count(mt.trip_id) from "+ RestConstants.SCHEMA_NAME + ".mst_trip mt  "
			+ " join "+ RestConstants.SCHEMA_NAME + ".mst_route mr on mt.root_id = mr.id where mr.status = true and mr.is_deleted = false ", nativeQuery = true)
	Integer fetchMinimumDriversAndCondRequired();
	
	@Query(value = "SELECT t from TripMaster t where t.serviceId in ?1")
	TripMaster findByServiceId(String serviceId);
	
}