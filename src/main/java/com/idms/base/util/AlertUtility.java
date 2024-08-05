package com.idms.base.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.NotificationCount;
import com.idms.base.api.v1.model.dto.NotificationCountList;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusUnavailabilityMaster;
import com.idms.base.dao.entity.DipChartReadings;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.ReviewCommentsHistory;
import com.idms.base.dao.entity.TankInspection;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.entity.User;
import com.idms.base.dao.entity.notificationEntity;
import com.idms.base.dao.repository.BlockOrRouteOffRepository;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusUnavailableRepository;
import com.idms.base.dao.repository.DipChartReadingsRepository;
import com.idms.base.dao.repository.FuelNotificationRepository;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.ReviewCommentsRepository;
import com.idms.base.dao.repository.RoleRepository;
import com.idms.base.dao.repository.TankInspectionRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.dao.repository.UserRepository;
import com.idms.base.support.rest.RestConstants;

@Service
public class AlertUtility {

	@Autowired
	FuelTankMasterRepository fueltankRepo;

	@Autowired
	TyreMasterRepository tyreRepo;

	@Autowired
	FuelNotificationRepository fuelNotificationRepo;

	@Autowired
	TankInspectionRepository tankInspectionRepo;

	@Autowired
	DipChartReadingsRepository dipChartReadingRepo;
	
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;

	@Autowired
	TyreCostCalculationUtility tyreUtility;
	
	@Autowired
	BlockOrRouteOffRepository blockOrRouteOffRepository;
	
	@Autowired
	ReviewCommentsRepository reviewCommentsRepo;
	
	@Autowired
	BusUnavailableRepository busUnavailablityRepository;
	
	@Autowired
	BusMasterRepository busMasterRepository;
	
	String reorderTag = "FROA";
	String explosiveTag = "FELA";
	String cleaningTag = "FCTA";
	String roleIds[]=RestConstants.AlertRoles;
	
