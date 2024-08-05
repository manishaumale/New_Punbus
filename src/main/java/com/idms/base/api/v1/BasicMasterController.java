package com.idms.base.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.AddBlueDrumMasterDto;
import com.idms.base.api.v1.model.dto.BusRegNoDto;
import com.idms.base.api.v1.model.dto.BusSubTypeDto;
import com.idms.base.api.v1.model.dto.BusTypeDto;
import com.idms.base.api.v1.model.dto.BusTypeTyreTypeSizeForm;
import com.idms.base.api.v1.model.dto.BusTyreTypeSizeMappingDto;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.DepotMasterDetailsDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.MakerTyreDetailsDto;
import com.idms.base.api.v1.model.dto.MakerTyreDetailsFormDto;
import com.idms.base.api.v1.model.dto.MobilOilDrumMasterDto;
import com.idms.base.api.v1.model.dto.StateDto;
import com.idms.base.api.v1.model.dto.TaxMasterDto;
import com.idms.base.api.v1.model.dto.TransportDto;
import com.idms.base.api.v1.model.dto.TypeOfTaxDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.api.v1.model.dto.TyreTypeDto;
import com.idms.base.dao.entity.AddBlueDrumMaster;
import com.idms.base.dao.entity.BusSubTypeMaster;
import com.idms.base.dao.entity.BusTyperMaster;
import com.idms.base.dao.entity.BusTyreTypeSizeMapping;
import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.MakerTyreDetails;
import com.idms.base.dao.entity.MobilOilDrumMaster;
import com.idms.base.dao.entity.StateMaster;
import com.idms.base.dao.entity.TaxMaster;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.TyreSize;
import com.idms.base.dao.entity.TyreType;
import com.idms.base.dao.entity.User;
import com.idms.base.dao.entity.UserGroups;
import com.idms.base.dao.repository.BusSubTypeMasterRepository;
import com.idms.base.dao.repository.BusTyperMasterRepository;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.DepotMasterRepository;
import com.idms.base.dao.repository.StateMasterRepository;
import com.idms.base.dao.repository.TaxMasterRepository;
import com.idms.base.dao.repository.TaxTypeMasterRepository;
import com.idms.base.dao.repository.TransportUnitRepository;
import com.idms.base.service.BasicMasterService;
import com.idms.base.service.UserService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/basic")
@Log4j2
public class BasicMasterController {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private BasicMasterService service;

	@Autowired
	private DepotMasterRepository depotMasterRepository;

	@Autowired
	StateMasterRepository stateMasterRepository;

	@Autowired
	CityMasterRepository cityMasterRepository;

	@Autowired
	TransportUnitRepository transportUnitRepository;

	@Autowired
	BusTyperMasterRepository busTyperMasterRepository;

	@Autowired
	BusSubTypeMasterRepository busSubTypeMasterRepository;

	@Autowired
	TaxMasterRepository taxMasterRepository;

	@Autowired
	TaxTypeMasterRepository taxTypeMasterRepository;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation("Returns list of all StateMaster")
	@GetMapping(path = "/allStates")
	public List<StateDto> getAllState() {
		log.info("Enter into getAllState service");
		return this.service.findAllStatesByActiveStatus().stream().map(state -> this.mapper.map(state, StateDto.class))
				.collect(Collectors.toList());
	}

	@ApiOperation("Returns a specific State by their identifier. 404 if does not exist.")
	@GetMapping(path = "/specificState/{id}")
	public StateDto getSpecificState(@PathVariable("id") Integer id) {
		StateDto dto = this.service.findByStateId(id);
		return dto;
	}

	@ApiOperation("Creates a new State returning status 200 when persisted successfully.")
	@PostMapping("/saveStateMaster")
	public ResponseEntity<ResponseStatus> saveStateMaster(@RequestBody StateDto newStateDto) {
		return this.service.saveStateMaster(this.mapper.map(newStateDto, StateMaster.class));
	}

