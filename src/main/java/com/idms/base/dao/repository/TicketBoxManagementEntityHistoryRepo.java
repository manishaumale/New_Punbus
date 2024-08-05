package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.QTicketBoxManagementEntityHistory;
import com.idms.base.dao.entity.TicketBoxManagementEntityHistory;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface TicketBoxManagementEntityHistoryRepo  extends JpaRepository<TicketBoxManagementEntityHistory, Integer>,QuerydslPredicateExecutor<TicketBoxManagementEntityHistory>, QuerydslBinderCustomizer<QTicketBoxManagementEntityHistory>{

	@Override
	default void customize(QuerydslBindings bindings, QTicketBoxManagementEntityHistory root) {
		bindings.excluding(root.id);
		bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
	}
}
