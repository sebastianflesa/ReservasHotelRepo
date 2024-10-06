package com.hotel.main.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.hotel.main.model.Habitacion;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HabitacionRepositoryTest {

    @Autowired
    private HabitacionRepository habitacionRepository;
    private Habitacion habitacion;

    @Test
    public void testSaveHabitacion() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setNumero("101");
        habitacion.setDisponible(true);
        Habitacion savedHabitacion = habitacionRepository.save(habitacion);
        assertNotNull(savedHabitacion.getId());
        assertEquals("101", savedHabitacion.getNumero());
        assertNotNull(savedHabitacion.getId());
    }
    @Test
    public void testFindById() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setNumero("101");
        habitacion.setDisponible(true);
        Habitacion savedHabitacion = habitacionRepository.save(habitacion);
        Optional<Habitacion> foundHabitacion = habitacionRepository.findById(savedHabitacion.getId());
        assertTrue(foundHabitacion.isPresent());
        assertEquals("101", foundHabitacion.get().getNumero());
    }


}