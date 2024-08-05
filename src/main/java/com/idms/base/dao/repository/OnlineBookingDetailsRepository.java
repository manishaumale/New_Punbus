package com.idms.base.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idms.base.dao.entity.OnlineBookingDetails;
import com.idms.base.dao.entity.TripMaster;

@Repository
public interface OnlineBookingDetailsRepository extends JpaRepository<OnlineBookingDetails, Integer>{

	@Query(value="SELECT p from OnlineBookingDetails p where p.bookingId = ?1")
	OnlineBookingDetails findByBookingId(String bookingId);
	
	@Query(value="SELECT p from OnlineBookingDetails p where p.trip = ?1")
	List<OnlineBookingDetails> findByTripId(TripMaster tripMaster);
/*
	//tripId
		@Query(value="SELECT id, created_by, created_client_ip, created_on, status, updated_by, updated_client_ip, updated_on, amount, booking_id, cancelation_date, is_canceld, seat_numbers, seats_count, travel_date, from_city_id, to_city_id, trip_id"
	+"FROM punbus_dev.online_booking_details where trip_id=?1", nativeQuery = true)
		OnlineBookingDetails findByTripId(Integer tripId);
*/
	
	
}
