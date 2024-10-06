package com.hotel.main.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.hotel.main.model.Habitacion;
import com.hotel.main.model.Reserva;
import com.hotel.main.model.Reserva.EstadoReserva;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    private Reserva reserva;
    private Habitacion habitacion;

    @Test
    public void testSaveReserva() {
        habitacion = new Habitacion();
        habitacion.setNumero("101");
        habitacion.setDisponible(true);
        Habitacion savedHabitacion = habitacionRepository.save(habitacion);

        reserva = new Reserva();
        reserva.setNombreCliente("Juan Pérez");
        reserva.setTelefonoCliente("123456789");
        reserva.setFechaInicio(LocalDate.of(2024, 10, 6));
        reserva.setFechaFin(LocalDate.of(2024, 10, 10));
        reserva.setHabitacion(savedHabitacion);
        reserva.setEstadoReserva(EstadoReserva.ACTIVA);
        Reserva savedReserva = reservaRepository.save(reserva);

        assertNotNull(savedReserva.getId());
        assertEquals("Juan Pérez", savedReserva.getNombreCliente());
        assertEquals("101", savedReserva.getHabitacion().getNumero());
        assertEquals(EstadoReserva.ACTIVA, savedReserva.getEstadoReserva());
    }

    @Test
    public void testFindById() {
        habitacion = new Habitacion();
        habitacion.setNumero("101");
        habitacion.setDisponible(true);
        Habitacion savedHabitacion = habitacionRepository.save(habitacion);

        reserva = new Reserva();
        reserva.setNombreCliente("Ana Gómez");
        reserva.setTelefonoCliente("987654321");
        reserva.setFechaInicio(LocalDate.of(2024, 11, 1));
        reserva.setFechaFin(LocalDate.of(2024, 11, 5));
        reserva.setHabitacion(savedHabitacion);
        reserva.setEstadoReserva(EstadoReserva.ACTIVA);
        Reserva savedReserva = reservaRepository.save(reserva);

        Optional<Reserva> foundReserva = reservaRepository.findById(savedReserva.getId());

        assertTrue(foundReserva.isPresent());
        assertEquals("Ana Gómez", foundReserva.get().getNombreCliente());
        assertEquals("101", foundReserva.get().getHabitacion().getNumero());
    }

}
