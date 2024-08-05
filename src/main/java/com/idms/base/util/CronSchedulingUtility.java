package com.idms.base.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.ConductorMaster;
import com.idms.base.dao.entity.DailyRoasterAuto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DriverMaster;
import com.idms.base.dao.entity.RoasterAuto;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.entity.VTSBusDieselEntity;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.ConductorMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.RoasterAutoRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.dao.repository.VTSBusDieselRepository;

import lombok.extern.log4j.Log4j2;

/**
*
* @author Hemant Makkar
*/
@Component
@Log4j2
public class CronSchedulingUtility {
	
	
	@Autowired
	DepotMasterRepository depotRepo;
	
	@Autowired 
	TripMasterRepository tripRepo;
	
	@Autowired
	DriverMasterRepository driverRepo;
	
	@Autowired
	ConductorMasterRepository conductorRepo;
	
	@Autowired
	BusMasterRepository busRepo;
	
	@Autowired
	RoasterAutoRepository roasterRepo;
	
	@Autowired
	VTS_Util vtsUtil;
	
	@Autowired
	VTSBusDieselRepository vTSBusDieselRepository;
	
	//@Scheduled(cron = "0 55 14 ? * *")
	public void schedulingJob() {

		DepotMaster depot = depotRepo.findByDepotCode("DP-JAL-1");
		List<TripMaster> tripList = tripRepo.findByRouteType(depot.getId(), 1);
		DailyRoasterAuto dailyObj = null;
		Date rotaDate = this.getDate(1, new Date());
		RoasterAuto roaster = new RoasterAuto();
		List<DailyRoasterAuto> drList = new ArrayList<>();
		int i = 0;
		Calendar c = Calendar.getInstance();
		TransportUnitMaster tu = new TransportUnitMaster();
		tu.setId(1);
		roaster.setDepot(depot);
		roaster.setTransport(tu);
		roaster.setGenerationDate(new Date());
		roaster.setIsNormalRota(true);
		roaster.setIsSpecialRota(false);
		Date dt = new Date();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		roaster.setRotaDate(this.getDate(1, new Date()));
		roaster.setRoastedCode(depot.getId() + "/" + 1 + "/" + sdf.format(dt));
		try {
			for (TripMaster trip : tripList) {
				/*
				 * boolean flag = true; for (RoutePermitMaster rpm :
				 * trip.getRouteMaster().getRoutePermitMasterList()) {
				 * PermitDetailsMaster permitDto = rpm.getPermitDetailsMaster();
				 * if (permitDto.getValidUpTo() != null &&
				 * (permitDto.getValidUpTo().after(today) ||
				 * formatter.format(permitDto.getValidUpTo()) ==
				 * (formatter.format(today)))) { flag=true; } else { flag=false;
				 * break; } } if(flag){ tripListWithPermit.add(trip); }
				 */
				dailyObj = new DailyRoasterAuto();

				List<Object[]> driverList = driverRepo.getAvailableDrivers(depot.getId(), 1, rotaDate);
				DriverMaster driver = new DriverMaster();
				driver.setId(Integer.parseInt((driverList.get(i)[0]).toString()));
				dailyObj.setDriver(driver);

				List<Object[]> conductorList = conductorRepo.getAvailableConductors(depot.getId(), 1, rotaDate);
				ConductorMaster conductor = new ConductorMaster();
				conductor.setId(Integer.parseInt((conductorList.get(i)[0].toString())));
				dailyObj.setConductor(conductor);

				List<Object[]> busList = busRepo.getAvailableBusesForRota(depot.getId(), 1, rotaDate);
				BusMaster busMaster = new BusMaster();
				busMaster.setId(Integer.parseInt((busList.get(i)[0].toString())));
				dailyObj.setBus(busMaster);

				dailyObj.setRotaAuto(roaster);
				dailyObj.setTripStatus(false);
				dailyObj.setRoute(trip.getRouteMaster());
				dailyObj.setIsDeleted(false);
				dailyObj.setRouteType(trip.getRouteMaster().getRouteTypeMaster());
				dailyObj.setTrip(trip);
				drList.add(dailyObj);
				i++;
			}
			roaster.setDailyRoasterList(drList);
			roaster = roasterRepo.save(roaster);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
	
	//@Scheduled(cron = "0 55 14 ? * *")
	/*public void insertDataForDieselJob() throws JSONException{
		log.info("Entering into Insert Data For Diesel Job");
		List<BusMaster> bus = busRepo.findAll();
		
		if(bus.size()>0){
			for(BusMaster busmaster: bus)
			{
				String response = vtsUtil.VTS_insert_diesel_api(busmaster.getBusRegNumber(),new Date(), "0");
				JSONObject jsonObj = new JSONObject(response);
				JSONArray root = jsonObj.getJSONArray("output");			
				String status = "";
				
				for (int i = 0; i < root.length(); i++) {
					JSONObject jsonObj2 = root.getJSONObject(i);

					try {
						status = jsonObj2.getString("status");
					} catch (Exception e) {
						log.info("Error occured while retriving vts data , please check the vts api.");
					}
				}
				
				if (status.equals("ERROR")) {
					log.info("Getting error while inserting diesel data for bus number :-"+busmaster.getBusRegNumber());
				}
				if (status.equals("SUCCESS")) {
					log.info("Successfully record added for bus number :-"+busmaster.getBusRegNumber());
				}
				
			}	
			
		}else{
			log.info("No data found from DB"+ bus.size());
		}
	}*/
	
@Scheduled(cron = "0 00 04 ? * *")
public void fetchDataForDieselJob() {
		log.info("Entering into fetch Data For Diesel Job");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<BusMaster> bus = busRepo.findAllByStatusAndIsDeleted(true, false);
			Date date = null;
			String todayDate = null;

			if (bus.size() > 0) {
				for (BusMaster busmaster : bus) {
					date = getDate(-1, new Date());
					todayDate = sdf.format(date);
					List<VTSBusDieselEntity> list = vTSBusDieselRepository.findAllByBusnoAndDate(busmaster.getBusRegNumber(), date);
					if (list.size() == 0) {
						VTSBusDieselEntity vTSBusDieselEntity = null;
						String response = vtsUtil.VTS_tyre_wise_kms_api(busmaster.getBusRegNumber(), "");
						JSONObject jsonObj = new JSONObject(response);

						if (jsonObj.has("output")) {
							JSONArray root = jsonObj.getJSONArray("output");
							if (root.length() > 0) {
								for (int i = 0; i < root.length(); i++) {
									JSONObject jsonObj2 = root.getJSONObject(i);

									vTSBusDieselEntity = new VTSBusDieselEntity();
									vTSBusDieselEntity.setBus_reg_no(jsonObj2.getString("reg_no"));
									vTSBusDieselEntity.setDate(sdf.parse(jsonObj2.getString("date")));
									vTSBusDieselEntity.setKms_covered(Double.valueOf(jsonObj2.getString("kms_covered")));
									vTSBusDieselEntity.setCreatedOn(new Timestamp(System.currentTimeMillis()));
									vTSBusDieselEntity.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
									vTSBusDieselRepository.save(vTSBusDieselEntity);
								}
							} else {
								log.info("No data found from VTS api for bus number :-" + busmaster.getBusRegNumber());
							}
						} else {
							log.info("Vts Api response for  bus number " + busmaster.getBusRegNumber() + " is "
									+ jsonObj.getString("status"));
						}
					} else {
						log.info("Record already present for bus number " + busmaster.getBusRegNumber() + " in given date" + todayDate);
					}
				}
			} else {
				log.info("No data found from DB" + bus.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Date getDate(Integer i, Date dt) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dt = sdf.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}
	
	/*public static void main(String  args[])
	{
		
		System.out.println(getDate(-1, new Date()));
	}*/
}
