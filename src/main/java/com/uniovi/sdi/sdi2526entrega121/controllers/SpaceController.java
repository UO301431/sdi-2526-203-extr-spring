package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.entities.SpaceType;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;


@Controller
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private MessageSource messageSource;

    // ── Admin: list all spaces ────────────────────────────────────────────────

    @GetMapping("/admin/spaces/list")
    public String listSpaces(Model model, Pageable pageable) {
        Page<Space> spaces = spaceService.getSpaces(pageable);
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        return "space/admin-list";
    }

    // ── Admin: new space form ─────────────────────────────────────────────────

    @GetMapping("/admin/spaces/new")
    public String newSpaceForm(Model model) {
        model.addAttribute("space", new Space());
        model.addAttribute("spaceTypes", SpaceType.values());
        return "space/form";
    }

    @PostMapping("/admin/spaces/new")
    public String createSpace(@ModelAttribute Space space,
                              Model model,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        String error = spaceService.addSpace(space);
        if (error != null) {
            model.addAttribute("errorMessage", messageSource.getMessage(error, null, locale));
            model.addAttribute("space", space);
            model.addAttribute("spaceTypes", SpaceType.values());
            return "space/form";
        }
        redirectAttrs.addFlashAttribute("successMessage",
                messageSource.getMessage("space.success.created", null, locale));
        return "redirect:/admin/spaces/list";
    }

    // ── Admin: edit space ─────────────────────────────────────────────────────

    @GetMapping("/admin/spaces/edit/{id}")
    public String editSpaceForm(@PathVariable Long id, Model model) {
        Optional<Space> opt = spaceService.findById(id);
        if (opt.isEmpty()) return "redirect:/admin/spaces/list";
        model.addAttribute("space", opt.get());
        model.addAttribute("spaceTypes", SpaceType.values());
        return "space/edit";
    }

    @PostMapping("/admin/spaces/edit/{id}")
    public String updateSpace(@PathVariable Long id,
                              @ModelAttribute Space space,
                              Model model,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        String error = spaceService.editSpace(id, space);
        if (error != null) {
            model.addAttribute("errorMessage", messageSource.getMessage(error, null, locale));
            space.setId(id);
            model.addAttribute("space", space);
            model.addAttribute("spaceTypes", SpaceType.values());
            return "space/edit";
        }
        redirectAttrs.addFlashAttribute("successMessage",
                messageSource.getMessage("space.success.edited", null, locale));
        return "redirect:/admin/spaces/list";
    }

    // ── Admin: AJAX – devuelve solo el fragmento de la tabla ──────────────────

    @GetMapping("/admin/spaces/update")
    public String updateSpacesTable(Model model, Pageable pageable) {
        Page<Space> spaces = spaceService.getSpaces(pageable);
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        return "space/admin-list :: tableSpaces";
    }

    // ── Admin: toggle active/inactive (llamado por AJAX) ──────────────────────

    @PostMapping("/admin/spaces/toggle/{id}")
    public String toggleSpace(@PathVariable Long id, Model model, Pageable pageable) {
        spaceService.toggleActive(id);
        Page<Space> spaces = spaceService.getSpaces(pageable);
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        return "space/admin-list :: tableSpaces";
    }

    // ── Standard user: list active spaces ─────────────────────────────────────

    @GetMapping("/spaces/list")
    public String listActiveSpaces(Model model,
                                   Pageable pageable,
                                   @RequestParam(required = false) SpaceType type,
                                   @RequestParam(required = false) Integer minCapacity) {
        Page<Space> spaces;
        if (type != null || minCapacity != null) {
            spaces = spaceService.getActiveSpacesFiltered(type, minCapacity, pageable);
        } else {
            spaces = spaceService.getActiveSpaces(pageable);
        }
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        model.addAttribute("spaceTypes", SpaceType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("minCapacity", minCapacity);
        return "space/user-list";
    }

    // ── Standard user: space detail ───────────────────────────────────────────

    @GetMapping("/spaces/detail/{id}")
    public String spaceDetail(@PathVariable Long id, Model model) {
        Optional<Space> opt = spaceService.findById(id);
        if (opt.isEmpty()) return "redirect:/spaces/list";
        model.addAttribute("space", opt.get());
        return "space/detail";
    }
}