package com.idms.base.support.persist.querydsl;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.StringPath;

/**
 * QueryDsl Generic Binder 
 * <p>
 * Define the default binding for String attributes to be a case insensitive "like" match
 * 
 * @author Hemant Makkar
 */
public class EqualityLikeQuerydslBinder<Q extends EntityPath<?>> implements QuerydslBinderCustomizer<Q> {

	@Override
	public void customize(QuerydslBindings bindings, Q root) {
		
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);
	}

}