	@Scheduled(cron = "0 0/5 * * * * ")
	public void fuelReorderAlert() {
		List<FuelTankMaster> fuelTanks = fueltankRepo.findAll();
		if (fuelTanks.size() > 0) {
			try {
				for (FuelTankMaster master : fuelTanks) {
					if (master.getStatus() != null && master.getStatus()) {
						if (master.getReorderLevel() != null && master.getCurrentValue() != null
								&& (master.getReorderLevel() > master.getCurrentValue()) && master.getDepot() != null) {
							for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(master.getId(),
									reorderTag,s);
							if (d == null && s!=null && !s.isEmpty()) {
								insertNotification(master.getTankName() + " has reached less than reorder level",
										reorderTag, "FUEL", master.getId(), master.getDepot().getId(),s);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				for (FuelTankMaster master : fuelTanks) {
					if (master.getStatus() != null && master.getStatus() && master.getExplosiveDetails() != null) {
						if (master.getExplosiveDetails().getExplosiveValidity() != null) {
							long diffInMillies = Math.abs(master.getExplosiveDetails().getExplosiveValidity().getTime()
									- new Date().getTime());
							long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
							if (diff <= 30) {
								for(String s : roleIds) {
								notificationEntity d = fuelNotificationRepo.checkExistingNotification(master.getId(),
										explosiveTag,s);
								if (d == null && s!=null && !s.isEmpty() ) {
									insertNotification(
											master.getTankName() + " explosive license expires in less than 30 days",
											explosiveTag, "FUEL", master.getId(), master.getDepot().getId(),s);
								}
								}
							}
						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				for (FuelTankMaster master : fuelTanks) {
					if (master.getStatus() != null && master.getStatus()) {
						long diffInMillies = Math.abs(master.getCleaningDate().getTime() - new Date().getTime());
						long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
						if (diff <= 30) {
							for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(master.getId(),
									cleaningTag,s);
							if (d == null && s!=null && !s.isEmpty()) {
								insertNotification(master.getTankName() + " cleaning date is in less than 30 days",
										cleaningTag, "FUEL", master.getId(), master.getDepot().getId(),s);
							}}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	String tankInspectionAlert = "FTIA";

	@Scheduled(cron = "0 0/5 * * * * ")
	public void tankInspectionAlert() {
		List<TankInspection> tankInspection = tankInspectionRepo.findTomorrowInspections();
		if (tankInspection.size() > 0) {
			for (TankInspection ins : tankInspection) {
				for(String s : roleIds) {				notificationEntity d = fuelNotificationRepo.checkExistingNotification(ins.getId(), tankInspectionAlert,s);
				if (d == null && s!=null && !s.isEmpty()) {
					insertNotification("Fuel tanks are to be inspected tomorrow", tankInspectionAlert, "FUEL",
							ins.getId(), ins.getDepot().getId(),s);
				}}
			}
		}
	}

	String excessShortMorningTag = "FESMA";
    String excessShortEveningTag = "FESEA";
	@Scheduled(cron = "0 0/5 * * * * ")
	public void getExcessShortNotification() {
		DipChartReadings readings = dipChartReadingRepo.findMaxRecord();
		
		if (readings != null) {
			String todate = new SimpleDateFormat("dd-MM-yyyy").format(readings.getCreatedOn());
			if (readings.getVariationMorningFlag()) {
				for(String s : roleIds) {
				notificationEntity d = fuelNotificationRepo.checkExistingNotification(readings.getDip_id(),
						excessShortMorningTag,s);
				if (d == null && s!=null && !s.isEmpty()) {
					if(readings.getVariationMorning() < 0 ){
					insertNotification("Dip-Chart-Reading-Morning :- There is a short in fuel tanks on "+todate, excessShortMorningTag, "FUEL",
							readings.getDip_id(), readings.getDepotId().getId(),s);
					}else{
						insertNotification("Dip-Chart-Reading-Morning :- There is a excess in fuel tanks on "+todate, excessShortMorningTag, "FUEL",
								readings.getDip_id(), readings.getDepotId().getId(),s);
					}
				}
				}
			}

			if (readings.getVariationEveningFlag()) {
				for(String s : roleIds) {
				notificationEntity d = fuelNotificationRepo.checkExistingNotification(readings.getDip_id(),
						excessShortEveningTag,s);
				if (d == null && s!=null && !s.isEmpty()) {
					if(readings.getVariationEvening() < 0 ){
						insertNotification("Dip-Chart-Reading-Evening :- There is a short in fuel tanks on "+todate, excessShortEveningTag, "FUEL",
								readings.getDip_id(), readings.getDepotId().getId(),s);
						}else{
							insertNotification("Dip-Chart-Reading-Evening :- There is a excess in fuel tanks on "+todate, excessShortEveningTag, "FUEL",
									readings.getDip_id(), readings.getDepotId().getId(),s);
						}
				}
				}
			}
		}
	}

	@Scheduled(cron = "0 0/5 * * * * ")
	public void tankInspectionAlert2() { // method for todays inspection
		List<TankInspection> tankInspection = tankInspectionRepo.findTodayInspections();
		if (tankInspection.size() > 0) {
			for (TankInspection ins : tankInspection) {
				for(String s : roleIds) {
				notificationEntity d = fuelNotificationRepo.checkExistingNotification(ins.getId(), tankInspectionAlert,s);
				if (d == null && s!=null && !s.isEmpty()) {
					insertNotification("Fuel tanks are to be inspected today", tankInspectionAlert, "FUEL", ins.getId(),
							ins.getDepot().getId(),s);
				}
			}
		}
	}
	}

	String tyreLifeAlert = "TELA";
	String tyreRearAlert = "TNRA";

	@Scheduled(cron = "0 0/5 * * * * ")
	public void tyreExpectedLifeAlert() {
		List<TyreMaster> tyres = tyreRepo.findAll();
		if (tyres.size() > 0) {
			try {
				for (TyreMaster master : tyres) {
					Float kms = tyreUtility.calculateTyreTotalKms(master.getId());
					  String busNumber = fuelNotificationRepo.getBusNumber(master.getId());
					if (kms != null && kms > 0 && master.getStatus() != null && master.getStatus()) {
						if (master.getExpectedLife() != null && (kms < Integer.parseInt(master.getExpectedLife()))
								&& !master.isAvailable() && master.getDepot() != null && busNumber!=null) {
							for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(master.getId(),
									tyreLifeAlert,s);
							 Date tyreRemovalDate = fuelNotificationRepo.getTyreRemovalDate(master.getId());
							if (d == null && s!=null && !s.isEmpty() && tyreRemovalDate!=null)
							{
								SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
								String format = sdf.format(tyreRemovalDate);
								insertNotification(master.getTyreNumber() + " is removed on " + format +" from this "+  busNumber +". "+" Before expected life",
									tyreLifeAlert, "TYRE", master.getId(), master.getDepot().getId(),s);
								 
							}}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			/*try {
				for (TyreMaster master : tyres) {
					if (master.getStatus() != null && master.getStatus()) {
						if (master.getTyreCondition() != null && master.getTyreCondition().getName() != null
								&& master.getTyreCondition().getName().equals("New") && master.getTyrePosition() != null
								&& master.getTyrePosition().getName() != null
								&& master.getTyrePosition().getName().contains("Rear") && master.getDepot() != null) {
							for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(master.getId(),
									tyreRearAlert,s);
							if (d == null && s!=null && !s.isEmpty()) {
								insertNotification(
										master.getTyreNumber() + " is a new tyre and fitted to rear position",
										tyreRearAlert, "TYRE", master.getId(), master.getDepot().getId(),s);
							}}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
	}

	@Transactional
	public notificationEntity insertNotification(String desc, String alertTyre, String module, Integer moduleId,
			Integer depot,String roles) {
		notificationEntity notification = new notificationEntity();
		notification.setDescription(desc);
		notification.setAlertType(alertTyre);
		notification.setIsRead(false);
		notification.setModule(module);
		notification.setStatus(true);
		notification.setModuleId(moduleId);
		notification.setCreatedDate(new Date());
		notification.setDepotId(depot);
		notification.setDisplayId(roles);
		return fuelNotificationRepo.save(notification);
	}

	public List<notificationEntity> getAllFuelNotifications(Integer depot,String userName) {
		List<notificationEntity> notifications = new ArrayList<>();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()){
			notifications = fuelNotificationRepo.getFuelNotifications(depot,user.getRole().getName());
		}
		return notifications;
	}

	public List<notificationEntity> getAllTyreNotifications(Integer depot,String userName) {
		List<notificationEntity> notifications = new ArrayList<>();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()){
		notifications = fuelNotificationRepo.getTyreNotifications(depot,user.getRole().getName());
		}
		return notifications;
	}

	public NotificationCountList listgetFuelCounts(Integer id,String userName) {
		NotificationCountList counts = new NotificationCountList();
		NotificationCount fuelCount = new NotificationCount();
		NotificationCount tyreCount = new NotificationCount();
		NotificationCount routeCrewCounts=new NotificationCount();
		NotificationCount ticketcount= new NotificationCount();
		NotificationCount drivercount= new NotificationCount();
		NotificationCount conductorcount= new NotificationCount();
		NotificationCount busCounts= new NotificationCount();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()) {
		Integer fuelRead = fuelNotificationRepo.getFuelReadNotifications(id,user.getRole().getName());
		Integer fuelunRead = fuelNotificationRepo.getFuelUnReadNotifications(id,user.getRole().getName());
		fuelCount.setRead(fuelRead != null ? fuelRead : 0);
		fuelCount.setUnRead(fuelunRead != null ? fuelunRead : 0);
		Integer tyreRead = fuelNotificationRepo.getTyreReadNotifications(id,user.getRole().getName());
		Integer tyreUnRead = fuelNotificationRepo.getTyreUnReadNotifications(id,user.getRole().getName());
		tyreCount.setRead(tyreRead != null ? tyreRead : 0);
		tyreCount.setUnRead(tyreUnRead != null ? tyreUnRead : 0);
		counts.setFuelCounts(fuelCount);
		counts.setTyreCounts(tyreCount);
		Integer routeCrewRead = fuelNotificationRepo.getRouteCrewReadNotifications(id,user.getRole().getName());
		Integer routeCrewUnRead = fuelNotificationRepo.getRouteCrewUnReadNotifications(id,user.getRole().getName());
		routeCrewCounts.setRead(routeCrewRead != null ? routeCrewRead : 0);
		routeCrewCounts.setUnRead(routeCrewUnRead != null ? routeCrewUnRead : 0);
		counts.setRouteCrewCounts(routeCrewCounts);
		 Integer ticketRead = fuelNotificationRepo.getTicketReadNotifications(id,user.getRole().getName());
		 Integer ticketUnRead = fuelNotificationRepo.getTicketUnReadNotifications(id,user.getRole().getName());
		 ticketcount.setRead(ticketRead != null ? ticketRead : 0);
		 ticketcount.setUnRead(ticketUnRead != null ? ticketUnRead : 0);
		 counts.setTicketCounts(ticketcount);
		 Integer driverRead = fuelNotificationRepo.getDriverReadNotifications(id,user.getRole().getName());
		 Integer driverUnRead = fuelNotificationRepo.getDriverUnReadNotifications(id,user.getRole().getName());
		 drivercount.setRead(driverRead != null ? driverRead : 0);
		 drivercount.setUnRead(driverUnRead != null ? driverUnRead : 0);
		 counts.setDriverCounts(drivercount);
		 Integer conductorRead = fuelNotificationRepo.getConductorReadNotifications(id,user.getRole().getName());
		 Integer conductorUnRead = fuelNotificationRepo.getConductorUnReadNotifications(id,user.getRole().getName());
		 conductorcount.setRead(conductorRead != null ? conductorRead : 0);
		 conductorcount.setUnRead(conductorUnRead != null ? conductorUnRead : 0);
		 counts.setConductorCounts(conductorcount);
		 Integer busRead = fuelNotificationRepo.getBusReadNotifications(id,user.getRole().getName());
		 Integer busUnRead = fuelNotificationRepo.getBusUnReadNotifications(id,user.getRole().getName());
		 busCounts.setRead(busRead != null ? busRead : 0);
		 busCounts.setUnRead(busUnRead != null ? busUnRead : 0);
		 counts.setBusCounts(busCounts);
			
		}
		return counts;
	}

	public Boolean markNotificationAsRead(Integer id, String readBy) {
		Boolean Status = false;
		Optional<notificationEntity> notification = fuelNotificationRepo.findById(id);
		if (notification.isPresent()) {
			notification.get().setIsRead(true);
			notification.get().setReadDate(new Date());
			notification.get().setReadBy(readBy);
			fuelNotificationRepo.save(notification.get());
			Status = true;
		}
		return Status;
	}

	public List<notificationEntity> getAllRouteCrewNotifications(Integer depot, String userName) {
		List<notificationEntity> notifications = new ArrayList<>();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()){
		notifications = fuelNotificationRepo.getRouteCrewNotifications(depot,user.getRole().getName());
		}
		return notifications;
	}
	
	String routeNextReviewDateAlert = "RNRDA";
    @Transactional
    @Scheduled(cron = "0 0/5 * * * * ")
    public  void routeNextReviewDateAlert() {

        List<ReviewCommentsHistory> historyRecordlist = reviewCommentsRepo.findAll();
        try {      
        if (historyRecordlist.size() > 0) {
                for (ReviewCommentsHistory historyRecord : historyRecordlist) {
                    if(historyRecord.getNextReviewDate()!=null && historyRecord.getRouteMaster()!=null ){
                    Calendar cal = Calendar.getInstance();
                    String NextReviewDateStr = new SimpleDateFormat("yyyy-MM-dd").format(historyRecord.getNextReviewDate());
                    String NextReviewDateStr1 = new SimpleDateFormat("dd-MM-yyyy").format(historyRecord.getNextReviewDate());
                    String currentDateStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = myFormat.parse(NextReviewDateStr);
                    Date currentDate = myFormat.parse(currentDateStr);              
                    long diff = date1.getTime() - currentDate.getTime();
                    long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);          
                    if(date1.after(currentDate) || date1.equals(currentDate)){                  
                    if (days <= 7 && days >=0) {                      
                        for (String s : roleIds) {
                            notificationEntity d = fuelNotificationRepo.checkExistingNotification(historyRecord.getRouteMaster().getId(),
                                    routeNextReviewDateAlert,s);
                            if (d == null && s!=null && !s.isEmpty()) {
                            insertNotification(
                                    "Route:- " + historyRecord.getRouteMaster().getId() + " (" + historyRecord.getRouteMaster().getRouteCode()
                                            + ")" + " review next date is "+NextReviewDateStr1,
                                            routeNextReviewDateAlert, "ROUTE", historyRecord.getRouteMaster().getId(),
                                            historyRecord.getDepotId().getId(), s);
                        }
                        }
                    }
                    }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();

        }
    }
	
	
	
	public List<notificationEntity> getAllTicketNotifications(Integer depot, String userName) {
		List<notificationEntity> notifications = new ArrayList<>();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()){
		notifications = fuelNotificationRepo.getAllTicketNotifications(depot,user.getRole().getName());
		}
		return notifications;
	}
	
	public List<notificationEntity> getAllDriverNotifications(Integer depot, String userName) {
		List<notificationEntity> notifications = new ArrayList<>();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()){
		notifications = fuelNotificationRepo.getAllDriverNotifications(depot,user.getRole().getName());
		}
		return notifications;
	}
	
	public List<notificationEntity> getAllConductorNotifications(Integer depot, String userName) {
		List<notificationEntity> notifications = new ArrayList<>();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()){
		notifications = fuelNotificationRepo.getAllConductorNotifications(depot,user.getRole().getName());
		}
		return notifications;
	}

	/*@Transactional
	@Scheduled(cron = "0 0/1 * * * * ")
	public void routeCrewBlockUnBlockAlert() {
	String blockConductorAlert = "BLOCKCON";
	String routeOffDriverAlert = "ROUTEOFF";
		List<BlockOrRouteOffEntity> routeBlockList = blockOrRouteOffRepository.findAll();
		if (routeBlockList.size() > 0) {
			try {
				for (BlockOrRouteOffEntity routeBlock : routeBlockList) {
					if (routeBlock.getStatus() != null && routeBlock.getIsBlocked()== true && routeBlock.getDriverId() !=null && routeBlock.getRouteOff()==true) {
						
						for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(routeBlock.getId(),
									routeOffDriverAlert,s);
							if (d == null && s!=null && !s.isEmpty()) {
								insertNotification(routeBlock.getDriverId().getDriverName() + " Driver is Route Off", routeOffDriverAlert, "ROUTE", routeBlock.getId(), routeBlock.getDepotId().getId(), s);
							}}
					}
					if (routeBlock.getStatus() != null && routeBlock.getIsBlocked()== true && routeBlock.getBlocked() == true && routeBlock.getConductorId() !=null && routeBlock.getRouteOff()==false) {
						
						for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(routeBlock.getId(),
									blockConductorAlert,s);
							if (d == null && s!=null && !s.isEmpty()) {
								insertNotification(routeBlock.getConductorId().getConductorName() + " Conductor is Blocked", blockConductorAlert, "ROUTE", routeBlock.getId(), routeBlock.getDepotId().getId(), s);
							}}
					}
					
					if (routeBlock.getStatus() != null && routeBlock.getIsBlocked()== false && routeBlock.getDriverId() !=null && routeBlock.getRouteOff()==true) {
						
						for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(routeBlock.getId(),
									routeOffDriverAlert,s);
							if (d == null && s!=null && !s.isEmpty()) {
								insertNotification(routeBlock.getDriverId().getDriverName() + " Driver is Unblocked", routeOffDriverAlert, "ROUTE", routeBlock.getId(), routeBlock.getDepotId().getId(), s);
							}}
					}
					if (routeBlock.getStatus() != null && routeBlock.getIsBlocked()== false && routeBlock.getBlocked() == true && routeBlock.getConductorId() !=null && routeBlock.getRouteOff()==false) {
						
						for(String s : roleIds) {
							notificationEntity d = fuelNotificationRepo.checkExistingNotification(routeBlock.getId(),
									blockConductorAlert,s);
							if (d == null && s!=null && !s.isEmpty()) {
								insertNotification(routeBlock.getConductorId().getConductorName() + " Conductor is Unblocked", blockConductorAlert, "ROUTE", routeBlock.getId(), routeBlock.getDepotId().getId(), s);
							}}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}*/
	
	public List<notificationEntity> getAllBusNotifications(Integer depot, String userName) {
		List<notificationEntity> notifications = new ArrayList<>();
		User user = userRepo.findByUserName(userName);
		if(user!=null && user.getRole()!=null && !user.getRole().getName().isEmpty()){
		notifications = fuelNotificationRepo.getAllBusNotifications(depot,user.getRole().getName());
		}
		return notifications;
	}
	String busAlert = "BUS";
	@Scheduled(cron = "0 0 03 * * ?")
		public void busAlert() {
		try{
		List<BusUnavailabilityMaster> busUnavailable = busUnavailablityRepository.findAll();
			if (busUnavailable.size() > 0) {
				for (BusUnavailabilityMaster bus : busUnavailable) {
					
					if(bus.getLikelyToReadyDate()!=null){
					Optional<BusMaster> busMaster = busMasterRepository.findById(bus.getBusMaster().getId());
					SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					
					String likelyToReadyDate = new SimpleDateFormat("yyyy-MM-dd")
							.format(bus.getLikelyToReadyDate());
					String likelyToReadyDate1 = new SimpleDateFormat("dd-MM-yyyy")
							.format(bus.getLikelyToReadyDate());
					String currentDateStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()); 
					
					Date readyDate = myFormat.parse(likelyToReadyDate);
					Date currentDate = myFormat.parse(currentDateStr);
					
					long diff = readyDate.getTime() - currentDate.getTime();
					long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					if (days == 1){
						for(String s : roleIds) {
							/*notificationEntity d = fuelNotificationRepo.checkExistingNotification(busMaster.get().getId(),
									busAlert,s);*/
							if (s!=null && !s.isEmpty()) {
								insertNotification("Bus:-"+busMaster.get().getBusRegNumber()+" is available from "+likelyToReadyDate1, busAlert, "Bus",
										busMaster.get().getId(),busMaster.get().getDepot().getId(),s);
							}
							}
				}}
			}
		}
		}catch (Exception e) {
			// TODO: handle exception
		}
		}
	
    String fuelTankInpectionExcessShort = "FTISEA";
	@Scheduled(cron = "0 0/15 * * * * ")
	public void getFuelTankExcessShortNotification() {		
		TankInspection tankInspection = tankInspectionRepo.findMaxRecord();
		if (tankInspection != null) {
			String todate = new SimpleDateFormat("dd-MM-yyyy").format(tankInspection.getCreatedOn());
			if (tankInspection.getVariationFlag()) {
				for(String s : roleIds) {					
				notificationEntity d = fuelNotificationRepo.checkExistingNotification(tankInspection.getId(), fuelTankInpectionExcessShort,s);
				if (d == null && s!=null && !s.isEmpty()) {
					if(tankInspection.getVariation() < 0){
					insertNotification("Fuel-Tank-Inspection :- There is a short in fuel tanks on "+todate, fuelTankInpectionExcessShort, "FUEL",
							tankInspection.getId(), tankInspection.getDepot().getId(),s);
					}else{
						insertNotification("Fuel-Tank-Inspection :- There is a excess in fuel tanks on "+todate, fuelTankInpectionExcessShort, "FUEL",
								tankInspection.getId(), tankInspection.getDepot().getId(),s);
					}
				}}
			}
		}
	}	
	
}
