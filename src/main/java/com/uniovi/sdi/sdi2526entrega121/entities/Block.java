package com.uniovi.sdi.sdi2526entrega121.entities;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocks")
public class Block {

    @Id
    @GeneratedValue
    private Long id;

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
    private BlockStatus status = BlockStatus.ACTIVE;

    public Block() {
    }

    public Block(Long id, BlockStatus status, Space space, LocalDateTime startDate, LocalDateTime endDate, String reason) {
        this.id = id;
        this.status = status;
        this.space = space;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }

    public Block(Space space, LocalDateTime startDate, LocalDateTime endDate, String reason, BlockStatus status) {
        this.space = space;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
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

    public BlockStatus getStatus() {
        return status;
    }

    public void setStatus(BlockStatus status) {
        this.status = status;
    }
}
