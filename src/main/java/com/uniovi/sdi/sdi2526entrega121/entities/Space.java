package com.uniovi.sdi.sdi2526entrega121.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "spaces")
public class Space {

    @Id
    @GeneratedValue
    private Long id;

    //el enunciado dice que no pueden existir dos espacios activos con el mismo nombre, no que el nombre sea globalmente único en BD. Si lo dejas así, no podrías reutilizar el nombre de un espacio desactivado. La unicidad se controla en el servicio
    private String name;

    private SpaceType type;

    private String location;

    private Integer capacity;

    private String description;

    private Boolean active = true; // Baja lógica

    // Relaciones según el diagrama
    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private Set<MaintenanceBlock> maintenanceBlocks = new HashSet<>();


    // Constructores
    public Space() {}

    public Space(String name, SpaceType type, String location, Integer capacity) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.active = true;
    }

    public Set<MaintenanceBlock> getMaintenanceBlocks() { return maintenanceBlocks; }
    public void setMaintenanceBlocks(Set<MaintenanceBlock> maintenanceBlocks) { this.maintenanceBlocks = maintenanceBlocks; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SpaceType getType() { return type; }
    public void setType(SpaceType type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean isActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Set<Reservation> getReservations() { return reservations; }
    public void setReservations(Set<Reservation> reservations) { this.reservations = reservations; }
}