package br.com.dunnastecnologia.chamados.infrastructure.controller.api;

import br.com.dunnastecnologia.chamados.infrastructure.controller.web.WebControllerSupport;
import br.com.dunnastecnologia.chamados.infrastructure.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping
    public String listarReservas(Authentication authentication, Model model){
        model.addAttribute("reservas", reservaService.listarTodasReservasAdmin());

        model.addAttribute("pageTitle", "Admin - Reservas");
        model.addAttribute("isAdministrador", true);
        model.addAttribute("currentUserHome", "/admin");
        model.addAttribute("currentUserRoleLabel", "Administrador");

        return "admin/reservas/lista";
    }

    @Operation(summary = "Aprova uma reserva de área comum", tags = "14 - Admin Web - Reservas")
    @PostMapping("/{reservaId}/aprovar")
    public String aprovarReserva(
            Authentication authentication,
            @PathVariable UUID reservaId,
            RedirectAttributes redirectAttributes
    ){
        var currentUser = support.authenticatedUser(authentication);
        try {
            reservaService.aprovarReserva(currentUser, reservaId);
            redirectAttributes.addFlashAttribute("sucessMessage", "Reserva aprovada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/reservas";
    }

    @Operation(summary = "Nega uma reserva de área comum", tags = "14 - Admin Web - Reservas")
    @PostMapping("/{reservaId}/negar")
    public String negarReserva(
            Authentication authentication,
            @PathVariable UUID reservaId,
            @RequestParam String motivo,
            RedirectAttributes redirectAttributes
    ){
        var currentUser = support.authenticatedUser(authentication);
        try {
            reservaService.negarReserva(currentUser, reservaId, motivo);
            redirectAttributes.addFlashAttribute("sucessMessage", "Reserva negada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/reservas";
    }
}