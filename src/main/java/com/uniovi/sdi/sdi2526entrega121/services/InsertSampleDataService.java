package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.*;
import com.uniovi.sdi.sdi2526entrega121.repositories.MaintenanceBlockRepository;
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
    private final MaintenanceBlockRepository maintenanceBlockRepository;

    public InsertSampleDataService(UsersService usersService,
                                   SpaceRepository spaceRepository,
                                   ReservationRepository reservationRepository,
                                   MaintenanceBlockRepository maintenanceBlockRepository) {
        this.usersService = usersService;
        this.spaceRepository = spaceRepository;
        this.reservationRepository = reservationRepository;
        this.maintenanceBlockRepository = maintenanceBlockRepository;
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
        // 2. CREAR ESPACIOS (mínimo 6, al menos 1 desactivado)
        // ==========================================
        Space space1 = new Space("Sala Ada Lovelace", SpaceType.SALA, "Planta 1 - Edificio A", 10);
        Space space2 = new Space("Laboratorio Alan Turing", SpaceType.AULA, "Planta 2 - Edificio B", 25);
        Space space3 = new Space("Auditorio Grace Hopper", SpaceType.COWORK, "Planta Baja", 150);
        Space space4 = new Space("Despacho 404", SpaceType.COWORK, "Planta 4 - Edificio A", 4);
        Space space5 = new Space("Sala Linus Torvalds", SpaceType.SALA, "Planta 3 - Edificio B", 8);
        Space space6 = new Space("Sala Desactivada", SpaceType.AULA, "Planta 0 - Edificio C", 20);
        space6.setActive(false);

        spaceRepository.save(space1);
        spaceRepository.save(space2);
        spaceRepository.save(space3);
        spaceRepository.save(space4);
        spaceRepository.save(space5);
        spaceRepository.save(space6);

        // ==========================================
        // 3. CREAR RESERVAS
        // ==========================================
        // RESTRICCIONES:
        // PR24: space1 debe tener exactamente 3 reservas
        // PR25: solo 1 reserva en rango 2026-03-14 a 2026-03-18
        // PR34: user1 debe tener exactamente 5 reservas en total
        // PR35: user1 debe tener exactamente 1 CANCELADA
        // PR36: fila 2 de user1 (ordenada por fecha) debe ser ACTIVA y cancelable
        //       → res3 (cancelada) debe estar en fecha posterior a res7 para quedar en fila 5
        // PR44: user4 debe empezar con 0 reservas ACTIVAS
        // PR45: user5 debe empezar con exactamente 1 reserva ACTIVA
        // PR46: exactamente 9 reservas ACTIVAS en total
        // PR42: user2 debe tener margen para crear 4 más (tiene 3 activas, límite 8)

        // ── Usuario 1 (10000001S) — 5 total, 4 activas, 1 cancelada ──────────
        // Orden por fecha: res1(+1d) → res7(+4d) → res_space5_1(2027) → resTest21(2099) → res3(2030,CANC)
        // Fila 1: res1     ACTIVA   → cancelable ✓
        // Fila 2: res7     ACTIVA   → cancelable ✓  (PR36 clickea esta)
        // Fila 3: res_space5_1 ACTIVA
        // Fila 4: resTest21 ACTIVA
        // Fila 5: res3     CANCELADA → disabled (no importa, PR36 no la toca)

        Reservation res1 = new Reservation(users.get(1), space1,
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(12).withMinute(0),
                "Reunión de planificación de sprint");                         // ACTIVA - space1

        Reservation res7 = new Reservation(users.get(1), space4,
                LocalDateTime.now().plusDays(4).withHour(9).withMinute(0),
                LocalDateTime.now().plusDays(4).withHour(11).withMinute(0),
                "Revisión de código cruzada");                                  // ACTIVA

        Reservation res_space5_1 = new Reservation(users.get(1), space5,
                LocalDateTime.of(2027, 3, 15, 9, 0),
                LocalDateTime.of(2027, 3, 15, 11, 0),
                "Reunión de seguimiento de proyecto");                          // ACTIVA

        // Reserva fija para PR21 (bloqueo solapado con reserva activa en space1)
        Reservation resTest21 = new Reservation(users.get(1), space1,
                LocalDateTime.of(2099, 9, 15, 9, 0),
                LocalDateTime.of(2099, 9, 15, 18, 0),
                "Reserva fija para test de solapamiento");                      // ACTIVA - space1

        // CANCELADA en fecha lejana → siempre aparece en la fila 5 (última)
        // así PR36 nunca la toca al hacer click en la fila 2
        Reservation res3 = new Reservation(users.get(1), space3,
                LocalDateTime.of(2030, 6, 15, 9, 0),
                LocalDateTime.of(2030, 6, 15, 14, 0),
                "Conferencia de ciberseguridad (Suspendida)");
        res3.setStatus(ReservationStatus.CANCELLED);                            // CANCELADA

        // user1: res1(A,s1) + res7(A) + res_space5_1(A) + resTest21(A,s1) + res3(C) = 5 ✓
        // space1: res1 + resTest21 = 2 activas hasta aquí

        // ── Usuario 2 (10000002Q) — 5 total, 3 activas, 2 canceladas ─────────
        // PR42: 3 activas → puede crear 5 más sin superar límite de 8

        Reservation res2 = new Reservation(users.get(2), space2,
                LocalDateTime.now().minusDays(3).withHour(16).withMinute(0),
                LocalDateTime.now().minusDays(3).withHour(18).withMinute(30),
                "Prácticas de Sistemas Distribuidos");                          // ACTIVA - cae en PR25

        Reservation res8 = new Reservation(users.get(2), space3,
                LocalDateTime.now().plusDays(10).withHour(16).withMinute(0),
                LocalDateTime.now().plusDays(10).withHour(20).withMinute(0),
                "Presentación de proyectos finales");                           // ACTIVA

        Reservation res_space5_2 = new Reservation(users.get(2), space5,
                LocalDateTime.of(2027, 5, 15, 12, 0),
                LocalDateTime.of(2027, 5, 15, 14, 0),
                "Revisión de código");                                          // ACTIVA

        // CANCELADA en space1 → completa las 3 reservas de space1 para PR24
        Reservation res_u2_c1 = new Reservation(users.get(2), space1,
                LocalDateTime.of(2040, 3, 10, 9, 0),
                LocalDateTime.of(2040, 3, 10, 11, 0),
                "Reunión cancelada usuario 2");
        res_u2_c1.setStatus(ReservationStatus.CANCELLED);                      // CANCELADA - space1
        // space1: res1 + resTest21 + res_u2_c1 = 3 ✓ (PR24)

        Reservation res_u2_c2 = new Reservation(users.get(2), space4,
                LocalDateTime.of(2040, 4, 15, 10, 0),
                LocalDateTime.of(2040, 4, 15, 12, 0),
                "Formación cancelada usuario 2");
        res_u2_c2.setStatus(ReservationStatus.CANCELLED);                      // CANCELADA

        // user2: res2(A) + res8(A) + res_space5_2(A) + res_u2_c1(C) + res_u2_c2(C) = 5 ✓

        // ── Usuario 3 (10000003V) — 5 total, 1 activa, 4 canceladas ──────────

        Reservation res4 = new Reservation(users.get(3), space4,
                LocalDateTime.now().plusDays(2).withHour(11).withMinute(0),
                LocalDateTime.now().plusDays(2).withHour(13).withMinute(0),
                "Entrevista con cliente");                                       // ACTIVA

        Reservation res_u3_c1 = new Reservation(users.get(3), space2,
                LocalDateTime.of(2040, 5, 10, 9, 0),
                LocalDateTime.of(2040, 5, 10, 11, 0),
                "Taller cancelado usuario 3 - 1");
        res_u3_c1.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u3_c2 = new Reservation(users.get(3), space3,
                LocalDateTime.of(2040, 5, 15, 14, 0),
                LocalDateTime.of(2040, 5, 15, 16, 0),
                "Sesión cancelada usuario 3 - 2");
        res_u3_c2.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u3_c3 = new Reservation(users.get(3), space5,
                LocalDateTime.of(2040, 6, 10, 10, 0),
                LocalDateTime.of(2040, 6, 10, 12, 0),
                "Reunión cancelada usuario 3 - 3");
        res_u3_c3.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u3_c4 = new Reservation(users.get(3), space2,
                LocalDateTime.of(2040, 7, 20, 9, 0),
                LocalDateTime.of(2040, 7, 20, 11, 0),
                "Formación cancelada usuario 3 - 4");
        res_u3_c4.setStatus(ReservationStatus.CANCELLED);

        // user3: res4(A) + res_u3_c1(C) + res_u3_c2(C) + res_u3_c3(C) + res_u3_c4(C) = 5 ✓

        // ── Usuario 4 (10000004H) — 5 total, 0 activas, 5 canceladas ─────────
        // PR44: necesita 0 activas para poder crear 8 hasta el límite

        Reservation res5 = new Reservation(users.get(4), space1,
                LocalDateTime.now().minusDays(5).withHour(10).withMinute(0),
                LocalDateTime.now().minusDays(5).withHour(11).withMinute(30),
                "Reunión de seguimiento (Cancelada por enfermedad)");
        res5.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u4_c1 = new Reservation(users.get(4), space2,
                LocalDateTime.of(2040, 8, 10, 9, 0),
                LocalDateTime.of(2040, 8, 10, 11, 0),
                "Taller cancelado usuario 4 - 1");
        res_u4_c1.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u4_c2 = new Reservation(users.get(4), space3,
                LocalDateTime.of(2040, 8, 15, 14, 0),
                LocalDateTime.of(2040, 8, 15, 16, 0),
                "Sesión cancelada usuario 4 - 2");
        res_u4_c2.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u4_c3 = new Reservation(users.get(4), space5,
                LocalDateTime.of(2040, 9, 10, 10, 0),
                LocalDateTime.of(2040, 9, 10, 12, 0),
                "Reunión cancelada usuario 4 - 3");
        res_u4_c3.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u4_c4 = new Reservation(users.get(4), space4,
                LocalDateTime.of(2040, 9, 20, 9, 0),
                LocalDateTime.of(2040, 9, 20, 11, 0),
                "Formación cancelada usuario 4 - 4");
        res_u4_c4.setStatus(ReservationStatus.CANCELLED);

        // user4: res5(C) + res_u4_c1(C) + res_u4_c2(C) + res_u4_c3(C) + res_u4_c4(C) = 5 ✓
        // Activas = 0 ✓ (PR44)

        // ── Usuario 5 (10000005L) — 5 total, 1 activa, 4 canceladas ──────────
        // PR45: necesita exactamente 1 activa para poder crear 7 más hasta el límite

        Reservation res6 = new Reservation(users.get(5), space2,
                LocalDateTime.now().plusDays(3).withHour(15).withMinute(0),
                LocalDateTime.now().plusDays(3).withHour(17).withMinute(0),
                "Taller de Inteligencia Artificial");                           // ACTIVA

        Reservation res_u5_c1 = new Reservation(users.get(5), space3,
                LocalDateTime.of(2040, 10, 10, 9, 0),
                LocalDateTime.of(2040, 10, 10, 11, 0),
                "Conferencia cancelada usuario 5 - 1");
        res_u5_c1.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u5_c2 = new Reservation(users.get(5), space4,
                LocalDateTime.of(2040, 10, 15, 14, 0),
                LocalDateTime.of(2040, 10, 15, 16, 0),
                "Sesión cancelada usuario 5 - 2");
        res_u5_c2.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u5_c3 = new Reservation(users.get(5), space5,
                LocalDateTime.of(2040, 11, 10, 10, 0),
                LocalDateTime.of(2040, 11, 10, 12, 0),
                "Reunión cancelada usuario 5 - 3");
        res_u5_c3.setStatus(ReservationStatus.CANCELLED);

        Reservation res_u5_c4 = new Reservation(users.get(5), space2,
                LocalDateTime.of(2040, 11, 20, 9, 0),
                LocalDateTime.of(2040, 11, 20, 11, 0),
                "Taller cancelado usuario 5 - 4");
        res_u5_c4.setStatus(ReservationStatus.CANCELLED);

        // user5: res6(A) + res_u5_c1(C) + res_u5_c2(C) + res_u5_c3(C) + res_u5_c4(C) = 5 ✓
        // Activas = 1 ✓ (PR45)

        // ── Guardar todas las reservas ────────────────────────────────────────
        // Activas totales: res1+res2+res4+res6+res7+res8+res_space5_1+res_space5_2+resTest21 = 9 ✓ (PR46)
        // Space1 total: res1(A) + resTest21(A) + res_u2_c1(C) = 3 ✓ (PR24)
        // PR25: solo res2 (now-3 = 15 marzo) cae en rango 14-18 marzo = 1 ✓

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
        reservationRepository.save(resTest21);
        reservationRepository.save(res_u2_c1);
        reservationRepository.save(res_u2_c2);
        reservationRepository.save(res_u3_c1);
        reservationRepository.save(res_u3_c2);
        reservationRepository.save(res_u3_c3);
        reservationRepository.save(res_u3_c4);
        reservationRepository.save(res_u4_c1);
        reservationRepository.save(res_u4_c2);
        reservationRepository.save(res_u4_c3);
        reservationRepository.save(res_u4_c4);
        reservationRepository.save(res_u5_c1);
        reservationRepository.save(res_u5_c2);
        reservationRepository.save(res_u5_c3);
        reservationRepository.save(res_u5_c4);

        // ==========================================
        // 4. CREAR BLOQUEOS
        // ==========================================

        // Bloqueos en space5 para PR29 (disponibilidad) y PR43 (recurrente con solape)
        MaintenanceBlock block_space5_1 = new MaintenanceBlock(space5,
                LocalDateTime.of(2027, 4, 10, 8, 0),
                LocalDateTime.of(2027, 4, 10, 18, 0),
                "Mantenimiento de instalaciones");

        MaintenanceBlock block_space5_2 = new MaintenanceBlock(space5,
                LocalDateTime.of(2027, 7, 20, 9, 0),
                LocalDateTime.of(2027, 7, 20, 17, 0),
                "Reservado para evento corporativo");

        MaintenanceBlock block_space5_3 = new MaintenanceBlock(space5,
                LocalDateTime.of(2028, 7, 20, 9, 0),
                LocalDateTime.of(2028, 7, 20, 17, 0),
                "Reservado para evento educativo");

        // Bloqueo en space1 para PR33 (reservar dentro de bloqueo debe fallar)
        MaintenanceBlock block_space1_test_33 = new MaintenanceBlock(space1,
                LocalDateTime.of(2029, 4, 10, 8, 0),
                LocalDateTime.of(2029, 4, 10, 18, 0),
                "Mantenimiento de instalaciones");

        maintenanceBlockRepository.save(block_space5_1);
        maintenanceBlockRepository.save(block_space5_2);
        maintenanceBlockRepository.save(block_space5_3);
        maintenanceBlockRepository.save(block_space1_test_33);
    }
}