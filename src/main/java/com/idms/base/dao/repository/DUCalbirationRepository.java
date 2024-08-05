package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DispensingUnitCalibration;

@Repository
public interface DUCalbirationRepository extends JpaRepository<DispensingUnitCalibration, Integer>{

	@Query(value="Select calib from DispensingUnitCalibration calib where calib.depot.id=?1 order by  calib.id desc")
	List<DispensingUnitCalibration> findAllByDepot(Integer depotId);

}
