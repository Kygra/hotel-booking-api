package br.app.henrique.hotelbookingapi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import br.app.henrique.hotelbookingapi.model.Booking;
import br.app.henrique.hotelbookingapi.repository.BookingRepository;
import br.app.henrique.hotelbookingapi.service.BookingService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
	
	@Mock
	BookingRepository bookingRepository;
	
	@InjectMocks
	BookingService bookingService;
	
	static List<Booking> listOfBookings;
	static Booking booking;
	
	@BeforeAll
	static void setUpBeforeTests() throws Exception {
		listOfBookings = new ArrayList<Booking>();
		listOfBookings.add(new Booking ("Henrique", LocalDate.parse("2022-07-01"), LocalDate.parse("2022-07-10")));
		listOfBookings.add(new Booking ("Romano", LocalDate.parse("2022-07-11"), LocalDate.parse("2022-08-01")));	
		booking =          new Booking ("Correia", LocalDate.parse("2022-08-02"), LocalDate.parse("2022-08-04"));
	}

	
	@Test
	void testCheckBookingsByDate() {
		//Setup
		when(bookingRepository.getBookingsByDate(any(), any())).thenReturn(listOfBookings);
				
	    //Execute
		List<Booking> result = bookingService.checkBookingsByDate(
				new Booking (null, LocalDate.parse("2022-07-05"), LocalDate.parse("2022-07-06")));
		
		//Validate
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(listOfBookings, result);
	}
	
	@Test
	void testCheckBookingsByDateShouldReturnErrorIfNullStartDate() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.checkBookingsByDate(new Booking (null, null, LocalDate.parse("2022-07-06")));
	    });
	    assertTrue((exception.getMessage()).contains("Missing values: startDate or endDate"));
	}
	
	@Test
	void testCheckBookingsByDateShouldReturnErrorIfNullEndDate() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.checkBookingsByDate(new Booking (null, LocalDate.parse("2022-07-06"), null));
	    });
	    assertTrue((exception.getMessage()).contains("Missing values: startDate or endDate"));
	}
	
	@Test
	void testCheckBookingsByDateShouldReturnErrorIfStartDateIsHigherThanEndDate() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.checkBookingsByDate(new Booking (null, LocalDate.parse("2022-07-08"), LocalDate.parse("2022-07-06")));
	    });
	    assertTrue((exception.getMessage()).contains("startDate cannot be higher than endDate"));
	}

	
	@Test
	void testReturnAllBookings() {
		//Setup
		when(bookingRepository.findAll()).thenReturn(listOfBookings);
			
	    //Execute
		List<Booking> result = bookingService.returnAllBookings();
		
		//Validate
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(listOfBookings, result);
	}

	
	@Test
	void testCreateBookingWithEmptyBookingList() {
		//Setup
		Booking newBooking = new Booking ("Correia", LocalDate.parse("2022-08-02"), LocalDate.parse("2022-08-04"));
		Booking savedBooking = newBooking;
		savedBooking.setId(1l);
		when(bookingService.createBooking(newBooking)).thenReturn(savedBooking);
				
	    //Execute
		Booking result = bookingService.createBooking(newBooking);
		
		//Validate
		assertNotNull(result);
		assertEquals(savedBooking.getName(), result.getName());
		assertEquals(savedBooking.getStartDate(), result.getStartDate());
		assertEquals(savedBooking.getEndDate(), result.getEndDate());
		assertEquals(savedBooking.getId(), result.getId());
	}
		
	@Test
	void testCreateBookingShouldReturnErrorIfNullStartDate() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking (null, null, LocalDate.parse("2022-07-06")));
	    });
	    assertTrue((exception.getMessage()).contains("Missing values: startDate or endDate"));
	}
	
	@Test
	void testCreateBookingShouldReturnErrorIfNullEndDate() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking (null, LocalDate.parse("2022-07-06"), null));
	    });
	    assertTrue((exception.getMessage()).contains("Missing values: startDate or endDate"));
	}
	
	@Test
	void testCreateBookingShouldReturnErrorIfStartDateIsHigherThanEndDate() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking (null, LocalDate.parse("2022-07-08"), LocalDate.parse("2022-07-06")));
	    });
	    assertTrue((exception.getMessage()).contains("startDate cannot be higher than endDate"));
	}
	
	/*
	@Test
	void testCreateBookingFailScenario() {
		fail("Not yet implemented");
	}
	*/
	
	/*
	@Test
	void testCreateBookingHotelBusinessRules() {
		fail("Not yet implemented");
	}
	*/
	
	
	@Test
	void testCancelBooking() {
	    //Execute
		bookingService.cancelBooking("1");
		
		//Validate
		verify(bookingRepository, times(1)).deleteById(1l);
	}
	
	
	/*
	@Test
	void testUpdateBooking() {
		fail("Not yet implemented");
	}
	*/
	
	/*
	@Test
	void testUpdateBookingFailScenario() {
		fail("Not yet implemented");
	}
	*/
	
	/*
	@Test
	void testUpdateBookingHotelBusinessRules() {
		fail("Not yet implemented");
	}
	*/
		
	@Test
	void testUpdateBookingShouldReturnErrorIfStartDateIsHigherThanEndDate() {
		//Setup
		when(bookingRepository.getReferenceById(1l)).thenReturn(booking);
		
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.updateBooking("1", new Booking (null, LocalDate.parse("2022-07-08"), LocalDate.parse("2022-07-06")));
	    });
	    assertTrue((exception.getMessage()).contains("startDate cannot be higher than endDate"));
	}
	
}
