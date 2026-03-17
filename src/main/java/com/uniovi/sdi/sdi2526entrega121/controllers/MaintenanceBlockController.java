package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.MaintenanceBlock;
import com.uniovi.sdi.sdi2526entrega121.services.MaintenanceBlockService;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Controlador para la gestión de bloqueos de mantenimiento.
 * Todas las rutas están bajo /blocks.
 * Los bloqueos son períodos en los que un espacio no está disponible para reservas.
 * Solo los administradores pueden crear, listar y cancelar bloqueos.
 * Los usuarios estándar son redirigidos a /spaces/list si intentan acceder.
 */
@Controller
@RequestMapping("/blocks")
@PreAuthorize("hasRole('ADMIN')")
public class MaintenanceBlockController {

    @Autowired
    private MaintenanceBlockService blockService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Muestra el listado paginado de bloqueos de mantenimiento de un espacio concreto.
     * Incluye tanto los bloqueos activos como los cancelados.
     * Solo accesible para administradores.
     * @param spaceId identificador del espacio cuyos bloqueos se quieren listar
     */
    @GetMapping("/list/{spaceId}")
    public String listBlocks(@PathVariable Long spaceId, Model model, Pageable pageable) {
        //if (!isAdmin()) return "redirect:/spaces/list";
        Page<MaintenanceBlock> blocks = blockService.getBlocksBySpace(spaceId, pageable);
        model.addAttribute("blocks", blocks.getContent());
        model.addAttribute("page", blocks);
        model.addAttribute("spaceId", spaceId);
        spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
        return "maintenance/list";
    }

    /**
     * Muestra el formulario para crear un nuevo bloqueo de mantenimiento para un espacio.
     * Solo accesible para administradores.
     * @param spaceId identificador del espacio al que pertenecerá el bloqueo
     */
    @GetMapping("/new/{spaceId}")
    public String newBlockForm(@PathVariable Long spaceId, Model model) {
        //if (!isAdmin()) return "redirect:/spaces/list";
        model.addAttribute("spaceId", spaceId);
        spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
        return "maintenance/form";
    }

    /**
     * Procesa el formulario de creación de un nuevo bloqueo de mantenimiento.
     * Delega todas las validaciones al servicio:
     *   - Fechas no nulas y con inicio anterior al fin
     *   - Motivo no vacío
     *   - Espacio existente
     *   - Sin solapamiento con otros bloqueos activos
     *   - Sin solapamiento con reservas activas
     * Si hay error, vuelve al formulario mostrando el mensaje de error i18n.
     * Si tiene éxito, redirige al listado de bloqueos del espacio con mensaje de confirmación.
     * Solo accesible para administradores.
     * @param spaceId identificador del espacio al que pertenecerá el bloqueo
     * @param startDate fecha y hora de inicio del bloqueo
     * @param endDate fecha y hora de fin del bloqueo
     * @param reason motivo del bloqueo de mantenimiento
     */
    @PostMapping("/new/{spaceId}")
    public String createBlock(
            @PathVariable Long spaceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
            @RequestParam String reason,
            Model model,
            RedirectAttributes redirectAttrs,
            Locale locale) {

        //if (!isAdmin()) return "redirect:/spaces/list";

        String error = blockService.createBlock(spaceId, startDate, endDate, reason);
        if (error != null) {

            model.addAttribute("errorMessage", messageSource.getMessage(error, null, locale));

            model.addAttribute("spaceId", spaceId);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("reason", reason);
            spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
            return "maintenance/form";
        }

        redirectAttrs.addFlashAttribute("successMessage",
                messageSource.getMessage("block.success.created", null, locale));
        return "redirect:/blocks/list/" + spaceId;
    }

    /**
     * Cancela un bloqueo de mantenimiento (baja lógica).
     * No elimina el registro de la base de datos, sino que cambia su estado a CANCELLED.
     * Antes de cancelar, obtiene el spaceId del bloqueo para poder redirigir al listado
     * del espacio correspondiente tras la operación.
     * Si el bloqueo no existe o ya estaba cancelado, redirige con mensaje de error.
     * Si tiene éxito, redirige al listado de bloqueos del espacio con mensaje de confirmación.
     * Solo accesible para administradores.
     * @param blockId identificador del bloqueo a cancelar
     */
    @PostMapping("/cancel/{blockId}")
    public String cancelBlock(@PathVariable Long blockId,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        //if (!isAdmin()) return "redirect:/spaces/list";

        Long spaceId = blockService.findById(blockId)
                .map(b -> b.getSpace().getId())
                .orElse(null);

        String error = blockService.cancelBlock(blockId);
        if (error != null) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    messageSource.getMessage(error, null, locale));
        } else {
            redirectAttrs.addFlashAttribute("successMessage",
                    messageSource.getMessage("block.success.cancelled", null, locale));
        }

        if (spaceId != null) {
            return "redirect:/blocks/list/" + spaceId;
        }
        return "redirect:/spaces/list";
    }
}