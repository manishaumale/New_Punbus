package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.support.rest.RestConstants;

@Repository
public interface DocketTyreAssociationRepository extends JpaRepository<DocketTyreAssociation, Integer> {

	@Query(value=" select dta.dta_id, dta.tyre_id, mt.tyre_number, mts.size, mtm.name "
			+ " from "+RestConstants.SCHEMA_NAME+"."+"retreading_docket_details rdd "
			+ " inner join "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta on dta.docket_id = rdd.docket_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre mt on mt.tyre_id = dta.tyre_id "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_size mts on mts.tyre_size_id =mt.tyre_size "
			+ " left join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_maker mtm on mtm.tyre_maker_id = mt.tyre_make "
			+ " where rdd.docket_id = ?1 and dta.is_received=false" , nativeQuery=true)
	List<Object[]> findAllByDocketId(Integer docketId);

	@Query(value="SELECT dta from DocketTyreAssociation dta where dta.isReceived=false and dta.docket.id=?1 ")
	List<DocketTyreAssociation> findByDocketIdAndRec(Integer id);
	
	@Query(value="SELECT dta from DocketTyreAssociation dta where dta.newConditon.id=?1 and dta.tyre.id=?2 ")
	DocketTyreAssociation findByConditionIdAndTyreId(Integer conditionId,Integer tyreId);
	
	@Query(value="SELECT dta from DocketTyreAssociation dta where  dta.docket.id=?1 ")
	List<DocketTyreAssociation> findByDocketId(Integer id);
	
	@Query(value="SELECT dta from DocketTyreAssociation dta where dta.id=?1 ")
	DocketTyreAssociation findObjectByDocketId(Integer id);
	
	@Query(nativeQuery=true,value="SELECT dta from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta where dta.tyre_id=?1 order by created_on desc limit 1 ")
	DocketTyreAssociation findObjectByTyreId(Integer id);
	
	@Query(nativeQuery=true,value="SELECT * from "+RestConstants.SCHEMA_NAME+"."+"docket_tyre_association dta join "+RestConstants.SCHEMA_NAME+"."+"mst_tyre_condition mtc on dta.new_conditon_id =mtc.tyre_condition_id where dta.tyre_id=?1 and mtc.name=?2 order by dta.created_on desc limit 1 ")
	DocketTyreAssociation findObjectByTyreIdAndCondition(Integer id,String condition);

}
