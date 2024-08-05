package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QStateMaster;
import com.idms.base.dao.entity.StateMaster;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface StateMasterRepository extends JpaRepository<StateMaster, Integer>, QuerydslPredicateExecutor<StateMaster>, QuerydslBinderCustomizer<QStateMaster> {//, RevisionRepository<Employee, Long, Integer> {

	@Override
	default void customize(QuerydslBindings bindings, QStateMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	List<StateMaster> findAllByStatus(Boolean status);
	
	List<StateMaster> findAll();
	
	
	
	@Modifying
    @Query(value = "Update StateMaster  set status=?1 where id=?2 ")
	int updateStatusFlag(Boolean flag,Integer id);
	
	@Query(value = "Select s from StateMaster s where s.state_tax = true and s.status=true ")
	List<StateMaster> taxstates();

}
