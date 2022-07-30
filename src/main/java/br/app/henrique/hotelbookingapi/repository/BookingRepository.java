package br.app.henrique.hotelbookingapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.app.henrique.hotelbookingapi.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{

	@Query(value = "select a from Booking a "
			+ "where (a.startDate >= :startDate and a.startDate <= :endDate) "
			+ "or (a.endDate >= :startDate and a.endDate <= :endDate) "
			+ "order by a.startDate ASC")
	List<Booking> getBookingsByDate(LocalDate startDate, LocalDate endDate);
	
	@Query(value = "select a from Booking a "
			+ "where a.id != :idToIgnore "
			+ "and ((a.startDate >= :startDate and a.startDate <= :endDate) "
			+ "or (a.endDate >= :startDate and a.endDate <= :endDate)) "
			+ "order by a.startDate ASC")
	List<Booking> getBookingsByDateIgnoringId(LocalDate startDate, LocalDate endDate, Long idToIgnore);
	
}
