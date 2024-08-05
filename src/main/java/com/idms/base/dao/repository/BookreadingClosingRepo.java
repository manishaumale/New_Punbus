package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BookReadingClosingMaster;
import com.idms.base.dao.entity.QBookReadingClosingMaster;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface BookreadingClosingRepo extends JpaRepository<BookReadingClosingMaster, Integer> , QuerydslPredicateExecutor<BookReadingClosingMaster>, QuerydslBinderCustomizer<QBookReadingClosingMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBookReadingClosingMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query(value = "SELECT book from BookReadingClosingMaster book where book.status=?1")
	List<BookReadingClosingMaster> findAllByStatus(boolean status);
	
	
}
