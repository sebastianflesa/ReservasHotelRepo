package com.hotel.main.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.main.model.Habitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByDisponibleTrue();
    Optional<Habitacion> findByNumero(String numero);
    Optional<Habitacion> findById(Long id);
}
