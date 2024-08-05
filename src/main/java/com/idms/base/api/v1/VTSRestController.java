package com.idms.base.api.v1;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.idms.base.api.v1.model.dto.VTSDataDto;
import com.idms.base.dao.entity.VTSData;
import com.idms.base.service.VTSDataService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/VTS")
@Log4j2
public class VTSRestController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	VTSDataService vtsService;
	
	@ApiOperation("VTS data")
	@PostMapping("/postVTSData")
	public ResponseStatus postVTSData(@RequestBody VTSDataDto vtsDataDto) {
		return vtsService.saveVTSData(this.mapper.map(vtsDataDto, VTSData.class));
	}
	
	@ApiOperation("Get VTS Data")
	@GetMapping("/getVTSData")
	public ResponseEntity<VTSDataDto> getVTSData() {
		return null;
	}

}
