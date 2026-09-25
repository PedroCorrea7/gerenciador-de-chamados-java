package br.com.dunnastecnologia.chamados.infrastructure.service;


import br.com.dunnastecnologia.chamados.application.Security.AuthenticatedUser;
import br.com.dunnastecnologia.chamados.domain.model.AreaComum;
import br.com.dunnastecnologia.chamados.domain.model.Morador;
import br.com.dunnastecnologia.chamados.domain.model.Reserva;
import br.com.dunnastecnologia.chamados.domain.model.StatusReserva;
import br.com.dunnastecnologia.chamados.infrastructure.exception.BusinessRuleException;
import br.com.dunnastecnologia.chamados.infrastructure.exception.ResourceNotFoundException;
import br.com.dunnastecnologia.chamados.infrastructure.repository.AreaComumRepository;
import br.com.dunnastecnologia.chamados.infrastructure.repository.MoradorRepository;
import br.com.dunnastecnologia.chamados.infrastructure.repository.ReservaRepository;
import br.com.dunnastecnologia.chamados.infrastructure.repository.StatusReservaRepository;
import br.com.dunnastecnologia.chamados.infrastructure.service.support.AuthenticatedUserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ReservaService {

    private static final String STATUS_SOLICITADO = "SOLICITADO";
    private static final String STATUS_APROVADO = "APROVADO";
    private static final String STATUS_REPROVADO = "NEGADO";
    private static final String STATUS_CANCELADO = "CANCELADO";

    private final ReservaRepository reservaRepository;
    private final AreaComumRepository areaComumRepository;
    private final StatusReservaRepository statusReservaRepository;
    private final MoradorRepository moradorRepository;
    private final AuthenticatedUserValidator  authenticatedUserValidator;

    public ReservaService(
            ReservaRepository reservaRepository,
            AreaComumRepository areaComumRepository,
            StatusReservaRepository statusReservaRepository,
            MoradorRepository moradorRepository,
            AuthenticatedUserValidator authenticatedUserValidator
    ){
        this.reservaRepository = reservaRepository;
        this.areaComumRepository = areaComumRepository;
        this.statusReservaRepository = statusReservaRepository;
        this.moradorRepository = moradorRepository;
        this.authenticatedUserValidator = authenticatedUserValidator;
    }

    @Transactional
    public Reserva solicitarReserva(AuthenticatedUser morador, UUID areaComumId, LocalDate data, LocalTime inicio, LocalTime fim){
        authenticatedUserValidator.assertMorador(morador);

        if (!fim.isAfter(inicio)){
            throw new BusinessRuleException("O horário de término deve ser posterior ao horário de ínicio");
        }

        AreaComum areaComum = areaComumRepository.findById(areaComumId)
                .orElseThrow(()-> new ResourceNotFoundException("Área comum não encontrada"));

        Morador moradorEntity = moradorRepository.findByIdAndAtivoTrue(morador.id())
                .orElseThrow(()-> new ResourceNotFoundException("Morador não encontrado"));

        if (!reservaRepository.findReservaConflitante(areaComumId, data, inicio, fim).isEmpty()){
            throw new BusinessRuleException("Já existe uma reserva nessa área aprovada para este horário");
        }

        StatusReserva status = statusReservaRepository.findByNome(STATUS_SOLICITADO)
                .orElseThrow(()-> new BusinessRuleException("Status" + STATUS_SOLICITADO + " não configurado"));

        Reserva reserva = new Reserva();
        reserva.setAreaComum(areaComum);
        reserva.setMorador(moradorEntity);
        reserva.setStatusReserva(status);
        reserva.setDataReserva(data);
        reserva.setHoraInicio(inicio);
        reserva.setHoraFim(fim);
        reserva.setCriadoEm(LocalDateTime.now());

        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva aprovarReserva(AuthenticatedUser admin, UUID reservaId) {
        authenticatedUserValidator.assertAdministrador(admin);
        return alterarStatusReserva(reservaId, STATUS_APROVADO);
    }

    @Transactional
    public  Reserva negarReserva(AuthenticatedUser admin, UUID reservaId) {
        authenticatedUserValidator.assertAdministrador(admin);
        return alterarStatusReserva(reservaId, STATUS_REPROVADO);
    }

    @Transactional
    public Reserva cancelarReserva(AuthenticatedUser morador, UUID reservaId) {
        authenticatedUserValidator.assertMorador(morador);

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(()-> new ResourceNotFoundException("Reserva não encontrada"));

        if (STATUS_CANCELADO.equals(reserva.getStatusReserva().getNome()) || STATUS_REPROVADO.equals(reserva.getStatusReserva().getNome())){
            throw new BusinessRuleException("Não é possível alterar uma reserva que já foi cancelada ou negada");
        }

        return alterarStatusReserva(reservaId, STATUS_CANCELADO);
    }

    private Reserva alterarStatusReserva(UUID reservaId, String nomeStatus){
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(()-> new ResourceNotFoundException("Reserva não encontrada"));

        if (STATUS_CANCELADO.equals(reserva.getStatusReserva().getNome()) || STATUS_REPROVADO.equals(reserva.getStatusReserva().getNome())){
            throw new BusinessRuleException("Não é possível alterar uma reserva que já foi finalizada");
        }

        StatusReserva novoStatus = statusReservaRepository.findByNome(nomeStatus)
                .orElseThrow(()-> new BusinessRuleException("Status" + nomeStatus + "não configurado"));

        reserva.setStatusReserva(novoStatus);
        return reservaRepository.save(reserva);
    }
}
