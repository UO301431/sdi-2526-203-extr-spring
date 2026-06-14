package com.uniovi.sdi.sdi2526extr203spring.entities;

import java.time.LocalDateTime;

//Clase creada para el requisito 12
//Representa los slots bloqueados al consultar disponibilidad
public class OccupiedSlot {
    private String type; //reserva o bloqueo
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reason;

    public OccupiedSlot(String type, LocalDateTime startDate, LocalDateTime endDate, String reason) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
