package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import com.idms.base.dao.entity.Module;
import com.idms.base.dao.entity.QModule;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer>, QuerydslPredicateExecutor<Module>, QuerydslBinderCustomizer<QModule>{

	@Override
	default void customize(QuerydslBindings bindings, QModule root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query("select mm from Module AS mm left join mm.modules mrma where mrma.role.id = ?1 and mm.parentId is null and mrma.status=true order by mm.displayOrder")
	List<Module> getParentModulesByRole(Integer roleId);
	
	@Query("select mm from Module AS mm left join mm.modules mrma where mrma.role.id = ?1 and mm.parentId is not null order by mm.displayOrder")
	List<Module> getChildModulesByRole(Integer roleId);
}
