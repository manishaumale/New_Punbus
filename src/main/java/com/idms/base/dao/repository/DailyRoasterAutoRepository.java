package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DailyRoasterAuto;
import com.idms.base.dao.entity.QDailyRoasterAuto;
import com.querydsl.core.types.dsl.StringPath;

	
	@Repository
	public interface DailyRoasterAutoRepository extends JpaRepository<DailyRoasterAuto, Integer> , QuerydslPredicateExecutor<DailyRoasterAuto>, QuerydslBinderCustomizer<QDailyRoasterAuto> {
		
		@Override
		default void customize(QuerydslBindings bindings, QDailyRoasterAuto root) {
			// Exclude the Id from query filter
			bindings.excluding(root.id);

			// Perform IgnoreCase & EqualsLike comparison
			bindings.bind(String.class).first(
					(StringPath path, String value) -> path.containsIgnoreCase(value)
			);		
		}
	

	@Query(value = "SELECT auto from DailyRoaster auto where auto.route.id = ?1 and auto.bus.id = ?2 and auto.driver.id = ?3")
	List<DailyRoasterAuto> findByRouteBusAndDriver(Integer routeId, Integer busId,Integer driverId);
    
	@Query(value = "SELECT r from DailyRoasterAuto r where r.rotaAuto.id = ?1 and r.trip.id =?2")
	List<DailyRoasterAuto> findAllByRotaAndTripId(Integer rotaId, Integer tripId);
	
	@Modifying
    @Query(value = "Update Roaster r  set r.masterStatus.id = ?1 where r.id=?2 ")
	int approveOrRejectManualSpecialRota(Integer id,Integer rotaId);
	
	
	
	
}
