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

import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.entity.QPermitDetailsMaster;
import com.idms.base.dao.entity.RouteMaster;
import com.querydsl.core.types.dsl.StringPath;

@Repository
public interface PermitDetailsMasterRepository extends JpaRepository<PermitDetailsMaster, Integer> , QuerydslPredicateExecutor<PermitDetailsMaster>, QuerydslBinderCustomizer<QPermitDetailsMaster> {
	
	@Override
	default void customize(QuerydslBindings bindings, QPermitDetailsMaster root) {
		// Exclude the Id from query filter
		bindings.excluding(root.id);

		// Perform IgnoreCase & EqualsLike comparison
		bindings.bind(String.class).first(
				(StringPath path, String value) -> path.containsIgnoreCase(value)
		);		
	}

	List<PermitDetailsMaster> findAllByStatus(boolean status);
	
	List<PermitDetailsMaster> findAll();
	
	List<PermitDetailsMaster> findAllByIsDeleted(boolean isDeleted);
	
	@Query(value = "SELECT p from PermitDetailsMaster p where p.isDeleted=false and p.depotMaster.depotCode=?1 order by p.id desc")
	List<PermitDetailsMaster> findAllByDepotCode(String dpCode);
	
	@Query(value = "SELECT p from PermitDetailsMaster p where p.status=?1 and p.isDeleted=?2 and p.depotMaster.id=?3 ")
	List<PermitDetailsMaster> findAllByStatusAndIsDeleted(boolean status,boolean flag, Integer depotId);
	
	@Modifying
    @Query(value = "Update PermitDetailsMaster permit set permit.document.id=?2,permit.issueDate=?3,permit.validUpTo=?4 where id=?1 ")
	int updatePermitDetailsForRenewal(Integer permitId, Integer docId,Date issueDate, Date validUpTo);
	
	@Modifying
    @Query(value = "Update PermitDetailsMaster  set status=?1 where id=?2 ")
	int updatePermitMasterStatusFlag(Boolean flag, Integer id);
	
	@Modifying
    @Query(value = "Update PermitDetailsMaster  set isDeleted=?1 where id=?2 ")
	int updatePermitMasterIsDeletedFlag(Boolean flag, Integer id);

	@Query(value = "SELECT p from PermitDetailsMaster p where p.isDeleted=false and p.status=true and p.depotMaster.id=?1 and transport.id=?2 ")
	List<PermitDetailsMaster> getPermitsForTransportAndDepot(Integer dpId, Integer tpId);

	@Query(value="from PermitDetailsMaster p where p.isDeleted=false and p.status=true and p.depotMaster.id=?1 and p.validUpTo >=?2")
	List<PermitDetailsMaster> getRecordByStatusAndExp(Integer depoId, Date date);
	
	
	@Query(value=" select sum(p.totalKms) from PermitDetailsMaster p where p.isDeleted=false and p.status=true and p.id IN (?1)")
	Long getToatalKm(List<Integer> permitId);
	
	@Query(value = "from PermitDetailsMaster p where  p.id=?1 ")
	PermitDetailsMaster findPermitById(Integer permitId);
	
}