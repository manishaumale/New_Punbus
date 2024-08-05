package com.idms.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idms.base.api.v1.model.dto.TotalPlainHillKmsCalculationDto;
import com.idms.base.dao.entity.BusTyreAssociation;
import com.idms.base.dao.entity.BusTyreAssociationHistory;
import com.idms.base.dao.entity.DocketTyreAssociation;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.BusRefuelingMasterRepository;
import com.idms.base.dao.repository.BusTyreAssociationHistoryRepository;
import com.idms.base.dao.repository.BusTyreAssociationRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DocketTyreAssociationRepository;
import com.idms.base.dao.repository.TyreConditionRepository;
import com.idms.base.dao.repository.TyreMasterRepository;

import lombok.extern.log4j.Log4j2;

/**
 *
 * @author Hemant Makkar
 */
@Component
@Log4j2
public class TyreCostCalculationUtility {

	@Autowired
	DepotMasterRepository depotRepo;

	@Autowired
	TyreMasterRepository tyreMasterRepository;

	@Autowired
	TyreConditionRepository tcRepo;

	@Autowired
	DocketTyreAssociationRepository docketRepository;

	@Autowired
	BusTyreAssociationRepository busTyreAssociationRepository;

	@Autowired
	BusRefuelingMasterRepository busRefuelingMasterRepository;

	@Autowired
	BusTyreAssociationHistoryRepository historyRepository;

