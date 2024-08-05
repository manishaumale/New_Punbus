package com.idms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.idms.base.api.v1.model.dto.BusAssemblyDto;
import com.idms.base.api.v1.model.dto.BusFormLoadDto;
import com.idms.base.api.v1.model.dto.BusLayoutDto;
import com.idms.base.api.v1.model.dto.BusMakerDto;
import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusSubTypeDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.BusTyreAssociationDto;
import com.idms.base.api.v1.model.dto.BusTyreCountDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.DriverMasterDto;
import com.idms.base.api.v1.model.dto.EuroNormsDto;
import com.idms.base.api.v1.model.dto.PartsMasterDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyrePositionDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.api.v1.model.dto.TyreTypeDto;
import com.idms.base.dao.entity.BusAssemblyMaster;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.BusSubTypeMaster;
import com.idms.base.dao.entity.BusTyreAssociation;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.TyrePosition;
import com.idms.base.dao.repository.BusLayoutRepository;
import com.idms.base.dao.repository.BusMakerRepository;
import com.idms.base.dao.repository.BusMasterRepository;
import com.idms.base.dao.repository.BusSubTypeMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.BusTyreAssociationRepository;
import com.idms.base.dao.repository.BusTyreCountRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.DriverMasterRepository;
import com.idms.base.dao.repository.EuroNormsRepository;
import com.idms.base.dao.repository.PartsMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TyreMakersRepository;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.dao.repository.TyrePositionRepository;
import com.idms.base.dao.repository.TyreSizeRepository;
import com.idms.base.dao.repository.TyreTypeRepository;
import com.idms.base.service.BusMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BusMasterServiceImpl implements BusMasterService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	BusMakerRepository busMakerRepository;

	@Autowired
	TransportUnitRepository transportUnitRepository;

	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;

	@Autowired
	BusSubTypeMasterRepository busSubTypeMasterRepository;

	@Autowired
	DepotMasterRepository depotMasterRepository;

	@Autowired
	DriverMasterRepository driverMasterRepository;

	@Autowired
	EuroNormsRepository euroNormsRepository;

	@Autowired
	BusLayoutRepository busLayoutRepository;

	@Autowired
	PartsMasterRepository partsMasterRepository;

	@Autowired
	TyreTypeRepository tyreTypeRepository;

	@Autowired
	TyreSizeRepository tyreSizeRepository;

	@Autowired
	TyreMakersRepository tyreMakersRepository;

	@Autowired
	TyrePositionRepository tyrePositionRepository;

	@Autowired
	BusMasterRepository busMasterRepository;

	@Autowired
	TyreMasterRepository tyreMasterRepository;

	@Autowired
	BusTyreAssociationRepository busTyreAssociationRepository;

	@Autowired
	BusTyreCountRepository busTyreCountRepo;

	@Override
	public BusFormLoadDto busMasterFormOnLoad(String dpCode) {
		log.info("Entering into busMasterFormOnLoad service");
		BusFormLoadDto busFormLoadDto = new BusFormLoadDto();
		try {

			List<BusMakerDto> busMakerList = busMakerRepository.findAllByStatus(true).stream()
					.map(busMakerDto -> new BusMakerDto(busMakerDto.getId(), busMakerDto.getName()))
					.collect(Collectors.toList());
			if (busMakerList != null && busMakerList.size() > 0)
				busFormLoadDto.setBusMakerList(busMakerList);

			List<BusTypeDto> busTypeList = busTyperMasterRepository.findAllByStatus(true).stream()
					.map(busTypeDto -> new BusTypeDto(busTypeDto.getId(), busTypeDto.getBusTypeName()))
					.collect(Collectors.toList());
			if (busTypeList != null && busTypeList.size() > 0)
				busFormLoadDto.setBusTypeList(busTypeList);

			List<BusSubTypeDto> busSubTypeList = busSubTypeMasterRepository.findAllByStatus(true).stream()
					.map(busSubTypeDto -> new BusSubTypeDto(busSubTypeDto.getId(), busSubTypeDto.getBusSubTypeName()))
					.collect(Collectors.toList());
			if (busSubTypeList != null && busSubTypeList.size() > 0)
				busFormLoadDto.setBusSubTypeList(busSubTypeList);

			List<BusTyreCountDto> busTyreCount = busTyreCountRepo.findAll().stream()
					.map(tyreCount -> new BusTyreCountDto(tyreCount.getId(), tyreCount.getCount()))
					.collect(Collectors.toList());

			if (busTyreCount != null && busTyreCount.size() > 0) {
				busFormLoadDto.setBusTyreCount(busTyreCount);
			}

			DepotMaster depotMaster = depotMasterRepository.findByDepotCode(dpCode);

			if (depotMaster != null)
				busFormLoadDto.setDepotList(this.mapper.map(depotMaster, DepotMasterDto.class));

			List<TransportDto> transportList = transportUnitRepository.allTransportMasterByDepot(depotMaster.getId())
					.stream().map(transport -> new TransportDto(transport.getTransportUnitMaster().getId(),
							transport.getTransportUnitMaster().getTransportName()))
					.collect(Collectors.toList());
			if (transportList != null && transportList.size() > 0)
				busFormLoadDto.setTransportList(transportList);

			List<DriverMasterDto> driverList = driverMasterRepository.findAllByDepot(dpCode).stream().map(
					driverMasterDto -> new DriverMasterDto(driverMasterDto.getId(), driverMasterDto.getDriverName()))
					.collect(Collectors.toList());

			if (driverList != null && driverList.size() > 0)
				busFormLoadDto.setDriverList(driverList);

			List<EuroNormsDto> euroList = euroNormsRepository.findAllByStatus(true).stream()
					.map(euroNormsDto -> new EuroNormsDto(euroNormsDto.getId(), euroNormsDto.getNormName()))
					.collect(Collectors.toList());
			if (euroList != null && euroList.size() > 0)
				busFormLoadDto.setEuroList(euroList);

			List<BusLayoutDto> layoutList = busLayoutRepository.findAllByStatus(true).stream()
					.map(busLayoutDto -> new BusLayoutDto(busLayoutDto.getId(), busLayoutDto.getLayoutName()))
					.collect(Collectors.toList());
			if (layoutList != null && layoutList.size() > 0)
				busFormLoadDto.setLayoutList(layoutList);

			List<PartsMasterDto> partsList = partsMasterRepository.findAllByStatus(true).stream()
					.map(partsMasterDto -> new PartsMasterDto(partsMasterDto.getId(), partsMasterDto.getName()))
					.collect(Collectors.toList());
			if (partsList != null && partsList.size() > 0)
				busFormLoadDto.setPartsList(partsList);

			List<TyreTypeDto> tyreTypeList = tyreTypeRepository.findAllByStatus(true).stream()
					.map(tyreTypeDto -> new TyreTypeDto(tyreTypeDto.getId(), tyreTypeDto.getName()))
					.collect(Collectors.toList());
			if (tyreTypeList != null && tyreTypeList.size() > 0)
				busFormLoadDto.setTyreTypeList(tyreTypeList);

			List<TyreSizeDto> tyreSizeList = tyreSizeRepository.findAllByStatus(true).stream()
					.map(tyreSizeDto -> new TyreSizeDto(tyreSizeDto.getId(), tyreSizeDto.getSize()))
					.collect(Collectors.toList());
			if (tyreSizeList != null && tyreSizeList.size() > 0)
				busFormLoadDto.setTyreSizeList(tyreSizeList);

			List<TyreMakerDto> tyreMakerList = tyreMakersRepository.findAllByStatus(true).stream()
					.map(tyreMakerDto -> new TyreMakerDto(tyreMakerDto.getId(), tyreMakerDto.getName()))
					.collect(Collectors.toList());
			if (tyreMakerList != null && tyreMakerList.size() > 0)
				busFormLoadDto.setTyreMakerList(tyreMakerList);

			List<TyrePositionDto> tyrePositionList = tyrePositionRepository.findAllByStatus(true).stream()
					.map(tyrePositionDto -> new TyrePositionDto(tyrePositionDto.getId(), tyrePositionDto.getName()))
					.collect(Collectors.toList());
			if (tyrePositionList != null && tyrePositionList.size() > 0)
				busFormLoadDto.setTyrePositionList(tyrePositionList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return busFormLoadDto;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResponseEntity<ResponseStatus> saveBusMaster(BusMaster busMaster) {
		log.info("Entering into saveBusMaster service");
		List<BusAssemblyMaster> assemblyList = new ArrayList<>();
		try {

			if (busMaster.getId() == null) {
				List<BusMaster> busMasterList = busMasterRepository
						.findByDriverId(busMaster.getPrimaryDriver().getId());

				if (busMasterList != null && busMasterList.size() > 0) {
					return new ResponseEntity<>(
							new ResponseStatus("Primary Driver is already been allocated.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				if (busMaster.getBusRegNumber() == null || busMaster.getBusRegNumber().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus registered no is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getMaker() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus maker is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (busMaster.getBusModel() == null || busMaster.getBusModel().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus model is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (busMaster.getTransportUnit() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Transport is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (busMaster.getBusType() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus type is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (busMaster.getBusSubType() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus sub type is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getBusCost() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus cost is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (busMaster.getChessisCost() == null || busMaster.getChessisCost().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus chesis cost is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getBodyCost() == null || busMaster.getBodyCost().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus body cost is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getDepot() == null) {
					return new ResponseEntity<>(new ResponseStatus("Depot is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getPrimaryDriver() == null) {
						return new ResponseEntity<>(
								new ResponseStatus("Primary driver is a mandatory field.", HttpStatus.FORBIDDEN),
								HttpStatus.OK);

					} else if (busMaster.getSecondaryDriver() == null) {
						return new ResponseEntity<>(
								new ResponseStatus("Secondry driver is a mandatory field.", HttpStatus.FORBIDDEN),
								HttpStatus.OK);

					} else if (busMaster.getChessisNumber() == null || busMaster.getChessisNumber().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus chesis number is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getEngineNumber() == null || busMaster.getEngineNumber().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus engine number is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getTypeOfAxle() == null || busMaster.getTypeOfAxle().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Bus type of axle is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getTotalSeats() == null || busMaster.getTotalSeats().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Total seats is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getChessisPurDate() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Chesis purchase date is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getBodyFabricateDate() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Body fabrication date is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getFabricatorName() == null || busMaster.getFabricatorName().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("fabricator name is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getBodyFabricateDate() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Body fabricated date is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getBusPassingDate() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Body passing date is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (busMaster.getEuroNorm() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Euro norm is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (busMaster.getLayout() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Layout is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				}/* else if (busMaster.getAssemblies() == null || busMaster.getAssemblies().size() == 0) {
					return new ResponseEntity<>(
							new ResponseStatus("Adding assemblies is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				}*/
				if(busMaster.getPrimaryDriver() != null && busMaster.getPrimaryDriver().getId() == null)
					busMaster.setPrimaryDriver(null);
				if(busMaster.getSecondaryDriver() != null && busMaster.getSecondaryDriver().getId() == null)
				    busMaster.setSecondaryDriver(null);
				for (BusAssemblyMaster busAssemblyMaster : busMaster.getAssemblies()) {
					busAssemblyMaster.setBus(busMaster);
					assemblyList.add(busAssemblyMaster);
				}
				busMaster.setAssemblies(assemblyList);

				busMaster.setIsDeleted(false);
				/*if (busMaster.getSpareBus() == true) {
					busMaster.setPrimaryDriver(null);
					busMaster.setSecondaryDriver(null);

				}*/
				BusMaster savedBusMaster = busMasterRepository.save(busMaster);
				// for(TyreMaster tyreMaster : busMaster.getTyreList()) {
				// TyreMaster tempTyre = tyreMasterRepository.save(tyreMaster);
				// BusTyreAssociation busTyreAsso = new BusTyreAssociation();
				// busTyreAsso.setBus(savedBusMaster);
				// busTyreAsso.setTyre(tempTyre);
				// busTyreAssociationRepository.save(busTyreAsso);
				// }

				return new ResponseEntity<>(
						new ResponseStatus("Bus master has been persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
		return null;
	}

	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateBusMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateBusMasterStatusFlag service");
		try {
			int i = busMasterRepository.updateBusMasterStatusFlag(flag, id);
			if (i == 1)
				return new ResponseEntity<>(new ResponseStatus("Status has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateBusMasterIsDeletedFlag(Integer id, Boolean flag) {
		log.info("Entering into updateBusMasterIsDeletedFlag service");
		try {
			int i = busMasterRepository.updateBusMasterIsDeletedFlag(flag, id);
			if (i == 1)
				return new ResponseEntity<>(
						new ResponseStatus("Bus master has been deleted successfully.", HttpStatus.OK), HttpStatus.OK);
			else
				return new ResponseEntity<>(
						new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<BusMasterDto> getAllBusMaster(String dpCode) {
		log.info("Entering into getAllBusMaster service");
		List<BusMaster> busMasterList = null;
		List<BusMasterDto> busDtoList = new ArrayList<>();
		try {
			busMasterList = busMasterRepository.findAllBusesByDepot(dpCode);
			if (busMasterList.size() > 0) {
				for (BusMaster bu : busMasterList) {
					BusMasterDto bus = new BusMasterDto();
					bus.setId(bu.getId());
					bus.setBusRegNumber(bu.getBusRegNumber());
					bus.setMaker(this.mapper.map(new BusMakerDto(bu.getMaker().getId(), bu.getMaker().getName()),
							BusMakerDto.class));
					bus.setBusModel(bu.getBusModel());
					bus.setTransportUnit(this.mapper.map(bu.getTransportUnit(), TransportDto.class));
					bus.setBusType(
							this.mapper.map(new BusTypeDto(bu.getBusType().getId(), bu.getBusType().getBusTypeName()),
									BusTypeDto.class));
					bus.setBusSubType(this.mapper.map(
							new BusSubTypeDto(bu.getBusSubType().getId(), bu.getBusSubType().getBusSubTypeName()),
							BusSubTypeDto.class));
					bus.setBusCost(bu.getBusCost());
					bus.setChessisCost(bu.getChessisCost());
					bus.setBodyCost(bu.getBodyCost());
					bus.setDepot(
							this.mapper.map(new DepotMasterDto(bu.getDepot().getId(), bu.getDepot().getDepotName()),
									DepotMasterDto.class));
					/*if (bu.getSpareBus() == null) {
						if(bu.getPrimaryDriver() != null && bu.getPrimaryDriver().getId() == null)
						bus.setPrimaryDriver(this.mapper.map(new DriverMasterDto(bu.getPrimaryDriver().getId(),
								bu.getPrimaryDriver().getDriverName()), DriverMasterDto.class));
						bus.setSecondaryDriver(this.mapper.map(new DriverMasterDto(bu.getSecondaryDriver().getId(),
								bu.getSecondaryDriver().getDriverName()), DriverMasterDto.class));
					} else if (bu.getSpareBus() == false) {
						bus.setPrimaryDriver(this.mapper.map(new DriverMasterDto(bu.getPrimaryDriver().getId(),
								bu.getPrimaryDriver().getDriverName()), DriverMasterDto.class));
						bus.setSecondaryDriver(this.mapper.map(new DriverMasterDto(bu.getSecondaryDriver().getId(),
								bu.getSecondaryDriver().getDriverName()), DriverMasterDto.class));
					} else {
						bus.setPrimaryDriver(this.mapper.map(new DriverMasterDto(null, ""), DriverMasterDto.class));
						bus.setSecondaryDriver(this.mapper.map(new DriverMasterDto(null, ""), DriverMasterDto.class));
					}*/
					
					if(bu.getPrimaryDriver() == null ){
						bus.setPrimaryDriver(this.mapper.map(new DriverMasterDto(null, ""), DriverMasterDto.class));
					}else if(bu.getPrimaryDriver() != null && bu.getPrimaryDriver().getId() != null){
						bus.setPrimaryDriver(this.mapper.map(new DriverMasterDto(bu.getPrimaryDriver().getId(),
								bu.getPrimaryDriver().getDriverName()), DriverMasterDto.class));
					}
					if(bu.getSecondaryDriver() == null ){
						bus.setSecondaryDriver(this.mapper.map(new DriverMasterDto(null, ""), DriverMasterDto.class));
					}else if(bu.getSecondaryDriver() != null && bu.getPrimaryDriver().getId() != null){
						bus.setSecondaryDriver(this.mapper.map(new DriverMasterDto(bu.getSecondaryDriver().getId(),
								bu.getSecondaryDriver().getDriverName()), DriverMasterDto.class));
					}
					
					
					
					bus.setChessisNumber(bu.getChessisNumber());
					bus.setEngineNumber(bu.getEngineNumber());
					bus.setFrontAxle(bu.getFrontAxle());
					bus.setRearAxle(bu.getRearAxle());
					bus.setTotalSeats(bu.getTotalSeats());
					bus.setChessisPurDate(bu.getChessisPurDate());
					bus.setBodyFabricateDate(bu.getBodyFabricateDate());
					bus.setFabricatorName(bu.getFabricatorName());
					bus.setBusPassingDate(bu.getBusPassingDate());
					bus.setEuroNorm(
							this.mapper.map(new EuroNormsDto(bu.getEuroNorm().getId(), bu.getEuroNorm().getNormName()),
									EuroNormsDto.class));
					bus.setTyreCount(this.mapper.map(
							new BusTyreCountDto(bu.getTyreCount().getId(), bu.getTyreCount().getCount()),
							BusTyreCountDto.class));
					bus.setLayout(
							this.mapper.map(new BusLayoutDto(bu.getLayout().getId(), bu.getLayout().getLayoutName()),
									BusLayoutDto.class));
					bus.setStatus(bu.getStatus());
					//bus.setSpareBus(bu.getSpareBus());
					if (bu.getAssemblies().size() > 0)
						bus.setAssemblies(bu.getAssemblies().stream()
								.map(amb -> this.mapper.map(amb, BusAssemblyDto.class)).collect(Collectors.toList()));
					if (bu.getTyreLists().size() > 0) {
						List<BusTyreAssociationDto> dtoList = new ArrayList<>();
						for (BusTyreAssociation bta : bu.getTyreLists()) {
							BusTyreAssociationDto bd = new BusTyreAssociationDto();
							bd.setId(bta.getId());
							bd.setTyre(this.mapper.map(
									new TyreMasterDto(bta.getTyre().getId(), bta.getTyre().getTyreNumber()),
									TyreMasterDto.class));
							bd.setTyrePosition(this.mapper.map(
									new TyrePositionDto(bta.getTyrePosition().getId(), bta.getTyrePosition().getName()),
									TyrePositionDto.class));
							bd.setKmsDone(bta.getKmsDone());
							bd.setInstallDate(bta.getInstallDate());
							bd.setTyreCondition(this.mapper.map(new TyreConditionDto(bta.getTyreCondition().getId(),
									bta.getTyreCondition().getName()), TyreConditionDto.class));
							dtoList.add(bd);
						}
						bus.setTyreLists(dtoList);
					}

					busDtoList.add(bus);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busDtoList;
	}

	@Override
	public List<TyrePosition> getTyrePositionByCount(Integer id) {
		List<TyrePosition> tyrePositionList = null;
		try {
			tyrePositionList = tyrePositionRepository.findAllByTyreCount(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tyrePositionList;
	}

	@Override
	public List<BusMasterDto> getAllBusMasterByBusType(Integer busType) {
		log.info("Entering into getAllBusMaster service");
		List<BusMaster> busMasterList = null;
		List<BusMasterDto> busDtoList = new ArrayList<>();
		try {
			busMasterList = busMasterRepository.findAllBusesByBusType(busType);
			if (busMasterList.size() > 0) {
				for (BusMaster bu : busMasterList) {
					BusMasterDto bus = new BusMasterDto();
					bus.setId(bu.getId());
					bus.setBusRegNumber(bu.getBusRegNumber());
					busDtoList.add(bus);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busDtoList;
	}

	@Override
	public String getBusMasterbydate(Integer id) {

		/*
		 * busSubTypeMasterRepository.findAll().forEach(entity -> { Integer year
		 * = entity.getCreatedOn().getYear(); BusSubTypeMaster master = new
		 * BusSubTypeMaster(); if (year >= 2018 && year < 2023) {
		 * 
		 * master.setBusSubTypeName("CLASS A");
		 * 
		 * } else { master.setBusSubTypeName("CLASS B"); }
		 * busSubTypeMasterRepository.save(master); });
		 */

		BusSubTypeMaster busSubTypeMaster = busSubTypeMasterRepository.findById(id).get();

		Integer year = busSubTypeMaster.getCreatedOn().getYear();
		Integer currentyear = year + 1900;
		if (currentyear >= 2018 && currentyear < 2023) {
			busSubTypeMaster.setBusSubTypeName("CLASS A");
		} else if (currentyear >= 2023 && currentyear < 2028) {
			busSubTypeMaster.setBusSubTypeName("ClASS B");

		} else if (currentyear >= 2028 && currentyear < 2032) {
			busSubTypeMaster.setBusSubTypeName("ClASS C");

		} else if (currentyear >= 2032 && currentyear < 2037) {
			busSubTypeMaster.setBusSubTypeName("CLASS D");
        }
		busSubTypeMasterRepository.save(busSubTypeMaster);

		return "Bus Stypes names are updated sucessfully";

	}
	

	
	public List<BusMasterDto> getBusRegistrationNumbers(Integer busType)
	{
		List<BusMasterDto> busMasterDtos= new ArrayList<>();
		List<Object[]> busRegistrationNumbers = busMasterRepository.getBusRegistrationNumbers(busType);
		
		for(Object[] o:busRegistrationNumbers)
		{
			BusMasterDto busMaster= new BusMasterDto();
			
			busMaster.setBusTypes(o[0].toString());
			busMaster.setBusTypeName(o[1].toString());
			busMaster.setBusRegNumber(o[2].toString());
			busMasterDtos.add(busMaster);
			
		}
		
		return busMasterDtos;
		
	}
	
	public List<BusTypeDto> getBusTypeName()
	{
		List<BusTypeDto> busTypeDtos= new ArrayList<>();
		List<Object[]> busTypeName = busTyperMasterRepository.getBusTypeName();
		for(Object[]o:busTypeName)
		{
			BusTypeDto busTypeDto = new BusTypeDto();
			busTypeDto.setId(Integer.parseInt(o[0].toString()));
			busTypeDto.setBusTypeName(o[1].toString());
			busTypeDtos.add(busTypeDto);
		}
		return busTypeDtos;
		
	}
	
	public List<BusMasterDto> getPrintSlipBuses(String dpCode) {
		List<BusMasterDto> output = new  ArrayList<>();
		DepotMaster depotMaster = depotMasterRepository.findByDepotCode(dpCode);
		List<BusMaster>  busMasterRecord  = busMasterRepository.findPrintSlipBuses(depotMaster.getId());
		for(BusMaster a  :busMasterRecord) {
			BusMasterDto dto = new BusMasterDto();
			dto.setBusRegNumber(a.getBusRegNumber());
			dto.setId(a.getId());
			TransportDto transportDto = new TransportDto();
			transportDto.setTransportName(a.getTransportUnit().getTransportName());
			transportDto.setTransportCode(a.getTransportUnit().getTransportCode());
			transportDto.setId(a.getTransportUnit().getId());
			dto.setTransportUnit(transportDto);
			output.add(dto);
			}
		return output;
	}
	
	

}
