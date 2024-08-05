package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ConductorRotaHistory;


@Repository
public interface ConductorRotaHistoryRepository extends JpaRepository<ConductorRotaHistory, Integer> {

	@Query("select history from ConductorRotaHistory history where history.roaster.id =?1")
	List<ConductorRotaHistory> findAllByDailyRoasterId(Integer dailyRoasterList);
	
	
	
	
}
