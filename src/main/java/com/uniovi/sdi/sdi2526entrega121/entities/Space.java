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

    @Column(unique = true) // Requisito 127
    private String name;

    private String type;

    private String location;

    private Integer capacity;

    private Boolean active = true; // Baja lógica

    // Relaciones según el diagrama
    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<>();

    // Constructores
    public Space() {}

    public Space(String name, String type, String location, Integer capacity) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.active = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Set<Reservation> getReservations() { return reservations; }
    public void setReservations(Set<Reservation> reservations) { this.reservations = reservations; }
}