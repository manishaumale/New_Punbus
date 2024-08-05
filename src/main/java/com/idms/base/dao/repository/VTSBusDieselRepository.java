package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QVTSBusDieselEntity;
import com.idms.base.dao.entity.VTSBusDieselEntity;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface VTSBusDieselRepository extends JpaRepository<VTSBusDieselEntity, Integer> , QuerydslPredicateExecutor<VTSBusDieselEntity>, QuerydslBinderCustomizer<QVTSBusDieselEntity>{

	@Override
	default void customize(QuerydslBindings bindings, QVTSBusDieselEntity root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query(value = "SELECT vts from VTSBusDieselEntity vts where vts.bus_reg_no= ?1 and vts.date=?2")
	List<VTSBusDieselEntity> findAllByBusnoAndDate(String bus_reg_no , Date date);
	
	@Query(value = "SELECT vts from VTSBusDieselEntity vts where vts.date=?1")
	List<VTSBusDieselEntity> findAllByDate(Date date);
	
	@Query(value = "SELECT vts from VTSBusDieselEntity vts where vts.bus_reg_no= ?1")
	List<VTSBusDieselEntity> findAllByBusno(String bus_reg_no);
	
	

	
}
