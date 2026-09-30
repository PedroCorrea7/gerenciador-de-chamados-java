package br.com.dunnastecnologia.chamados.infrastructure.controller.api;

import br.com.dunnastecnologia.chamados.infrastructure.controller.web.WebControllerSupport;
import br.com.dunnastecnologia.chamados.infrastructure.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/reservas")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminReservaController {

    private final ReservaService reservaService;
    private final WebControllerSupport support;

    public AdminReservaController(ReservaService reservaService, WebControllerSupport support) {
        this.reservaService = reservaService;
        this.support = support;
    }

    @Operation(summary = "Lista todas as reservas para o administrador", tags = "14 - Admin Web Reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas retornada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado para o perfil autenticado.")
    })
    @GetMapping
    public String listarReservas(Authentication authentication, Model model){
        var currentUser = support.authenticatedUser(authentication);
        model.addAttribute("reservas", reservaService.listarTodasReservasAdmin());
        return "admin/reservas/lista";
    }

    @Operation(summary = "Aprova uma reserva de área comum", tags = "14 - Admin Web - Reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Reserva aprovada com sucesso e redirecionada."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou violação das regras de negócio."),
            @ApiResponse(responseCode = "403", description = "Acesso negado."),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada.")
    })
    @PatchMapping("/{reservaId}/aprovar")
    public String aprovarReserva(
            Authentication authentication,
            @PathVariable UUID reservaId,
            RedirectAttributes redirectAttributes

    ){
        var currentUser = support.authenticatedUser(authentication);
        reservaService.aprovarReserva(currentUser, reservaId);
        redirectAttributes.addFlashAttribute("message", "Reserva aprovada com sucesso.");
        return "redirect:/admin/reservas";
    }

    @Operation(summary = "Nega uma reserva de área comum", tags = "14 - Admmin Web - Reservas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Reserva negada com sucesso e redirecionada."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou violação das regras de negócio."),
            @ApiResponse(responseCode = "403", description = "Acesso negado."),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada.")
    })
    @PatchMapping("/{reservaId}/negar")
    public String negarReserva(
            Authentication authentication,
            @PathVariable UUID reservaId,
            RedirectAttributes redirectAttributes
    ){
        var currentUser = support.authenticatedUser(authentication);
        reservaService.negarReserva(currentUser, reservaId);
        redirectAttributes.addFlashAttribute("message", "Reserva negada.");
        return "redirect:/admin/reservas";
    }
}
