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
				booking.getStartDate().plusDays(1), booking.getEndDate().minusDays(1));
		
		if(checkBooking.size()>0) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Dates chosen conflict with existing bookings for this room: " + checkBooking);
		}
	}
	
	private void checkIfDatesAreAvailableForUpdate(Booking toBeUpdated) {		
		//Reservations start at least the next day of booking
		List<Booking> checkBooking = bookingRepository.getBookingsByDateIgnoringId(
				toBeUpdated.getStartDate().plusDays(1), toBeUpdated.getEndDate().minusDays(1), toBeUpdated.getId());
		
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
		if((booking.getEndDate().minusDays(3)).isAfter(booking.getStartDate())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stays cannot be longer than 3 days");
		}
		if(booking.getStartDate().isAfter(LocalDate.now().plusDays(30l))){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stays cannot be reserved more than 30 days in advance");
		}
		if(booking.getStartDate().isEqual(booking.getEndDate())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate cannot be same as endDate");
		}
		if(booking.getStartDate().isAfter(booking.getEndDate())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate cannot be higher than endDate");
		}
		if(booking.getStartDate().isEqual(LocalDate.now()) || LocalDate.now().isAfter(booking.getStartDate())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate is invalid: Reservations start at least the next day of booking");
		}
	}

}
