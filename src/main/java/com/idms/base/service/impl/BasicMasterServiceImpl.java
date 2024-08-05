package com.idms.base.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.AddBlueDrumMasterDto;
import com.idms.base.api.v1.model.dto.BusRegNoDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.BusTypeTyreTypeSizeForm;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.DepotMasterDetailsDto;
import com.idms.base.api.v1.model.dto.MakerTyreDetailsFormDto;
import com.idms.base.api.v1.model.dto.MobilOilDrumMasterDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.TaxMasterDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TypeOfTaxDto;
import com.idms.base.api.v1.model.dto.TyreMakerDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.api.v1.model.dto.TyreTypeDto;
import com.idms.base.dao.entity.AdBlueUsed;
import com.idms.base.dao.entity.AddBlueDrumMaster;
import com.idms.base.dao.entity.BusSubTypeMaster;
import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.BusTyreTypeSizeMapping;
import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.DepotTransportUnit;
import com.idms.base.dao.entity.MakerTyreDetails;
import com.idms.base.dao.entity.MobilOilDrumMaster;
import com.idms.base.dao.entity.StateMaster;
import com.idms.base.dao.entity.TaxMaster;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TypeOfTaxMaster;
import com.idms.base.dao.entity.TyreSize;
import com.idms.base.dao.entity.TyreType;
import com.idms.base.dao.repository.AdBlueUsedRepository;
import com.idms.base.dao.repository.AddBlueDrumMasterRepository;
import com.idms.base.dao.repository.BusSubTypeMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.BusTyreTypeSizeMappingRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.MakerTyreDetailsRepository;
import com.idms.base.dao.repository.MobilOilDrumMasterRepository;
import com.idms.base.dao.repository.MobilOilUsedRepository;
import com.idms.base.dao.repository.PermitDetailsMasterRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.TaxMasterRepository;
import com.idms.base.dao.repository.TaxTypeMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.dao.repository.TyreMakersRepository;
import com.idms.base.dao.repository.TyreSizeRepository;
import com.idms.base.dao.repository.TyreTypeRepository;
import com.idms.base.service.BasicMasterService;
import com.idms.base.support.persist.ResponseStatus;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BasicMasterServiceImpl implements BasicMasterService {

	@Autowired
	StateMasterRepository stateMasterRepository;

	@Autowired
	CityMasterRepository cityMasterRepository;

	@Autowired
	TransportUnitRepository transportUnitRepository;

	@Autowired
	DepotMasterRepository depotMasterRepository;

	@Autowired
	TaxTypeMasterRepository taxTypeMasterRepository;

	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;

	@Autowired
	BusSubTypeMasterRepository busSubTypeMasterRepository;

	@Autowired
	TaxMasterRepository taxMasterRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	PermitDetailsMasterRepository permitDetailsMasterRepository;

	@Autowired
	TyreTypeRepository tyreTypeRepo;

	@Autowired
	TyreSizeRepository tyreSizeRepo;

	@Autowired
	BusTyreTypeSizeMappingRepository bttsRepo;

	@Autowired
	TyreMakersRepository tyreMakerRepo;

	@Autowired
	MakerTyreDetailsRepository makerDetailsRepo;

	@Autowired
	private MobilOilDrumMasterRepository mobilOilDrumMasterRepository;

	@Autowired
	private AddBlueDrumMasterRepository addBlueDrumMasterRepository;

	@Autowired
	private MobilOilUsedRepository mobilOilUsedRepository;

	@Autowired
	private AdBlueUsedRepository adBlueUsedRepository;

	@Override
	public List<StateMaster> findAllStatesByActiveStatus() {
		log.info("Entering into findAllStatesByActiveStatus service");
		List<StateMaster> stateList = null;
		try {
			// stateList = stateMasterRepository.findAllByStatus(true);
			stateList = stateMasterRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stateList;
	}

	@Override
	public List<CityMaster> findAllCityByActiveStatus(Integer id) {
		log.info("Entering into findAllCityByActiveStatus service");
		List<CityMaster> cityList = null;
		try {
			// cityList = cityMasterRepository.findByStateIdAndStatus(id,true);
			cityList = cityMasterRepository.findByStateId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cityList;
	}

	@Override
	public List<TransportUnitMaster> findAllTransportUnitByActiveStatus() {
		log.info("Entering into findAllTransportUnitByActiveStatus service");
		List<TransportUnitMaster> transportList = null;
		try {
			// transportList = transportUnitRepository.findAllByStatus(true);
			transportList = transportUnitRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transportList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveDepotMaster(DepotMaster depotMaster) {
		List<DepotTransportUnit> depotTransportList = new ArrayList<>();
		log.info("Entering into saveDepotMaster service");
		try {
			if (depotMaster.getId() == null) {
				if (depotMaster.getDepotName() == null || depotMaster.getDepotName().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot name is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (depotMaster.getFuelLevel() != null && !depotMaster.getFuelLevel().equals("")
						&& !depotMaster.getFuelLevel().matches("^\\d{0,9}$")) {
					return new ResponseEntity<>(
							new ResponseStatus("Fuel Re-Order level allows numbers only.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (depotMaster.getPinCode() != null && !depotMaster.getPinCode().equals("")
						&& !depotMaster.getPinCode().matches("^\\d{0,9}$")) {
					return new ResponseEntity<>(
							new ResponseStatus("Pin code allows number only.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (depotMaster.getDepotCode() == null || depotMaster.getDepotCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot code is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getDepotAddress() == null || depotMaster.getDepotAddress().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot address is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getState() == null) {
					return new ResponseEntity<>(new ResponseStatus("State is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getCity() == null) {
					return new ResponseEntity<>(new ResponseStatus("City is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getPinCode() == null || depotMaster.getPinCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Pincode is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (depotMaster.getDepotTransportList() == null
						|| depotMaster.getDepotTransportList().size() == 0) {
					return new ResponseEntity<>(
							new ResponseStatus("Transport Unit is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getFuelLevel() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Fuel Re-Order level is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getDepotName() != null && (!depotMaster.getDepotName().equals(""))
						&& depotMaster.getDepotName().length() > 30) {
					return new ResponseEntity<>(new ResponseStatus("Depot name should not be more than 30 characters.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (depotMaster.getDepotCode() != null && (!depotMaster.getDepotCode().equals(""))
						&& depotMaster.getDepotCode().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot code length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getDepotAddress() != null && (!depotMaster.getDepotAddress().equals(""))
						&& depotMaster.getDepotAddress().length() > 100) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot address should not be more than 100 characters.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getPinCode() != null && (!depotMaster.getPinCode().equals(""))
						&& depotMaster.getPinCode().length() > 6) {
					return new ResponseEntity<>(new ResponseStatus("Pin code length should not be more than 6 numbers.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (depotMaster.getFuelLevel() != null && (!depotMaster.getFuelLevel().equals(""))
						&& depotMaster.getFuelLevel().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("Fuel Re-Order level length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				for (DepotTransportUnit depotTransport : depotMaster.getDepotTransportList()) {
					depotTransport.setDepotMaster(depotMaster);
					depotTransportList.add(depotTransport);
				}
				if (depotTransportList != null && depotTransportList.size() > 0) {
					depotMaster.setDepotTransportList(depotTransportList);
				}
				depotMasterRepository.save(depotMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Depot Master has been persisted successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				if (depotMaster.getDepotName() == null || depotMaster.getDepotName().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot name is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (depotMaster.getFuelLevel() != null && !depotMaster.getFuelLevel().equals("")
						&& !depotMaster.getFuelLevel().matches("^\\d{0,9}$")) {
					return new ResponseEntity<>(
							new ResponseStatus("Fuel Re-Order level allows numbers only.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (depotMaster.getPinCode() != null && !depotMaster.getPinCode().equals("")
						&& !depotMaster.getPinCode().matches("^\\d{0,9}$")) {
					return new ResponseEntity<>(
							new ResponseStatus("Pin code allows number only.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (depotMaster.getDepotCode() == null || depotMaster.getDepotCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot code is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getDepotAddress() == null || depotMaster.getDepotAddress().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot address is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getState() == null) {
					return new ResponseEntity<>(new ResponseStatus("State is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getCity() == null) {
					return new ResponseEntity<>(new ResponseStatus("City is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getPinCode() == null || depotMaster.getPinCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Pincode is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (depotMaster.getDepotTransportList() == null
						|| depotMaster.getDepotTransportList().size() == 0) {
					return new ResponseEntity<>(
							new ResponseStatus("Transport Unit is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getFuelLevel() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Fuel Re-Order level is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getDepotName() != null && (!depotMaster.getDepotName().equals(""))
						&& depotMaster.getDepotName().length() > 30) {
					return new ResponseEntity<>(new ResponseStatus("Depot name should not be more than 30 characters.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (depotMaster.getDepotCode() != null && (!depotMaster.getDepotCode().equals(""))
						&& depotMaster.getDepotCode().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot code length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getDepotAddress() != null && (!depotMaster.getDepotAddress().equals(""))
						&& depotMaster.getDepotAddress().length() > 100) {
					return new ResponseEntity<>(
							new ResponseStatus("Depot address should not be more than 100 characters.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (depotMaster.getPinCode() != null && (!depotMaster.getPinCode().equals(""))
						&& depotMaster.getPinCode().length() > 6) {
					return new ResponseEntity<>(new ResponseStatus("Pin code length should not be more than 6 numbers.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (depotMaster.getFuelLevel() != null && (!depotMaster.getFuelLevel().equals(""))
						&& depotMaster.getFuelLevel().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("Fuel Re-Order level length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				for (DepotTransportUnit depotTransport : depotMaster.getDepotTransportList()) {
					depotTransport.setDepotMaster(depotMaster);
					depotTransportList.add(depotTransport);
				}
				if (depotTransportList != null && depotTransportList.size() > 0) {
					depotMaster.setDepotTransportList(depotTransportList);
				}
				depotMasterRepository.save(depotMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Depot Master has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("This depot code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<DepotMaster> findAllDepotMasterByActiveStatus(List<Integer> tpIds) {
		log.info("Entering into findAllDepotMasterByActiveStatus service");
		List<DepotMaster> depotList = null;
		try {
			// depotList = depotMasterRepository.findAllByStatus(true);
			depotList = depotMasterRepository.findAllByTPIds(tpIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return depotList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveStateMaster(StateMaster stateMaster) {
		log.info("Entering into saveStateMaster service");
		try {
			if (stateMaster.getId() == null) {
				System.out.println(stateMaster.getStateName().length());
				if (stateMaster.getStateName() == null || stateMaster.getStateName().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("State name is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (stateMaster.getStateName() != null && !stateMaster.getStateName().equals("")
						&& !stateMaster.getStateName().matches("^[a-zA-Z \\]]+$")) {
					return new ResponseEntity<>(
							new ResponseStatus("State name allow alphabets only.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (stateMaster.getStateCode() == null || stateMaster.getStateCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("State code is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (stateMaster.getStateName() != null && (!stateMaster.getStateName().equals(""))
						&& stateMaster.getStateName().length() > 30) {
					return new ResponseEntity<>(new ResponseStatus("State name should not be more than 30 characters.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (stateMaster.getStateCode() != null && !(stateMaster.getStateCode().equals(""))
						&& stateMaster.getStateCode().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("State code length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				stateMasterRepository.save(stateMaster);
				return new ResponseEntity<>(new ResponseStatus("State has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				if (stateMaster.getStateName() == null || stateMaster.getStateName().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("State name is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (stateMaster.getStateName() != null && !stateMaster.getStateName().equals("")
						&& !stateMaster.getStateName().matches("^[a-zA-Z \\]]+$")) {
					return new ResponseEntity<>(
							new ResponseStatus("State name allow alphabets only.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (stateMaster.getStateCode() == null || stateMaster.getStateCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("State code is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);

				} else if (stateMaster.getStateName() != null && (!stateMaster.getStateName().equals(""))
						&& stateMaster.getStateName().length() > 30) {
					return new ResponseEntity<>(new ResponseStatus("State name should not be more than 30 characters.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (stateMaster.getStateCode() != null && !(stateMaster.getStateCode().equals(""))
						&& stateMaster.getStateCode().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("State code length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				stateMasterRepository.save(stateMaster);
				return new ResponseEntity<>(new ResponseStatus("State has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("DataIntegrityViolationException");
			return new ResponseEntity<>(
					new ResponseStatus("This state code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<ResponseStatus> saveCityMaster(CityMaster cityMaster) {
		log.info("Entering into saveCityMaster service");
		try {
			if (cityMaster.getId() == null) {
				if (cityMaster.getCityName().equals("") || cityMaster.getCityName() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("City name is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (cityMaster.getCityName() != null && !cityMaster.getCityName().equals("")
						&& !cityMaster.getCityName().matches("^[a-zA-Z \\]]+$")) {
					return new ResponseEntity<>(
							new ResponseStatus("City name allow alphabets only.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (cityMaster.getCityCode() == null || cityMaster.getCityCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("City code is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (cityMaster.getCityName() != null && (!cityMaster.getCityName().equals(""))
						&& cityMaster.getCityName().length() > 20) {
					return new ResponseEntity<>(new ResponseStatus("City name should not be more than 20 characters.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (cityMaster.getCityCode() != null && (!cityMaster.getCityCode().equals(""))
						&& cityMaster.getCityCode().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("City code length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (cityMaster.getState() == null) {
					return new ResponseEntity<>(new ResponseStatus("State is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				Optional<StateMaster> stateObj = stateMasterRepository.findById(cityMaster.getState().getId());
				cityMaster.setState(stateObj.get());
				cityMasterRepository.save(cityMaster);
				return new ResponseEntity<>(new ResponseStatus("City has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				if (cityMaster.getCityName().equals("") || cityMaster.getCityName() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("City name is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (cityMaster.getCityName() != null && !cityMaster.getCityName().equals("")
						&& !cityMaster.getCityName().matches("^[a-zA-Z \\]]+$")) {
					return new ResponseEntity<>(
							new ResponseStatus("City name allow alphabets only.", HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (cityMaster.getCityCode() == null || cityMaster.getCityCode().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("City code is a mandatory field.", HttpStatus.FORBIDDEN), HttpStatus.OK);

				} else if (cityMaster.getCityName() != null && (!cityMaster.getCityName().equals(""))
						&& cityMaster.getCityName().length() > 20) {
					return new ResponseEntity<>(new ResponseStatus("City name should not be more than 20 characters.",
							HttpStatus.FORBIDDEN), HttpStatus.OK);
				} else if (cityMaster.getCityCode() != null && (!cityMaster.getCityCode().equals(""))
						&& cityMaster.getCityCode().length() > 10) {
					return new ResponseEntity<>(
							new ResponseStatus("City code length should not be more than 10 numbers.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (cityMaster.getState() == null) {
					return new ResponseEntity<>(new ResponseStatus("State is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				cityMasterRepository.save(cityMaster);
				return new ResponseEntity<>(new ResponseStatus("City has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("DataIntegrityViolationException");
			return new ResponseEntity<>(
					new ResponseStatus("This city code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<ResponseStatus> saveTransportMaster(TransportUnitMaster transportUnitMaster) {
		log.info("Entering into saveTransportMaster service");
		try {
			if (transportUnitMaster.getId() == null) {
				if (transportUnitMaster.getTransportName().equals("")
						|| transportUnitMaster.getTransportName() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Transport Unit name is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (transportUnitMaster.getTransportName() != null
						&& (!transportUnitMaster.getTransportName().equals(""))
						&& transportUnitMaster.getTransportName().length() > 20) {
					return new ResponseEntity<>(
							new ResponseStatus("Transport Unit name should not be more than 20 characters.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				transportUnitRepository.save(transportUnitMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Transport Unit has been saved successfully.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else {
				if (transportUnitMaster.getTransportName().equals("")
						|| transportUnitMaster.getTransportName() == null) {
					return new ResponseEntity<>(
							new ResponseStatus("Transport Unit name is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (transportUnitMaster.getTransportName() != null
						&& (!transportUnitMaster.getTransportName().equals(""))
						&& transportUnitMaster.getTransportName().length() > 20) {
					return new ResponseEntity<>(
							new ResponseStatus("Transport Unit name should not be more than 20 characters.",
									HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
				transportUnitRepository.save(transportUnitMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Transport Unit has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("DataIntegrityViolationException");
			return new ResponseEntity<>(
					new ResponseStatus("This transport unit code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<CityDto> findAllCityByActiveStatus() {
		log.info("Entering into findAllCityByActiveStatus service");
		List<CityDto> cityList = new ArrayList<>();
		try {
			// cityList = cityMasterRepository.findAllByStatus(true);
			List<Object[]> cities = cityMasterRepository.findAllCitiesQuery();
			for (Object[] o : cities) {
				CityDto dto = new CityDto();
				StateDto state = new StateDto();
				state.setId(Integer.parseInt(o[0].toString()));
				state.setStateName(o[1].toString());
				dto.setState(state);
				dto.setId(Integer.parseInt(o[2].toString()));
				dto.setCityName(o[3].toString());
				dto.setCityCode(o[4].toString());
				dto.setStatus(Boolean.parseBoolean(o[5].toString()));
				cityList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cityList;
	}

	@Override
	public List<TypeOfTaxMaster> findAllTaxTypeByStatus() {
		log.info("Entering into findAllTaxTypeByStatus service");
		List<TypeOfTaxMaster> typeOfTaxMasterList = null;
		try {
			// typeOfTaxMasterList =
			// taxTypeMasterRepository.findAllByStatus(true);
			typeOfTaxMasterList = taxTypeMasterRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return typeOfTaxMasterList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveBusTypetMaster(BusTyperMaster busTyperMaster) {
		log.info("Entering into saveTransportMaster service");
		try {
			if (busTyperMaster.getId() == null) {
				busTyperMasterRepository.save(busTyperMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Bus type master has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				busTyperMasterRepository.save(busTyperMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Bus type master has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("DataIntegrityViolationException");
			return new ResponseEntity<>(
					new ResponseStatus("This bus type master code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<BusTyperMaster> getAllBusTypeMaster() {
		log.info("Entering into getAllBusTypeMaster service");
		List<BusTyperMaster> busTyperMaster = null;
		try {
			// busTyperMaster = busTyperMasterRepository.findAllByStatus(true);
			busTyperMaster = busTyperMasterRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busTyperMaster;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveBusSubTypetMaster(BusSubTypeMaster busSubTypeMaster) {
		log.info("Entering into saveBusSubTypetMaster service");
		try {
			if (busSubTypeMaster.getId() == null) {
				busSubTypeMasterRepository.save(busSubTypeMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Bus sub type master has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				busSubTypeMasterRepository.save(busSubTypeMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Bus sub type master has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("DataIntegrityViolationException");
			return new ResponseEntity<>(
					new ResponseStatus("This bus type master code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<BusSubTypeMaster> getAllBusSubTypeMaster() {
		log.info("Entering into getAllBusSubTypeMaster service");
		List<BusSubTypeMaster> busSubTypeMaster = null;
		try {
			// busSubTypeMaster =
			// busSubTypeMasterRepository.findAllByStatus(true);
			busSubTypeMaster = busSubTypeMasterRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return busSubTypeMaster;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveTaxMaster(TaxMaster taxMaster) {
		log.info("Entering into saveTaxMaster service");
		try {
			if (taxMaster.getId() == null) {
				taxMasterRepository.save(taxMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Tax master has been saved successfully.", HttpStatus.OK), HttpStatus.OK);
			} else {
				taxMasterRepository.save(taxMaster);
				return new ResponseEntity<>(
						new ResponseStatus("Tax master has been updated successfully.", HttpStatus.OK), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<TaxMasterDto> getAllTaxMaster() {
		log.info("Entering into getAllTaxMaster service");
		List<TaxMasterDto> taxMaster = new ArrayList<>();
		try {
			List<Object[]> array = taxMasterRepository.findAllTaxByQuery();
			for (Object[] o : array) {
				TaxMasterDto dto = new TaxMasterDto();
				dto.setId(Integer.parseInt(o[0].toString()));
				StateDto state = new StateDto();
				state.setId(Integer.parseInt(o[1].toString()));
				state.setStateName(o[2].toString());
				dto.setState(state);
				TypeOfTaxDto taxType = new TypeOfTaxDto();
				taxType.setId(Integer.parseInt(o[3].toString()));
				taxType.setTaxTypeName(o[4].toString());
				dto.setTypeOfTaxMaster(taxType);
				BusTypeDto busType = new BusTypeDto();
				busType.setId(Integer.parseInt(o[5].toString()));
				busType.setBusTypeName(o[6].toString());
				dto.setBusTyperMaster(busType);
				dto.setTaxAmount(o[7].toString());
				dto.setApplicableFrom(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o[8].toString()));
				dto.setStatus(Boolean.parseBoolean(o[9].toString()));
				taxMaster.add(dto);
			}
			// taxMaster = taxMasterRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxMaster;
	}

	@Override
	public StateDto findByStateId(Integer id) {
		Optional<StateMaster> stateMaster = stateMasterRepository.findById(id);
		StateDto stateDto = null;
		if (stateMaster.isPresent()) {
			stateDto = new StateDto();
			BeanUtils.copyProperties(stateMaster.get(), stateDto);
		}
		return stateDto;
	}

	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateStatusFlag service");
		try {
			int i = stateMasterRepository.updateStatusFlag(flag, id);
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
	public ResponseEntity<ResponseStatus> updateCityMasterStatus(Integer id, Boolean flag) {
		log.info("Entering into updateCityMasterStatus service");
		try {
			int i = cityMasterRepository.updateCityMasterStatusFlag(flag, id);
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
	public ResponseEntity<ResponseStatus> updateTransportMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateTransportMasterStatusFlag service");
		try {
			int i = transportUnitRepository.updateTransportMasterStatusFlag(flag, id);
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
	public ResponseEntity<ResponseStatus> updateDepotMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateDepotMasterStatusFlag service");
		try {
			int i = depotMasterRepository.updateDepotMasterStatusFlag(flag, id);
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
	public ResponseEntity<ResponseStatus> updateBusTypeStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateBusTypeStatusFlag service");
		try {
			int i = busTyperMasterRepository.updateBusTypeStatusFlag(flag, id);
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
	public ResponseEntity<ResponseStatus> updateBusSubTypeStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateBusSubTypeStatusFlag service");
		try {
			int i = busSubTypeMasterRepository.updateBusSubTypeStatusFlag(flag, id);
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
	public ResponseEntity<ResponseStatus> updateTaxMasterStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateTaxMasterStatusFlag service");
		try {
			int i = taxMasterRepository.updateTaxMasterStatusFlag(flag, id);
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

	@Override
	public List<TransportUnitMaster> allTransportMasterByDepot(Integer depotId) {
		List<TransportUnitMaster> transportList = new ArrayList<>();
		List<DepotTransportUnit> depotTransportList = null;
		log.info("Entering into allTransportMasterByDepot service");
		depotTransportList = transportUnitRepository.allTransportMasterByDepot(depotId);
		for (DepotTransportUnit depotTransport : depotTransportList) {
			transportList.add(depotTransport.getTransportUnitMaster());
		}
		return transportList;
	}

	@Override
	public Optional<DepotMaster> getDepotById(Integer id) {
		return depotMasterRepository.findById(id);
	}

	@Override
	public List<TransportUnitMaster> findAllTUByActiveStatus() {
		log.info("Entering into findAllTransportUnitByActiveStatus service");
		List<TransportUnitMaster> transportList = null;
		try {
			transportList = transportUnitRepository.findAllByStatus(true);
			// transportList = transportUnitRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transportList;
	}

	@Override
	public List<TyreType> getAllTyreType() {
		log.info("Entering into getAllTyreType service");
		List<TyreType> tyreTypeList = null;
		try {
			tyreTypeList = tyreTypeRepo.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tyreTypeList;
	}

	public ResponseEntity<ResponseStatus> saveTyreTypetMaster(TyreType tyreType) {

		log.info("Entering into saveTyreTypetMaster service");
		try {
			if (tyreType.getId() == null) {
				tyreTypeRepo.save(tyreType);
				return new ResponseEntity<>(
						new ResponseStatus("Tyre type master has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				tyreTypeRepo.save(tyreType);
				return new ResponseEntity<>(
						new ResponseStatus("Tyre type master has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("DataIntegrityViolationException");
			return new ResponseEntity<>(
					new ResponseStatus("This Tyre type master code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
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
	public ResponseEntity<ResponseStatus> updateTyreTypeStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateTyreTypeStatusFlag service");
		try {
			int i = tyreTypeRepo.updateStatusFlag(flag, id);
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

	@Override
	public List<TyreSize> getAllTyreSize() {
		log.info("Entering into getAllTyreSize service");
		List<TyreSize> tyreSizeList = null;
		try {
			tyreSizeList = tyreSizeRepo.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tyreSizeList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveTyreSizeMaster(TyreSize tyreSize) {
		log.info("Entering into saveTyreSizeMaster service");
		try {
			if (tyreSize.getId() == null) {
				tyreSizeRepo.save(tyreSize);
				return new ResponseEntity<>(
						new ResponseStatus("Tyre size master has been saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			} else {
				tyreSizeRepo.save(tyreSize);
				return new ResponseEntity<>(
						new ResponseStatus("Tyre size master has been updated successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (DataIntegrityViolationException e) {
			System.out.println("DataIntegrityViolationException");
			return new ResponseEntity<>(
					new ResponseStatus("This Tyre size master code is already exist. Please use a different code.",
							HttpStatus.FORBIDDEN),
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
	public ResponseEntity<ResponseStatus> updateTyreSizeStatusFlag(Integer id, Boolean flag) {
		log.info("Entering into updateTyreSizeStatusFlag service");
		try {
			int i = tyreSizeRepo.updateStatusFlag(flag, id);
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

	@Override
	public BusTypeTyreTypeSizeForm getBusTyreTypeSizeFormData() {
		log.info("Entering into getBusTyreTypeSizeFormData service");

		BusTypeTyreTypeSizeForm form = new BusTypeTyreTypeSizeForm();

		List<BusTypeDto> busTypeList = busTyperMasterRepository.findAllByStatus(true).stream()
				.map(busType -> new BusTypeDto(busType.getId(), busType.getBusTypeName())).collect(Collectors.toList());

		if (busTypeList != null && busTypeList.size() > 0) {
			form.setBusTypeList(busTypeList);
		}

		List<TyreTypeDto> tyreTypeList = tyreTypeRepo.findAllByStatus(true).stream()
				.map(tyreType -> new TyreTypeDto(tyreType.getId(), tyreType.getName())).collect(Collectors.toList());

		if (tyreTypeList != null && tyreTypeList.size() > 0) {
			form.setTyreTypeList(tyreTypeList);
		}

		List<TyreSizeDto> tyreSizeList = tyreSizeRepo.findAllByStatus(true).stream()
				.map(tyreSize -> new TyreSizeDto(tyreSize.getId(), tyreSize.getSize())).collect(Collectors.toList());

		if (tyreSizeList != null && tyreSizeList.size() > 0) {
			form.setTyreSizeList(tyreSizeList);
		}

		return form;
	}

	@Override
	public List<BusTyreTypeSizeMapping> getAllBusTyreTypeSizeMapping() {
		log.info("Entering into getAllBusTyreTypeSizeMapping service");
		List<BusTyreTypeSizeMapping> list = null;
		try {
			list = bttsRepo.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveBusTyreTypeSizeMapping(BusTyreTypeSizeMapping map) {
		log.info("Entering into saveBusTyreTypeSizeMapping service");
		try {
			List<BusTyreTypeSizeMapping> list = bttsRepo.findByBusTyreTypeSize(map.getBusType().getId(),
					map.getTyreType().getId(), map.getTyreSize().getId());
			if (list != null && list.size() > 0) {
				return new ResponseEntity<>(new ResponseStatus("This combination already exist.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else {
				bttsRepo.save(map);
				return new ResponseEntity<>(new ResponseStatus("Record saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public MakerTyreDetailsFormDto getFormLoadTyreMakerDetails() {

		log.info("Entering into getBusTyreTypeSizeFormData service");

		MakerTyreDetailsFormDto form = new MakerTyreDetailsFormDto();

		List<TyreMakerDto> tyreMakerList = tyreMakerRepo.findAllByStatus(true).stream()
				.map(list -> new TyreMakerDto(list.getId(), list.getName())).collect(Collectors.toList());

		if (tyreMakerList != null && tyreMakerList.size() > 0) {
			form.setMakerList(tyreMakerList);
		}

		List<TyreTypeDto> tyreTypeList = tyreTypeRepo.findAllByStatus(true).stream()
				.map(tyreType -> new TyreTypeDto(tyreType.getId(), tyreType.getName())).collect(Collectors.toList());

		if (tyreTypeList != null && tyreTypeList.size() > 0) {
			form.setTyreTypeList(tyreTypeList);
		}

		List<TyreSizeDto> tyreSizeList = tyreSizeRepo.findAllByStatus(true).stream()
				.map(tyreSize -> new TyreSizeDto(tyreSize.getId(), tyreSize.getSize())).collect(Collectors.toList());

		if (tyreSizeList != null && tyreSizeList.size() > 0) {
			form.setTyreSizeList(tyreSizeList);
		}

		return form;
	}

	@Override
	public List<MakerTyreDetails> getTyreMakerDetailsList() {
		log.info("Entering into getAllBusTyreTypeSizeMapping service");
		List<MakerTyreDetails> list = null;
		try {
			list = makerDetailsRepo.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveTyreMakerDetails(MakerTyreDetails map) {
		log.info("Entering into saveTyreMakerDetails service");
		try {
			List<BusTyreTypeSizeMapping> list = makerDetailsRepo.findByBusTyreTypeSize(map.getMaker().getId(),
					map.getTyreType().getId(), map.getTyreSize().getId());
			if (list != null && list.size() > 0) {
				return new ResponseEntity<>(new ResponseStatus("This combination already exist.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else {
				makerDetailsRepo.save(map);
				return new ResponseEntity<>(new ResponseStatus("Record saved successfully.", HttpStatus.OK),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<BusRegNoDto> getBusRegNoList() {
		log.info("getBusRegNoList");
		List<BusRegNoDto> abc = new ArrayList<BusRegNoDto>();
		try {
			List<Object[]> busMaster = busSubTypeMasterRepository.getBusList();
			for (Object[] objects : busMaster) {
				BusRegNoDto ab = new BusRegNoDto();
				if(objects[0]!=null)
				ab.setBusNo(objects[0].toString());
				if(objects[1]!=null){
				Integer i = Integer.parseInt(objects[1].toString());
				ab.setBusId(i);
				if(objects[2]!=null)
					ab.setTransportName(objects[2].toString());
				}
				abc.add(ab);
			}

		} catch (Exception e) {
			log.info(e + "getBusRegNoList");
		}
		return abc;
	}

	@Override
	public List<MobilOilDrumMasterDto> getDrumMasterListOnLoad(String depotCode) {
		MobilOilDrumMasterDto dtoObj = null;
		List<MobilOilDrumMasterDto> dtoList = new ArrayList<>();
		TransportDto transportDto = null;
		List<MobilOilDrumMaster> mobilOilDrumList = mobilOilDrumMasterRepository.findAllDrumsByDepot(true, false,
				depotCode);
		for (MobilOilDrumMaster mobilDrumObj : mobilOilDrumList) {
			dtoObj = new MobilOilDrumMasterDto();
			transportDto = new TransportDto();
			dtoObj.setId(mobilDrumObj.getId());
			dtoObj.setNameOfDrum(mobilDrumObj.getNameOfDrum());
			dtoObj.setCapacity(mobilDrumObj.getCapacity());
			dtoObj.setNoOfDrums(mobilDrumObj.getNoOfDrums());
			dtoObj.setTotalCapacity(mobilDrumObj.getTotalCapacity());
			Float mobilOilUsed = mobilOilUsedRepository.toalAddBlueUsedFromDrum(mobilDrumObj.getId());
			if (mobilOilUsed != null && mobilDrumObj.getTotalCapacity() != null) {
				Float availableStock = mobilDrumObj.getTotalCapacity() - mobilOilUsed;
				String avialStock = availableStock.toString();
				dtoObj.setAvailableStock(avialStock);
			} else if (mobilOilUsed == null && mobilDrumObj.getTotalCapacity() != null) {
				dtoObj.setAvailableStock(mobilDrumObj.getTotalCapacity().toString());
			}
			// depot.setId(mobilDrumObj.getDepot().getId());
			// depot.setDepotName(mobilDrumObj.getDepot().getDepotName());
			// dtoObj.setDepotCode(depotCode);
			if (mobilDrumObj.getTransportUnit() != null) {
				transportDto.setId(mobilDrumObj.getTransportUnit().getId());
				transportDto.setTransportName(mobilDrumObj.getTransportUnit().getTransportName());
			} else {
				transportDto.setId(null);
				transportDto.setTransportName(null);
			}
			dtoObj.setTransportUnit(transportDto);
			dtoObj.setValue(mobilDrumObj.getValue());
			dtoObj.setReorderLevel(mobilDrumObj.getReorderLevel());
			dtoList.add(dtoObj);
		}
		return dtoList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveDrumMaster(MobilOilDrumMaster mobilOilDrumMaster) {
		log.info("Entering into saveDrumMaster service");
		DepotMaster depot = null;
		try {
			if (mobilOilDrumMaster.getId() == null) {
				depot = depotMasterRepository.findByDepotCode(mobilOilDrumMaster.getDepotCode());
				if (mobilOilDrumMaster.getNameOfDrum() == null || mobilOilDrumMaster.getNameOfDrum().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Name of drum is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (mobilOilDrumMaster.getReorderLevel() == null
						|| mobilOilDrumMaster.getReorderLevel().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Reorder level is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
			} else if (mobilOilDrumMaster.getCapacity() == null || mobilOilDrumMaster.getCapacity().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Capacity is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (mobilOilDrumMaster.getTransportUnit() == null) {
				return new ResponseEntity<>(
						new ResponseStatus("Transport Unit is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (mobilOilDrumMaster.getValue() == null) {
				return new ResponseEntity<>(new ResponseStatus("Amount is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (mobilOilDrumMaster.getTotalCapacity() == null) {
				return new ResponseEntity<>(new ResponseStatus("Total Capacity is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (mobilOilDrumMaster.getNoOfDrums() == null) {
				return new ResponseEntity<>(new ResponseStatus("No of drums is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			mobilOilDrumMaster.setDepot(depot);
			mobilOilDrumMasterRepository.save(mobilOilDrumMaster);
			return new ResponseEntity<>(
					new ResponseStatus("Mobil Drum Master has been persisted successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<AddBlueDrumMasterDto> getAddBlueDrumMasterList(String depotCode) {
		AddBlueDrumMasterDto dtoObj = null;
		List<AddBlueDrumMasterDto> dtoList = new ArrayList<>();
		TransportDto transportDto = null;
		List<AddBlueDrumMaster> mobilOilDrumList = addBlueDrumMasterRepository.findAllDrumsByDepot(true, false,
				depotCode);
		for (AddBlueDrumMaster mobilDrumObj : mobilOilDrumList) {
			dtoObj = new AddBlueDrumMasterDto();
			transportDto = new TransportDto();
			dtoObj.setId(mobilDrumObj.getId());
			dtoObj.setNameOfDrum(mobilDrumObj.getNameOfDrum());
			dtoObj.setCapacity(mobilDrumObj.getCapacity());
			dtoObj.setNoOfDrums(mobilDrumObj.getNoOfDrums());
			dtoObj.setTotalCapacity(mobilDrumObj.getTotalCapacity());
			Float addBlueUsed = adBlueUsedRepository.toalMobilUsedFromDrum(mobilDrumObj.getId());
			if (addBlueUsed != null && mobilDrumObj.getTotalCapacity() != null) {
				Float availableStock = mobilDrumObj.getTotalCapacity() - addBlueUsed;
				String avialStock = availableStock.toString();
				dtoObj.setAvailableStock(avialStock);
			} else if (addBlueUsed == null && mobilDrumObj.getTotalCapacity() != null) {
				dtoObj.setAvailableStock(mobilDrumObj.getTotalCapacity().toString());
			}
			// depot.setId(mobilDrumObj.getDepot().getId());
			// depot.setDepotName(mobilDrumObj.getDepot().getDepotName());
			// dtoObj.setDepotCode(depotCode);
			if (mobilDrumObj.getTransportUnit() != null) {
				transportDto.setId(mobilDrumObj.getTransportUnit().getId());
				transportDto.setTransportName(mobilDrumObj.getTransportUnit().getTransportName());
			} else {
				transportDto.setId(null);
				transportDto.setTransportName(null);
			}
			dtoObj.setTransportUnit(transportDto);
			dtoObj.setValue(mobilDrumObj.getValue());
			dtoObj.setReorderLevel(mobilDrumObj.getReorderLevel());
			dtoList.add(dtoObj);
		}
		return dtoList;
	}

	@Override
	public ResponseEntity<ResponseStatus> saveAddBlueDrumMaster(AddBlueDrumMaster addBlueDrumMaster) {
		log.info("Entering into saveAddBlueDrumMaster service");
		DepotMaster depot = null;
		try {
			if (addBlueDrumMaster.getId() == null) {
				depot = depotMasterRepository.findByDepotCode(addBlueDrumMaster.getDepotCode());
				if (addBlueDrumMaster.getNameOfDrum() == null || addBlueDrumMaster.getNameOfDrum().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Name of drum is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				} else if (addBlueDrumMaster.getReorderLevel() == null
						|| addBlueDrumMaster.getReorderLevel().equals("")) {
					return new ResponseEntity<>(
							new ResponseStatus("Reorder level is a mandatory field.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}
			} else if (addBlueDrumMaster.getCapacity() == null || addBlueDrumMaster.getCapacity().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Capacity is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (addBlueDrumMaster.getTransportUnit() == null) {
				return new ResponseEntity<>(
						new ResponseStatus("Transport Unit is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (addBlueDrumMaster.getValue() == null) {
				return new ResponseEntity<>(new ResponseStatus("Amount is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (addBlueDrumMaster.getTotalCapacity() == null) {
				return new ResponseEntity<>(new ResponseStatus("Total Capacity is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			} else if (addBlueDrumMaster.getNoOfDrums() == null) {
				return new ResponseEntity<>(new ResponseStatus("No of drums is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			addBlueDrumMaster.setDepot(depot);
			addBlueDrumMasterRepository.save(addBlueDrumMaster);
			return new ResponseEntity<>(
					new ResponseStatus("AdBlue Drum Master has been persisted successfully.", HttpStatus.OK),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new ResponseStatus("Something went wrong please try again later.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public List<DepotMasterDetailsDto> getDepotmasterIdAndDepocode() {

		List<DepotMasterDetailsDto> depotmasterDtls = new ArrayList();
		try {

			List<Object[]> depocodeAndId = depotMasterRepository.findByDepocodeAndId();

			for (Object[] ob : depocodeAndId) {
				DepotMasterDetailsDto dm = new DepotMasterDetailsDto();
				if (ob[0] != null) {
					dm.setDepoId(Integer.parseInt(ob[0].toString()));
				}

				else {
					dm.setDepoId(0);
				}
				if (ob[1] != null) {
					dm.setDepoCode(ob[1].toString());
				} else {
					dm.setDepoCode("");
				}
				depotmasterDtls.add(dm);

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return depotmasterDtls;
	}

	@Transactional()
	@Override
	public ResponseEntity<ResponseStatus> updateBusTyreTypeSizeMapping(Integer id, Boolean flag) {
		
		log.info("Entering into updateBusTyreTypeSizeFlag service");
		try {
			int i = bttsRepo.updateStatusFlag(flag,id);
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

}
