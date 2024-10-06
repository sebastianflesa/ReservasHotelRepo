package com.hotel.main.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.main.model.Reserva;
import com.hotel.main.service.ReservaService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
@RequestMapping("/reserva")
public class ReservaController {
    private ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }
    @PostMapping("/crear_reserva")
    public ResponseEntity<EntityModel<Reserva>> reservarHabitacion(@RequestBody Reserva reservaNueva) {
        Reserva reserva = reservaService.reservarHabitacion(reservaNueva);
        EntityModel<Reserva> reservaModel = EntityModel.of(reserva,
                linkTo(methodOn(ReservaController.class).listaReservas()).withRel("lista_reservas"));  // Enlace a la lista de reservas

        return new ResponseEntity<>(reservaModel, HttpStatus.CREATED);
    }

    @GetMapping("/lista_reservas")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> listaReservas() {
        List<Reserva> reservas = reservaService.listaReservas();
        CollectionModel<EntityModel<Reserva>> collectionModel = CollectionModel.of(
                reservas.stream().map(EntityModel::of).toList(),
                linkTo(methodOn(ReservaController.class).listaReservas()).withSelfRel()); 

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<EntityModel<Reserva>> cancelarReserva(@PathVariable Long id) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id);
        EntityModel<Reserva> reservaModel = EntityModel.of(reservaCancelada,
                linkTo(methodOn(ReservaController.class).listaReservas()).withRel("listaReservas"));
        return new ResponseEntity<>(reservaModel, HttpStatus.OK);
    }
  
}
