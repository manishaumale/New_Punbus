package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.AuthorizeRoute;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.QAuthorizeRoute;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface AuthorizeRouteRepository extends JpaRepository<AuthorizeRoute, Integer> , QuerydslPredicateExecutor<AuthorizeRoute>, QuerydslBinderCustomizer<QAuthorizeRoute> {
	
	@Override
	default void customize(QuerydslBindings bindings, QAuthorizeRoute root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	@Modifying
    @Query(value = "Update AuthorizeRoute set route_id=?1,bus_id=?2,driver_id=?3,conductor_id=?4,trip_id=?5,reason=?6 where authorize_route_id=?7 ")
	int updateAuthorizeRouteById(Integer route_Id,Integer busId,Integer driverId,Integer conductorId, Integer tripId,String reason,Integer id);

	@Modifying
    @Query(value = "Update AuthorizeRoute set isDeleted=?1 where id=?2")
	int updateAuthorizeRouteIsDeletedFlag(Boolean flag, Integer id);
	
	
	@Query(value = "SELECT ar from AuthorizeRoute ar where ar.depot.id = ?1 and ar.isDeleted=false ")
	List<AuthorizeRoute> findAllAuthorizeRoutesByDepot(Integer depotId);
}