	@ApiOperation("Update the status of specific state master by its identifier. ")
	@PutMapping(path = "/updateState/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateStateMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateStatusFlag(id, flag);
	}

	@ApiOperation("Updates or partially updates an State by their identifier. 404 if does not exist.")
	@PutMapping(path = "/updateStateMaster/{id}")
	public ResponseEntity<ResponseStatus> updateStateMaster(@PathVariable("id") Integer id,
			@RequestBody StateDto stateDto) {
		StateMaster stateMaster = null;
		Optional<StateMaster> optional = stateMasterRepository.findById(id);
		if (optional.isPresent()) {
			stateMaster = optional.get();
			BeanUtils.copyProperties(stateDto, stateMaster);
		}
		return this.service.saveStateMaster(stateMaster);
	}

	@ApiOperation("Returns list of all CityMaster")
	@GetMapping(path = "/allCities")
	public List<CityDto> getAllCityByStatus() {
		log.info("Enter into getAllCityByStatus service");
		List<CityDto> cities = this.service.findAllCityByActiveStatus();
		return cities;
	}

	@ApiOperation("Creates a new City returning status 200 when persisted successfully.")
	@PostMapping("/saveCityMaster")
	public ResponseEntity<ResponseStatus> saveCityMaster(@RequestBody CityDto newCityDto) {
		return this.service.saveCityMaster(this.mapper.map(newCityDto, CityMaster.class));
	}

	@ApiOperation("Updates or partially updates an Depot by their identifier. 404 if does not exist.")
	@PutMapping(path = "/updateCityMaster/{id}")
	public ResponseEntity<ResponseStatus> updateCityMaster(@PathVariable("id") Integer id,
			@RequestBody CityDto cityDto) {
		CityMaster cityMaster = null;
		Optional<CityMaster> optional = cityMasterRepository.findById(id);
		if (optional.isPresent()) {
			cityMaster = optional.get();
			BeanUtils.copyProperties(cityDto, cityMaster);
		}
		return this.service.saveCityMaster(cityMaster);
	}

	@ApiOperation("Update the status of specific city master by its identifier. ")
	@PutMapping(path = "/updateCity/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateCityMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateCityMasterStatus(id, flag);
	}

	@ApiOperation("Returns list of all CityMaster By State Id")
	@GetMapping(path = "/allCities/{id}")
	public List<CityDto> getAllCityByStateId(@PathVariable("id") Integer id) {
		log.info("Enter into getAllCity service");
		return this.service.findAllCityByActiveStatus(id).stream().map(city -> this.mapper.map(city, CityDto.class))
				.collect(Collectors.toList());
	}

	@ApiOperation("Returns list of all TransportUnit")
	@GetMapping(path = "/allTranport")
	public List<TransportDto> getAllTransportUnit() {
		log.info("Enter into getAllTransportUnit service");
		return this.service.findAllTransportUnitByActiveStatus().stream()
				.map(transport -> this.mapper.map(transport, TransportDto.class)).collect(Collectors.toList());
	}

	@ApiOperation("Creates a new Transport Unit returning status 200 when persisted successfully.")
	@PostMapping("/saveTransportMaster")
	public ResponseEntity<ResponseStatus> saveTransportMaster(@RequestBody TransportDto newTransportDto) {
		return this.service.saveTransportMaster(this.mapper.map(newTransportDto, TransportUnitMaster.class));
	}

	@ApiOperation("Updates or partially updates an Depot by their identifier. 404 if does not exist.")
	@PutMapping(path = "/updateTransportMaster/{id}")
	public ResponseEntity<ResponseStatus> updateTransportMaster(@PathVariable("id") Integer id,
			@RequestBody TransportDto newTransportDto) {
		TransportUnitMaster transportUnitMaster = null;
		Optional<TransportUnitMaster> optional = transportUnitRepository.findById(id);
		if (optional.isPresent()) {
			transportUnitMaster = optional.get();
			BeanUtils.copyProperties(newTransportDto, transportUnitMaster);
		}
		return this.service.saveTransportMaster(transportUnitMaster);
	}

