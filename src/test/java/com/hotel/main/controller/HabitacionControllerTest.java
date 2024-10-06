package com.hotel.main.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hotel.main.model.Habitacion;
import com.hotel.main.service.HabitacionService;

@ExtendWith(MockitoExtension.class)
public class HabitacionControllerTest {

    @Mock
    private HabitacionService habitacionService;

    @InjectMocks
    private HabitacionController habitacionController;

    private Habitacion habitacion;

    @BeforeEach
    public void setup() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setNumero("101");
        habitacion.setDisponible(true);
    }

    @Test
    public void testListarHabitaciones() {
        Habitacion habitacion2 = new Habitacion();
        habitacion2.setId(2L);
        habitacion2.setNumero("102");
        habitacion2.setDisponible(false);

        List<Habitacion> habitaciones = Arrays.asList(habitacion, habitacion2);
        when(habitacionService.listarHabitaciones()).thenReturn(habitaciones);
        ResponseEntity<CollectionModel<EntityModel<Habitacion>>> response = habitacionController.listarHabitaciones();
        verify(habitacionService).listarHabitaciones();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
    }


    @Test
    public void testCrearHabitacion() {
        when(habitacionService.agregarHabitacion(any(Habitacion.class))).thenReturn(habitacion);
        ResponseEntity<EntityModel<Habitacion>> response = habitacionController.crearHabitacion(habitacion);
        verify(habitacionService).agregarHabitacion(habitacion);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("101", response.getBody().getContent().getNumero());
    }

    @Test
    public void testEliminarHabitacion_Existente() {
        Long habitacionId = 1L;
        when(habitacionService.eliminarHabitacion(habitacionId)).thenReturn(true);
        ResponseEntity<Map<String, Object>> response = habitacionController.eliminarHabitacion(habitacionId);
        verify(habitacionService, times(1)).eliminarHabitacion(habitacionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Habitacion eliminada", response.getBody().get("mensaje"));
    }

    @Test
    public void testEliminarHabitacion_NoExistente() {
        Long habitacionId = 2L;
        when(habitacionService.eliminarHabitacion(habitacionId)).thenReturn(false);
        ResponseEntity<Map<String, Object>> response = habitacionController.eliminarHabitacion(habitacionId);
        verify(habitacionService, times(1)).eliminarHabitacion(habitacionId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Habitacion no encontrada", response.getBody().get("mensaje"));
    }
}