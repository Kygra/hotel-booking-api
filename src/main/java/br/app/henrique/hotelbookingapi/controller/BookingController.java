package br.app.henrique.hotelbookingapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.app.henrique.hotelbookingapi.model.Booking;
import br.app.henrique.hotelbookingapi.repository.BookingRepository;

@RestController
@RequestMapping("/booking")
public class BookingController {

	
	@Autowired
	BookingRepository bookingRepository;

	@GetMapping("/all")
	public List<Booking> returnAllBookings() {
		return bookingRepository.findAll();
	}
	
	@PostMapping("/new")
	@ResponseStatus(value = HttpStatus.CREATED)
	public Booking createBooking(@RequestBody Booking booking) {
		return bookingRepository.save(booking);
	}
}
