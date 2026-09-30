package br.com.dunnastecnologia.chamados.infrastructure.controller.api;

import br.com.dunnastecnologia.chamados.infrastructure.controller.web.WebControllerSupport;
import br.com.dunnastecnologia.chamados.infrastructure.controller.web.form.SolicitarReservaForm;
import br.com.dunnastecnologia.chamados.infrastructure.repository.AreaComumRepository;
import br.com.dunnastecnologia.chamados.infrastructure.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.channels.ScatteringByteChannel;
import java.util.UUID;

@Controller
@RequestMapping("/morador/reservas")
@PreAuthorize("hasRole('MORADOR')")
public class MoradorReservaCotroller {

    private final ReservaService reservaService;
    private final AreaComumRepository areaComumRepository;
    private final WebControllerSupport support;

    public MoradorReservaCotroller(ReservaService reservaService, AreaComumRepository areaComumRepository, WebControllerSupport support) {
        this.reservaService = reservaService;
        this.areaComumRepository = areaComumRepository;
        this.support = support;
    }

    @Operation(summary = "Lista o histórico de reservas do morador", tags = "15 - Morador Web - Reservas")
    @GetMapping
    public String listarMinhasReservas(Authentication authentication, Model model) {
        var currentUser = support.authenticatedUser(authentication);
        model.addAttribute("reservas", reservaService.listarMinhasReservas(currentUser.id()));
        return "morador/reservas/lista";
    }

    @Operation(summary = "Abre o formulário para solicitar uma nova reserva", tags = "15 - Morador Web - Reservas")
    @GetMapping("/nova")
    public String telaNovaReserva(Model model) {
        model.addAttribute("areasComuns", areaComumRepository.findAll());
        return "morador/reservas/detalhe";
    }

    @Operation(summary = "Submete uma nova solicitação de reserva", tags = "15 - Morador Web - Reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Reserva solicitada e redirecionada"),
            @ApiResponse(responseCode = "400", description = "Conflito de horário ou regra de negócio")

    })
    @PostMapping
    public String solicitarReserva(
            Authentication authentication,
            @ModelAttribute SolicitarReservaForm form,
            RedirectAttributes redirectAttributes

    ){
        var currentUser = support.authenticatedUser(authentication);
        try {
            reservaService.solicitarReserva(
                    currentUser,
                    form.getAreaComumId(),
                    form.getData(),
                    form.getInicio(),
                    form.getFim()
            );
            redirectAttributes.addFlashAttribute("sucessMessage", "Reserva realizada com sucesso. Aguarde aprovação.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return  "redirect:/morador/reservas/nova";
        }
        return "redirect:/morador/reservas";
    }

    @Operation(summary = "Cancela uma reserva previamente solicitada pelo próprio morador", tags = "15 - Morador Web - Reservas")
    @PatchMapping("/{reservaId}/cancelar")
    public String cancelarReserva(
            Authentication authentication,
            @PathVariable UUID reservaId,
            RedirectAttributes redirectAttributes
    ){
        var currentUser = support.authenticatedUser(authentication);
        try{
            reservaService.cancelarReserva(currentUser, reservaId);
            redirectAttributes.addFlashAttribute("sucessMessage", "Reserva cancelada com sucesso.");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/morador/reservas";
    }
}
