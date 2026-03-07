package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InsertSampleDataService {

    private final UsersService usersService;

    public InsertSampleDataService(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostConstruct
    public void init() {
        List<User> users = new ArrayList<>();

        // 1. Crear el usuario administrador
        User admin = new User("12345678Z", "Admin", "Administrador");
        admin.setPassword("@Dm1n1str@D0r");
        admin.setRole("ROLE_ADMIN");
        usersService.addUser(admin);
        users.add(admin);

        // 2. Crear al menos 5 usuarios estándar
        String letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE";

        for (int i = 1; i <= 5; i++) {
            int dniNumber = 10000000 + i;
            char letra = letrasDni.charAt(dniNumber % 23);
            String login = dniNumber + String.valueOf(letra);
            String password = String.format("Us3r@%d-PASSW", i);

            User user = new User(login, "Nombre" + i, "Apellido" + i);
            user.setPassword(password);
            user.setRole("ROLE_EMPLOYEE");
            usersService.addUser(user);
            users.add(user);
        }
    }
}
