package com.uniovi.sdi.sdi2526entrega121.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_block")
public class MaintenanceBlock {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reason;

    @Enumerated(EnumType.STRING)
    private BlockStatus status;

    public MaintenanceBlock() {}

    public MaintenanceBlock(Space space, LocalDateTime startDate, LocalDateTime endDate, String reason) {
        this.space = space;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = BlockStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Space getSpace() { return space; }
    public void setSpace(Space space) { this.space = space; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public BlockStatus getStatus() { return status; }
    public void setStatus(BlockStatus status) { this.status = status; }
}