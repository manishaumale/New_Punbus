package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.VTSTyreKmsDetailsDto;
import com.idms.base.service.VTSTyreKmsDetailsService;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/VTSTyreKmsDetails")
public class VTSTyreKmsDetailsController {
	
	@Autowired
	private VTSTyreKmsDetailsService vtsTyreKmsDetailsService;
	
	@ApiOperation("Get List of VTS tyre kms details")
	@GetMapping(path = "/getVtsTyreKmsDetails/{busNumber}/{date}")
	public List<VTSTyreKmsDetailsDto> getVtsTyreKmsDetails(@PathVariable("busNumber") String busNumber,@PathVariable("date") String date) {
		List<VTSTyreKmsDetailsDto> list = vtsTyreKmsDetailsService.getVTSTyreKmsDetailsDto(busNumber,date);
		return list;
	}

}
