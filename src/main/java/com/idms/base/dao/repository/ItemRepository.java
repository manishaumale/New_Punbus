package com.idms.base.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.ItemMaster;

@Repository
public interface ItemRepository extends JpaRepository<ItemMaster,Integer> 
{

}
