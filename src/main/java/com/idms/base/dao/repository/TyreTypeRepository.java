package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTyreType;
import com.idms.base.dao.entity.TyreType;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface TyreTypeRepository extends JpaRepository<TyreType, Integer> , QuerydslPredicateExecutor<TyreType>, QuerydslBinderCustomizer<QTyreType> {
	
	@Override
	default void customize(QuerydslBindings bindings, QTyreType root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<TyreType> findAllByStatus(boolean status);
	
	@Modifying
    @Query(value = "Update TyreType  set status=?1 where id=?2 ")
	int updateStatusFlag(Boolean flag,Integer id);

	@Query(value = "SELECT distinct tt from TyreType tt JOIN tt.makerDetailList md where md.maker.id = ?1 ")
	List<TyreType> findAllByTyreMakerId(Integer makerId);
	
	
}