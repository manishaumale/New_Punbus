package com.idms.base.api.v1;

import java.util.List;

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

import com.idms.base.api.v1.model.dto.CentralTicketStockDto;
import com.idms.base.api.v1.model.dto.MasterFormListDataDto;
import com.idms.base.api.v1.model.dto.MasterStockDto;
import com.idms.base.api.v1.model.dto.MasterStockfrmLoadDto;
import com.idms.base.dao.entity.CentralTicketStock;
import com.idms.base.dao.entity.MasterStock;
import com.idms.base.service.MasterStockService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/bimmasterstock")
@Log4j2
public class MasterStockController {

	@Autowired(required = true)
	private MasterStockService service;

	@GetMapping("/masterFormLoad/{tpGroup}")
	public MasterStockfrmLoadDto masterFrmLoad(@PathVariable("tpGroup") String tpGroup) {
		MasterStockfrmLoadDto dto = this.service.masterFormOnLoad(tpGroup);
		return dto;
	}
	
	@ApiOperation("Creates ticket master stock returning status 200 when persisted successfully.")
	@PostMapping("/saveMasterStock")
	public ResponseEntity<ResponseStatus> saveMasterStock(@RequestBody CentralTicketStock centralTicketStock) {
		return service.saveCentralStock(centralTicketStock);
	}
	
	@GetMapping("/getMasterStockListUpdated/{tpGroup}") 
	public List<CentralTicketStockDto> getMasterStockListUpdated(@PathVariable("tpGroup") String tpGroup) {
		return service.getMasterStockListUpdated(tpGroup);
	}
	
	@GetMapping("/getMasterStockTicketList/{tpId}/{denoId}") 
	public List<MasterStockDto> getMasterStockTicketList(@PathVariable("tpId") Integer tpId, @PathVariable("denoId") Integer denoId) {
		return service.getMasterStockTicketList(tpId, denoId);
	}
}
