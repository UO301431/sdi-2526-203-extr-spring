package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.RecurrenceFrequency;
import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import com.uniovi.sdi.sdi2526entrega121.entities.User;
import com.uniovi.sdi.sdi2526entrega121.services.ReservationsService;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import com.uniovi.sdi.sdi2526entrega121.services.UsersService;
import com.uniovi.sdi.sdi2526entrega121.validators.AddReservationValidator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador encargado de gestionar las peticiones web relacionadas con las reservas.
 * Permite listar, filtrar, añadir, cancelar y exportar reservas a CSV.
 */
@Controller
public class ReservationsController {

    @Autowired
    private ReservationsService reservationsService;

    @Autowired
    private SpaceService spaceService;

    private final UsersService usersService;
    private final AddReservationValidator addReservationValidator;

    /**
     * Constructor del controlador que inyecta el servicio de usuarios.
     *
     * @param usersService Servicio para la gestión de usuarios.
     */
    public ReservationsController(UsersService usersService, AddReservationValidator addReservationValidator) {
        this.usersService = usersService;
        this.addReservationValidator = addReservationValidator;
    }

    /**
     * Obtiene y muestra la lista de reservas paginada y filtrada.
     *
     * @param model     Modelo de Spring para pasar datos a la vista.
     * @param pageable  Objeto que contiene la información de paginación.
     * @param principal Objeto que representa al usuario autenticado actual.
     * @param spaceId   (Opcional) ID del espacio para filtrar.
     * @param status    (Opcional) Estado de la reserva para filtrar.
     * @param startDate (Opcional) Fecha de inicio para filtrar.
     * @param endDate   (Opcional) Fecha de fin para filtrar.
     * @return El nombre de la vista de listado de reservas ("reservation/list").
     */
    @RequestMapping(value = "/reservations/list")
    public String getReservations(Model model, Pageable pageable, Principal principal,
                                  @RequestParam(value = "spaceId", required = false) Long spaceId,
                                  @RequestParam(value = "status", required = false) ReservationStatus status,
                                  @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String dni = principal.getName();

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        Page<Reservation> reservations = reservationsService.getFilteredReservations(dni, status, spaceId, start, end, pageable);

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("page", reservations);
        model.addAttribute("spaces", spaceService.getSpaces(pageable));
        model.addAttribute("statuses", ReservationStatus.values());

        return "reservation/list";
    }

    /**
     * Actualiza asíncronamente (AJAX) la tabla de reservas aplicando los filtros y la paginación.
     * Devuelve únicamente el fragmento HTML correspondiente a la tabla.
     *
     * @param model     Modelo de Spring para pasar datos a la vista.
     * @param pageable  Objeto que contiene la información de paginación.
     * @param principal Objeto que representa al usuario autenticado actual.
     * @param spaceId   (Opcional) ID del espacio para filtrar.
     * @param status    (Opcional) Estado de la reserva para filtrar.
     * @param startDate (Opcional) Fecha de inicio para filtrar.
     * @param endDate   (Opcional) Fecha de fin para filtrar.
     * @return El fragmento Thymeleaf de la tabla de reservas ("reservation/list :: tableReservation").
     */
    @RequestMapping(value = "/reservations/update")
    public String updateReservations(Model model, Pageable pageable, Principal principal,
                                     @RequestParam(value = "spaceId", required = false) Long spaceId,
                                     @RequestParam(value = "status", required = false) ReservationStatus status,
                                     @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String dni = principal.getName();

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        Page<Reservation> reservations = reservationsService.getFilteredReservations(dni, status, spaceId, start, end, pageable);

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("page", reservations);

        return "reservation/list :: tableReservation";
    }

    /**
     * Cancela una reserva existente verificando que pertenece al usuario autenticado.
     *
     * @param id        ID de la reserva que se desea cancelar.
     * @param principal Objeto que representa al usuario autenticado actual.
     * @return Redirección a la vista del listado de reservas.
     */
    @RequestMapping(value = "/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, Principal principal) {
        String dni = principal.getName();

        reservationsService.cancelReservation(id, dni);

        return "redirect:/reservations/list";
    }

