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

import com.idms.base.dao.entity.BusTyreAssociation;
import com.idms.base.dao.entity.QBusTyreAssociation;
import com.idms.base.support.rest.RestConstants;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface BusTyreAssociationRepository extends JpaRepository<BusTyreAssociation, Integer> , QuerydslPredicateExecutor<BusTyreAssociation>, QuerydslBinderCustomizer<QBusTyreAssociation> {
	
	@Override
	default void customize(QuerydslBindings bindings, QBusTyreAssociation root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<BusTyreAssociation> findAllByStatus(boolean status);

	@Query(value=" select mbd.bus_reg_number , mt.tyre_number , mtp.name, bta.install_date, bta.bus_fitted, bta.kms_done,mts.size, "
			+ " mtm.name as makername, mtt.name as ttype,mt.tyre_tag,mt.tyre_id,mbd.bus_id "
			+ " from  "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta "
			+ " join  "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on mbd.bus_id = bta.bus_id "
			+ " join  "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on mt.tyre_id = bta.tyre_id "
			+ " join  "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_position mtp on mtp.tyre_position_id = bta.tyre_position_id "
			+ " join "+RestConstants.SCHEMA_NAME + "." + "mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make " + " join "
			+ RestConstants.SCHEMA_NAME + "." + "mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
			+ " join " + RestConstants.SCHEMA_NAME + "." + "mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type  "
			+ " where bta.bus_id = ?1 ", nativeQuery=true)
	List<Object[]> findAllByBusId(Integer busId);
	
	@Query(value = "SELECT t from BusTyreAssociation t where t.bus.id=?1")
	List<BusTyreAssociation> findAllByBusId1(Integer busId);
	
	@Query(value = "SELECT t from BusTyreAssociation t where t.bus.depot.depotCode=?1 and t.createdOn >=?2 and t.createdOn <=?3")
	List<BusTyreAssociation> findAllByDepot(String depotCode,Date startDate, Date endDate);
	
	@Query(value="select md.depot_name, mt2.transport_name as bus_unit_type, mbd.bus_reg_number as bus_number, mt.tyre_number, mtm.name as makername,"
+"mts.size "+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md on md.depot_id = mt.depot_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2 on mt2.transport_id = mt.transport_unit "
+"left join "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta on bta.tyre_id = mt.tyre_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd on bta.bus_id = mbd.bus_id "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
+"where mt.depot_id=?1 and to_date(to_char(mt.created_on, 'YYYY/MM/DD'), 'YYYY/MM/DD') between ?2 and ?3 "
+"group by md.depot_name, mt2.transport_name, mbd.bus_reg_number, mt.tyre_number, mtm.name, mts.size",nativeQuery=true)
List<Object[]>findAllByDepot(Integer depotId,Date startDate, Date endDate);

@Query(value=" select bus_id from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta where tyre_id =?1 ", nativeQuery=true)
 Integer[] findAllBusesByTyreId(Integer tyreId);

@Query(value = "SELECT t from BusTyreAssociation t where t.tyre.id=?1")
List<BusTyreAssociation> fetchAllAssociationByTyreId(Integer tyreId);

@Query(value = "SELECT t from BusTyreAssociation t where t.tyre.id=?1 and t.bus.id=?2")
List<BusTyreAssociation> fetchAllAssociationByTyreIdAndBusId(Integer tyreId,Integer busId);

@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".bus_tyre_association where bus_id =?1")
List<BusTyreAssociation> findByBusId(Integer id);

@Query(nativeQuery=true ,value="select created_on from "+RestConstants.SCHEMA_NAME+".bus_tyre_association where bus_id =?1 order by updated_on desc limit 1")
Date findTheAfterTyreDate(Integer id);

@Modifying
@Query(value = "Delete from BusTyreAssociation t  where t.id=?1 ")
void deleteByAssociationId(Integer id);

}