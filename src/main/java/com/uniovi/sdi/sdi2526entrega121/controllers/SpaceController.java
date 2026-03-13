package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.entities.SpaceType;
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
            //No esta internacionalizado
            //TODO cambiar cuando este internacionalizado
            //model.addAttribute("errorMessage", messageSource.getMessage(error, null, locale));
            model.addAttribute("error", error);
            model.addAttribute("space", space);
            model.addAttribute("spaceTypes", SpaceType.values());
            return "space/form";
        }
        /*redirectAttrs.addFlashAttribute("successMessage",
                messageSource.getMessage("space.success.created", null, locale));*/
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
            //No esta internacionalizado
            //TODO cambiar cuando este internacionalizado
            //model.addAttribute("errorMessage", messageSource.getMessage(error, null, locale));
            model.addAttribute("error", error);
            space.setId(id);
            model.addAttribute("space", space);
            model.addAttribute("spaceTypes", SpaceType.values());
            return "space/edit";
        }
        //TODO cambiar cuando este internacionalizado
        /*redirectAttrs.addFlashAttribute("successMessage",
                messageSource.getMessage("space.success.edited", null, locale));*/
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


    //logica duplicada de lista de espacios disponibles y detalle de espacio
    /**
     * Muestra la lista de espacios disponibles
     * @param searchType, cadena de texto para filtar por tipo
     * @param searchCapacity, número para filtar por capacidad minima
     */
    /*
    @GetMapping("spaces")
    public String getList(Model model, Pageable pageable,
                          @RequestParam(value = "", required = false) String searchType,
                          @RequestParam(value = "", required = false) Integer searchCapacity){

        Page<Space> activeSpace;
        boolean hasType = searchType != null && !searchType.isEmpty();
        boolean hasCapacity = searchCapacity != null;

        //Se comprueba que se ha buscado por tipo y capacidad
        if(hasType && hasCapacity){
            activeSpace = spaceService.getActivSpacesByTypeAndCapacity(pageable, searchType, searchCapacity);
        }
        //Se comprueba que se ha buscado por tipo
        else if (hasType) {
            activeSpace = spaceService.getActiveSpacesByType(pageable, searchType);
        }
        //Se comprueba que se ha buscado por capacidad
        else if (hasCapacity) {
            activeSpace = spaceService.getActiveSpacesByCapacity(pageable, searchCapacity);
        }
        //Se busca sin aplicar ningun filtro
        else{
            activeSpace = spaceService.getActiveSpaces(pageable);
        }

        model.addAttribute("availableSpaceList", activeSpace.getContent());
        model.addAttribute("page", activeSpace);
        return "space/list";
    }

     */

    /**
     * Se devuelven los detalles del espacio solicitado
     * @param id, id del espacio del que se consultan los detalles
     */
    /*
    @GetMapping("space/details/{id}")
    public String getDetail(Model model, @PathVariable Long id){
        model.addAttribute("space", spaceService.getSpace(id));
        return "space/details";
    }
     */

    //finlogica duplicada de lista de espacios disponibles y detalle de espacio

    /**
     * Muestra la vista para seleccionar el rango de fechas a consultar
     */
    @GetMapping("space/availability/{id}")
    public String getAvailability(Model model, @PathVariable Long id){
        model.addAttribute("space", spaceService.getSpace(id));
        return "space/availability";
    }

    /**
     * Se devuelven los dias en los que el esapcio no esta disponible, indicandp
     * si es por reserva o bloqueo, fechas en las que no esta disponible y motivo
     * @param id, id del espacio del que se consulta la disponibilidad
     * @param startDate, primera fecha del rango a consultar
     * @param endDate, fecha final del rango a consultar
     */
    @GetMapping("/space/availability/info")
    public String getAvailabilityInfo(Model model, @RequestParam Long id,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        model.addAttribute("occupiedSlots", spaceService.getAvailabilityForSpace(id, startDate, endDate));
        model.addAttribute("spaceName", spaceService.getSpace(id).getName());
        return "space/availabilityInfo";
    }
}