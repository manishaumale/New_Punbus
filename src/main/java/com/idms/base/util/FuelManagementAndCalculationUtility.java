package com.idms.base.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.idms.base.dao.entity.FuelTankMaster;
import com.idms.base.dao.entity.RecieveDieselSupplyMaster;
import com.idms.base.dao.repository.FuelTankMasterRepository;
import com.idms.base.dao.repository.RecieveDieselSupplyMasterRepository;

import lombok.extern.log4j.Log4j2;

/**
 *
 * @author Hemant Makkar
 */
@Component
@Log4j2
public class FuelManagementAndCalculationUtility {
	
	@Autowired
	RecieveDieselSupplyMasterRepository repository;
	
	@Autowired
	FuelTankMasterRepository fuelTankMasterRepository;
	
	
	public Float mixtureFuelCostCalculation(Integer fuelTankId) {
		Float newPriceAndVolume = 0.f;
		Float volumeBeforeSupply = 0.f;
		Float oldPrice = 0.f;
		Float currentPrice = 0.f;
		Float oldPriceAndVolume = 0.f;
		Float sumNewAndOld = 0.f;
		Float sumOldVolumeNewVilume = 0.f;
		try {
			FuelTankMaster fuelTank = fuelTankMasterRepository.findById(fuelTankId).get();
			if (fuelTank.getCurrentValue() != null && fuelTank.getCurrentValue() > 0) {
				Object[] obj = repository.fetchupdateSupplyId(fuelTank.getId());
				if (obj[0] != null) {
					RecieveDieselSupplyMaster recieveDieselSupplyMaster = repository
							.fetchRecieceObject(Integer.parseInt(obj[0].toString()));
					if(recieveDieselSupplyMaster.getQuantityRecieved() != null && recieveDieselSupplyMaster.getDieselRate() != null){
						newPriceAndVolume = recieveDieselSupplyMaster.getQuantityRecieved() * recieveDieselSupplyMaster.getDieselRate();
					}
					if(recieveDieselSupplyMaster.getUpdateSupply().getVolumeBeforeSupply() != null){
						volumeBeforeSupply = recieveDieselSupplyMaster.getUpdateSupply().getVolumeBeforeSupply();
					}
					else{
						volumeBeforeSupply = recieveDieselSupplyMaster.getQuantityRecieved();
					}
					oldPrice = recieveDieselSupplyMaster.getDieselRate();
					oldPriceAndVolume = volumeBeforeSupply * oldPrice;
					sumNewAndOld = newPriceAndVolume + oldPriceAndVolume;
					sumOldVolumeNewVilume = recieveDieselSupplyMaster.getQuantityRecieved() + volumeBeforeSupply;
					currentPrice = sumNewAndOld / sumOldVolumeNewVilume;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return currentPrice;
	}

}
