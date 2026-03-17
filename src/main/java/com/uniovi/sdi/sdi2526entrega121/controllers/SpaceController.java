package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.entities.SpaceType;
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
import java.util.Optional;

/**
 * Controlador para la gestión de espacios.
 * Todas las rutas están bajo /spaces.
 * El control de acceso se realiza a nivel de controlador mediante isAdmin():
 *   - ROLE_ADMIN: accede a todas las operaciones (listar, crear, editar, toggle, bloqueos)
 *   - ROLE_EMPLOYEE: solo puede listar espacios activos y ver el detalle
 */
@Controller
@RequestMapping("/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Comprueba si el usuario autenticado tiene el rol de administrador.
     * Se usa en cada método que requiere permisos de admin.
     * @return true si el usuario tiene ROLE_ADMIN, false en caso contrario
     */
    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Listado de espacios. La vista es la misma para admin y usuario estándar (space/list),
     * pero el contenido varía según el rol:
     *   - Admin: ve todos los espacios (activos e inactivos) sin filtros
     *   - Usuario estándar: ve solo los espacios activos, con filtros opcionales por tipo y capacidad mínima
     * @param type filtro opcional por tipo de espacio (solo para usuarios estándar)
     * @param minCapacity filtro opcional por capacidad mínima (solo para usuarios estándar)
     */
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

    /**
     * Endpoint AJAX que devuelve únicamente el fragmento Thymeleaf "tableSpaces"
     * de la vista space/list. Se usa para refrescar la tabla sin recargar toda la página,
     * por ejemplo tras cambiar el idioma o forzar una actualización manual.
     * Solo accesible para administradores.
     */
    @GetMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateSpacesTable(Model model, Pageable pageable) {
        //if (!isAdmin()) return "redirect:/spaces/list";
        Page<Space> spaces = spaceService.getSpaces(pageable);
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        return "space/list :: tableSpaces";
    }

    /**
     * Cambia el estado activo/inactivo de un espacio (baja lógica).
     * Llamado por AJAX desde el botón de toggle en la tabla de admin.
     * Devuelve solo el fragmento "tableSpaces" para que jQuery lo reemplace
     * en el DOM sin recargar la página completa.
     * Solo accesible para administradores.
     * @param id identificador del espacio a activar o desactivar
     */
    @PostMapping("/toggle/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String toggleSpace(@PathVariable Long id, Model model, Pageable pageable) {
        //if (!isAdmin()) return "redirect:/spaces/list";
        spaceService.toggleActive(id);
        Page<Space> spaces = spaceService.getSpaces(pageable);
        model.addAttribute("spaces", spaces.getContent());
        model.addAttribute("page", spaces);
        return "space/list :: tableSpaces";
    }

    /**
     * Muestra el formulario para registrar un nuevo espacio.
     * Solo accesible para administradores.
     */
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newSpaceForm(Model model) {
        //if (!isAdmin()) return "redirect:/spaces/list";

        model.addAttribute("space", new Space());
        model.addAttribute("spaceTypes", SpaceType.values());
        return "space/form";
    }

    /**
     * Procesa el formulario de creación de un nuevo espacio.
     * Delega las validaciones al servicio (nombre no vacío, capacidad >= 1, nombre no duplicado).
     * Si hay error, vuelve al formulario mostrando el mensaje de error i18n.
     * Si tiene éxito, redirige al listado con mensaje de confirmación.
     * Solo accesible para administradores.
     * @param space datos del espacio recibidos del formulario
     */
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createSpace(@ModelAttribute Space space,
                              Model model,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        //if (!isAdmin()) return "redirect:/spaces/list";
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

    /**
     * Muestra el formulario de edición de un espacio existente.
     * Si el espacio no existe, redirige al listado.
     * Solo accesible para administradores.
     * @param id identificador del espacio a editar
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editSpaceForm(@PathVariable Long id, Model model) {
        //sif (!isAdmin()) return "redirect:/spaces/list";
        Optional<Space> opt = spaceService.findById(id);
        if (opt.isEmpty()) return "redirect:/spaces/list";
        model.addAttribute("space", opt.get());
        model.addAttribute("spaceTypes", SpaceType.values());
        return "space/edit";
    }

    /**
     * Procesa el formulario de edición de un espacio.
     * Delega las validaciones al servicio (nombre no vacío, capacidad >= 1, nombre no duplicado).
     * Si hay error, vuelve al formulario mostrando el mensaje de error i18n.
     * Si tiene éxito, redirige al listado con mensaje de confirmación.
     * Solo accesible para administradores.
     * @param id identificador del espacio que se está editando
     * @param space datos actualizados del espacio recibidos del formulario
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateSpace(@PathVariable Long id,
                              @ModelAttribute Space space,
                              Model model,
                              RedirectAttributes redirectAttrs,
                              Locale locale) {
        //if (!isAdmin()) return "redirect:/spaces/list";
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

    /**
     * Muestra el detalle de un espacio concreto.
     * Accesible tanto para administradores como para usuarios estándar.
     * Si el espacio no existe, redirige al listado.
     * @param id identificador del espacio a consultar
     */
    @GetMapping("/detail/{id}")
    public String spaceDetail(@PathVariable Long id, Model model) {
        Optional<Space> opt = spaceService.findById(id);
        if (opt.isEmpty()) return "redirect:/spaces/list";
        model.addAttribute("space", opt.get());
        return "space/detail";
    }

    /**
     * Muestra la vista para seleccionar el rango de fechas a consultar para un espacio.
     * @param id identificador del espacio del que se consulta la disponibilidad
     */
    @GetMapping("space/availability/{id}")
    public String getAvailability(Model model, @PathVariable Long id){
        model.addAttribute("space", spaceService.getSpace(id));
        return "space/availability";
    }

    /**
     * Devuelve los slots ocupados de un espacio en un rango de fechas dado,
     * tanto por reservas activas como por bloqueos de mantenimiento activos.
     * Se usa también como validación de solapamientos: si la lista devuelta
     * no está vacía, existe al menos un conflicto en ese rango.
     * @param id identificador del espacio a consultar
     * @param startDate inicio del rango de fechas a consultar
     * @param endDate fin del rango de fechas a consultar
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