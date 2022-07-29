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
		validateDatesNotNull(booking);
		validateDateOrder(booking);
		validateHotelRules(booking);
		
		return bookingRepository.save(booking);
	}

	public void cancelBooking(String id) {
		bookingRepository.deleteById(Long.valueOf(id));
	}
	
	public Booking updateBooking(String id, Booking bookingUpdates) {
		//This method allows partial and complete update of name and dates
		
		Booking toBeUpdated = bookingRepository.getReferenceById(Long.valueOf(id));
		
		if(bookingUpdates.getName()!=null) toBeUpdated.setName(bookingUpdates.getName());
		if(bookingUpdates.getStartDate()!=null) toBeUpdated.setStartDate(bookingUpdates.getStartDate());
		if(bookingUpdates.getEndDate()!=null) toBeUpdated.setEndDate(bookingUpdates.getEndDate());
		
		//validate booking after requested updates
		validateDatesNotNull(toBeUpdated);
		validateDateOrder(toBeUpdated);
		validateHotelRules(toBeUpdated);

		return bookingRepository.save(toBeUpdated);
	}
	
	private void validateDatesNotNull(Booking booking) {
		if(booking.getStartDate()==null || booking.getEndDate()==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing values: startDate or endDate");
		}
	}
	
	private void validateDateOrder(Booking booking) {
		if(booking.getStartDate().isAfter(booking.getEndDate())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate cannot be higher than endDate");
		}
	}
		
	private void validateHotelRules(Booking booking) {
		//TODO ADICIONAR TODOS OS DEMAIS CHECKS, A SEREM USADOS PELO CREATE E PELO UPDATE
	}


}
