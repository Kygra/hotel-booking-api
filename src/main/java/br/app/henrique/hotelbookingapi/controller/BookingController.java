package br.app.henrique.hotelbookingapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/booking")
public class BookingController {
	
	private static final Logger log = LoggerFactory.getLogger(BookingController.class);
	
	@Autowired
	BookingService bookingService;

	@PostMapping("/check")
	@ApiOperation(value = "Check bookings within two dates", 
                  notes = "By passing startDate and endDate, the api returns all bookings within the dates.")
	public List<Booking> checkBookingsByDate(@RequestBody Booking booking) {
		log.info("Received request to check bookings by date: " + booking);
		return bookingService.checkBookingsByDate(booking);
	}
	
	@GetMapping("/all")
	@ApiOperation(value = "Return all bookings")
	public List<Booking> returnAllBookings() {
		log.info("Received request to return all bookings");
		return bookingService.returnAllBookings();
	}
	
	@PostMapping("/new")
	@ApiOperation(value = "Create new booking",
                  notes = "name, startDate and endDate are required")
	@ResponseStatus(value = HttpStatus.CREATED)
	public Booking createBooking(@RequestBody Booking booking) {
		log.info("Received request to create new booking: " + booking);
		return bookingService.createBooking(booking);
	}
	
	@PostMapping("/cancel/{id}")
	@ApiOperation(value = "Cancel booking by it's id")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public void cancelBooking(@PathVariable String id) {
		log.info("Received request to cancel booking with Id: " + id);
		bookingService.cancelBooking(id);
	}
	
	@PostMapping("/update/{id}")
	@ApiOperation(value = "Update booking by it's id",
                  notes = "All fields are optional, so you can update some or all of the following values: name, startDate, endDate")
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public Booking updateBooking(@PathVariable String id, @RequestBody Booking booking) {
		log.info("Received request to update booking with Id: " + id + ", Request: " + booking);
		return bookingService.updateBooking(id, booking);
	}
}
