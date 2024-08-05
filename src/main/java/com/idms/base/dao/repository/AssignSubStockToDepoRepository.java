package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.AssignSubStockToDepoEntity;

@Repository
public interface AssignSubStockToDepoRepository extends JpaRepository<AssignSubStockToDepoEntity, Integer> {
	List<AssignSubStockToDepoEntity> findByDepotName(String depotName);

	@Query(value = "SELECT  denomination_id,denomination FROM punbus_dev.tbl_denomination td;", nativeQuery = true)
	List<Object[]> findAllDenomination();
}
