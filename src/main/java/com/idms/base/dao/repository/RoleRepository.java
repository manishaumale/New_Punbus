package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QRole;
import com.idms.base.dao.entity.Role;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> , QuerydslPredicateExecutor<Role>, QuerydslBinderCustomizer<QRole>{
	
	@Override
	default void customize(QuerydslBindings bindings, QRole root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	Role findByName(String name);
	
	@Query("SELECT r from Role r where r.roleCode in ('TM', 'SSI', 'DI')")
	List<Role> findAllByRoleCode();
	
	
	

}
