package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ConcessionTicketEntity;

@Repository
public interface ConcessionTicketMstRepository extends JpaRepository<ConcessionTicketEntity,Integer>   {

}
