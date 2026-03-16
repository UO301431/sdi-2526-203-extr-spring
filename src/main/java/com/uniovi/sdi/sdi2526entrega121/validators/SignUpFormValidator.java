package com.uniovi.sdi.sdi2526entrega121.validators;

import com.uniovi.sdi.sdi2526entrega121.services.UsersService;
import org.passay.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.uniovi.sdi.sdi2526entrega121.entities.User;

@Component
public class SignUpFormValidator implements Validator {
    private final UsersService usersService;

    public SignUpFormValidator(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        // Todos los campos cumplimentados
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dni", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "Error.empty");

        //Comprobacion de no permitir dos usuarios con el mismo identificador
        if (usersService.getUserByDni(user.getDni()) != null) {
            errors.rejectValue("dni", "Error.signup.dni.duplicate");
        }

        //DNI valido
        if (!user.getDni().matches("^[0-9]{8}[A-Za-z]$")) {
            errors.rejectValue("dni", "Error.signup.dni.length");
        }

        //Las contraseñas coinciden
        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "Error.signup.passwordConfirm.coincidence");
        }

        //Validaciones de contraseña
        PasswordValidator passwordValidator = new PasswordValidator(
                new LengthRule(12,20),
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new CharacterRule(EnglishCharacterData.LowerCase,1),
                new CharacterRule(EnglishCharacterData.Digit,1),
                new CharacterRule(EnglishCharacterData.Special,1));
        RuleResult result = passwordValidator.validate(new PasswordData(user.getPassword()));
        if (!result.isValid()) {
            errors.rejectValue("password", "Error.signup.password.valid");
        }

    }
}
