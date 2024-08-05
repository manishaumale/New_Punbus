package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTripRotationEntity;
import com.idms.base.dao.entity.RouteRotation;
import com.idms.base.dao.entity.TripRotationEntity;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TripRotationReposiotry extends JpaRepository<TripRotationEntity, Integer> , QuerydslPredicateExecutor<TripRotationEntity>, QuerydslBinderCustomizer<QTripRotationEntity>{
	
	@Override
	default void customize(QuerydslBindings bindings, QTripRotationEntity rotation) {
		// Exclude the Id from query filter
		bindings.excluding(rotation.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	@Query(value="from TripRotationEntity tr where tr.tripMaster.id =?1")
	List<TripRotationEntity> getReocrdByTripId(Integer id);

	@Query(nativeQuery = true, value = "select count (*) from trip_rotation tr join mst_trip mt on tr.trip_master_id = mt.trip_id join mst_permit_details mpd on tr.permit_id = mpd.permit_id "
			+ " where tr.permit_id =?1 and mt.is_deleted=false and mt.status=true and current_date<mpd.valid_up_to")
	Long getReocrdCountBypermitMaster(Integer permitId);
	
	
	@Query(value="select max(rotation.total_nights_id),mtn.night_detail from "+RestConstants.SCHEMA_NAME+"."+"trip_rotation rotation "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_total_nights mtn  on mtn.total_nights_id = rotation.total_nights_id  "
			+ "where rotation.trip_master_id = ?1 group by mtn.night_detail", nativeQuery=true)
	Object[] findDayByTripId(Integer tripId);

	@Query(nativeQuery=true,value="select * from "+ RestConstants.SCHEMA_NAME + ".trip_rotation where trip_master_id=?1")
	List<TripRotationEntity> findByTripMaster(Integer id);
	
	@Query(nativeQuery = true, value = "select count (*) from " + RestConstants.SCHEMA_NAME
			+ ".trip_rotation tr join " + RestConstants.SCHEMA_NAME
			+ ".mst_trip mt on tr.trip_master_id = mt.trip_id where permit_id=?1 and trip_master_id!=?2 and mt.is_deleted=false and mt.status=true")
	int countOfPermit(Integer permitId, Integer tripId);
}
