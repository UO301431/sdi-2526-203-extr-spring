package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    /**
     * Muestra la lista de espacios disponibles
     * @param searchType, cadena de texto para filtar por tipo
     * @param searchCapacity, número para filtar por capacidad minima
     */
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

    /**
     * Se devuelven los detalles del espacio solicitado
     * @param id, id del espacio del que se consultan los detalles
     */
    @GetMapping("space/details/{id}")
    public String getDetail(Model model, @PathVariable Long id){
        model.addAttribute("space", spaceService.getSpace(id));
        return "space/details";
    }

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
