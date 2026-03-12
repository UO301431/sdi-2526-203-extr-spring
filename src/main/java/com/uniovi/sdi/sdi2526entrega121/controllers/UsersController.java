package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.User;
import com.uniovi.sdi.sdi2526entrega121.services.RolesService;
import com.uniovi.sdi.sdi2526entrega121.services.SecurityService;
import com.uniovi.sdi.sdi2526entrega121.services.UsersService;
import com.uniovi.sdi.sdi2526entrega121.validators.SignUpFormValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class UsersController {

    private final UsersService usersService;
    private final SecurityService securityService;
    private final RolesService rolesService;
    private final SignUpFormValidator signUpFormValidator;

    public UsersController(UsersService usersService, SecurityService securityService, RolesService rolesService, SignUpFormValidator signUpFormValidator) {
        this.usersService = usersService;
        this.securityService = securityService;
        this.rolesService = rolesService;
        this.signUpFormValidator = signUpFormValidator;
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, BindingResult result, Model model) {
        signUpFormValidator.validate(user,result);
        if (result.hasErrors()) {
            return "signup";
        }
        user.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user);
        securityService.autoLogin(user.getDni(), user.getPasswordConfirm());

        return "redirect:home";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping("/home")
    public String home(Model model, Pageable pageable, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "/home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}

