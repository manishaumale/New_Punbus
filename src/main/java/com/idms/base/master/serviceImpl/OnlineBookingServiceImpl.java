package com.idms.base.master.serviceImpl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.idms.base.api.v1.model.dto.OnlineBookingDetailsDto;
import com.idms.base.dao.entity.CityMaster;
import com.idms.base.dao.entity.OnlineBookingDetails;
import com.idms.base.dao.entity.TripMaster;
import com.idms.base.dao.repository.CityMasterRepository;
import com.idms.base.dao.repository.OnlineBookingDetailsRepository;
import com.idms.base.dao.repository.TripMasterRepository;
import com.idms.base.service.OnlineBookingService;
import com.idms.base.support.persist.ResponseStatus;

@Service
public class OnlineBookingServiceImpl implements OnlineBookingService {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private TripMasterRepository tripRepo;
	
	@Autowired
	private CityMasterRepository cityRepo;
	
	@Autowired
	private OnlineBookingDetailsRepository bookingRepo;

	@Override
	public ResponseEntity<ResponseStatus> saveOnlineBooking(OnlineBookingDetailsDto bookingDetails) {
		try {
			
			if(bookingDetails.getBookingId()==null || bookingDetails.getBookingId().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Booking ID is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			OnlineBookingDetails det = bookingRepo.findByBookingId(bookingDetails.getBookingId());
			if(det!=null) {
				return new ResponseEntity<>(new ResponseStatus("Booking ID already existed.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getTripCode()==null || bookingDetails.getTripCode().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Trip Code is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getFromCityCode()==null || bookingDetails.getFromCityCode().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("From City Code is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getToCityCode()==null || bookingDetails.getToCityCode().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("To City Code is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getTravelDate()==null || bookingDetails.getTravelDate().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Travel Date is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getAmount()==null || bookingDetails.getAmount().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Amount is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getAmount()<=0) {
				return new ResponseEntity<>(new ResponseStatus("Invalid.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getSeatsCount()==null) {
				return new ResponseEntity<>(new ResponseStatus("Seat Count is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getSeatsCount()<=0 || bookingDetails.getSeatsCount()>50) {
				return new ResponseEntity<>(new ResponseStatus("Invalid Seat Count.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			if(bookingDetails.getSeatNumbers()==null || bookingDetails.getSeatNumbers().equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Seat Numbers are  mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			TripMaster trip = tripRepo.findByTripCode(bookingDetails.getTripCode());
			
			if(trip==null || trip.getId()==null) {
				return new ResponseEntity<>(new ResponseStatus("Invalid Trip Code.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			CityMaster fromCity = cityRepo.findByCityCode(bookingDetails.getFromCityCode());
			if(fromCity==null || fromCity.getId()==null) {
				return new ResponseEntity<>(new ResponseStatus("Invalid From City Code.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			CityMaster toCity = cityRepo.findByCityCode(bookingDetails.getToCityCode());
			if(toCity==null || toCity.getId()==null) {
				return new ResponseEntity<>(new ResponseStatus("Invalid To City Code.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			OnlineBookingDetails booking = new OnlineBookingDetails();
			booking.setBookingId(bookingDetails.getBookingId());
			booking.setTrip(trip);
			booking.setFromCity(fromCity);
			booking.setToCity(toCity);
			booking.setTravelDate(bookingDetails.getTravelDate());
			booking.setAmount(bookingDetails.getAmount());
			booking.setSeatsCount(bookingDetails.getSeatsCount());
			booking.setSeatNumbers(bookingDetails.getSeatNumbers());
			bookingRepo.save(booking);
			return new ResponseEntity<>(new ResponseStatus("Booking record has been saved successfully.", HttpStatus.OK),
					HttpStatus.OK);
			
		} catch(Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Something went wrong. Kindly contact system administrator.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<ResponseStatus> cancelOnlineBooking(String bookingId) {
		try {
			
			if(bookingId==null || bookingId.equals("")) {
				return new ResponseEntity<>(new ResponseStatus("Booking ID is a mandatory field.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			
			OnlineBookingDetails booking = bookingRepo.findByBookingId(bookingId);
			if(booking==null || booking.getId()==null) {
				return new ResponseEntity<>(new ResponseStatus("Invalid To Booking Id.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
			booking.setCanceld(true);
			booking.setCancelationDate(new Date());
			bookingRepo.save(booking);
			
			return new ResponseEntity<>(new ResponseStatus("Booking has been cancelled successfully.", HttpStatus.OK),
					HttpStatus.OK);
			
		} catch(Exception e) {
			return new ResponseEntity<>(new ResponseStatus("Something went wrong. Kindly contact system administrator.", HttpStatus.FORBIDDEN),
					HttpStatus.OK);
		}
	}

}
