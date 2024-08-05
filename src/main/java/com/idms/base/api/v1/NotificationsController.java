package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.NotificationCountList;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.notificationEntity;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.support.rest.RestConstants;
import com.idms.base.util.AlertUtility;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/notifications")
public class NotificationsController {

	@Autowired
	AlertUtility alerts;
	
	@Autowired
	DepotMasterRepository depotRepository;
	
	@GetMapping("/getFuelNotifications")
	public List<notificationEntity> getFuelNotifications(@RequestParam("depot")String depot,@RequestParam("userName")String userName) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
	return alerts.getAllFuelNotifications(depotMaster.getId(),userName);
	}

	@GetMapping("/getTyreNotifications")
	public List<notificationEntity> getTyreNotifications(@RequestParam("depot")String depot,@RequestParam("userName")String userName) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
	return alerts.getAllTyreNotifications(depotMaster.getId(),userName);
	}
	
	@PostMapping("/markAsRead")
	public Boolean markNotificationAsRead(@RequestParam("id")Integer id,@RequestParam("userName")String user) {
		return alerts.markNotificationAsRead(id, user);
	}
	
	@GetMapping("/getCounts")
	public NotificationCountList getCounts(@RequestParam("depot")String depot,@RequestParam("userName")String user) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
		return alerts.listgetFuelCounts(depotMaster.getId(),user);
	}
	
	@GetMapping("/getRouteCrewNotifications")
	public  List<notificationEntity> getRouteCrewNotifications(@RequestParam("depot")String depot,@RequestParam("userName")String userName) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
	return alerts.getAllRouteCrewNotifications(depotMaster.getId(),userName);
	}
	
	
	@GetMapping("/getTicketNotifications")
	public List<notificationEntity> getAllTicketNotification(@RequestParam("depot")String depot,@RequestParam("userName")String userName) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
	return alerts.getAllTicketNotifications(depotMaster.getId(),userName);
	}
	
	@GetMapping("/getDriverNotifications")
	public List<notificationEntity> getDriverNotifications(@RequestParam("depot")String depot,@RequestParam("userName")String userName) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
	return alerts.getAllDriverNotifications(depotMaster.getId(),userName);
	}
	
	@GetMapping("/getConductorNotifications")
	public List<notificationEntity> getConductorNotifications(@RequestParam("depot")String depot,@RequestParam("userName")String userName) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
	return alerts.getAllConductorNotifications(depotMaster.getId(),userName);
	}
	
	@GetMapping("/getBusNotifications")
	public List<notificationEntity> getBusNotifications(@RequestParam("depot")String depot,@RequestParam("userName")String userName) {
		DepotMaster	depotMaster = depotRepository.findByDepotCode(depot);
	return alerts.getAllBusNotifications(depotMaster.getId(),userName);
	}
	
	
}
