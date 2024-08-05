package com.idms.base.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idms.base.dao.entity.notificationEntity;
import com.idms.base.support.rest.RestConstants;

public interface FuelNotificationRepository extends JpaRepository<notificationEntity, Integer> {

	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where nl.module_id =?1 and date(created_date) =current_date and alert_type =?2 and display_id=?3 ")
	public notificationEntity checkExistingNotification(Integer moduleId,String code,String role);
	
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='FUEL' and depot_id=?1 and display_id=?2 order by id desc")
	public List<notificationEntity> getFuelNotifications(Integer id,String userName);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='TYRE' and depot_id=?1 and display_id=?2 order by id desc")
	public List<notificationEntity> getTyreNotifications(Integer id,String userName);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='FUEL' and is_read=true and depot_id=?1 and display_id=?2")
	public Integer getFuelReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='FUEL' and is_read=false and depot_id=?1 and display_id=?2")
	public Integer getFuelUnReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='TYRE' and is_read=true and depot_id=?1 and display_id=?2")
	public Integer getTyreReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='TYRE' and is_read=false and depot_id=?1 and display_id=?2")
	public Integer getTyreUnReadNotifications(Integer id,String role);

	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='ROUTE' and depot_id=?1 and display_id=?2 order by id desc")
	public List<notificationEntity> getRouteCrewNotifications(Integer depot, String name);

	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='ROUTE' and is_read=true and depot_id=?1 and display_id=?2")
	public Integer getRouteCrewReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='ROUTE' and is_read=false and depot_id=?1 and display_id=?2")
	public Integer getRouteCrewUnReadNotifications(Integer id,String role);
	
	
	
	//by gouri
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Ticket' and depot_id=?1 and display_id=?2 order by id desc")
	public List<notificationEntity> getAllTicketNotifications(Integer id,String userName);
	
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Ticket' and is_read=true and depot_id=?1 and display_id=?2")
	public Integer getTicketReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Ticket' and is_read=false and depot_id=?1 and display_id=?2")
	public Integer getTicketUnReadNotifications(Integer id,String role);
	
	//by Rohit
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Driver' and depot_id=?1 and display_id=?2 order by id desc")
	public List<notificationEntity> getAllDriverNotifications(Integer id,String userName);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Driver' and is_read=true and depot_id=?1 and display_id=?2")
	public Integer getDriverReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Driver' and is_read=false and depot_id=?1 and display_id=?2")
	public Integer getDriverUnReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Conductor' and depot_id=?1 and display_id=?2 order by id desc")
	public List<notificationEntity> getAllConductorNotifications(Integer id,String userName);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Conductor' and is_read=true and depot_id=?1 and display_id=?2")
	public Integer getConductorReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Conductor' and is_read=false and depot_id=?1 and display_id=?2")
	public Integer getConductorUnReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select * from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Bus' and depot_id=?1 and display_id=?2 order by id desc")
	public List<notificationEntity> getAllBusNotifications(Integer id,String userName);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Bus' and is_read=true and depot_id=?1 and display_id=?2")
	public Integer getBusReadNotifications(Integer id,String role);
	
	@Query(nativeQuery=true,value="select count(*) from "+RestConstants.SCHEMA_NAME+".notification_log nl where date(created_date) =current_date and module ='Bus' and is_read=false and depot_id=?1 and display_id=?2")
	public Integer getBusUnReadNotifications(Integer id,String role);
	
	@Query(value="select mbd.bus_reg_number from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association bta " 
 +"left join "+RestConstants.SCHEMA_NAME+"."+"mst_bus_details mbd  on mbd.bus_id =bta.bus_id where bta.tyre_id =?1",nativeQuery=true)
	String getBusNumber(Integer id);
	
	@Query(value="select btah.removal_date from "+RestConstants.SCHEMA_NAME+"."+"bus_tyre_association_history "
+ "btah where btah.tyre_id =?1",nativeQuery=true)
	Date getTyreRemovalDate(Integer id);
	
	
}