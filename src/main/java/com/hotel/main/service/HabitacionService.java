package com.hotel.main.service;
import org.springframework.stereotype.Service;

import com.hotel.main.model.Habitacion;
import com.hotel.main.repository.HabitacionRepository;
import java.util.List;
import java.util.Optional;

@Service
public class HabitacionService {

    private HabitacionRepository habitacionRepository;

    public HabitacionService(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    public Optional<Habitacion> findHabitacionById(Long id) {
        return habitacionRepository.findById(id);
    }


    public List<Habitacion> listarHabitaciones() {
        return habitacionRepository.findAll();
    }

    public Habitacion agregarHabitacion(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    public boolean eliminarHabitacion(Long id) {
        if (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}