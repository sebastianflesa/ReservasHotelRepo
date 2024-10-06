package com.hotel.main.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotel.main.model.Habitacion;
import com.hotel.main.repository.HabitacionRepository;

@ExtendWith(MockitoExtension.class)
public class HabitacionServiceTest {
    @Mock
    private HabitacionRepository habitacionRepository;

    @InjectMocks
    private HabitacionService habitacionService;

    private Habitacion habitacion;


    @BeforeEach
    void setup() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setNumero("101");
        habitacion.setDisponible(true);
    }

    @Test
    void testSaveHabitacion() {
        when(habitacionRepository.save(habitacion)).thenReturn(habitacion);
        Habitacion habitacionCreada = habitacionService.agregarHabitacion(habitacion);
        assertEquals(habitacion, habitacionCreada);
        verify(habitacionRepository).save(habitacion);
    }

    @Test
    void testListarHabitaciones() {
        when(habitacionRepository.findAll()).thenReturn(List.of(habitacion));
        List<Habitacion> foundUsers = habitacionService.listarHabitaciones();
        assertEquals(List.of(habitacion), foundUsers);
        verify(habitacionRepository).findAll();
    }

    @Test
    void testEliminarHabitacion_Existente() {
        when(habitacionRepository.existsById(habitacion.getId())).thenReturn(true);
        doNothing().when(habitacionRepository).deleteById(habitacion.getId());
        boolean resultado = habitacionService.eliminarHabitacion(habitacion.getId());
        verify(habitacionRepository, times(1)).deleteById(habitacion.getId());
        assertTrue(resultado);
    }

    @Test
    void testEliminarHabitacion_NoExistente() {
        when(habitacionRepository.existsById(habitacion.getId())).thenReturn(false);
        boolean resultado = habitacionService.eliminarHabitacion(habitacion.getId());
        verify(habitacionRepository, never()).deleteById(habitacion.getId());
        assertFalse(resultado);
    }
}
