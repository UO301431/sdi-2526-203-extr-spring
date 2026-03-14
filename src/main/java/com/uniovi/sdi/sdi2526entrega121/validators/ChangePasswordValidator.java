package com.uniovi.sdi.sdi2526entrega121.validators;

import com.uniovi.sdi.sdi2526entrega121.dtos.ChangePasswordDto;
import com.uniovi.sdi.sdi2526entrega121.entities.User;
import com.uniovi.sdi.sdi2526entrega121.services.UsersService;
import org.passay.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordValidator implements Validator {
    private final UsersService usersService;
    private String userDni;
    private BCryptPasswordEncoder encoder;


    public String getUserDni() {
        return userDni;
    }

    public void setUserDni(String userDni) {
        this.userDni = userDni;
    }

    public ChangePasswordValidator(UsersService usersService, BCryptPasswordEncoder encoder) {
        this.usersService = usersService;
        this.encoder = encoder;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordDto dto = (ChangePasswordDto) target;

        //Todos los campos cumplimentados
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "actualPassword", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "Error.empty");


        if (!encoder.matches(dto.getActualPassword(), usersService.getUserByDni(userDni).getPassword())) {
            errors.rejectValue("actualPassword", "Error.incorrect.actualPassword");
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "Error.signup.passwordConfirm.coincidence");
        }

        //Validaciones de contraseña
        PasswordValidator passwordValidator = new PasswordValidator(
                new LengthRule(12, 20),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1));
        RuleResult result = passwordValidator.validate(new PasswordData(dto.getPassword()));
        if (!result.isValid()) {
            errors.rejectValue("password", "Error.signup.password.valid");
        }
    }
}
