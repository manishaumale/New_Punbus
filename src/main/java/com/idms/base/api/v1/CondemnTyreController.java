package com.idms.base.api.v1;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.api.v1.model.dto.AuctionedDocketDto;
import com.idms.base.api.v1.model.dto.RetreadingDocketDto;
import com.idms.base.api.v1.model.dto.TyreCondemnFormDto;
import com.idms.base.api.v1.model.dto.TyreMasterDto;
import com.idms.base.service.CondemnTyreService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/TyreCondemn")
@Log4j2
public class CondemnTyreController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CondemnTyreService service;
	
	@ApiOperation("Get Form load for Tyre Change")
	@GetMapping(path = "/getCondemnTyreForm/{dpCode}") 
	private TyreCondemnFormDto getCondemnTyreForm(@PathVariable("dpCode") String dpCode) {
		return service.getCondemnTyreForm(dpCode);
	}
	
	@ApiOperation("Get Form load for Tyre Change")
	@GetMapping(path = "/getCondemnTyreList/{tpId}/{sizeId}/{dpCode}")
	private List<TyreMasterDto> getCondemnTyreList(@PathVariable("tpId") Integer tpId, @PathVariable("sizeId") Integer sizeId, @PathVariable("dpCode") String dpCode) {
		return service.getTyreList(tpId, sizeId, dpCode);
	}
	
	@ApiOperation("Save Received List")
	@PostMapping(path="/saveMarkedForAuction")
	public ResponseEntity<ResponseStatus> saveMarkedForAuction(@RequestBody RetreadingDocketDto docketDto) {
		return service.saveMarkedForAuction(docketDto);
	}
	
	@ApiOperation("Get Form load for Tyre Change")
	@GetMapping(path = "/getCondemnAvailTyreList/{tpId}/{sizeId}/{dpCode}")
	private List<TyreMasterDto> getCondemnAvailTyreList(@PathVariable("tpId") Integer tpId, @PathVariable("sizeId") Integer sizeId, @PathVariable("dpCode") String dpCode) {
		return service.getCondemnAvailTyreList(tpId, sizeId, dpCode);
	}
	
	@ApiOperation("Get Form load for Tyre Change")
	
	@PostMapping(value="/saveAuctionedCondemnTyres",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseStatus> saveAuctionedCondemnTyres(@RequestBody AuctionedDocketDto docketDto) {
		return this.service.saveAuctionedCondemnTyres(docketDto);
	}
	
	@ApiOperation("Get Auctioned Tyre Dockets")
	@GetMapping(path = "/getAuctionedDocketList/{dpCode}") 
	public List<AuctionedDocketDto> getAuctionedDocketList(@PathVariable("dpCode") String dpCode) {
		return this.service.getAuctionedDocketList(dpCode);
	}
	
	@ApiOperation("Get Form load for Tyre Change")
	@GetMapping(path = "/getAuctionedTyreList/{docketId}") 
	public List<TyreMasterDto> getAuctionedTyreList(@PathVariable("docketId") Integer docketId) {
		return this.service.getAuctionedTyreList(docketId);
	}
	
	
}
