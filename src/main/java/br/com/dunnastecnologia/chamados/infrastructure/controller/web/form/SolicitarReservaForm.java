package br.com.dunnastecnologia.chamados.infrastructure.controller.web.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class SolicitarReservaForm {

    private UUID areaComumId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime inicio;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime fim;

}
