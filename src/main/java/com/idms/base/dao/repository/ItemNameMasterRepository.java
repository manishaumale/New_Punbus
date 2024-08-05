package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ItemNameMaster;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface ItemNameMasterRepository extends JpaRepository<ItemNameMaster,Integer> {
	
	@Query(value="select mt.itemname_id , mt.name from "+RestConstants.SCHEMA_NAME+"."+"mst_item_name_tbl mt where mt.item_type_id =?1",nativeQuery=true)
	List<Object[]> findByItemTypeId(Integer id);

}
