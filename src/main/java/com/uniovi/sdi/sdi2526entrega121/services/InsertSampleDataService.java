package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.*;
import com.uniovi.sdi.sdi2526entrega121.entities.*;
import com.uniovi.sdi.sdi2526entrega121.repositories.BlockRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.ReservationRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.SpaceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class InsertSampleDataService {

    private final UsersService usersService;
    private final SpaceRepository spaceRepository;
    private final ReservationRepository reservationRepository;
    private final BlockRepository blockRepository;

    public InsertSampleDataService(UsersService usersService,
                                   SpaceRepository spaceRepository,
                                   ReservationRepository reservationRepository, BlockRepository blockRepository) {
        this.usersService = usersService;
        this.spaceRepository = spaceRepository;
        this.reservationRepository = reservationRepository;
        this.blockRepository = blockRepository;
    }

    @PostConstruct
    public void init() {
        List<User> users = new ArrayList<>();

        // ==========================================
        // 1. CREAR USUARIOS
        // ==========================================
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

        // ==========================================
        // 2. CREAR ESPACIOS
        // ==========================================
        Space space1 = new Space("Sala Ada Lovelace", SpaceType.SALA, "Planta 1 - Edificio A", 10);
        Space space2 = new Space("Laboratorio Alan Turing", SpaceType.AULA, "Planta 2 - Edificio B", 25);
        Space space3 = new Space("Auditorio Grace Hopper", SpaceType.COWORK, "Planta Baja", 150);
        Space space4 = new Space("Despacho 404", SpaceType.COWORK, "Planta 4 - Edificio A", 4);
        Space space5 = new Space("Sala Linus Torvalds", SpaceType.SALA, "Planta 3 - Edificio B", 8);

        spaceRepository.save(space1);
        spaceRepository.save(space2);
        spaceRepository.save(space3);
        spaceRepository.save(space4);
        spaceRepository.save(space5);

        // ==========================================
        // 3. CREAR RESERVAS
        // ==========================================

        // Reserva 1: Futura, Activa (Usuario 1)
        Reservation res1 = new Reservation(users.get(1), space1,
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(12).withMinute(0),
                "Reunión de planificación de sprint");

        // Reserva 2: Pasada, Activa (Usuario 2)
        Reservation res2 = new Reservation(users.get(2), space2,
                LocalDateTime.now().minusDays(3).withHour(16).withMinute(0),
                LocalDateTime.now().minusDays(3).withHour(18).withMinute(30),
                "Prácticas de Sistemas Distribuidos");

        // Reserva 3: Futura, Cancelada (Usuario 1)
        Reservation res3 = new Reservation(users.get(1), space3,
                LocalDateTime.now().plusDays(5).withHour(9).withMinute(0),
                LocalDateTime.now().plusDays(5).withHour(14).withMinute(0),
                "Conferencia de ciberseguridad (Suspendida)");
        res3.setStatus(ReservationStatus.CANCELLED);

        // Reserva 4: Futura, Activa (Usuario 3)
        Reservation res4 = new Reservation(users.get(3), space4,
                LocalDateTime.now().plusDays(2).withHour(11).withMinute(0),
                LocalDateTime.now().plusDays(2).withHour(13).withMinute(0),
                "Entrevista con cliente");

        // --- NUEVAS RESERVAS ---

        // Reserva 5: Pasada, Cancelada (Usuario 4)
        Reservation res5 = new Reservation(users.get(4), space1,
                LocalDateTime.now().minusDays(5).withHour(10).withMinute(0),
                LocalDateTime.now().minusDays(5).withHour(11).withMinute(30),
                "Reunión de seguimiento (Cancelada por enfermedad)");
        res5.setStatus(ReservationStatus.CANCELLED);

        // Reserva 6: Futura, Activa (Usuario 5)
        Reservation res6 = new Reservation(users.get(5), space2,
                LocalDateTime.now().plusDays(3).withHour(15).withMinute(0),
                LocalDateTime.now().plusDays(3).withHour(17).withMinute(0),
                "Taller de Inteligencia Artificial");

        // Reserva 7: Futura, Activa (Usuario 1 repite, para probar filtros por usuario)
        Reservation res7 = new Reservation(users.get(1), space4,
                LocalDateTime.now().plusDays(4).withHour(9).withMinute(0),
                LocalDateTime.now().plusDays(4).withHour(11).withMinute(0),
                "Revisión de código cruzada");

        // Reserva 8: Futura (Más lejana), Activa (Usuario 2)
        Reservation res8 = new Reservation(users.get(2), space3,
                LocalDateTime.now().plusDays(10).withHour(16).withMinute(0),
                LocalDateTime.now().plusDays(10).withHour(20).withMinute(0),
                "Presentación de proyectos finales");

        // Reserva 9: Futura 2027, Activa (Usuario 1)
        Reservation res_space5_1 = new Reservation(users.get(1), space5,
                LocalDateTime.of(2027, 3, 15, 9, 0),
                LocalDateTime.of(2027, 3, 15, 11, 0),
                "Reunión de seguimiento de proyecto");

        // Reserva 10: Futura 2027, Activa (Usuario 2)
        Reservation res_space5_2 = new Reservation(users.get(2), space5,
                LocalDateTime.of(2027, 5, 15, 12, 0),
                LocalDateTime.of(2027, 5, 15, 14, 0),
                "Revisión de código");


        Reservation resTest21 = new Reservation(users.get(1), space1,
                LocalDateTime.of(2099, 9, 15, 9, 0),
                LocalDateTime.of(2099, 9, 15, 18, 0),
                "Reserva fija para test de solapamiento");
        reservationRepository.save(resTest21);

        // Guardar todas las reservas
        reservationRepository.save(res1);
        reservationRepository.save(res2);
        reservationRepository.save(res3);
        reservationRepository.save(res4);
        reservationRepository.save(res5);
        reservationRepository.save(res6);
        reservationRepository.save(res7);
        reservationRepository.save(res8);
        reservationRepository.save(res_space5_1);
        reservationRepository.save(res_space5_2);

        // ==========================================
        // 3. CREAR BLOQUEOS
        // ==========================================
        // Bloqueo 1: Mantenimiento programado
        Block block_space5_1 = new Block(space5,
                LocalDateTime.of(2027, 4, 10, 8, 0),
                LocalDateTime.of(2027, 4, 10, 18, 0),
                "Mantenimiento de instalaciones",
                BlockStatus.ACTIVE);

        // Bloqueo 2: Evento especial
        Block block_space5_2 = new Block(space5,
                LocalDateTime.of(2027, 7, 20, 9, 0),
                LocalDateTime.of(2027, 7, 20, 17, 0),
                "Reservado para evento corporativo",
                BlockStatus.ACTIVE);

        // Bloqueo 3: Fuera del rango
        Block block_space5_3 = new Block(space5,
                LocalDateTime.of(2028, 7, 20, 9, 0),
                LocalDateTime.of(2028, 7, 20, 17, 0),
                "Reservado para evento educativo",
                BlockStatus.ACTIVE);

        // Bloqueo para prueba
        Block block_space1_test_33 = new Block(space1,
                LocalDateTime.of(2029, 4, 10, 8, 0),
                LocalDateTime.of(2029, 4, 10, 18, 0),
                "Mantenimiento de instalaciones",
                BlockStatus.ACTIVE);

        // Guardar todas los bloqueos
        blockRepository.save(block_space5_1);
        blockRepository.save(block_space5_2);
        blockRepository.save(block_space5_3);
        blockRepository.save(block_space1_test_33);
    }
}