	@ApiOperation("Update the status of specific transport master by its identifier. ")
	@PutMapping(path = "/updateTransport/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateTransportMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateTransportMasterStatusFlag(id, flag);
	}

	@ApiOperation("Creates a new Depot returning status 200 when persisted successfully.")
	@PostMapping("/saveDepotMaster")
	public ResponseEntity<ResponseStatus> saveDepotMaster(@RequestBody DepotMasterDto depotMasterDto) {
		log.info("Enter into saveDepotMaster service");
		return this.service.saveDepotMaster(this.mapper.map(depotMasterDto, DepotMaster.class));
	}

	@ApiOperation("Updates or partially updates an Depot by their identifier. 404 if does not exist.")
	@PutMapping(path = "/updateDepotMaster/{id}")
	public ResponseEntity<ResponseStatus> updateDepotMaster(@PathVariable("id") Integer id,
			@RequestBody DepotMasterDto depotMasterDto) {
		DepotMaster depotMaster = null;
		Optional<DepotMaster> optional = this.depotMasterRepository.findById(id);
		if (optional.isPresent()) {
			depotMaster = optional.get();
			BeanUtils.copyProperties(depotMasterDto, depotMaster);
		}
		return this.service.saveDepotMaster(depotMaster);
	}

	@ApiOperation("Update the status of specific depot master by its identifier. ")
	@PutMapping(path = "/updateDepot/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateDepotMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateDepotMasterStatusFlag(id, flag);
	}

	@ApiOperation("Returns list of all Depot Master")
	@GetMapping(path = "/allDepot")
	public List<DepotMasterDto> getAllDepotMaster() {
		log.info("Enter into getAllDepotMaster service");
		List<Integer> tpIds = this.getTPUs();
		return this.service.findAllDepotMasterByActiveStatus(tpIds).stream()
				.map(depot -> this.mapper.map(depot, DepotMasterDto.class)).collect(Collectors.toList());
	}

