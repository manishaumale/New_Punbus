package com.idms.base.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.TyreHistoryDto;
import com.idms.base.api.v1.model.dto.TyreRepairDto;
import com.idms.base.dao.entity.TyreMaster;
import com.idms.base.service.impl.TyreRepairServiceImpl;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/TyreRepair")
@Log4j2
public class TyreRepairController {

	@Autowired
	TyreRepairServiceImpl service;
	@GetMapping("/getRepairTyres/{depotCode}")
	public List<TyreRepairDto> getAllRepairTyres(@PathVariable String depotCode) {
		return service.getRepairTyres(depotCode);
	}
	
	@PostMapping("/updateRepairedTyre")
	public ResponseEntity<ResponseStatus> updateRepairedTyre(@RequestParam Integer id, @RequestParam String depotCode,@RequestParam String Code,@RequestParam String remarks){
		return service.updateTyre(id, depotCode,Code,remarks);
	}
	
	@GetMapping("/getRepariredTyres/{depotCode}")
	public List<TyreHistoryDto> getAllRepairedTyres(@PathVariable String depotCode) {
		return service.getRepairedTyres(depotCode);
	}
	
}
