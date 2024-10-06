package com.hotel.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.main.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{
    
}
