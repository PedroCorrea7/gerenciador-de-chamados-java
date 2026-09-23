package br.com.dunnastecnologia.chamados.domain.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "reserva")
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_comum_id", nullable = false)
    private AreaComum areaComum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "morador_id", nullable = false)
    private  Morador morador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusReserva statusReserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Administrador administrador;

    @JoinColumn(name = "data_reserva", nullable = false)
    private LocalDate dataReserva;

    @JoinColumn(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @JoinColumn(name = "hora_fim", nullable = false)
    private  LocalTime horaFim;

    @Column(name = "motivo_negacao")
    private String motivoNegacao;

    @Column(name = "criado_em", insertable = false, updatable = false)
    private LocalDateTime criadoEm;




}
