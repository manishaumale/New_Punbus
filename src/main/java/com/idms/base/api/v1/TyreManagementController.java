package com.idms.base.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.BusMasterDto;
import com.idms.base.api.v1.model.dto.BusTyreAssoParentWrapper;
import com.idms.base.api.v1.model.dto.BusTyreAssociationForm;
import com.idms.base.api.v1.model.dto.BusTyreAssociationList;
import com.idms.base.api.v1.model.dto.MakerTyreDetailsDto;
import com.idms.base.api.v1.model.dto.MarkSpareBusDetailsDto;
import com.idms.base.api.v1.model.dto.OldTyreMasterDto;
import com.idms.base.api.v1.model.dto.TyreConditionDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.api.v1.model.dto.TyreMasterFormDto;
import com.idms.base.api.v1.model.dto.TyreSizeDto;
import com.idms.base.api.v1.model.dto.TyreTypeDto;
import com.idms.base.dao.entity.BusMaster;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.dao.repository.TyreMasterRepository;
import com.idms.base.service.TyreManagementService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/Tyre")
@Log4j2
public class TyreManagementController {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private TyreManagementService tyreService;
	
	@Autowired
	private TyreMasterRepository tyreRepo;

	@ApiOperation("Returns Object of  Permit Details Master When Load")
	@GetMapping(path = "/tyreMasterFormLoad/{dpCode}")
	public TyreMasterFormDto tyreMasterFormLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into tyreMasterFormLoad Method");
		TyreMasterFormDto form = this.tyreService.getTyreMasterForm(dpCode);
		return form;
	}

	@ApiOperation("Return list of tyres")
	@GetMapping(path = "/getTyreList/{dpCode}")
	public List<TyreMasterDto> getTyreList(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getTyreList Method Controller TyreManagementService");
		return this.tyreService.getAllTyreListByDepot(dpCode);

	}
	
	@ApiOperation("Get Tyre Details")
	@GetMapping(path="/getTyreDetails/{id}")
	public TyreMasterDto getTyreDetails(@PathVariable("id") Integer tyreId) {
		return this.tyreService.getTyreDetails(tyreId);
	}
	
	@ApiOperation("Return list of tyres types")
	@GetMapping(path = "/getTyreTypeList/{id}")
	public List<TyreTypeDto> getTyreTypeList(@PathVariable("id") Integer makerId) {
		log.info("Enter into getTyreTypeList Method Controller TyreManagementService");
		return this.tyreService.getTyreTypeList(makerId).stream()
				.map(tyreType -> this.mapper.map(tyreType, TyreTypeDto.class)).collect(Collectors.toList());

	}
	
	@ApiOperation("Return list of tyres size")
	@GetMapping(path = "/getTyreSizeList/{tyreMakerId}/{tyreTypeId}")
	public List<TyreSizeDto> getTyreSizeList(@PathVariable("tyreMakerId") Integer tyreMakerId, @PathVariable("tyreTypeId") Integer tyreTypeId) {
		log.info("Enter into getTyreTypeList Method Controller TyreManagementService");
		return this.tyreService.getTyreSizeList(tyreMakerId, tyreTypeId).stream()
				.map(tyreSize -> this.mapper.map(tyreSize, TyreSizeDto.class)).collect(Collectors.toList());

	}
	
	@ApiOperation("Return list of tyres size")
	@GetMapping(path = "/getTyreCostMilage/{tyreMakerId}/{tyreTypeId}/{tyreSizeId}")
	public MakerTyreDetailsDto getTyreCostMilage(@PathVariable("tyreMakerId") Integer tyreMakerId, @PathVariable("tyreTypeId") Integer tyreTypeId, @PathVariable("tyreSizeId") Integer tyreSizeId) {
		log.info("Enter into getTyreTypeList Method Controller TyreManagementService");
		return this.tyreService.getTyreCostMilage(tyreMakerId, tyreTypeId, tyreSizeId);

	}
	
	@ApiOperation("Return list of tyres size")
	@PostMapping("/saveTyreDetails")
	public ResponseEntity<ResponseStatus> saveTyreDetails(@RequestBody TyreMasterDto tyreMasterDto) {
		return this.tyreService.saveTyreDetails(this.mapper.map(tyreMasterDto, TyreMaster.class));
	}
	
	@ApiOperation("Get List of Bus Tyre Association")
	@GetMapping(path = "/getBusTyreAssociationList/{dpCode}")
	public List<BusTyreAssociationList> getBusTyreAssociationList(@PathVariable("dpCode") String dpCode) {
		return this.tyreService.getAllBusTyreAssociationList(dpCode).stream()
				.map(association -> this.mapper.map(association, BusTyreAssociationList.class)).collect(Collectors.toList());
		
	}
	
	@ApiOperation("Returns Form of Bus Tyre Association")
	@GetMapping(path = "/busTyreAssociationFormLoad/{busId}")
	public BusTyreAssociationForm busTyreAssociationFormLoad(@PathVariable("busId") Integer busId) {
		log.info("Enter into tyreMasterFormLoad Method");
		BusTyreAssociationForm form = this.tyreService.busTyreAssociationFormLoad(busId);
		return form;
	}
	
	@ApiOperation("")
	@PostMapping("/saveBusTyreAssociations")
	public ResponseEntity<ResponseStatus> saveBusTyreAssociations(@RequestBody BusMasterDto busMasterDto) {
		return this.tyreService.saveBusTyreAssociations(this.mapper.map(busMasterDto, BusMaster.class));
	}
	
	@ApiOperation("Return list of tyres condition")
	@GetMapping(path = "/getTyreCondition")
	public List<TyreConditionDto> getTyreCondition() {
		log.info("Enter into getTyreCondition Method Controller TyreManagementService");
		List<TyreConditionDto> conditionList = tyreService.getTyreConditionList();
		return conditionList;

	}
	
	@ApiOperation("Return list of tyres by Active Status")
	@GetMapping(path = "/getAllTyresByStatus")
	public List<TyreMasterDto> getAllTyresByStatus() {
		log.info("Enter into getAllTyresByStatus Method Controller TyreManagementService");
		List<TyreMasterDto> tyreList = tyreService.getAllTyresByStatus();
		return tyreList;

	}
	
	@PutMapping(path="/updateTyreDtls/{fromdate}/{todate}/{oldMileage}/{Id}")
	public  ResponseEntity<ResponseStatus> updateTyreDtls(@PathVariable("fromdate")String fromdate,@PathVariable("todate") String todate,@PathVariable("oldMileage")Float oldMileage,
			@PathVariable("Id")Integer Id)
	{
		
		return this.tyreService.updateTyreDtls(fromdate, todate, oldMileage,Id);
     }
	
	@GetMapping(path = "/getuseTyreId/{dpCode}/{tyreConditionId}")
	public ResponseEntity<TyreMasterDto> getuseTyreId(@PathVariable("dpCode")String dpCode,@PathVariable("tyreConditionId") Integer tyreConditionId) {
		log.info("Enter into getuseTyreIdDtls Controller TyreManagementService");
		 TyreMasterDto getuseTyreId = tyreService.getuseTyreId(dpCode,tyreConditionId);
		return new ResponseEntity<TyreMasterDto>(getuseTyreId,HttpStatus.OK);

	}
	
	@GetMapping(path="/getTyreNumbers/{dpCode}/{tyreConditionId}")
	public ResponseEntity <TyreMasterFormDto> getNumbers(@PathVariable("dpCode")String dpCode,@PathVariable("tyreConditionId") Integer tyreConditionId)
	{
		
		 TyreMasterFormDto numbers = tyreService.getNumbers(dpCode, tyreConditionId);
		return new ResponseEntity<TyreMasterFormDto>(numbers,HttpStatus.OK);
		
	}
	
	@ApiOperation("Save Tyre And Associations")
	@PostMapping("/saveTyreDetailsAndAssociations")
	public ResponseEntity<ResponseStatus> saveTyreDetailsAndAssociations(@RequestBody BusTyreAssoParentWrapper parentDto) {
		return this.tyreService.saveTyreDetailsAndAssociations(parentDto);
	}
	
	@ApiOperation(" Fetch All Bus Dtls")
	@GetMapping(value="/getbusdetails/{transportId}")
	public ResponseEntity<List<MarkSpareBusDetailsDto>> getBusDetails(@PathVariable("transportId") Integer transportId) {
		log.info("Enter into getBusDetails Controller TyreManagementService");
		List<MarkSpareBusDetailsDto> allBusDetails = tyreService.getAllBusDetails(transportId);
		return new ResponseEntity<List<MarkSpareBusDetailsDto>>(allBusDetails, HttpStatus.OK);

	}
	
	@ApiOperation("Save Old Tyre And Associations history")
	@PostMapping("/saveOldTyreUnfitted")
	public ResponseEntity<ResponseStatus> saveOldTyreUnfitted(@RequestBody TyreMasterDto tyreMasterDto) {
		return this.tyreService.saveOldTyreUnfitted(this.mapper.map(tyreMasterDto, TyreMaster.class));
	}
	
	@ApiOperation("Return list of old tyres")
	@GetMapping(path = "/getAllOldTyreListByDepot/{dpCode}")
	public List<OldTyreMasterDto> getAllOldTyreListByDepot(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into getAllOldTyreListByDepot Method Controller");
		return this.tyreService.getAllOldTyreListByDepot(dpCode);

	}
	
	@ApiOperation("Return PurchaseDate  On change Of Bus")
	@GetMapping(path = "/getPurchaseDateByBus/{busId}")
	public BusMasterDto getPurchaseDateByBus(@PathVariable("busId") Integer busId) {
		log.info("Enter into getPurchaseDateByBus Method Controller");
		return this.tyreService.getPurchaseDateByBus(busId);

	}
	
	@ApiOperation("Return Tyre List  On change Of Size Make And Type ")
	@GetMapping(path = "/getTyreListBySizeMakeAndType/{sizeId}/{makeId}/{typeId}/{conditionId}")
	public List<TyreMasterDto> getTyreListBySizeMakeAndType(@PathVariable("sizeId") Integer sizeId,@PathVariable("makeId") 
	Integer makeId,@PathVariable("typeId") Integer typeId,@PathVariable("conditionId") Integer conditionId) {
		log.info("Enter into getTyreListBySizeMakeAndType Method Controller");
		return this.tyreService.getTyreListBySizeMakeAndType(sizeId,makeId,typeId,conditionId);

	}
	@ApiOperation("Return old tyre and history details On Tyre id")
	@GetMapping(path = "/getOldTyreByTyreId/{tyreId}")
	public OldTyreMasterDto getOldTyreByTyreId(@PathVariable("tyreId") Integer tyreId) {
		log.info("Enter into getOldTyreByTyreId Method Controller");
		return this.tyreService.getOldTyreByTyreId(tyreId);

	}
	
	@ApiOperation("Validate duplicate tyre number by tyre number")
	@GetMapping(path = "/validateDuplicateByTyreNo/{tyreNo}")
	public ResponseEntity<ResponseStatus> validateDuplicateByTyreNo(@PathVariable("tyreNo") String tyreNo) {
		log.info("Enter into validateDuplicateByTyreNo Method Controller");
		return this.tyreService.validateDuplicateByTyreNo(tyreNo);

	}
	
}
