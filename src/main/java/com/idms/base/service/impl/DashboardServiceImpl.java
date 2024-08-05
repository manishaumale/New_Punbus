package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.BusDetailsDto;
import com.idms.base.api.v1.model.dto.ConductorMasterDto;
import com.idms.base.api.v1.model.dto.DashboardDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.EmploymentTypeDto;
import com.idms.base.api.v1.model.dto.RouteCategoryDto;
import com.idms.base.api.v1.model.dto.RouteDetailsDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.RouteMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	RouteMasterRepository routeRepo;
	
	@Autowired
	TransportUnitRepository transportRepo;
	
	@Autowired
	BusMasterRepository busRepo;
	
	@Autowired
	DriverMasterRepository driverRepo;
	
	@Autowired
	ConductorMasterRepository conductorRepo;
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired
	TripMasterRepository tripMasterRepository;
	
	@Override
	public DashboardDto getSADashboard(String tpGroups) {
		DashboardDto dto = new DashboardDto();
		List<TransportDto> transportList = new ArrayList<>();
		try {
			String[] tpGrp = tpGroups.split(",");
			transportList = transportRepo.findTPUByGroupIds(tpGrp).stream()
					.map(tpu -> new TransportDto(tpu.getId(), tpu.getTransportName())).collect(Collectors.toList());
			
			List<Integer> tpIds = new ArrayList<Integer>();
			for(TransportDto tp : transportList) {
				tpIds.add(tp.getId());
			}
			
			List<Object[]> routeList =  routeRepo.getTotalRoutes(tpIds);
			for(Object[] o : routeList) {
				dto.setTotalRoutes(Integer.parseInt(o[0].toString()));
				dto.setActiveRoutes(Integer.parseInt(o[1].toString()));
			}
			
			List<Object[]> busList = busRepo.getTotalBuses(tpIds);
			for(Object[] o : busList) {
				dto.setTotalBuses(Integer.parseInt(o[0].toString()));
				dto.setActiveBuses(Integer.parseInt(o[1].toString()));
			}
			
			List<Object[]> driverList = driverRepo.getTotalDrivers(tpIds);
			for(Object[] o : driverList) {
				dto.setTotalDrivers(Integer.parseInt(o[0].toString()));
				dto.setActiveDrivers(Integer.parseInt(o[1].toString()));
			}
			
			List<Object[]> conductorList = conductorRepo.getTotalConductors(tpIds);
			for(Object[] o : conductorList) {
				dto.setTotalConductor(Integer.parseInt(o[0].toString()));
				dto.setActiveConductor(Integer.parseInt(o[1].toString()));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public DashboardDto getDADashboard(String dpCode) {
		DashboardDto dto = new DashboardDto();
		try {
			DepotMaster depot = depotRepo.findByDepotCode(dpCode);
			
			List<Object[]> routeList =  routeRepo.getTotalRoutesByDepot(depot.getId());
			for(Object[] o : routeList) {
				dto.setTotalRoutes(Integer.parseInt(o[0].toString()));
				dto.setActiveRoutes(Integer.parseInt(o[1].toString()));
			}
			
			List<Object[]> busList = busRepo.getTotalBusesByDepot(depot.getId());
			for(Object[] o : busList) {
				dto.setTotalBuses(Integer.parseInt(o[0].toString()));
				dto.setActiveBuses(Integer.parseInt(o[1].toString()));
			}
			
			List<Object[]> driverList = driverRepo.getTotalDriversByDepot(depot.getId());
			for(Object[] o : driverList) {
				dto.setTotalDrivers(Integer.parseInt(o[0].toString()));
				dto.setActiveDrivers(Integer.parseInt(o[1].toString()));
			}
			
			List<Object[]> conductorList = conductorRepo.getTotalConductorsByDepot(depot.getId());
			for(Object[] o : conductorList) {
				dto.setTotalConductor(Integer.parseInt(o[0].toString()));
				dto.setActiveConductor(Integer.parseInt(o[1].toString()));
			}
			
			Integer minDriversCondRequired = tripMasterRepository.fetchMinimumDriversAndCondRequired();
			if(minDriversCondRequired != null)
				dto.setMinDriversAndConRequired(minDriversCondRequired);
		} catch(Exception e) {
			
		}
		return dto;
	}

	@Override
	public DashboardDto getTotalRouteDetails(String dpCode) {
		DashboardDto dto = new DashboardDto();
		RouteDetailsDto list =null;
		List<RouteDetailsDto>  routeList= new ArrayList<>();
		try{
		DepotMaster depot = depotRepo.findByDepotCode(dpCode);
		List<Object[]> obj = routeRepo.getTotalRouteDetails(depot.getId());
		
		for(Object[] ob : obj)
		{
			list = new RouteDetailsDto();
			if(ob[0]!=null)
			   list.setTransUnit(ob[0].toString());
			if(ob[1]!=null)
				list.setDepot(ob[1].toString());
			if(ob[2]!=null)
				list.setRouteCode(ob[2].toString());
			if(ob[3]!=null)
				list.setRouteId(ob[3].toString());
			if(ob[4]!=null)
				list.setRouteName(ob[4].toString());
			if(ob[5]!=null)
				list.setRouteCatName(ob[5].toString());
			if(ob[6]!=null)
				list.setRouteTypeName(ob[6].toString());
			routeList.add(list);
		}
		dto.setRouteList(routeList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public DashboardDto getTotalBusDetails(String dpCode, String type) {
		BusDetailsDto list =null;
		List<BusDetailsDto>  routeList= new ArrayList<>();
		DashboardDto  dto= new  DashboardDto();
		try{
		DepotMaster depot = depotRepo.findByDepotCode(dpCode);
		List<Object[]> obj = null;//routeRepo.getTotalRouteDetails(depot.getId());		
		if(type.equals("Active"))
			obj=busRepo.getActiveTotalBusDetails(depot.getId());
		else
			obj=busRepo.getTotalBusDetails(depot.getId());
		
		for(Object[] ob : obj)
		{
			list = new BusDetailsDto();
			if(ob[0]!=null)
			   list.setDepot(ob[0].toString());
			if(ob[1]!=null)
				list.setBusRegNo(ob[1].toString());
			if(ob[2]!=null)
				list.setBusPassDate(ob[2].toString());
			if(ob[3]!=null)
				list.setBusModel(ob[3].toString());
			if(ob[4]!=null)
				list.setBusType(ob[4].toString());
			if(ob[5]!=null)
				list.setBusSubType(ob[5].toString());
			if(ob[6]!=null)
				list.setManufeacturer(ob[6].toString());
			routeList.add(list);
		}
		dto.setBusList(routeList);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public List<DashboardDto> getAllConductorDetails(String depoCode, String type) {
		List<ConductorMasterDto> conductorlist= new ArrayList<>();
		DashboardDto dash= new DashboardDto();
		List<DashboardDto> conductorList= new ArrayList<>();
		List<Object[]> allActiveConductorList =null;
		try
		{
			DepotMaster depot = depotRepo.findByDepotCode(depoCode);
			if(type.equals("Active"))
			 allActiveConductorList = conductorRepo.getAllActiveConductorList(depot.getId());
			else
			 allActiveConductorList = conductorRepo.getAllConductorList(depot.getId());
			for(Object[] ob:allActiveConductorList)
			{
			ConductorMasterDto c = new ConductorMasterDto();
			
			   if(ob[0]!=null)
			   c.setConductorCode(ob[0].toString());
			   if(ob[1]!=null)
			   c.setConductorName(ob[1].toString());
			   if(ob[2]!=null)
			   c.setMobileNumber(ob[2].toString());
			   EmploymentTypeDto e= new EmploymentTypeDto();
			   if(ob[3]!=null)
			   e.setEnrolmentName(ob[3].toString());
			   c.setEmploymentType(e);
			   DepotMasterDto depo = new DepotMasterDto();
			   if(ob[4]!=null)
			   depo.setDepotCode(ob[4].toString());
			   c.setDepot(depo);
			   TransportDto t= new TransportDto();
			   if(ob[5]!=null)
			   t.setTransportName(ob[5].toString());
			   c.setTransportUnit(t);
			   RouteCategoryDto route= new RouteCategoryDto();
			   if(ob[6]!=null)
			   route.setRouteCategoryName(ob[6].toString());
			   c.setConductorCategory(route);
			   conductorlist.add(c);
			   dash.setConductorList(conductorlist);
			}
			conductorList.add(dash);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return conductorList;
	}

	@Override
	public List<DashboardDto> getAllDriverDetails(String depoCode, String type) {

		List<DriverMasterDto> driverlist= new ArrayList<>();
		DashboardDto dash= new DashboardDto();
		List<DashboardDto> driverList= new ArrayList<>();
		List<Object[]> allDriverDetails =null;
   try
     {
	   DepotMaster depot = depotRepo.findByDepotCode(depoCode);
	   //List<Object[]> allDriverDetails = driverRepo.getAllDriverDetails(depot.getId());
	   if(type.equals("Active"))
		   allDriverDetails = driverRepo.getAllActiveDriverDetails(depot.getId());
		else
		   allDriverDetails = driverRepo.getAllDriverDetails(depot.getId());
	   for(Object[] ob:allDriverDetails )
	   {
		   DriverMasterDto d= new DriverMasterDto();
		   if(ob[0]!=null)
		   d.setDriverCode(ob[0].toString());
		   if(ob[1]!=null)
		   d.setDriverName(ob[1].toString());
		   if(ob[2]!=null)
		   d.setMobileNumber(ob[2].toString());
		   EmploymentTypeDto e= new EmploymentTypeDto();
		   if(ob[3]!=null)
		   e.setEnrolmentName(ob[3].toString());
		   d.setEmploymentType(e);
		   DepotMasterDto depo = new DepotMasterDto();
		   if(ob[4]!=null)
		   depo.setDepotCode(ob[4].toString());
		   d.setDepot(depo);
		   TransportDto t= new TransportDto();
		   if(ob[5]!=null)
		   t.setTransportName(ob[5].toString());
		   d.setTransportUnit(t);
		   RouteCategoryDto route= new RouteCategoryDto();
		   if(ob[6]!=null)
		   route.setRouteCategoryName(ob[6].toString());
		   d.setDriverCategory(route);
		   driverlist.add(d);
		   dash.setDriverList(driverlist);
		   
		   
	   }
	   driverList.add(dash);
	
    }
   catch (Exception e)
   {
	   e.printStackTrace();
   }
		return driverList;
	}	

}
