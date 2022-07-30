package br.app.henrique.hotelbookingapi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
		listOfBookings.add(new Booking ("Henrique", LocalDate.now().plusDays(1) , LocalDate.now().plusDays(4)));
		listOfBookings.add(new Booking ("Romano", LocalDate.now().plusDays(5), LocalDate.now().plusDays(7)));	
		booking =          new Booking ("Correia", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
	}

	
	@Test
	void testCheckBookingsByDate() {
		//Setup
		when(bookingRepository.getBookingsByDate(any(), any())).thenReturn(listOfBookings);
				
	    //Execute
		List<Booking> result = bookingService.checkBookingsByDate(
				new Booking ("Test", LocalDate.now().plusDays(4), LocalDate.now().plusDays(6)));
		
		//Validate
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(listOfBookings, result);
	}
	
	@Test
	void testCheckBookingsByDateWithNullStartDateShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.checkBookingsByDate(new Booking ("Test", null, LocalDate.now().plusDays(2)));
	    });
	    assertTrue((exception.getMessage()).contains("Missing value: startDate"));
	}
	
	@Test
	void testCheckBookingsByDateWithNullEndDateShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.checkBookingsByDate(new Booking ("Test", LocalDate.now().plusDays(2), null));
	    });
	    assertTrue((exception.getMessage()).contains("Missing value: endDate"));
	}
	
	@Test
	void testCheckBookingsByDateWithStartDateIsHigherThanEndDateShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.checkBookingsByDate(new Booking ("Test", LocalDate.now().plusDays(2), LocalDate.now().plusDays(1)));
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
		Booking savedBooking = booking;
		savedBooking.setId(1l);
		when(bookingRepository.getBookingsByDate(LocalDate.now().plusDays(2), LocalDate.now().plusDays(1))).thenReturn(Collections.<Booking>emptyList());
		when(bookingRepository.save(booking)).thenReturn(savedBooking);
		
	    //Execute
		Booking result = bookingService.createBooking(booking);
		
		//Validate
		assertNotNull(result);
		assertEquals(savedBooking.getName(), result.getName());
		assertEquals(savedBooking.getStartDate(), result.getStartDate());
		assertEquals(savedBooking.getEndDate(), result.getEndDate());
		assertEquals(savedBooking.getId(), result.getId());
	}
			
	@Test
	void testCreateBookingWithStartDateEqualAsEndDateOfExistingBooking() {
		//Setup
		Booking existingBooking = new Booking ("Correia", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
		Booking newBooking = new Booking ("Correia", LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
		Booking savedBooking = newBooking;
		savedBooking.setId(1l);
		List<Booking> listOfExistingBookings = new ArrayList<Booking>();
		listOfExistingBookings.add(existingBooking);
		lenient().when(bookingRepository.getBookingsByDate(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4))).thenReturn(listOfExistingBookings);
		lenient().when(bookingRepository.getBookingsByDate(LocalDate.now().plusDays(3), LocalDate.now().plusDays(4))).thenReturn(Collections.<Booking>emptyList());
		when(bookingRepository.save(newBooking)).thenReturn(savedBooking);
		
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
	void testCreateBookingWith3DayStay() {	
		//Setup
		Booking newBooking = new Booking ("Correia", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
		Booking savedBooking = newBooking;
		savedBooking.setId(1l);		
		when(bookingRepository.getBookingsByDate(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3))).thenReturn(Collections.<Booking>emptyList());
		when(bookingRepository.save(newBooking)).thenReturn(savedBooking);
				
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
	void testCreateBookingWithStartDate30DaysFromNow() {	
		//Setup
		Booking newBooking = new Booking ("Correia", LocalDate.now().plusDays(30), LocalDate.now().plusDays(33));
		Booking savedBooking = newBooking;
		savedBooking.setId(1l);		
		when(bookingRepository.getBookingsByDate(LocalDate.now().plusDays(31), LocalDate.now().plusDays(32))).thenReturn(Collections.<Booking>emptyList());
		when(bookingRepository.save(newBooking)).thenReturn(savedBooking);
				
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
	void testCreateBookingWithStartDateDuringExistingBookingShouldFail() {
		//Setup
		Booking newBooking = new Booking ("Correia", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));
		List<Booking> listOfExistingBookings = new ArrayList<Booking>();
		listOfExistingBookings.add(booking);		
		when(bookingRepository.getBookingsByDate(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3))).thenReturn(listOfBookings);
		
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(newBooking);
	    });
	    assertTrue((exception.getMessage()).contains("Dates chosen conflict with existing bookings"));
	}
	
	@Test
	void testCreateBookingWithNullNameShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking (null, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));
	    });
	    assertTrue((exception.getMessage()).contains("Missing value: name"));
	}
	
	@Test
	void testCreateBookingWithNullStartDateShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", null, LocalDate.now().plusDays(2)));
	    });
	    assertTrue((exception.getMessage()).contains("Missing value: startDate"));
	}
	
	@Test
	void testCreateBookingWithNullEndDateShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", LocalDate.now().plusDays(2), null));
	    });
	    assertTrue((exception.getMessage()).contains("Missing value: endDate"));
	}
	
	@Test
	void testCreateBookingWithStartDateIsHigherThanEndDateShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", LocalDate.now().plusDays(2), LocalDate.now().plusDays(1)));
	    });
	    assertTrue((exception.getMessage()).contains("startDate cannot be higher than endDate"));
	}
	
	@Test
	void testCreateBookingWithStartDateEqualAsEndDateShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", LocalDate.now().plusDays(2), LocalDate.now().plusDays(2)));
	    });
	    assertTrue((exception.getMessage()).contains("startDate cannot be same as endDate"));
	}
	
	@Test
	void testCreateBookingWithStartDateAsTodayShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", LocalDate.now(), LocalDate.now().plusDays(2)));
	    });
	    assertTrue((exception.getMessage()).contains("startDate is invalid: Reservations start at least the next day of booking"));
	}
	
	@Test
	void testCreateBookingWithStartDateBeforeTodayShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", LocalDate.now().minusDays(1), LocalDate.now().plusDays(2)));
	    });
	    assertTrue((exception.getMessage()).contains("startDate is invalid: Reservations start at least the next day of booking"));
	}
	
	@Test
	void testCreateBookingWith4DayStayShouldFail() {
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)));
	    });
	    assertTrue((exception.getMessage()).contains("Stays cannot be longer than 3 days"));
	}
	
	@Test
	void testCreateBookingWithStartDate31DaysFromNowShouldFail() {
		//Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.createBooking(new Booking ("Test", LocalDate.now().plusDays(31), LocalDate.now().plusDays(33)));
	    });
	    assertTrue((exception.getMessage()).contains("Stays cannot be reserved more than 30 days in advance"));
	}
	
	
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
	void testUpdateBookingWithStartDateIsHigherThanEndDateShouldFail() {
		//Setup
		when(bookingRepository.getReferenceById(1l)).thenReturn(booking);
		
	    //Execute and validate
	    Exception exception = assertThrows(ResponseStatusException.class, () -> {
	    	bookingService.updateBooking("1", new Booking ("Test", LocalDate.now().plusDays(3), LocalDate.now().plusDays(2)));
	    });
	    assertTrue((exception.getMessage()).contains("startDate cannot be higher than endDate"));
	}
	
}
