package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.entities.SpaceType;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/spaces")
public class SpaceController {

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

    // ── Listado: admin ve todos, usuario ve solo activos ──────────────────────

    @GetMapping("/list")
    public String listSpaces(Model model,
                             Pageable pageable,
                             @RequestParam(required = false) SpaceType type,
                             @RequestParam(required = false) Integer minCapacity) {
        Page<Space> spaces;
        if (isAdmin()) {
            spaces = spaceService.getSpaces(pageable);
        } else {
            spaces = (type != null || minCapacity != null)
                    ? spaceService.getActiveSpacesFiltered(type, minCapacity, pageable)
                    : spaceService.getActiveSpaces(pageable);
        }
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        model.addAttribute("spaceTypes", SpaceType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("minCapacity", minCapacity);
        return "space/list";
    }

    // ── AJAX: fragmento de tabla para el toggle ───────────────────────────────

    @GetMapping("/update")
    public String updateSpacesTable(Model model, Pageable pageable) {
        if (!isAdmin()) return "redirect:/spaces/list";
        Page<Space> spaces = spaceService.getSpaces(pageable);
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        return "space/list :: tableSpaces";
    }

    // ── Toggle activo/inactivo (solo admin) ───────────────────────────────────

    @PostMapping("/toggle/{id}")
    public String toggleSpace(@PathVariable Long id, Model model, Pageable pageable) {
        if (!isAdmin()) return "redirect:/spaces/list";
        spaceService.toggleActive(id);
        Page<Space> spaces = spaceService.getSpaces(pageable);
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        return "space/list :: tableSpaces";
    }

    // ── Formulario nuevo espacio (solo admin) ─────────────────────────────────

    @GetMapping("/new")
    public String newSpaceForm(Model model) {
        if (!isAdmin()) return "redirect:/spaces/list";
        model.addAttribute("space", new Space());
        model.addAttribute("spaceTypes", SpaceType.values());
        return "space/form";
    }

    @PostMapping("/new")
    public String createSpace(@ModelAttribute Space space,
                              Model model,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        if (!isAdmin()) return "redirect:/spaces/list";
        String error = spaceService.addSpace(space);
        if (error != null) {

            model.addAttribute("errorMessage", messageSource.getMessage(error, null, locale));

            model.addAttribute("space", space);
            model.addAttribute("spaceTypes", SpaceType.values());
            return "space/form";
        }
        redirectAttrs.addFlashAttribute("successMessage",
                messageSource.getMessage("space.success.created", null, locale));
        return "redirect:/spaces/list";
    }

    // ── Formulario editar espacio (solo admin) ────────────────────────────────

    @GetMapping("/edit/{id}")
    public String editSpaceForm(@PathVariable Long id, Model model) {
        if (!isAdmin()) return "redirect:/spaces/list";
        Optional<Space> opt = spaceService.findById(id);
        if (opt.isEmpty()) return "redirect:/spaces/list";
        model.addAttribute("space", opt.get());
        model.addAttribute("spaceTypes", SpaceType.values());
        return "space/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateSpace(@PathVariable Long id,
                              @ModelAttribute Space space,
                              Model model,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        if (!isAdmin()) return "redirect:/spaces/list";
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
        return "redirect:/spaces/list";
    }

    // ── Detalle de un espacio ─────────────────────────────────────────────────

    @GetMapping("/detail/{id}")
    public String spaceDetail(@PathVariable Long id, Model model) {
        Optional<Space> opt = spaceService.findById(id);
        if (opt.isEmpty()) return "redirect:/spaces/list";
        model.addAttribute("space", opt.get());
        return "space/detail";
    }
}