package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.AuctionedDocket;

@Repository
public interface AuctionedDocketRepository extends JpaRepository<AuctionedDocket, Integer>{

	@Query(value="SELECT ad from AuctionedDocket ad where ad.depot.id=?1 order by ad.id desc")
	List<AuctionedDocket> findAllByDepotId(Integer id);

}
