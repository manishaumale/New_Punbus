package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.IndentChildEntity;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface IndentChildRepository extends JpaRepository<IndentChildEntity,Integer>{

	 @Modifying
     @Query(value="Update IndentChildEntity set isDeleted=?1 where id=?2")
     int deleteIndentChildEntityStatusFlag(Boolean flag,Integer id);
	 
	 @Modifying
	 @Query(value="UPDATE IndentChildEntity set itemQuantity=?1 where id =?2")
	 int updateChildIndentQuantity(Float itemQuantity,Integer id);
}
