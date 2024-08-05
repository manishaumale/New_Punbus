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

import com.idms.base.dao.entity.BusUnavailabilityMaster;
import com.idms.base.dao.entity.QBusUnavailabilityMaster;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;


@Repository
public interface BusUnavailableRepository extends JpaRepository<BusUnavailabilityMaster, Integer> , QuerydslPredicateExecutor<BusUnavailabilityMaster>, QuerydslBinderCustomizer<QBusUnavailabilityMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBusUnavailabilityMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<BusUnavailabilityMaster> findAllByStatus(boolean status);

	@Query("select detail.busRegNumber,detail.busType.busTypeName,unAvailablity.busUnavailablityReasonMaster.reason,unAvailablity.detailedReason, unAvailablity.id,"
			+ " unAvailablity.likelyToReadyDate,unAvailablity.detentionDate  from BusMaster As detail "
			+ " join BusUnavailabilityMaster As unAvailablity on detail.id = unAvailablity.busMaster.id "                                                       
			+ " join DepotMaster As depot on depot.id = detail.depot"
			+ " where detail.busType.id=?1 and unAvailablity.likelyToReadyDate >= CAST(now() AS date) and detail.transportUnit.id=?2 and depot.depotCode=?3 ")
	List<Object[]> listOfUnavailableBuses(Integer busTypeId, Integer transportId, String dpCode);

	@Query(value = "select busType.bus_type_name bus_type_name,count(distinct detail.bus_id) as total_buses,count(distinct mbu.bus_id) as unavailable_buses,busType.bus_type_id as bus_type_id,transport.transport_name as transport_name,transport.transport_id as TransportId from "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details detail "
			+ " join  "+RestConstants.SCHEMA_NAME+"."+"mst_bus_type busType on detail.bus_type = busType.bus_type_id join  "+RestConstants.SCHEMA_NAME+"."+"mst_transport transport on detail.transport_unit = transport.transport_id and transport.group_id in ?1 "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id=detail.depot "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_unavailable mbu on mbu.bus_id = detail.bus_id and mbu.un_availability_id in (select max(a.un_availability_id) from "+RestConstants.SCHEMA_NAME+"."+" mst_bus_unavailable a where a.bus_id=mbu.bus_id and a.likely_to_ready_date >= cast(now() as DATE) ) "
			+"  where detail.status=true and detail.is_deleted =false and md.depot_code=?2 "
			+ " group by busType.bus_type_name,busType.bus_type_id,transport.transport_name,transport.transport_id", nativeQuery = true)
	List<Object[]> listOfAllBuses(List<String> groupList, String dpCode);

	@Modifying
	@Query(value = "Update BusUnavailabilityMaster  set likelyToReadyDate=?2 where id=?1 ")
	int updateBusLikelyReadyDate(Integer id, Date likelyToBeReadyDate);
	
	
	
}