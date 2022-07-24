package br.app.henrique.hotelbookingapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.app.henrique.hotelbookingapi.model.Booking;
import br.app.henrique.hotelbookingapi.repository.BookingRepository;

@Service
public class BookingService {
		
	@Autowired
	BookingRepository bookingRepository;
	
	public List<Booking> getBookingByDate(Booking booking) {				
		validate(booking);

		return bookingRepository.getBookingByDate(booking.getStartDate(), booking.getEndDate());
	}
	
	public List<Booking> returnAllBookings() {
		return bookingRepository.findAll();
	}
	
	public Booking createBooking(Booking booking) {
		validate(booking);
		
		return bookingRepository.save(booking);
	}

	public void cancelBooking(String id) {
		bookingRepository.deleteById(Long.valueOf(id));
	}
	
	private void validate(Booking booking) {
		LocalDate startDate = booking.getStartDate();
		LocalDate endDate = booking.getEndDate();
		if(startDate==null || endDate==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing values: startDate or endDate");
		}
		if(startDate.isAfter(endDate)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate cant be after endDate");
		}
	}

}
