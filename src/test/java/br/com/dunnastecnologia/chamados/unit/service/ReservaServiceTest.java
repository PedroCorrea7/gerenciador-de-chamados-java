package br.com.dunnastecnologia.chamados.infrastructure.service;

import br.com.dunnastecnologia.chamados.application.Security.AuthenticatedUser;
import br.com.dunnastecnologia.chamados.domain.model.AreaComum;
import br.com.dunnastecnologia.chamados.domain.model.Morador;
import br.com.dunnastecnologia.chamados.domain.model.Reserva;
import br.com.dunnastecnologia.chamados.domain.model.StatusReserva;
import br.com.dunnastecnologia.chamados.infrastructure.exception.BusinessRuleException;
import br.com.dunnastecnologia.chamados.infrastructure.repository.AreaComumRepository;
import br.com.dunnastecnologia.chamados.infrastructure.repository.MoradorRepository;
import br.com.dunnastecnologia.chamados.infrastructure.repository.ReservaRepository;
import br.com.dunnastecnologia.chamados.infrastructure.repository.StatusReservaRepository;
import br.com.dunnastecnologia.chamados.infrastructure.service.support.AuthenticatedUserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private AreaComumRepository areaComumRepository;
    @Mock
    private StatusReservaRepository statusReservaRepository;
    @Mock
    private MoradorRepository moradorRepository;
    @Mock
    private AuthenticatedUserValidator authenticatedUserValidator;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void deveRecusarReservaComHorarioFimAnteriorAoInicio() {
        AuthenticatedUser moradorMock = mock(AuthenticatedUser.class);
        UUID areaId = UUID.randomUUID();
        LocalDate data = LocalDate.now().plusDays(1);
        LocalTime inicio = LocalTime.of(15, 0);
        LocalTime fim = LocalTime.of(14, 0);


        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            reservaService.solicitarReserva(moradorMock, areaId, data, inicio, fim);
        });

        assertTrue(exception.getMessage().contains("horário de término deve ser posterior"));
    }

    @Test
    void deveRecusarReservaParaAreaInativa() {
        AuthenticatedUser moradorMock = mock(AuthenticatedUser.class);
        UUID areaId = UUID.randomUUID();
        LocalDate data = LocalDate.now().plusDays(1);
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fim = LocalTime.of(11, 0);

        AreaComum areaInativa = new AreaComum();
        areaInativa.setId(areaId);
        areaInativa.setAtiva(false);

        when(areaComumRepository.findById(areaId)).thenReturn(Optional.of(areaInativa));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.solicitarReserva(moradorMock, areaId, data, inicio, fim);
        });

        assertEquals("Area comum inativa", exception.getMessage());
    }

    @Test
    void deveRecusarReservaComConflitoDeHorario() {
        AuthenticatedUser moradorMock = mock(AuthenticatedUser.class);
        UUID areaId = UUID.randomUUID();
        LocalDate data = LocalDate.now().plusDays(1);
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fim = LocalTime.of(12, 0);

        AreaComum areaAtiva = new AreaComum();
        areaAtiva.setId(areaId);
        areaAtiva.setAtiva(true);

        Morador moradorEntity = new Morador();
        moradorEntity.setId(UUID.randomUUID());

        when(areaComumRepository.findById(areaId)).thenReturn(Optional.of(areaAtiva));
        when(moradorRepository.findByIdAndAtivoTrue(any())).thenReturn(Optional.of(moradorEntity));


        when(reservaRepository.findReservaConflitante(eq(areaId), eq(data), eq(inicio), eq(fim)))
                .thenReturn(List.of(new Reserva()));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            reservaService.solicitarReserva(moradorMock, areaId, data, inicio, fim);
        });

        assertTrue(exception.getMessage().contains("Já existe uma reserva nessa área aprovada"));
    }

    @Test
    void deveImpedirNegacaoDeReservaSemMotivo() {

        AuthenticatedUser adminMock = mock(AuthenticatedUser.class);
        UUID reservaId = UUID.randomUUID();
        String motivoVazio = "   ";

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            reservaService.negarReserva(adminMock, reservaId, motivoVazio);
        });

        assertTrue(exception.getMessage().contains("obrigatório informar um motivo"));
    }

    @Test
    void deveImpedirSolicitacaoDeReservaNoPassado() {
        AuthenticatedUser moradorMock = mock(AuthenticatedUser.class);
        UUID areaId = UUID.randomUUID();

        LocalDate dataPassada = LocalDate.now().minusDays(1);
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fim = LocalTime.of(12, 0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.solicitarReserva(moradorMock, areaId, dataPassada, inicio, fim);
        });

        assertTrue(exception.getMessage().contains("datas e horários passados"));
    }
}