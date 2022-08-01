package br.app.henrique.hotelbookingapi.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.app.henrique.hotelbookingapi.model.Booking;
import br.app.henrique.hotelbookingapi.repository.BookingRepository;
import br.app.henrique.hotelbookingapi.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService{
	
	private static final Integer MAX_DAYS_OF_STAY = 3;
	private static final Integer MAX_DAYS_TO_RESERVE_IN_ADVANCE = 30;
			
	@Autowired
	BookingRepository bookingRepository;
	
	public List<Booking> checkBookingsByDate(Booking booking) {				
		validateDatesNotNull(booking);
		validateDateOrder(booking);

		return bookingRepository.getBookingsByDate(booking.getStartDate(), booking.getEndDate());
	}
	
	public List<Booking> returnAllBookings() {
		return bookingRepository.findAll();
	}
	
	public Booking createBooking(Booking booking) {
		validateNameNotNull(booking);
		validateDatesNotNull(booking);
		validateDateOrder(booking);
		validateHotelRules(booking);
		checkIfDatesAreAvailable(booking);
		
		return bookingRepository.save(booking);
	}

	public void cancelBooking(String id) {
		bookingRepository.deleteById(Long.valueOf(id));
	}
	
	public Booking updateBooking(String id, Booking bookingUpdates) {
		//This method allows partial or complete update of name and dates
		
		Booking toBeUpdated = bookingRepository.getReferenceById(Long.valueOf(id));		
		if(bookingUpdates.getName()!=null) toBeUpdated.setName(bookingUpdates.getName());
		if(bookingUpdates.getStartDate()!=null) toBeUpdated.setStartDate(bookingUpdates.getStartDate());
		if(bookingUpdates.getEndDate()!=null) toBeUpdated.setEndDate(bookingUpdates.getEndDate());
		
		//validate booking after requested updates
		validateNameNotNull(toBeUpdated);
		validateDatesNotNull(toBeUpdated);
		validateDateOrder(toBeUpdated);
		validateHotelRules(toBeUpdated);
		checkIfDatesAreAvailableForUpdate(toBeUpdated);

		return bookingRepository.save(toBeUpdated);
	}
	
	private void checkIfDatesAreAvailable(Booking booking) {
		//Reservations start at least the next day of booking
		List<Booking> checkBooking = bookingRepository.getBookingsByDate(
				booking.getStartDate(), booking.getEndDate());
		if(checkBooking.size()>0) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Dates chosen conflict with existing bookings for this room: " + checkBooking);
		}
	}
	
	private void checkIfDatesAreAvailableForUpdate(Booking toBeUpdated) {		
		//Reservations start at least the next day of booking
		List<Booking> checkBooking = bookingRepository.getBookingsByDateIgnoringId(
				toBeUpdated.getStartDate(), toBeUpdated.getEndDate(), toBeUpdated.getId());
		if(checkBooking.size()>0) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Dates chosen conflict with existing bookings for this room: " + checkBooking);
		}
	}
	
	private void validateNameNotNull(Booking booking) {
		if(booking.getName()==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value: name");
		}
	}
	
	private void validateDatesNotNull(Booking booking) {
		if(booking.getStartDate()==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value: startDate");
		}
		if(booking.getEndDate()==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value: endDate");
		}
	}
	
	private void validateDateOrder(Booking booking) {
		if(booking.getStartDate().isAfter(booking.getEndDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate cannot be higher than endDate");
		}
	}
		
	private void validateHotelRules(Booking booking) {
		if((booking.getEndDate().minusDays(MAX_DAYS_OF_STAY-1)).isAfter(booking.getStartDate())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stays cannot be longer than " + MAX_DAYS_OF_STAY + " days");
		}
		if(booking.getStartDate().isAfter(LocalDate.now().plusDays(MAX_DAYS_TO_RESERVE_IN_ADVANCE))){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stays cannot be reserved more than " + MAX_DAYS_TO_RESERVE_IN_ADVANCE + " days in advance");
		}
		if(booking.getStartDate().isAfter(booking.getEndDate())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate cannot be higher than endDate");
		}
		if(LocalDate.now().equals(booking.getStartDate()) || LocalDate.now().isAfter(booking.getStartDate())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate is invalid: Reservations start at least the next day of booking");
		}
	}

}
