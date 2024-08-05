package com.idms.base.api.v1;

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

import com.idms.base.api.v1.model.dto.OnlineBookingDetailsDto;
import com.idms.base.service.OnlineBookingService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/onlineBooking")
@Log4j2
public class OnlineBookingAPIController {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private OnlineBookingService service;
	
	@ApiOperation("")
	@PostMapping("/saveOnlineBookingDetail")
	public ResponseEntity<ResponseStatus> saveOnlineBookingDetail(@RequestBody OnlineBookingDetailsDto bookingDetails) {
		return this.service.saveOnlineBooking(bookingDetails);
	}
	
	@ApiOperation("")
	@GetMapping("/cancelOnlineBooking/{bookingId}")
	public ResponseEntity<ResponseStatus> cancelOnlineBooking(@PathVariable("bookingId") String bookingId) {
		return this.service.cancelOnlineBooking(bookingId);
	}
	
	

}
