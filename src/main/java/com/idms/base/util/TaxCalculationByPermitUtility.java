package com.idms.base.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idms.base.dao.entity.DailyRoaster;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.dao.entity.StateWiseKmMaster;
import com.idms.base.dao.entity.TaxCalculationOnSubmitETM;
import com.idms.base.dao.entity.TaxCalculationOnSubmitETMChild;
import com.idms.base.dao.entity.TaxMaster;
import com.idms.base.dao.entity.TripRotationEntity;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.DailyRoasterRepository;
import com.idms.base.dao.repository.PermitDetailsMasterRepository;
import com.idms.base.dao.repository.TaxCalculationOnSubmitETMRepo;
import com.idms.base.dao.repository.TaxMasterRepository;

import lombok.extern.log4j.Log4j2;

/**
*
* @author Hemant Makkar
*/
@Component
@Log4j2
public class TaxCalculationByPermitUtility {
	
	@Autowired
	PermitDetailsMasterRepository permitDetailsMasterRepository;
	
	@Autowired
	TaxMasterRepository taxmasterRepo;
	
	@Autowired
	BusMasterRepository busMasterRepository;
	
	@Autowired
	DailyRoasterRepository rosterRepo;
	
	@Autowired
	TaxCalculationOnSubmitETMRepo taxCalculationOnSubmitETMRepo;
	
	
	public Double calculateTaxByPermitId(Integer permitId){
		Double stateWiseAmount = 0.0;
		Double totalTaxCalculated = 0.0;
		try {
			PermitDetailsMaster permitMaster = permitDetailsMasterRepository.findById(permitId).get();
			if(permitMaster != null && permitMaster.getStateWiseKmList() != null && permitMaster.getStateWiseKmList().size() > 0)
			for (StateWiseKmMaster stateWise : permitMaster.getStateWiseKmList()) {
				List<TaxMaster> taxmasterList = taxmasterRepo.findAllByStateAndBusTypeId(stateWise.getState().getId(),
						stateWise.getPermitDetails().getBusTyperMaster().getId(),new Date());
				if (taxmasterList.size() > 0) {
					TaxMaster taxMasterObj = taxmasterList.get(taxmasterList.size() - 1);
					if (taxMasterObj.getTypeOfTaxMaster() != null && taxMasterObj.getTypeOfTaxMaster().getTaxTypeName().equals("Per Kilometer")
							&& taxMasterObj.getTaxAmount() != null) {
						stateWiseAmount = Double.parseDouble(taxMasterObj.getTaxAmount())
								* stateWise.getKilometers();
					} else if (taxMasterObj.getTypeOfTaxMaster().getTaxTypeName().equals("Per Seat")) {
						Object[] obj = busMasterRepository
								.fetchTotalSeatsByBusType(stateWise.getPermitDetails().getBusTyperMaster().getId());
						if (obj[0] != null) {
							stateWiseAmount = Double.parseDouble(taxMasterObj.getTaxAmount())
									* Integer.parseInt(obj[0].toString());
						}
					}
					totalTaxCalculated = totalTaxCalculated + stateWiseAmount;
					
				}
			}
			
			totalTaxCalculated = (totalTaxCalculated * (permitMaster.getTotalTrips() * 2)); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return totalTaxCalculated;
	}
	
	public Double calculateTaxByRouteAndRotaIdAndPersist(Integer dailyRosterId) {
		// Integer dailyRosterId = 1333;
		TaxCalculationOnSubmitETMChild child = null;
		List<TaxCalculationOnSubmitETMChild> childList = new ArrayList<>();
		TaxCalculationOnSubmitETM calculateSubmit = new TaxCalculationOnSubmitETM();
		Double stateWiseAmount = 0.0;
		Double totalTaxCalculated = 0.0;
		try {
			DailyRoaster daily = rosterRepo.findById(dailyRosterId).get();
			calculateSubmit.setDailyRoaster(daily);
			calculateSubmit.setBus(daily.getBus());
			calculateSubmit.setDriver(daily.getDriver());
			calculateSubmit.setConductor(daily.getConductor());
			calculateSubmit.setRoute(daily.getRoute());
			for (TripRotationEntity tripRotation : daily.getRoute().getTripRotation()) {
				PermitDetailsMaster permitMaster = tripRotation.getPermitMaster();
				if (permitMaster != null && permitMaster.getStateWiseKmList() != null
						&& permitMaster.getStateWiseKmList().size() > 0)
					for (StateWiseKmMaster stateWise : permitMaster.getStateWiseKmList()) {
						child = new TaxCalculationOnSubmitETMChild();
						List<TaxMaster> taxmasterList = taxmasterRepo.findAllByStateAndBusTypeId(
								stateWise.getState().getId(), stateWise.getPermitDetails().getBusTyperMaster().getId(),
								new Date());
						if (taxmasterList.size() > 0) {
							TaxMaster taxMasterObj = taxmasterList.get(taxmasterList.size() - 1);
							if (taxMasterObj.getTypeOfTaxMaster() != null
									&& taxMasterObj.getTypeOfTaxMaster().getTaxTypeName().equals("Per Kilometer")
									&& taxMasterObj.getTaxAmount() != null) {
								stateWiseAmount = Double.parseDouble(taxMasterObj.getTaxAmount())
										* stateWise.getKilometers();
							} else if (taxMasterObj.getTypeOfTaxMaster().getTaxTypeName().equals("Per Seat")) {
								Object[] obj = busMasterRepository.fetchTotalSeatsByBusType(
										stateWise.getPermitDetails().getBusTyperMaster().getId());
								if (obj[0] != null) {
									stateWiseAmount = Double.parseDouble(taxMasterObj.getTaxAmount())
											* Integer.parseInt(obj[0].toString());
								}
							}
							totalTaxCalculated = totalTaxCalculated + stateWiseAmount;
							child.setTaxCalculated(stateWiseAmount);
							Optional<PermitDetailsMaster> permitObj = permitDetailsMasterRepository
									.findById(stateWise.getPermitDetails().getId());
							if (permitObj != null) {
								PermitDetailsMaster permit = permitObj.get();
								child.setPermitDetailsMaster(permit);
							}
							child.setStateMaster(stateWise.getState());
							child.setTaxCalculationOnSubmitETM(calculateSubmit);
							child.setTaxMaster(taxMasterObj);
							childList.add(child);
						}
					}

			}
			calculateSubmit.setTotalTaxCalculated(totalTaxCalculated);
			calculateSubmit.setChildList(childList);
			if (childList.size() > 0)
				taxCalculationOnSubmitETMRepo.save(calculateSubmit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
