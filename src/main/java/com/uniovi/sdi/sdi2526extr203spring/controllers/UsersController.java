package com.uniovi.sdi.sdi2526extr203spring.controllers;

import com.uniovi.sdi.sdi2526extr203spring.dtos.ChangePasswordDto;
import com.uniovi.sdi.sdi2526extr203spring.entities.User;
import com.uniovi.sdi.sdi2526extr203spring.services.RolesService;
import com.uniovi.sdi.sdi2526extr203spring.services.SecurityService;
import com.uniovi.sdi.sdi2526extr203spring.services.UsersService;
import com.uniovi.sdi.sdi2526extr203spring.validators.ChangePasswordValidator;
import com.uniovi.sdi.sdi2526extr203spring.validators.SignUpFormValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controlador encargado de la gestion de usuarios y procesos de la autenticacion
 * Maneja el registro de nuevos usuarios, el acceso al sistema y la redireccion a la vista principal
 * @author Pablo Roces
 * @version 1.0
 *
 */
@Controller
public class UsersController {

    private final UsersService usersService;
    private final SecurityService securityService;
    private final RolesService rolesService;
    private final SignUpFormValidator signUpFormValidator;
    private final ChangePasswordValidator changePasswordValidator;

    public UsersController(UsersService usersService, SecurityService securityService, RolesService rolesService, SignUpFormValidator signUpFormValidator, ChangePasswordValidator changePasswordValidator) {
        this.usersService = usersService;
        this.securityService = securityService;
        this.rolesService = rolesService;
        this.signUpFormValidator = signUpFormValidator;
        this.changePasswordValidator = changePasswordValidator;
    }

    /**
     * Procesa el formulario de registro de un nuevo usuario
     * Realiza validaciones personalizadas y si tiene exito, registra al nuevo usuario
     * en el sistema e inicia su sesion automaticamente
     * @param user El objeto {@link User} vinculado a los campos del formulario
     * @param result Objeto que contiene los posibles errores de validacion
     * @param model El modelo de la vista
     * @return El nombre de la vista de registro si hay errores o redireccion a home si es correcto
     */
    @PostMapping("/signup")
    public String signup(Authentication auth, @ModelAttribute User user, BindingResult result, Model model) {
        if(auth != null && auth.isAuthenticated()) { return "redirect:/home"; }
        signUpFormValidator.validate(user,result);
        if (result.hasErrors()) {
            return "signup";
        }
        user.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user);
        securityService.autoLogin(user.getDni(), user.getPasswordConfirm());

        return "redirect:home";
    }

    /**
     *  Muestra el formulario de registro al usuario
     *  Inicializa un objeto {@link User} vacio para el binding del formulario
     * @param model El modelo de la vista para añadir el objeto usuario
     * @return El nombre de la vista "signup"
     */
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    /**
     * Gestiona la peticion a la pagina de inicio del sistema
     * Recupera la informacion de autenticacion del contexto de seguridad
     * @param model El modelo de la vista
     * @param pageable Informacion de paginacion opcional
     * @param principal El usuario autenticado actualmente
     * @return El nombre de la vista "/home"
     */
    @RequestMapping("/home")
    public String home(Model model, Pageable pageable, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "/home";
    }

    /**
     * Muestra la vista de inicio de sesion
     * @return El nombre de la vista "login"
     */
    @GetMapping("/login")
    public String login(Authentication auth) {
        if(auth != null && auth.isAuthenticated()) { return "redirect:/home"; }
        return "login";
    }

    /**
     * Muestra el formulario para cambiar la contraseña del usuario que esta autenticado
     * Este metodo crea un objeto {@link ChangePasswordDto} vacio y lo añade al modelo
     * para que el formulario pueda enlazar sus campos con dicho objeto.
     * La vista devuelta corresponde a la pagina de cambio de contraseña del perfil
     *
     * @param model modelo de Spring utilizado para enviar atributos a la vista
     * @return nombre de la vista que contiene el formulario de cambio de contraseña
     */
    @GetMapping("/profile/password")
    public String getChangePassword(Model model){
        model.addAttribute("passwordDto", new ChangePasswordDto());
        return "user/profile-password";
    }

    /**
     * Procesa la solicitud de cambio de contraseña enviada por el usuario
     * @param dto objeto que contiene los datos introducidos en el formulario de cambio
     * @param result objeto que almacena los errores de validacion
     * @param principal objeto que contiene la informacion del usuario autenticado
     * @param model modelo de Spring utilizado para enviar atributos a la vista
     * @return redireccion a la pagina principal si el cambio de contraseña es correcto, en caso contrario, devuelve la vista con el formulario de errores
     */
    @PostMapping("/profile/password")
    public String setChangePassword(@ModelAttribute("passwordDto") ChangePasswordDto dto,
                                    BindingResult result,
                                    Principal principal,
                                    Model model){
        String dni = principal.getName();
        changePasswordValidator.setUserDni(dni);
        changePasswordValidator.validate(dto,result);
        if(result.hasErrors()){
            return "user/profile-password";
        }

        usersService.changeUserPassword(dni, dto);
        return "redirect:/home";
    }
}

