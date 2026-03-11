package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("space/activeList")
    public String getList(Model model, Pageable pageable,
                          @RequestParam(value = "", required = false) String searchType,
                          @RequestParam(value = "", required = false) Integer searchCapacity){

        Page<Space> activeSpace;

        //Se comprueba que se ha buscado por tipo
        if(searchType != null && !searchType.isEmpty()){
            //Se comprueba que se ha buscado por capacidad
            if(searchCapacity != null){
                //se busca por tipo y capacidad
                activeSpace = spaceService.getActivSpacesByTypeAndCapacity(pageable, searchType, searchCapacity);
            }else{
                //se busca por tipo
                activeSpace = spaceService.getActiveSpacesByType(pageable, searchType);
            }
        //no se ha buscado por tipo
        }else{
            //Se comprueba que se ha buscado por capacidad
            if(searchCapacity != null){
                //Se busca por capacidad
                activeSpace = spaceService.getActiveSpacesByCapacity(pageable, searchCapacity);
            }else{
                //Se busca sin aplicar ningun filtro
                activeSpace = spaceService.getActiveSpaces(pageable);
            }
        }

        model.addAttribute("availableSpaceList", activeSpace.getContent());
        model.addAttribute("page", activeSpace);
        return "space/list";
    }
}