	public Double calculateTyreRecoveredCostByCondition(Integer tyreId) {
		Double tyrePurchaseCost = null;
		Integer expectedLife = null;
		Double bookingOfTyreCost = null;
		Double costRecovered = 0.00;
		Float kms = 0f;
		try {
			TyreMaster tyreObj = tyreMasterRepository.findById(tyreId).get();
			kms = calculateTyreKmsByCondition(tyreId);
			if (tyreObj.getTyreCondition().getName() != null && tyreObj.getTyreCondition().getName().equals("New")) {
				tyrePurchaseCost = tyreObj.getTyreCost();
				expectedLife = Integer.parseInt(tyreObj.getExpectedLife());
				if (tyrePurchaseCost != null && expectedLife != null) {
					bookingOfTyreCost = tyrePurchaseCost / expectedLife;
				} else {
					return 0.0;
				}

			} else if (tyreObj.getTyreCondition().getName() != null
					&& (!(tyreObj.getTyreCondition().getName()).equals("New"))) {
				DocketTyreAssociation docketObj = docketRepository
						.findByConditionIdAndTyreId(tyreObj.getTyreCondition().getId(), tyreObj.getId());
				if (docketObj != null) {
					tyrePurchaseCost = docketObj.getTyreCost();
					expectedLife = docketObj.getExpectedLife();
				}
				if (tyrePurchaseCost != null && expectedLife != null) {
					bookingOfTyreCost = tyrePurchaseCost / expectedLife;
				} else {
					return 0.0;
				}
			}
			if (kms != null) {
				costRecovered = bookingOfTyreCost * kms;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
		return costRecovered;

	}

	public Float calculateTyreKmsByCondition(Integer tyreId) {
		Float kmsDone = 0f;
		Float vtsKms = 0f;
		try {
			Date today = this.getDate(0, new Date());
			TyreMaster tyreObj = tyreMasterRepository.findById(tyreId).get();
			List<BusTyreAssociation> busTyreList = busTyreAssociationRepository.fetchAllAssociationByTyreId(tyreId);
			if (busTyreList.size() > 0) {
				Date installedDate = busTyreList.get(0).getInstallDate();
				if (installedDate != null) {
					vtsKms = busRefuelingMasterRepository
							.findVtsKmsByBusIdsAndDates(busTyreList.get(0).getBus().getId(), installedDate, today);
					kmsDone = kmsDone + vtsKms;
				}
				/*if (vtsKms == 0.0) {
					Integer totalKms = busRefuelingMasterRepository
							.findTotalKmsByBusIdsAndDates(busTyreList.get(0).getBus().getId(), installedDate, today);
					kmsDone = kmsDone + totalKms;
				}*/
			}
			List<BusTyreAssociationHistory> historyList = historyRepository
					.findAllHistoryByTyreAndCondition(tyreObj.getId(), tyreObj.getTyreCondition().getId());
			for (BusTyreAssociationHistory historyObj : historyList) {
				if (historyObj.getInstallDate() != null && historyObj.getRemovalDate() != null) {
					vtsKms = busRefuelingMasterRepository.findVtsKmsByBusIdsAndDates(
							historyObj.getBus().getId(), historyObj.getInstallDate(),
							historyObj.getRemovalDate());
					kmsDone = kmsDone + vtsKms;
					/*if (vtsKms == 0.0) {
						Integer totalKms = busRefuelingMasterRepository.findTotalKmsByBusIdsAndDates(
								busTyreList.get(0).getBus().getId(), historyObj.getRemovalDate(),
								historyObj.getRemovalDate());
						kmsDone = kmsDone + totalKms;
					}*/
				}
			}

			if (tyreObj.getTyreCondition().getName() != null
					&& (!(tyreObj.getTyreCondition().getName()).equals("New"))) {
				if (tyreObj.getKmsRunTillDate() != null) {
					kmsDone = kmsDone + tyreObj.getKmsRunTillDate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0f;
		}
		return kmsDone;

	}

	public Date getDate(Integer i, Date dt) {
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
	
	public Float calculateTyreTotalKms(Integer tyreId) {
		Float kmsDone = 0f;
		Float vtsKms = 0f;
		try {
			Date today = this.getDate(0, new Date());
			TyreMaster tyreObj = tyreMasterRepository.findById(tyreId).get();
			List<BusTyreAssociation> busTyreList = busTyreAssociationRepository.fetchAllAssociationByTyreId(tyreId);
			if (busTyreList.size() > 0) {
				Date installedDate = busTyreList.get(0).getInstallDate();
				if (installedDate != null) {
					vtsKms = busRefuelingMasterRepository
							.findVtsKmsByBusIdsAndDates(busTyreList.get(0).getBus().getId(), installedDate, today);
					kmsDone = kmsDone + vtsKms;
				}
				/*if (vtsKms == 0.0) {
					Integer totalKms = busRefuelingMasterRepository
							.findTotalKmsByBusIdsAndDates(busTyreList.get(0).getBus().getId(), installedDate, today);
					kmsDone = kmsDone + totalKms;
				}*/
			}
			List<BusTyreAssociationHistory> historyList = historyRepository
					.findAllHistoryByTyreId(tyreObj.getId());
			for (BusTyreAssociationHistory historyObj : historyList) {
				if (historyObj.getInstallDate() != null && historyObj.getRemovalDate() != null) {
					vtsKms = busRefuelingMasterRepository.findVtsKmsByBusIdsAndDates(
							historyObj.getBus().getId(), historyObj.getInstallDate(),
							historyObj.getRemovalDate());
					kmsDone = kmsDone + vtsKms;
					/*if (vtsKms == 0.0) {
						Integer totalKms = busRefuelingMasterRepository.findTotalKmsByBusIdsAndDates(
								busTyreList.get(0).getBus().getId(), historyObj.getRemovalDate(),
								historyObj.getRemovalDate());
						kmsDone = kmsDone + totalKms;
					}*/
				}
			}

			if (tyreObj.getTyreCondition().getName() != null
					&& (!(tyreObj.getTyreCondition().getName()).equals("New"))) {
				if (tyreObj.getKmsRunTillDate() != null) {
					kmsDone = kmsDone + tyreObj.getKmsRunTillDate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0f;
		}
		return kmsDone;

	}
	
	public Float calculateTotalKmsInCurretBus(Integer tyreId,Integer busId) {
		Float kmsDone = 0f;
		Float vtsKms = 0f;
		try {
			Date today = this.getDate(0, new Date());
			TyreMaster tyreObj = tyreMasterRepository.findById(tyreId).get();
			List<BusTyreAssociation> busTyreList = busTyreAssociationRepository.fetchAllAssociationByTyreIdAndBusId(tyreId,busId);
			if (busTyreList.size() > 0) {
				Date installedDate = busTyreList.get(0).getInstallDate();
				if (installedDate != null) {
					vtsKms = busRefuelingMasterRepository
							.findVtsKmsByBusIdsAndDates(busTyreList.get(0).getBus().getId(), installedDate, today);
					kmsDone = kmsDone + vtsKms;
				}
				/*if (vtsKms == 0.0) {
					Integer totalKms = busRefuelingMasterRepository
							.findTotalKmsByBusIdsAndDates(busTyreList.get(0).getBus().getId(), installedDate, today);
					kmsDone = kmsDone + totalKms;
				}*/
			}
			List<BusTyreAssociationHistory> historyList = historyRepository
					.findAllHistoryByTyreIdAndBusId(tyreObj.getId(),busId);
			for (BusTyreAssociationHistory historyObj : historyList) {
				if (historyObj.getInstallDate() != null && historyObj.getRemovalDate() != null) {
					vtsKms = busRefuelingMasterRepository.findVtsKmsByBusIdsAndDates(
							historyObj.getBus().getId(), historyObj.getInstallDate(),
							historyObj.getRemovalDate());
					kmsDone = kmsDone + vtsKms;
					/*if (vtsKms == 0.0) {
						Integer totalKms = busRefuelingMasterRepository.findTotalKmsByBusIdsAndDates(
								busTyreList.get(0).getBus().getId(), historyObj.getRemovalDate(),
								historyObj.getRemovalDate());
						kmsDone = kmsDone + totalKms;
					}*/
				}
			}

			/*if (tyreObj.getTyreCondition().getName() != null
					&& (!(tyreObj.getTyreCondition().getName()).equals("New"))) {
				if (tyreObj.getKmsRunTillDate() != null) {
					kmsDone = kmsDone + tyreObj.getKmsRunTillDate();
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			return 0f;
		}
		return kmsDone;

	}
	
	public Double calculateTyreTotalRecoveredCost(Integer tyreId) {
		Double tyrePurchaseCost = null;
		Integer expectedLife = null;
		Double bookingOfTyreCost = null;
		Double costRecovered = 0.00;
		Float kms = 0f;
		TyreMaster tyreObj = null;
		try {
			 tyreObj = tyreMasterRepository.findById(tyreId).get();
			 if(!tyreObj.getTyreCondition().getName().equals("Condemn")){
			kms = calculateTyreKmsByCondition(tyreId);
			 }else{
				 kms = calculateTyreTotalKms(tyreId);
			 }
			if (tyreObj.getTyreCondition().getName() != null && tyreObj.getTyreCondition().getName().equals("New")) {
				tyrePurchaseCost = tyreObj.getTyreCost();
				expectedLife = Integer.parseInt(tyreObj.getExpectedLife());
				if (tyrePurchaseCost != null && expectedLife != null) {
					bookingOfTyreCost = tyrePurchaseCost / expectedLife;
				} else {
					return 0.0;
				}

			} else if (tyreObj.getTyreCondition().getName() != null
					&& (!(tyreObj.getTyreCondition().getName()).equals("New"))) {
				DocketTyreAssociation docketObj = docketRepository
						.findByConditionIdAndTyreId(tyreObj.getTyreCondition().getId(), tyreObj.getId());
				if (docketObj != null) {
					tyrePurchaseCost = docketObj.getTyreCost();
					expectedLife = docketObj.getExpectedLife();
				}else{
					tyrePurchaseCost = tyreObj.getTyreCost();
					if(tyreObj.getExpectedLife() != null){
					expectedLife = Integer.parseInt(tyreObj.getExpectedLife());
					}
				}
				if (tyrePurchaseCost != null && expectedLife != null) {
					bookingOfTyreCost = tyrePurchaseCost / expectedLife;
				} else {
					return 0.0;
				}
			}
			if (kms != null) {
				costRecovered = bookingOfTyreCost * kms;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
		return costRecovered;

	}
	
	public TotalPlainHillKmsCalculationDto calculateTyreKmsByConditionForPlainAndHillKms(Integer tyreId) {
		Double kmsDone = 0.0;
		Double plain = 0.0;
		Integer hillKms = 0;
		Object[] obj = null;
		TotalPlainHillKmsCalculationDto dtoObj = new TotalPlainHillKmsCalculationDto();
		try {
			Date today = this.getDate(0, new Date());
			TyreMaster tyreObj = tyreMasterRepository.findById(tyreId).get();
			List<BusTyreAssociation> busTyreList = busTyreAssociationRepository.fetchAllAssociationByTyreId(tyreId);
			if (busTyreList.size() > 0) {
				Date installedDate = busTyreList.get(0).getInstallDate();
				if (installedDate != null) {
					obj = busRefuelingMasterRepository
							.findTotalKmsAndHillAndPlainByBusIdsAndDates(busTyreList.get(0).getBus().getId(), installedDate, today);
					Object[] obj1 = (Object[]) obj[0]; 
					if(obj1[0] != null)
					kmsDone = kmsDone + Double.parseDouble(obj1[0].toString());
					if(obj1[1] != null)
					plain = plain + Double.parseDouble(obj1[1].toString());
					if(obj1[2] != null)
					hillKms = hillKms + Integer.parseInt(obj1[2].toString());
				}
			}
			List<BusTyreAssociationHistory> historyList = historyRepository
					.findAllHistoryByTyreAndCondition(tyreObj.getId(), tyreObj.getTyreCondition().getId());
			for (BusTyreAssociationHistory historyObj : historyList) {
				if ( historyObj.getInstallDate() != null && historyObj.getRemovalDate() != null) {
					obj = busRefuelingMasterRepository.findTotalKmsAndHillAndPlainByBusIdsAndDates(
							historyObj.getBus().getId(), historyObj.getInstallDate(),
							historyObj.getRemovalDate());
					Object[] obj1 = (Object[]) obj[0]; 
					if(obj1[0] != null)
						kmsDone = kmsDone + Double.parseDouble(obj1[0].toString());
						if(obj1[1] != null)
						plain = plain + Double.parseDouble(obj1[1].toString());
						if(obj1[2] != null)
						hillKms = hillKms + Integer.parseInt(obj1[2].toString());
				}
			}

			if (tyreObj.getTyreCondition().getName() != null
					&& (!(tyreObj.getTyreCondition().getName()).equals("New"))) {
				if (tyreObj.getKmsRunTillDate() != null) {
					kmsDone = kmsDone + tyreObj.getKmsRunTillDate();
				}
			}
			if(kmsDone != null) 
			dtoObj.setTotKms(String.format("%.2f", kmsDone).toString());
			else
				dtoObj.setTotKms("0");	
			if(plain != null) 
			dtoObj.setPlainKms(String.format("%.2f", plain).toString());
			else
				dtoObj.setPlainKms("0");	
			if(hillKms != null) 
			dtoObj.setHillKms(hillKms.toString());
			else
				dtoObj.setHillKms("0");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtoObj;

	}

}