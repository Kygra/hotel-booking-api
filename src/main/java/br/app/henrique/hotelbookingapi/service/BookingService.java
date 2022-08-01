package br.app.henrique.hotelbookingapi.service;

import java.util.List;

import br.app.henrique.hotelbookingapi.model.Booking;

public interface BookingService {
	
	public List<Booking> checkBookingsByDate(Booking booking);
	
	public List<Booking> returnAllBookings();
	
	public Booking createBooking(Booking booking);
	
	public void cancelBooking(String id);
	
	public Booking updateBooking(String id, Booking bookingUpdates);
}
