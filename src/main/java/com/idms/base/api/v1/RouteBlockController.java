package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.RouteBlockDto;
import com.idms.base.service.RouteBlockService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/routeBlock")
@Log4j2
public class RouteBlockController {

	@Autowired
	RouteBlockService service;

	@ApiOperation("Returns Route Block using dpcode")
	@GetMapping(path = "/getRouteBlockOnLoad/{dpCode}")
	public List<RouteBlockDto> getRouteBlockOnLoad(@PathVariable("dpCode") String dpCode) {

		log.info("Enter into getRouteBlock service");
		List<RouteBlockDto> getRouteBlockList = this.service.getRouteBlockOnLoad(dpCode);
		return getRouteBlockList;

	}
	@ApiOperation("Returns Route Block using dpcode")
	@GetMapping(path = "/getRouteBlock/{dpCode}")
	public List<RouteBlockDto> getRouteBlock(@PathVariable("dpCode") String dpCode) {

		log.info("Enter into getRouteBlock service");
		List<RouteBlockDto> getRouteBlockList = this.service.getRouteBlock(dpCode);
		return getRouteBlockList;

	}

	@ApiOperation("Creates a new Route Block returning status 200 when persisted successfully.")
	@PostMapping("/saveRouteBlock")
	public ResponseEntity<ResponseStatus> saveRouteBlock(@RequestBody RouteBlockDto routeBlockDto) {
		log.info("Enter into saveRouteBlock service");
		 return this.service.saveRouteblock(routeBlockDto);
	}

}
