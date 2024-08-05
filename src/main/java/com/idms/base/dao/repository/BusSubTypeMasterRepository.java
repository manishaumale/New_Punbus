package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusSubTypeMaster;
import com.idms.base.dao.entity.QBusSubTypeMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface BusSubTypeMasterRepository extends JpaRepository<BusSubTypeMaster, Integer> , QuerydslPredicateExecutor<BusSubTypeMaster>, QuerydslBinderCustomizer<QBusSubTypeMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBusSubTypeMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<BusSubTypeMaster> findAllByStatus(Boolean status);
	
	List<BusSubTypeMaster> findAll();
	
	@Modifying
    @Query(value = "Update BusSubTypeMaster  set status=?1 where id=?2 ")
	int updateBusSubTypeStatusFlag(Boolean flag, Integer id);

	@Query(value="select DISTINCT bus.bus_reg_number,bus.bus_id,transport.transport_name  "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details bus join "+RestConstants.SCHEMA_NAME+"."+"mst_transport transport on  "
			+ " transport.transport_id = bus.transport_unit",nativeQuery=true)
	List<Object[]> getBusList();
	
}
