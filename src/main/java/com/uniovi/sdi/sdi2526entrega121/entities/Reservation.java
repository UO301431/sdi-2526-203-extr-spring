package com.uniovi.sdi.sdi2526entrega121.entities;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dni", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @Column(nullable = true)
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.ACTIVE;

    private Boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private RecurrenceFrequency recurrenceFrequency;

    @Column(nullable = true)
    private LocalDate recurrenceEndDate;

    @Column(nullable = true)
    private Long recurrenceGroupId;

    public Reservation() {}

    public Reservation(User user, Space space, LocalDateTime startDate,
                       LocalDateTime endDate, String reason) {
        this.user = user;
        this.space = space;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = ReservationStatus.ACTIVE;
        this.isRecurring = false;
    }

    public boolean isPeriodValid() {
        if (startDate == null || endDate == null) return false;
        return startDate.isBefore(endDate);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Space getSpace() { return space; }
    public void setSpace(Space space) { this.space = space; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean recurring) { isRecurring = recurring; }

    public RecurrenceFrequency getRecurrenceFrequency() { return recurrenceFrequency; }
    public void setRecurrenceFrequency(RecurrenceFrequency recurrenceFrequency) { this.recurrenceFrequency = recurrenceFrequency; }

    public LocalDate getRecurrenceEndDate() { return recurrenceEndDate; }
    public void setRecurrenceEndDate(LocalDate recurrenceEndDate) { this.recurrenceEndDate = recurrenceEndDate; }

    public Long getRecurrenceGroupId() { return recurrenceGroupId; }
    public void setRecurrenceGroupId(Long recurrenceGroupId) { this.recurrenceGroupId = recurrenceGroupId; }
}