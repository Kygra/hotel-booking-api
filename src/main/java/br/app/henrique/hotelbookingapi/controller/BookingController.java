package br.app.henrique.hotelbookingapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.app.henrique.hotelbookingapi.model.Booking;
import br.app.henrique.hotelbookingapi.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	BookingService bookingService;

	@GetMapping
	public List<Booking> checkBookingsByDate(@RequestBody Booking booking) {
		return bookingService.checkBookingsByDate(booking);
	}
	
	@GetMapping("/all")
	public List<Booking> returnAllBookings() {
		return bookingService.returnAllBookings();
	}
	
	@PostMapping("/new")
	@ResponseStatus(value = HttpStatus.CREATED)
	public Booking createBooking(@RequestBody Booking booking) {
		return bookingService.createBooking(booking);
	}
	
	@PostMapping("/cancel/{id}")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public void cancelBooking(@PathVariable String id) {
		bookingService.cancelBooking(id);
	}
	
	@PostMapping("/update/{id}")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public Booking updateBooking(@PathVariable String id, @RequestBody Booking booking) {
		return bookingService.updateBooking(id, booking);
	}
}
