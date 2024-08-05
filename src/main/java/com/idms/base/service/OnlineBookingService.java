package com.idms.base.service;

import org.springframework.http.ResponseEntity;

import com.idms.base.api.v1.model.dto.OnlineBookingDetailsDto;
import com.idms.base.support.persist.ResponseStatus;

public interface OnlineBookingService {

	ResponseEntity<ResponseStatus> saveOnlineBooking(OnlineBookingDetailsDto bookingDetails);

	ResponseEntity<ResponseStatus> cancelOnlineBooking(String bookingId);

}
