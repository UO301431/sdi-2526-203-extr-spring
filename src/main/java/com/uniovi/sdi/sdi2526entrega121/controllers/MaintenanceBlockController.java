package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.MaintenanceBlock;
import com.uniovi.sdi.sdi2526entrega121.services.MaintenanceBlockService;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Locale;

@Controller
@RequestMapping("/blocks")
public class MaintenanceBlockController {

    @Autowired
    private MaintenanceBlockService blockService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private MessageSource messageSource;

    // ── Helper: comprueba si el usuario autenticado es admin ──────────────────

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // ── Listado de bloqueos de un espacio ─────────────────────────────────────

    @GetMapping("/list/{spaceId}")
    public String listBlocks(@PathVariable Long spaceId, Model model, Pageable pageable) {
        if (!isAdmin()) return "redirect:/spaces/list";
        Page<MaintenanceBlock> blocks = blockService.getBlocksBySpace(spaceId, pageable);
        model.addAttribute("blocks", blocks.getContent());
        model.addAttribute("page", blocks);
        model.addAttribute("spaceId", spaceId);
        spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
        return "maintenance/list";
    }

    // ── Formulario nuevo bloqueo ──────────────────────────────────────────────

    @GetMapping("/new/{spaceId}")
    public String newBlockForm(@PathVariable Long spaceId, Model model) {
        if (!isAdmin()) return "redirect:/spaces/list";
        model.addAttribute("spaceId", spaceId);
        spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
        return "maintenance/form";
    }

    @PostMapping("/new/{spaceId}")
    public String createBlock(
            @PathVariable Long spaceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
            @RequestParam String reason,
            Model model,
            RedirectAttributes redirectAttrs,
            Locale locale) {

        if (!isAdmin()) return "redirect:/spaces/list";

        String error = blockService.createBlock(spaceId, startDate, endDate, reason);
        if (error != null) {
            //No esta internacionalizado
            //model.addAttribute("errorMessage", messageSource.getMessage(error, null, locale));
            model.addAttribute("error", error);
            model.addAttribute("spaceId", spaceId);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("reason", reason);
            spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
            return "maintenance/form";
        }
        //TODO cambiar cuando este internacionalizado
        /*redirectAttrs.addFlashAttribute("successMessage",
                messageSource.getMessage("block.success.created", null, locale));*/
        return "redirect:/blocks/list/" + spaceId;
    }

    // ── Cancelar bloqueo ──────────────────────────────────────────────────────

    @PostMapping("/cancel/{blockId}")
    public String cancelBlock(@PathVariable Long blockId,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        if (!isAdmin()) return "redirect:/spaces/list";

        Long spaceId = blockService.findById(blockId)
                .map(b -> b.getSpace().getId())
                .orElse(null);

        String error = blockService.cancelBlock(blockId);
        if (error != null) {
            //No esta internacionalizado
            //TODO cambiar cuando este internacionalizado
            //redirectAttrs.addFlashAttribute("errorMessage", messageSource.getMessage(error, null, locale));
            redirectAttrs.addFlashAttribute("error", error);
        } else {
            //TODO cambiar cuando este internacionalizado
            /*redirectAttrs.addFlashAttribute("successMessage",
                    messageSource.getMessage("block.success.cancelled", null, locale));*/
        }

        if (spaceId != null) {
            return "redirect:/blocks/list/" + spaceId;
        }
        return "redirect:/spaces/list";
    }


}