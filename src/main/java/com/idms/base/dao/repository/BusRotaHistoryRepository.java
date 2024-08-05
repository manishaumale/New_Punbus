package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.BusRotaHistory;

@Repository
public interface BusRotaHistoryRepository extends JpaRepository<BusRotaHistory, Integer> {

	@Query("select daily from BusRotaHistory daily where daily.bus.id =?1 and daily.tripStatus is null order by daily.id desc")
	List<BusRotaHistory> findAllByBusId(Integer busId);
	
}
