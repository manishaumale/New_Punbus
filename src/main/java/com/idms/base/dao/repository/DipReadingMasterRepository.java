package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DipReadingMaster;
import com.idms.base.dao.entity.QDipReadingMaster;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface DipReadingMasterRepository extends JpaRepository<DipReadingMaster, Integer> , QuerydslPredicateExecutor<DipReadingMaster>, QuerydslBinderCustomizer<QDipReadingMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDipReadingMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	@Query(value = "SELECT drm from DipReadingMaster drm where drm.status=?1 order by drm.id desc")
	List<DipReadingMaster> findAllByStatus(boolean status);
	
	
}
