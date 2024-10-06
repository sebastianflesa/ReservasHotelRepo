package com.hotel.main.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
import com.hotel.main.model.Reserva;
import com.hotel.main.model.Reserva.EstadoReserva;
import com.hotel.main.service.ReservaService;

@ExtendWith(MockitoExtension.class)
public class ReservaControllerTest {
    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private HabitacionController habitacionController;
    @InjectMocks
    private ReservaController reservaController;
    private Reserva reserva;
    private Habitacion habitacion;
    private Reserva reserva2;


    @BeforeEach
    public void setup() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setNumero("101");
        habitacion.setDisponible(true);

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setNombreCliente("Juan Pérez");
        reserva.setTelefonoCliente("123456789");
        reserva.setFechaInicio(LocalDate.of(2024, 10, 6));
        reserva.setFechaFin(LocalDate.of(2024, 10, 10));
        reserva.setHabitacion(habitacion);
        reserva.setEstadoReserva(Reserva.EstadoReserva.ACTIVA);

        reserva2 = new Reserva();
        reserva2.setId(2L);
        reserva2.setNombreCliente("Ana Gómez");
        reserva2.setTelefonoCliente("987654321");
        reserva2.setFechaInicio(LocalDate.of(2024, 11, 1));
        reserva2.setFechaFin(LocalDate.of(2024, 11, 5));
        reserva2.setHabitacion(habitacion);
        reserva2.setEstadoReserva(Reserva.EstadoReserva.ACTIVA);
    }
    @Test
    public void testCrearReserva() {
        when(reservaService.reservarHabitacion(any(Reserva.class))).thenReturn(reserva);

        ReservaController reservaController2 = reservaController;
        ResponseEntity<EntityModel<Reserva>> response = reservaController2.reservarHabitacion(reserva);

        verify(reservaService).reservarHabitacion(any(Reserva.class));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Juan Pérez", response.getBody().getContent().getNombreCliente());
        assertEquals("101", response.getBody().getContent().getHabitacion().getNumero());
        assertEquals(Reserva.EstadoReserva.ACTIVA, response.getBody().getContent().getEstadoReserva());
    }

    @Test
    public void testListaReservas() {
        List<Reserva> reservas = Arrays.asList(reserva, reserva2);
        when(reservaService.listaReservas()).thenReturn(reservas);

        ResponseEntity<CollectionModel<EntityModel<Reserva>>> response = reservaController.listaReservas();

        verify(reservaService, times(1)).listaReservas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertTrue(response.getBody().getLinks().hasLink("self"));
        assertEquals(linkTo(methodOn(ReservaController.class).listaReservas()).withSelfRel().getHref(),
                     response.getBody().getLink("self").get().getHref());
    }

    @Test
    public void testCancelarReserva() {
        reserva.setEstadoReserva(EstadoReserva.CANCELADA);
        when(reservaService.cancelarReserva(1L)).thenReturn(reserva);
        ResponseEntity<EntityModel<Reserva>> response = reservaController.cancelarReserva(1L);

        verify(reservaService, times(1)).cancelarReserva(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EstadoReserva.CANCELADA, response.getBody().getContent().getEstadoReserva());
        assertTrue(response.getBody().getLinks().hasLink("listaReservas"));
        assertEquals(linkTo(methodOn(ReservaController.class).listaReservas()).withRel("listaReservas").getHref(),
                response.getBody().getLink("listaReservas").get().getHref());
    }

}
