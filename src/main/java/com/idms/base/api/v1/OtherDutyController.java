package com.idms.base.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.OtherDutyDto;
import com.idms.base.api.v1.model.dto.OtherDutyOnLoadDto;
import com.idms.base.dao.entity.OtherDuty;
import com.idms.base.service.OtherDutyService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/otherDuty")
@Log4j2
public class OtherDutyController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private OtherDutyService service;
	
	
	@ApiOperation("Returns Object of  Other Duty  When Load")
	@GetMapping(path = "/otherDutyOnLoad/{dpCode}")
	public OtherDutyOnLoadDto otherDutyOnLoad(@PathVariable("dpCode") String dpCode) {
		log.info("Enter into otherDutyOnLoad service");
		OtherDutyOnLoadDto otherDutyOnLoadDto = this.service.otherDutyOnLoad(dpCode);
		return otherDutyOnLoadDto;

	}
	
	@ApiOperation("Creates a new Other Duty returning status 200 when persisted successfully.")
	@PostMapping("/saveOtherDuty")
	public ResponseEntity<ResponseStatus> saveOtherPurpose(@RequestBody OtherDuty otherDuty) {
		log.info("Enter into saveOtherDuty service");
		return this.service.saveOtherDuty(otherDuty);
	}
	
	@ApiOperation("Returns List of Other Duty")
	@GetMapping(path = "/listOfAllOtherDuty")
	public List<OtherDutyDto> listOfAllOtherDuty() {
		log.info("Enter into listOfAllOtherDuty service");
		return this.service.listOfAllOtherDuty().stream()
				.map(other -> this.mapper.map(other, OtherDutyDto.class)).collect(Collectors.toList());
	}
	
	@ApiOperation("Returns Avialability of driver or conductor")
	@GetMapping(path = "/fetchAvailabilityStatus/{driverFlag}/{driverOrConductorId}")
	public ResponseEntity<ResponseStatus> fetchAvailabilityStatus(@PathVariable("driverFlag") String driverFlag,
			@PathVariable("driverOrConductorId") Integer driverOrConductorId) {
		log.info("Enter into fetchAvailabilityStatus service");
		return this.service.fetchAvailabilityStatus(driverFlag,driverOrConductorId);
	}
	
}
