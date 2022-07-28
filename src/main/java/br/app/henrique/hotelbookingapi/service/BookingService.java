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
		validateSearch(booking);

		return bookingRepository.getBookingsByDate(booking.getStartDate(), booking.getEndDate());
	}
	
	public List<Booking> returnAllBookings() {
		return bookingRepository.findAll();
	}
	
	public Booking createBooking(Booking booking) {
		validateCreate(booking);
		
		return bookingRepository.save(booking);
	}

	public void cancelBooking(String id) {
		bookingRepository.deleteById(Long.valueOf(id));
	}
	
	public Booking updateBooking(String id, Booking bookingUpdates) {
		// TODO Auto-generated method stub
		//ACEITAR UPDATES PARCIAIS E TOTAIS
		
		/*
		Booking toBeUpdated = bookingRepository.getById(Long.valueOf(id));
		
		if(bookingUpdates.getName()!=null) toBeUpdated.setName(bookingUpdates.getName());
		if(bookingUpdates.getStartDate()!=null) toBeUpdated.setStartDate(bookingUpdates.getStartDate());
		if(bookingUpdates.getEndDate()!=null) toBeUpdated.setEndDate(bookingUpdates.getEndDate());
		
		//CHECAR SE ESTÁ DISPONÍVEL
		
		return toBeUpdated;
		*/
		
		return bookingUpdates;
	}
	
	private void validateSearch(Booking booking) {
		LocalDate startDate = booking.getStartDate();
		LocalDate endDate = booking.getEndDate();
		if(startDate==null || endDate==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing values: startDate or endDate");
		}
		if(startDate.isAfter(endDate)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate cannot be higher than endDate");
		}
	}
	
	private void validateCreate(Booking booking) {
		validateSearch(booking);
		
		//ADICIONAR TODOS OS DEMAIS CHECKS, A SEREM USADOS PELO CREATE E PELO UPDATE
	}


}
