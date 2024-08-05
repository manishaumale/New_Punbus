package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ConductorUnavailabilityMaster;
import com.idms.base.dao.entity.QConductorUnavailabilityMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface ConductorUnavailableRepository extends JpaRepository<ConductorUnavailabilityMaster, Integer> , QuerydslPredicateExecutor<ConductorUnavailabilityMaster>, QuerydslBinderCustomizer<QConductorUnavailabilityMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QConductorUnavailabilityMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<ConductorUnavailabilityMaster> findAllByStatus(boolean status);
	
	@Query(value = "select detail.conductor_name,reason.reason,unavailable.to_date as unavailableupto,unavailable.detailed_reason,employ.enrolment_name,unavailable.from_date as unavailablefrom, unavailable.conductor_un_availability_id from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details As detail "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable unavailable on unavailable.conductor_id = detail.conductor_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason reason on unavailable.conductor_unavailability_reason_id= reason.reason_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depot on depot.depot_id = detail.depot_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type employ on detail.employment_type = employ.enrolment_id "
			+ " where detail.employment_type= ?1 and detail.transport_unit=?2 and unavailable.to_date >= cast(now() as DATE) and depot.depot_code=?3 ",nativeQuery = true)
	List<Object[]> listOfUnavailableConductors(Integer conductorTypeId,Integer transportId, String dpCode);
	
	@Query(value = "select employ.enrolment_name as conductorType,employ.enrolment_id,count(detail.*) as total_conductor,count(mdu.*) as unavailable_conductor,transport.transport_name,transport.transport_id from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details as detail "
			+ " join  "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type  employ on detail.employment_type = employ.enrolment_id "
			+ " join  "+RestConstants.SCHEMA_NAME+"."+"mst_transport transport on detail.transport_unit = transport.transport_id and group_id in ?1 "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id=detail.depot_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mdu on mdu.conductor_id = detail.conductor_id and mdu.conductor_un_availability_id in "
			+ " (select max(a.conductor_un_availability_id) from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable a where a.conductor_id=mdu.conductor_id and a.to_date >= cast(now() as DATE)) "
			+ " where detail.status=true and detail.is_deleted=false and md.depot_code=?2 "
			+ " group by employ.enrolment_name,employ.enrolment_id,transport.transport_name,transport.transport_id",nativeQuery = true)
	List<Object[]> listOfAllConductors(List<String> groupList, String dpCode);
	
	@Modifying
	@Query(value = "Update ConductorUnavailabilityMaster  set toDate=?2 where id=?1 ")
	int updateConductorToDate(Integer id, Date readyDate);
	
	@Query(value = "select detail.conductor_name,reason.reason,unavailable.to_date as unavailableupto,unavailable.detailed_reason,employ.enrolment_name,"
			+ " unavailable.from_date as unavailablefrom, unavailable.conductor_un_availability_id,status.name,u.username,unavailable.modified_on from "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_details As detail "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable unavailable on unavailable.conductor_id = detail.conductor_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason reason on unavailable.conductor_unavailability_reason_id= reason.reason_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depot on depot.depot_id = detail.depot_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type employ on detail.employment_type = employ.enrolment_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_workflow_status status on status.status_id = unavailable.status_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"users u on u.id = unavailable.modified_by "
			+ " where  depot.depot_code=?1 ",nativeQuery = true)
	List<Object[]> listOfAllUnavailableConductors(String dpCode,Integer statusId);
	
	@Modifying
	@Query(value = "Update ConductorUnavailabilityMaster  set status_id=?2,modified_by=?3,modified_on = NOW() where id=?1 ")
	int updateConductorApprovalStatus(Integer id, Integer statusId,String approvedBy);
	
	@Modifying
	@Query(value = "Update ConductorUnavailabilityMaster  set status_id=?2,modified_by=?3,modified_on = NOW() where id=?1 ")
	int updateConductorApprovalStatusAndRejectedBy(Integer id, Integer statusId,String rejectedBy);
	
	@Query(value = "select mcu.conductor_id ,mcd.conductor_name ,mcd.conductor_code,mcu.from_date,mcu.to_date from " +RestConstants.SCHEMA_NAME+"."+"mst_conductor_details mcd join "+RestConstants.SCHEMA_NAME+"."+"mst_conductor_unavailable mcu on mcd.conductor_id =mcu.conductor_id"
			+ " where  mcd.depot_id = ?1 and current_date between mcu.from_date and mcu.to_date ",nativeQuery = true)
	List<Object[]> unvailableConductorsAlert( Integer dpCode);
}