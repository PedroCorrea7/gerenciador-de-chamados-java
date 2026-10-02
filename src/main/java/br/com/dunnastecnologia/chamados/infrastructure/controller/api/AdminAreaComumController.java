package br.com.dunnastecnologia.chamados.infrastructure.controller.api;

import br.com.dunnastecnologia.chamados.infrastructure.service.AreaComumService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/areas")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminAreaComumController {

    private final AreaComumService areaComumService;

    public AdminAreaComumController(AreaComumService areaComumService) {
        this.areaComumService = areaComumService;
    }

    @Operation(summary = "Lista as áreas comuns cadastradas", tags = "17 - Admin Web - Áreas")
    @GetMapping
    public String listarAreas(Model model) {

        model.addAttribute("pageTitle", "Admin - Areas");
        model.addAttribute("areas", areaComumService.listarTodas());
        model.addAttribute("isAdministrador", true);
        model.addAttribute("currentUserHome", "/admin");
        model.addAttribute("currentUserRoleLabel", "Administrador");
        return "admin/areas/lista";
    }

    @Operation(summary = "Abre o formulário para nova área", tags = "17 - Admin Web - Áreas")
    @GetMapping("/nova")
    public String telaNovaArea(Model model) {
        model.addAttribute("isAdministrador", true);
        model.addAttribute("currentUserHome", "/admin");
        model.addAttribute("currentUserRoleLabel", "Administrador");
        return "admin/areas/detalhe";
    }

    @Operation(summary = "Salva uma nova área comum", tags = "17 - Admin Web - Áreas")
    @PostMapping
    public String salvarArea(@RequestParam String nome, @RequestParam String descricao, RedirectAttributes redirectAttributes) {
        try {
            areaComumService.salvar(nome, descricao);
            redirectAttributes.addFlashAttribute("sucessMessage", "Área comum cadastrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/areas/nova";
        }
        return "redirect:/admin/areas";
    }

    @Operation(summary = "Ativa/Desativa uma área comum", tags = "17 - Admin Web - Áreas")
    @PostMapping("/{areaId}/status")
    public String alternarStatus(@PathVariable UUID areaId, RedirectAttributes redirectAttributes) {
        try {
            areaComumService.alternarStatus(areaId);
            redirectAttributes.addFlashAttribute("sucessMessage", "Status da área atualizado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/areas";
    }
}