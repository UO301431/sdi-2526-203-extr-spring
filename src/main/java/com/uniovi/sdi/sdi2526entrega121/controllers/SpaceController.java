package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaceController {
    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("space/activeList")
    public String getList(Model model, Pageable pageable){

        Page<Space> activeSpace = spaceService.getActiveSpaces(pageable);

        model.addAttribute("availableSpaceList", activeSpace.getContent());
        model.addAttribute("page", activeSpace);
        return "space/list";
    }
}
