package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.MaintenanceBlock;
import com.uniovi.sdi.sdi2526entrega121.services.MaintenanceBlockService;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Locale;

@Controller
public class MaintenanceBlockController {

    @Autowired
    private MaintenanceBlockService blockService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private MessageSource messageSource;

    // ── List blocks for a space ───────────────────────────────────────────────

    @GetMapping("/admin/blocks/list/{spaceId}")
    public String listBlocks(@PathVariable Long spaceId, Model model, Pageable pageable) {
        Page<MaintenanceBlock> blocks = blockService.getBlocksBySpace(spaceId, pageable);
        model.addAttribute("blocks", blocks.getContent());
        model.addAttribute("page", blocks);
        model.addAttribute("spaceId", spaceId);
        spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
        return "maintenance/list";
    }

    // ── New block form ────────────────────────────────────────────────────────

    @GetMapping("/admin/blocks/new/{spaceId}")
    public String newBlockForm(@PathVariable Long spaceId, Model model) {
        model.addAttribute("spaceId", spaceId);
        spaceService.findById(spaceId).ifPresent(s -> model.addAttribute("space", s));
        return "maintenance/form";
    }

    @PostMapping("/admin/blocks/new/{spaceId}")
    public String createBlock(
            @PathVariable Long spaceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
            @RequestParam String reason,
            Model model,
            RedirectAttributes redirectAttrs,
            Locale locale) {

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
        return "redirect:/admin/blocks/list/" + spaceId;
    }

    // ── Cancel block ──────────────────────────────────────────────────────────

    @PostMapping("/admin/blocks/cancel/{blockId}")
    public String cancelBlock(@PathVariable Long blockId,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        // Retrieve spaceId before cancelling so we can redirect back
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
            return "redirect:/admin/blocks/list/" + spaceId;
        }
        return "redirect:/admin/spaces/list";
    }
}