package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.AucDocketTyreAssociation;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface AucDocketTyreAssociationRepository extends JpaRepository<AucDocketTyreAssociation, Integer> {

	/*@Query(value=" select mt.tyre_id, mt.tyre_number, mtm.name as makername, mts.size, mtt.name as type "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"auc_docket_tyre_association adta on adta.tyre_id = mt.tyre_id "
			+ " where adta.docket_id=?1 ", nativeQuery=true)
	List<Object[]> findTyreListByDocketId(Integer docketId);*/
	
	@Query(value="select mt.tyre_id, mt.tyre_number,mt.tyre_tag,mtm.name as manufacture, mts.size,"
        +"mtt.name as type,mt2.transport_name ,md.depot_code,mt.tyre_condition_id,mtc.name " 
		+"from "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt " 
		+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make " 
		+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id = mt.tyre_size " 
		+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_type mtt on mtt.tyre_type_id = mt.tyre_type " 
		+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_transport mt2  on mt.transport_unit = mt2.transport_id " 
		+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_depot md  on mt.depot_id = md.depot_id " 
		+"left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on mtc.tyre_condition_id = mt.tyre_condition_id "
		+"left join "+RestConstants.SCHEMA_NAME+"."+"auc_docket_tyre_association adta on adta.tyre_id = mt.tyre_id " 
		+"where adta.docket_id=?1 ",nativeQuery= true)
	List<Object[]> findTyreListByDocketId(Integer docketId);

}