    /**
     * Muestra la vista con el formulario para añadir una nueva reserva.
     * Carga los espacios activos disponibles para reservar.
     *
     * @param model Modelo de Spring para pasar datos a la vista.
     * @return El nombre de la vista de creación de reservas ("reservation/add").
     */
    @GetMapping("/reservations/add")
    public String getReservation(Model model){
        model.addAttribute("activeSpaces", spaceService.getActiveSpaces());
        model.addAttribute("reservation", new Reservation());
        return "reservation/add";
    }

    /**
     * Procesa el formulario para añadir una nueva reserva.
     * Asocia la reserva al usuario autenticado y la guarda en el sistema.
     *
     * @param reservation Objeto reserva rellenado desde el formulario.
     * @param principal   Objeto que representa al usuario autenticado actual.
     * @return El nombre de la vista ("reservation/add").
     */
    @PostMapping("/reservations/add")
    public String setReservation(@ModelAttribute Reservation reservation,
                                 Principal principal,
                                 BindingResult result,
                                 Model model,
                                 @RequestParam(value = "recurrenceFrequency", required = false) RecurrenceFrequency recurrenceFrequency,
                                 @RequestParam(value = "recurrenceEndDate", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recurrenceEndDate) {

        model.addAttribute("activeSpaces", spaceService.getActiveSpaces());

        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        reservation.setUser(user);

        // Validación básica de fechas (la que ya existía)
        addReservationValidator.validate(reservation, result);
        if(result.hasErrors()){
            return "reservation/add";
        }

        // Sin recurrencia: flujo normal ya existente
        if (recurrenceFrequency == null || recurrenceEndDate == null) {
            reservationsService.addReservation(reservation);
            model.addAttribute("successMessage", "reservation.add.success");
            return "reservation/add";
        }

        // Validar que la fecha fin de recurrencia sea posterior al inicio
        if (recurrenceEndDate.isBefore(reservation.getStartDate().toLocalDate())) {
            model.addAttribute("errorMessage", "reservation.recurrence.endDateBeforeStart");
            return "reservation/add";
        }

        boolean created = reservationsService.createRecurringReservations(
                reservation, recurrenceFrequency, recurrenceEndDate);

        if (!created) {
            model.addAttribute("errorMessage", "reservation.recurrence.overlap");
        } else {
            model.addAttribute("successMessage", "reservation.recurrence.success");
        }

        return "reservation/add";
    }

    /**
     * Exporta el listado de reservas filtrado a un archivo CSV descargable.
     * Se genera aplicando los mismos filtros que el listado visual pero sin paginación.
     *
     * @param response  Objeto HTTP Response usado para configurar la cabecera y escribir el archivo CSV.
     * @param principal Objeto que representa al usuario autenticado actual.
     * @param spaceId   (Opcional) ID del espacio para filtrar.
     * @param status    (Opcional) Estado de la reserva para filtrar.
     * @param startDate (Opcional) Fecha de inicio para filtrar.
     * @param endDate   (Opcional) Fecha de fin para filtrar.
     * @throws Exception Si ocurre un error al escribir la respuesta HTTP.
     */
    @RequestMapping(value = "/reservations/export")
    public void exportReservationsToCSV(HttpServletResponse response, Principal principal,
                                        @RequestParam(required = false) Long spaceId,
                                        @RequestParam(required = false) ReservationStatus status,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                        LocalDate startDate,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                        LocalDate endDate) throws Exception {

        String userLoggedDni = principal.getName();

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        List<Reservation> reservations = reservationsService.getFilteredReservations(userLoggedDni,
                status, spaceId, start, end, Pageable.unpaged()).getContent();

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"reservas.csv\"");

        PrintWriter writer = response.getWriter();
        writer.write('\ufeff');

        writer.println("Espacio;Nombre;DNI;Inicio;Fin;Estado");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Reservation reservation : reservations) {
            writer.printf("%s;%s;%s;%s;%s;%s%n",
                    reservation.getSpace().getName(),
                    reservation.getUser().getName(),
                    reservation.getUser().getDni(),
                    reservation.getStartDate().format(formatter),
                    reservation.getEndDate().format(formatter),
                    reservation.getStatus());
        }

        writer.flush();
        writer.close();
    }
}