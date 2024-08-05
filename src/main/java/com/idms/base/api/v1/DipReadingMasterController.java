package com.idms.base.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.DipReadingMasterDto;
import com.idms.base.api.v1.model.dto.ReadingMasterDto;
import com.idms.base.api.v1.model.dto.TankCapacityMasterDto;
import com.idms.base.dao.entity.DipReadingMaster;
import com.idms.base.dao.entity.ReadingMaster;
import com.idms.base.service.DipReadingMasterService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/dip/reading")
@Log4j2
public class DipReadingMasterController {

	@Autowired
	DipReadingMasterService service;

	@Autowired
	private ModelMapper mapper;

	@ApiOperation("Creates a new Dip Reading Master returning status 200 when persisted successfully.")
	@PostMapping("/saveDipReadingMaster")
	public ResponseEntity<ResponseStatus> saveFuelTankMaster(@RequestBody DipReadingMaster dipReadingMaster) {
		log.info("Enter into saveDipReadingMaster service");
		return this.service.saveDipReadingMaster(dipReadingMaster);
	}

	@ApiOperation("Returns List of All Dip Reading Master")
	@GetMapping(path = "/listOfAllDipReadingMaster")
	public List<DipReadingMasterDto> listOfAllDipReadingMaster() {
		log.info("Enter into listOfAllDipReadingMaster service");

		/*
		 * List<DipReadingMasterDto> readingList =
		 * this.service.listOfAllDipReadingMaster().stream().map(reading ->
		 * this.mapper.map(reading, DipReadingMasterDto.class))
		 * .collect(Collectors.toList());
		 */

		List<DipReadingMaster> readingMaster = service.listOfAllDipReadingMaster();

		List<DipReadingMasterDto> collect = readingMaster.stream().map(entity -> {
			DipReadingMasterDto dipReadingMasterDtoConversion = this.dipReadingMasterDtoConversion(entity.getId(), entity.getReadingName(),
					new TankCapacityMasterDto(entity.getCapacityMaster().getId(), entity.getCapacityMaster().getName(),
							entity.getCapacityMaster().getCapacity()),
					entity.getDiameter(), entity.getRadius(), entity.getLength(), entity.getDeadboard(),entity.getReadings());
			return dipReadingMasterDtoConversion;
		}).collect(Collectors.toList());

		return collect;

		
	}

	public DipReadingMasterDto dipReadingMasterDtoConversion(Integer id, String readingName, TankCapacityMasterDto capacityMaster, Float diameter,
			Float radius, Float length, Float deadboard,List<ReadingMaster> readings) {
		DipReadingMasterDto dipReadingMasterDto=new DipReadingMasterDto();
		dipReadingMasterDto.setId(id);
		dipReadingMasterDto.setReadingName(readingName);
		dipReadingMasterDto.setCapacityMaster(new TankCapacityMasterDto(capacityMaster.getId(), capacityMaster.getName(), capacityMaster.getCapacity()));
		dipReadingMasterDto.setDiameter(diameter);
		dipReadingMasterDto.setRadius(radius);
		dipReadingMasterDto.setLength(length);
		dipReadingMasterDto.setDeadboard(deadboard);
		List<ReadingMasterDto> readingsDto = new ArrayList<>();
		for(ReadingMaster read : readings) {
			ReadingMasterDto obj  =new ReadingMasterDto();
			obj.setId(read.getId());
			obj.setReading(read.getReading());
			obj.setVolume(read.getVolume());
			readingsDto.add(obj);
		}
		dipReadingMasterDto.setReadings(readingsDto);
		return dipReadingMasterDto;
	}
}
