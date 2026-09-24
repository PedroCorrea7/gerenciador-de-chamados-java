package br.com.dunnastecnologia.chamados.infrastructure.repository;

import br.com.dunnastecnologia.chamados.domain.model.Reserva;
import br.com.dunnastecnologia.chamados.domain.model.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservaRepository extends JpaRepository<StatusReserva, UUID> {

    @Query("""
        select r
        from Resera r
        where r.areaComum.id = :areaComumId
        and r.dataReserva = :dataReserva
        and r.status.nome = 'APROVADO'
        and (
            (r.horaInicio <= :horaInicio and r.horaFim > :horaInicio)
         or (r.horaInicio <= :horaFim and r.horaFim >= :horaFim)
         or (r.horaInicio >= :horaInicio and r.horaFim <= :horaFim)
            )
    """)
    List<Reserva> findReservaConflitante(
            @Param("areaComumId") UUID areaComumId,
            @Param("dataReserva") LocalDate dataReserva,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim
    );

    List<Reserva> findByMoradorId(UUID moradorId);
}
