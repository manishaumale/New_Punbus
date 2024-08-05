package com.idms.base.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.idms.base.dao.entity.QUser;
import com.idms.base.dao.entity.User;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

public interface UserRepository extends JpaRepository<User, Integer> , QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {
	
	@Override
	default void customize(QuerydslBindings bindings, QUser root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	@Query(value = "SELECT p from User p left join DepotMaster dm on dm.id=p.depot.id where dm.depotCode = ?1 ")
	List<User> getUsersByDepot(String depotCode);
	
	@Query(value = "SELECT distinct p from User p JOIN p.tpGroups ug where ug.transportUnit.id in (?1) ")
	List<User> getUsers(List<Integer> ids);

	Optional<User> findById(String name);
	
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".users where username=?1")
	public User findByUserName(String userName);
}
