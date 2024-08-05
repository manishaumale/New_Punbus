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

import com.idms.base.dao.entity.DriverUnavailabilityMaster;
import com.idms.base.dao.entity.QDriverUnavailabilityMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface DriverUnavailableRepository extends JpaRepository<DriverUnavailabilityMaster, Integer> , QuerydslPredicateExecutor<DriverUnavailabilityMaster>, QuerydslBinderCustomizer<QDriverUnavailabilityMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QDriverUnavailabilityMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}
	
	@Query(value = "select detail.driver_name,reason.reason,unavailable.to_date as unavailableupto,unavailable.detailed_reason,employ.enrolment_name,"
			+ " unavailable.from_date as unavailablefrom, unavailable.driver_un_availability_id,unavailable.from_date,unavailable.to_date from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details As detail "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable unavailable on unavailable.driver_id = detail.driver_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason reason on unavailable.driver_unavailability_reason_id= reason.reason_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depot on depot.depot_id = detail.depot_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type employ on detail.employment_type = employ.enrolment_id "
			+ " where detail.employment_type= ?1 and detail.transport_unit=?2 and unavailable.to_date >= cast(now() as DATE) and depot.depot_code=?3 ",nativeQuery = true)
	List<Object[]> listOfUnavailableDrivers(Integer driverTypeId,Integer transportId, String dpCode);
	
	@Query(value = "select employ.enrolment_name as driverType,employ.enrolment_id,count(detail.*) as total_drivers,count(mdu.*) as unavailable_drivers, transport.transport_name,transport.transport_id from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details as detail "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type  employ on detail.employment_type = employ.enrolment_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_transport transport on detail.transport_unit = transport.transport_id and group_id in ?1 "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id=detail.depot_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu on mdu.driver_id = detail.driver_id and mdu.driver_un_availability_id in "
			+ " (select max(a.driver_un_availability_id) from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable a where a.driver_id=mdu.driver_id and a.to_date >= cast(now() as DATE)) "
			+ " where detail.status=true and detail.is_deleted=false and md.depot_code=?2 "
			+ " group by employ.enrolment_name,employ.enrolment_id,transport.transport_name,transport.transport_id",nativeQuery = true)
	List<Object[]> listOfAllDrivers(List<String> groupList, String dpCode);
	
	@Modifying
	@Query(value = "Update DriverUnavailabilityMaster  set toDate=?2 where id=?1 ")
	int updateDriverToDate(Integer id, Date readyDate);
	
	
	@Query(value = "select detail.driver_name,reason.reason,unavailable.to_date as unavailableupto,unavailable.detailed_reason,employ.enrolment_name,"
			+ " unavailable.from_date as unavailablefrom, unavailable.driver_un_availability_id,unavailable.from_date,unavailable.to_date,status.name,u.username,unavailable.modified_on from "+RestConstants.SCHEMA_NAME+"."+"mst_driver_details As detail "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable unavailable on unavailable.driver_id = detail.driver_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable_reason reason on unavailable.driver_unavailability_reason_id= reason.reason_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_depot depot on depot.depot_id = detail.depot_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_enrolment_type employ on detail.employment_type = employ.enrolment_id "
			+ " join "+RestConstants.SCHEMA_NAME+"."+"mst_workflow_status status on status.status_id = unavailable.status_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"users u on u.id = unavailable.modified_by "
			+ " where  depot.depot_code=?1 ",nativeQuery = true)
	List<Object[]> listOfAllUnavailableDriversByStatusId(String dpCode,Integer statusId);
	
	@Modifying
	@Query(value = "Update DriverUnavailabilityMaster  set status_id=?2,modified_by=?3,modified_on = NOW() where id=?1 ")
	int updateDriverApprovalStatus(Integer id, Integer statusId,String approvedBy);
	
	@Modifying
	@Query(value = "Update DriverUnavailabilityMaster  set status_id=?2,modified_by=?3,modified_on = NOW() where id=?1 ")
	int updateDriverApprovalStatusAndRejectedBy(Integer id, Integer statusId,String rejectedBy);
	
	@Query(value = "select mdu.driver_id ,mdd.driver_name ,mdd.driver_code ,mdu.from_date ,mdu.to_date from " +RestConstants.SCHEMA_NAME+"."+"mst_driver_details mdd join "
	+RestConstants.SCHEMA_NAME+"."+"mst_driver_unavailable mdu on mdd.driver_id =mdu.driver_id where mdd.depot_id =3 and current_date between mdu.from_date and mdu.to_date",nativeQuery = true)
	List<Object[]> unvailableDriverAlert( Integer dpCode);
}
