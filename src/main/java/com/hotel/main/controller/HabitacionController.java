package com.hotel.main.controller;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.main.model.Habitacion;
import com.hotel.main.service.HabitacionService;
import com.hotel.main.service.ReservaService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {
    @Autowired
    private HabitacionService habitacionService;
    private ReservaService reservaService;

    public HabitacionController(HabitacionService habitacionService, ReservaService reservaService) {
        this.habitacionService = habitacionService;
        this.reservaService = reservaService;
    }
    
    
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Habitacion>>> listarHabitaciones() {
        String fecha = LocalDate.now().toString();
        List<EntityModel<Habitacion>> habitaciones = habitacionService.listarHabitaciones().stream()
                .map(habitacion -> EntityModel.of(habitacion,
                        linkTo(methodOn(HabitacionController.class).verificarDisponibilidad(habitacion.getNumero(), fecha)).withRel("verificarDisponibilidad") // Enlace a los detalles de cada habitación
                ))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Habitacion>> collectionModel = CollectionModel.of(habitaciones,
                linkTo(methodOn(HabitacionController.class).listarHabitaciones()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }
    
    @PostMapping("/crear_habitacion")
    public ResponseEntity<EntityModel<Habitacion>> crearHabitacion(@RequestBody Habitacion habitacion) {
        Habitacion nuevaHabitacion = habitacionService.agregarHabitacion(habitacion);
        EntityModel<Habitacion> habitacionModel = EntityModel.of(nuevaHabitacion,
                linkTo(methodOn(HabitacionController.class).listarHabitaciones()).withRel("habitaciones"));
        return new ResponseEntity<>(habitacionModel, HttpStatus.CREATED);
    }

   @GetMapping("/disponibilidad/{habitacionNumero}/{fecha}")
    public ResponseEntity<EntityModel<Map<String, Object>>> verificarDisponibilidad(
            @PathVariable String habitacionNumero,
            @PathVariable String fecha) {

        Map<String, Object> respuesta = new HashMap<>();
        try {
            LocalDate fechaConsulta = LocalDate.parse(fecha);
            boolean disponible = reservaService.verificarDisponibilidadHabitacion(habitacionNumero, fechaConsulta);

            if (disponible) {
                respuesta.put("disponible", true);
                respuesta.put("mensaje", "La habitación está disponible");
            } else {
                respuesta.put("disponible", false);
                respuesta.put("mensaje", "La habitación no está disponible");
            }

            EntityModel<Map<String, Object>> entityModel = EntityModel.of(respuesta,
                    linkTo(methodOn(HabitacionController.class).listarHabitaciones()).withRel("habitaciones"), // Enlace a la lista de habitaciones
                    linkTo(methodOn(HabitacionController.class).verificarDisponibilidad(habitacionNumero, fecha)).withSelfRel()); // Enlace a la verificación de disponibilidad actual

            return new ResponseEntity<>(entityModel, HttpStatus.OK);

        } catch (DateTimeParseException e) {
            respuesta.put("disponible", false);
            respuesta.put("mensaje", "Formato de fecha inválido. Use el formato 'YYYY-MM-DD'");
            EntityModel<Map<String, Object>> entityModel = EntityModel.of(respuesta,
                    linkTo(methodOn(HabitacionController.class).listarHabitaciones()).withRel("habitaciones"));

            return new ResponseEntity<>(entityModel, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarHabitacion(@PathVariable Long id) {
        boolean eliminado = habitacionService.eliminarHabitacion(id);

        Map<String, Object> respuesta = new HashMap<>();
        
        if (eliminado) {
            respuesta.put("mensaje", "Habitacion eliminada");
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            respuesta.put("mensaje", "Habitacion no encontrada");
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }
    }

}
