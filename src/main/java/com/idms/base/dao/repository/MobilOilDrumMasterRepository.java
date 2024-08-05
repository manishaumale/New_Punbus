package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.AddBlueDrumMaster;
import com.idms.base.dao.entity.MobilOilDrumMaster;
import com.idms.base.dao.entity.QMobilOilDrumMaster;
import com.querydsl.core.types.dsl.StringPath;



@Repository
public interface MobilOilDrumMasterRepository extends JpaRepository<MobilOilDrumMaster, Integer> , QuerydslPredicateExecutor<MobilOilDrumMaster>, QuerydslBinderCustomizer<QMobilOilDrumMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QMobilOilDrumMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<MobilOilDrumMaster> findAllByStatusAndIsDeleted(boolean status,boolean flag);
	
	@Query(value = "SELECT b from MobilOilDrumMaster b where b.status = ?1 and b.isDeleted=?2 and b.depot.depotCode = ?3 order by b.id desc")
	List<MobilOilDrumMaster> findAllDrumsByDepot(boolean status,boolean flag,String dpCode);

}
