package com.hotel.main.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.main.model.Habitacion;
import com.hotel.main.model.Reserva;
import com.hotel.main.repository.HabitacionRepository;
import com.hotel.main.repository.ReservaRepository;

@Service
public class ReservaService {

    private ReservaRepository reservaRepository;
    private HabitacionRepository habitacionRepository;
    @Autowired
    public ReservaService(ReservaRepository reservaRepository, HabitacionRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
    }
    public Reserva reservarHabitacion(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listaReservas() {
        return reservaRepository.findAll();
    }
    public Reserva cancelarReserva(Long id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.setEstadoReserva(Reserva.EstadoReserva.CANCELADA);
            return reservaRepository.save(reserva);
        } else {
            throw new RuntimeException("Reserva no encontrada");
        }
    }

    public boolean verificarDisponibilidadHabitacion(String habitacionNumero, LocalDate fechaConsulta) {
        Optional<Habitacion> habitacionOpt = habitacionRepository.findByNumero(habitacionNumero);
        if (habitacionOpt.isEmpty()) {
            throw new RuntimeException("Habitación no encontrada");
        }
        Habitacion habitacion = habitacionOpt.get();
        List<Reserva> reservas = habitacion.getReservas();
        for (Reserva reserva : reservas) {
            if (!fechaConsulta.isBefore(reserva.getFechaInicio()) && !fechaConsulta.isAfter(reserva.getFechaFin())) {
                return false; 
            }
        }

        return true;
    }
}