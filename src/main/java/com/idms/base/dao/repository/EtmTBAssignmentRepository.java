package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.EtmTBAssignment;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface EtmTBAssignmentRepository extends JpaRepository<EtmTBAssignment, Integer> {

	@Query(value=" SELECT b from EtmTBAssignment b where b.wayBillNo = ?1 and (b.etmSubmitStatus=false or b.etmSubmitStatus=true) ")
	EtmTBAssignment findByWaybillNo(String wayBillNo);
	
	@Query(nativeQuery=true, value="select * from "+RestConstants.SCHEMA_NAME+".etm_assignment where iet_id=?1 order by created_on")
	EtmTBAssignment findByIetmId(Integer id);

	/*@Query(value=" SELECT b.wayBillNo from EtmTBAssignment b where b.wayBillNo = ?1 and (b.etmSubmitStatus=false or b.etmSubmitStatus=true) ")
	String getWaybillNo(String wayBillNo);*/
}
