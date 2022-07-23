package br.app.henrique.hotelbookingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.app.henrique.hotelbookingapi.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{

}
