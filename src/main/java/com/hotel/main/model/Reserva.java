package com.hotel.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombreCliente;
    @NotNull
    private String telefonoCliente;
    @NotNull
    private LocalDate fechaInicio;
    @NotNull
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    @NotNull
    private Habitacion habitacion;

    @Enumerated(EnumType.STRING)
    
    private EstadoReserva estadoReserva = EstadoReserva.ACTIVA;
    @NotNull
    public enum EstadoReserva {
        ACTIVA,
        CANCELADA
    }
}