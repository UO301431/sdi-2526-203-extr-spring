package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

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

    @GetMapping("space/details/{id}")
    public String getDetail(Model model, @PathVariable Long id){
        model.addAttribute("space", spaceService.getSpace(id));
        return "space/details";
    }
}