	@ApiOperation("Returns list of all Tax Type Master")
	@GetMapping(path = "/allTaxType")
	public List<TypeOfTaxDto> getAllTaxTypeMaster() {
		log.info("Enter into getAllTaxTypeMaster service");
		return this.service.findAllTaxTypeByStatus().stream()
				.map(typeOfTaxDto -> this.mapper.map(typeOfTaxDto, TypeOfTaxDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Creates a new Bus Type returning status 200 when persisted successfully.")
	@PostMapping("/saveBusTypetMaster")
	public ResponseEntity<ResponseStatus> saveBusTypetMaster(@RequestBody BusTypeDto busTypeDto) {
		return this.service.saveBusTypetMaster(this.mapper.map(busTypeDto, BusTyperMaster.class));
	}
	
	@ApiOperation("Updates or partially updates an Bus Type by their identifier. 404 if does not exist.")
	@PutMapping(path = "/updateBusTypetMaster/{id}")
	public ResponseEntity<ResponseStatus> updateBusTypetMaster(@PathVariable("id") Integer id,
			@RequestBody BusTypeDto busTypeDto) {
		BusTyperMaster busTyperMaster = null;
		Optional<BusTyperMaster> optional = this.busTyperMasterRepository.findById(id);
		if (optional.isPresent()) {
			busTyperMaster = optional.get();
			BeanUtils.copyProperties(busTypeDto, busTyperMaster);
		}
		return this.service.saveBusTypetMaster(busTyperMaster);
	}

	@ApiOperation("Update the status of specific bus type master by its identifier. ")
	@PutMapping(path = "/updateBusType/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateBusTypeStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateBusTypeStatusFlag(id, flag);
	}

	@ApiOperation("Returns list of all Bus Type Master")
	@GetMapping(path = "/allBusType")
	public List<BusTypeDto> getAllBusTypeMaster() {
		log.info("Enter into getAllTaxTypeMaster service");
		return this.service.getAllBusTypeMaster().stream()
				.map(busTypeDto -> this.mapper.map(busTypeDto, BusTypeDto.class)).collect(Collectors.toList());
	}

	@ApiOperation("Creates a new Bus Sub Type returning status 200 when persisted successfully.")
	@PostMapping("/saveBusSubTypetMaster")
	public ResponseEntity<ResponseStatus> saveBusSubTypetMaster(@RequestBody BusSubTypeDto busSubTypeDto) {
		return this.service.saveBusSubTypetMaster(this.mapper.map(busSubTypeDto, BusSubTypeMaster.class));
	}

	@ApiOperation("Updates or partially updates an Bus Sub Type by their identifier. 404 if does not exist.")
	@PutMapping(path = "/updateBusSubTypetMaster/{id}")
	public ResponseEntity<ResponseStatus> updateBusSubTypetMaster(@PathVariable("id") Integer id,
			@RequestBody BusSubTypeDto busSubTypeDto) {
		BusSubTypeMaster busSubTypeMaster = null;
		Optional<BusSubTypeMaster> optional = this.busSubTypeMasterRepository.findById(id);
		if (optional.isPresent()) {
			busSubTypeMaster = optional.get();
			BeanUtils.copyProperties(busSubTypeDto, busSubTypeMaster);
		}
		return this.service.saveBusSubTypetMaster(busSubTypeMaster);
	}

	@ApiOperation("Update the status of specific bus sub type master by its identifier. ")
	@PutMapping(path = "/updateBusSubType/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateBusSubTypeStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateBusSubTypeStatusFlag(id, flag);
	}

	@ApiOperation("Returns list of all Bus Type Master")
	@GetMapping(path = "/allBusSubType")
	public List<BusSubTypeDto> getAllBusSubTypeMaster() {
		log.info("Enter into getAllBusSubTypeMaster service");
		return this.service.getAllBusSubTypeMaster().stream()
				.map(busSubTypeDto -> this.mapper.map(busSubTypeDto, BusSubTypeDto.class)).collect(Collectors.toList());
	}

	@ApiOperation("Creates a new Tax Master returning status 200 when persisted successfully.")
	@PostMapping("/saveTaxMaster")
	public ResponseEntity<ResponseStatus> saveTaxMaster(@RequestBody TaxMasterDto taxMasterDto) {
		return this.service.saveTaxMaster(this.mapper.map(taxMasterDto, TaxMaster.class));
	}

	@ApiOperation("Updates or partially updates a Tax Master by their identifier. 404 if does not exist.")
	@PutMapping(path = "/updateTaxMaster/{id}")
	public ResponseEntity<ResponseStatus> updateTaxMaster(@PathVariable("id") Integer id,
			@RequestBody TaxMasterDto taxMasterDto) {
		TaxMaster taxMaster = null;
		Optional<TaxMaster> optional = this.taxMasterRepository.findById(id);
		if (optional.isPresent()) {
			taxMaster = optional.get();
			BeanUtils.copyProperties(taxMasterDto, taxMaster);
			if (taxMasterDto != null && taxMasterDto.getState() != null && taxMasterDto.getState().getId() != null)
				taxMaster.setState(stateMasterRepository.findById(taxMasterDto.getState().getId()).get());
			if (taxMasterDto != null && taxMasterDto.getTypeOfTaxMaster() != null
					&& taxMasterDto.getTypeOfTaxMaster().getId() != null)
				taxMaster.setTypeOfTaxMaster(
						taxTypeMasterRepository.findById(taxMasterDto.getTypeOfTaxMaster().getId()).get());
			if (taxMasterDto != null && taxMasterDto.getBusTyperMaster() != null
					&& taxMasterDto.getBusTyperMaster().getId() != null)
				taxMaster.setBusTyperMaster(
						busTyperMasterRepository.findById(taxMasterDto.getBusTyperMaster().getId()).get());
		}
		return this.service.saveTaxMaster(taxMaster);
	}

	@ApiOperation("Update the status of specific tax master by its identifier. ")
	@PutMapping(path = "/updateTaxMaster/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateTaxMasterStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateTaxMasterStatusFlag(id, flag);
	}

	@ApiOperation("Returns list of all Tax Master")
	@GetMapping(path = "/allTaxMaster")
	public List<TaxMasterDto> getAllTaxMaster() {
		log.info("Enter into getAllTaxMaster service");
		return this.service.getAllTaxMaster();
	}
	
	@ApiOperation("Returns list of all Transport Master By Depot")
	@GetMapping(path = "/allTransportMasterByDepot/{id}")
	public List<TransportDto> allTransportMasterByDepot(@PathVariable("id") Integer id) {
		log.info("Enter into allTransportMasterByDepot service");
		return this.service.allTransportMasterByDepot(id).stream()
				.map(transportDto -> this.mapper.map(transportDto, TransportDto.class)).collect(Collectors.toList());
	}
	
	public List<Integer> getTPUs() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Integer> tpus = new ArrayList<Integer>();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<User> optional = userService.findByUserName(authentication.getName());
			if (optional.isPresent()) {
				for(UserGroups tpu : optional.get().getTpGroups()) {
					tpus.add(tpu.getTransportUnit().getId());
				}
			}
		}
		return tpus;
	}
	
	@ApiOperation("Returns list of all Tyre Type Master")
	@GetMapping(path="/allTyreType")
	public List<TyreTypeDto> getAllTyreType() {
		return this.service.getAllTyreType().stream()
				.map(tyreType -> this.mapper.map(tyreType, TyreTypeDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Creates a new Tyre Type returning status 200 when persisted successfully.")
	@PostMapping("/saveTyreTypetMaster")
	public ResponseEntity<ResponseStatus> saveTyreTypetMaster(@RequestBody TyreTypeDto tyreTypeDto) {
		return this.service.saveTyreTypetMaster(this.mapper.map(tyreTypeDto, TyreType.class));
	}
	
	@ApiOperation("Update the status of specific tyre type master by its identifier. ")
	@PutMapping(path = "/updateTyreType/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateTyreTypeStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateTyreTypeStatusFlag(id, flag);
	}
	
	@ApiOperation("Returns list of all Tyre Size Master")
	@GetMapping(path="/allTyreSize")
	public List<TyreSizeDto> getAllTyreSize() {
		return this.service.getAllTyreSize().stream()
				.map(tyreSize -> this.mapper.map(tyreSize, TyreSizeDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Creates a new Tyre Size returning status 200 when persisted successfully.")
	@PostMapping("/saveTyreSizeMaster")
	public ResponseEntity<ResponseStatus> saveTyreSizetMaster(@RequestBody TyreSizeDto tyreSizeDto) {
		return this.service.saveTyreSizeMaster(this.mapper.map(tyreSizeDto, TyreSize.class));
	}
	
	@ApiOperation("Update the status of specific tyre size master by its identifier. ")
	@PutMapping(path = "/updateTyreSize/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateTyreSizeStatusFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateTyreSizeStatusFlag(id, flag);
	}
	
	@ApiOperation("Returns Form Load Data for Bus, Tyre Type, Tyre Size list")
	@GetMapping(path="/getFormLoadBusTyreTypeSize")
	public BusTypeTyreTypeSizeForm getFormLoadBusTyreTypeSize() {
		return this.service.getBusTyreTypeSizeFormData();
	}
	
	@ApiOperation("Returns List of Bus Type, Tyre Type and Tyre Size Association")
	@GetMapping("/getFormLoadBusTyreTypeList")
	public List<BusTyreTypeSizeMappingDto> getFormLoadBusTyreTypeList() {
		return this.service.getAllBusTyreTypeSizeMapping().stream()
				.map(list -> this.mapper.map(list, BusTyreTypeSizeMappingDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Creates a new Bus Tyre, Type and Size Mapping returning status 200 when persisted successfully.")
	@PostMapping("/saveBusTyreTypeSizeMapping")
	public ResponseEntity<ResponseStatus> saveBusTyreTypeSizeMapping(@RequestBody BusTyreTypeSizeMappingDto dto) {
		return this.service.saveBusTyreTypeSizeMapping(this.mapper.map(dto, BusTyreTypeSizeMapping.class));
	} 
	
	@ApiOperation("Returns Form Load Data for Maker, Tyre Type and Tyre Size")
	@GetMapping(path="/getFormLoadTyreMakerDetails")
	public MakerTyreDetailsFormDto getFormLoadTyreMakerDetails() {
		return this.service.getFormLoadTyreMakerDetails();
	}
	
	@ApiOperation("Returns List of Bus Type, Tyre Type and Tyre Size Association")
	@GetMapping("/getTyreMakerDetailsList")
	public List<MakerTyreDetailsDto> getTyreMakerDetailsList() {
		return this.service.getTyreMakerDetailsList().stream()
				.map(list -> this.mapper.map(list, MakerTyreDetailsDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Creates a new Maker and Tyre Details Mapping returning status 200 when persisted successfully.")
	@PostMapping("/saveTyreMakerDetails")
	public ResponseEntity<ResponseStatus> saveTyreMakerDetails(@RequestBody MakerTyreDetailsDto dto) {
		return this.service.saveTyreMakerDetails(this.mapper.map(dto, MakerTyreDetails.class));
	}

	@ApiOperation("Returns List of Bus Reg No")
	@GetMapping("/getBusRegNoList")
	public List<BusRegNoDto> getBusRegNoList() {
		
		return this.service.getBusRegNoList();
	}
	
	@ApiOperation("Returns List Of Drum Master On Load")
	@GetMapping(path="/getDrumMasterList/{depotCode}")
	public List<MobilOilDrumMasterDto> getDrumMasterListOnLoad(@PathVariable("depotCode") String depotCode) {
		return this.service.getDrumMasterListOnLoad(depotCode);
	}
	
	@ApiOperation("Creates  new Drum Master returning status 200 when persisted successfully.")
	@PostMapping("/saveDrumMaster")
	public ResponseEntity<ResponseStatus> saveDrumMaster(@RequestBody MobilOilDrumMasterDto dto) {
		return this.service.saveDrumMaster(this.mapper.map(dto, MobilOilDrumMaster.class));
	}
	
	@ApiOperation("Returns List Of Add Blue Drum Master On Load")
	@GetMapping(path="/getAddBlueDrumMasterList/{depotCode}")
	public List<AddBlueDrumMasterDto> getAddBlueDrumMasterList(@PathVariable("depotCode") String depotCode) {
		return this.service.getAddBlueDrumMasterList(depotCode);
	}
	
	@ApiOperation("Creates  new Add Blue Drum Master returning status 200 when persisted successfully.")
	@PostMapping("/saveAddBlueDrumMaster")
	public ResponseEntity<ResponseStatus> saveAddBlueDrumMaster(@RequestBody AddBlueDrumMasterDto dto) {
		return this.service.saveAddBlueDrumMaster(this.mapper.map(dto, AddBlueDrumMaster.class));
	}
	
	
	
	@GetMapping(path="/depotCodeAnddepoId")
	public ResponseEntity<List<DepotMasterDetailsDto>> getDepotcodeAndId()
	{
		
		List<DepotMasterDetailsDto> depotmasterIdAndDepocode = service.getDepotmasterIdAndDepocode();
		return new ResponseEntity<List<DepotMasterDetailsDto>>(depotmasterIdAndDepocode,HttpStatus.OK);
		
	}
	
	
	
	@ApiOperation("Update the status of specific BusTyreTypeSizeMapping by its identifier. ")
	@PutMapping(path = "/updateBusTyreTypeSizeMapping/{id}/{flag}")
	public ResponseEntity<ResponseStatus> updateBusTyreTypeSizeMappingFlag(@PathVariable("id") Integer id,
			@PathVariable("flag") Boolean flag) {
		return this.service.updateBusTyreTypeSizeMapping(id, flag);
	}
	
	}
