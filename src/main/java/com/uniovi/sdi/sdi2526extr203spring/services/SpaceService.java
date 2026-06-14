package com.uniovi.sdi.sdi2526extr203spring.services;

import com.uniovi.sdi.sdi2526extr203spring.entities.OccupiedSlot;
import com.uniovi.sdi.sdi2526extr203spring.entities.Space;
import com.uniovi.sdi.sdi2526extr203spring.entities.SpaceType;
import com.uniovi.sdi.sdi2526extr203spring.repositories.MaintenanceBlockRepository;
import com.uniovi.sdi.sdi2526extr203spring.repositories.ReservationRepository;
import com.uniovi.sdi.sdi2526extr203spring.repositories.SpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    private static final Logger log = LoggerFactory.getLogger(SpaceService.class);

    @Autowired
    private SpaceRepository spaceRepository;

    private final MaintenanceBlockRepository maintenanceBlockRepository;
    private final ReservationRepository reservationRepository;

    public SpaceService(MaintenanceBlockRepository maintenanceBlockRepository, ReservationRepository reservationRepository) {
        this.maintenanceBlockRepository = maintenanceBlockRepository;
        this.reservationRepository = reservationRepository;
    }

    public Page<Space> getSpaces(Pageable pageable) {
        return spaceRepository.findAll(pageable);
    }

    // ── Standard user: active spaces with optional filters ───────────────────

    public Page<Space> getActiveSpaces(Pageable pageable) {
        return spaceRepository.findByActiveTrue(pageable);
    }

    public Page<Space> getActiveSpacesFiltered(SpaceType type, Integer minCapacity, Pageable pageable) {
        return spaceRepository.findActiveByFilters(type, minCapacity, pageable);
    }

    // ── Dropdowns ────────────────────────────────────────────────────────────

    public List<Space> getAllActiveSpaces() {
        return spaceRepository.findByActiveTrue();
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    public Optional<Space> findById(Long id) {
        return spaceRepository.findById(id);
    }


    public String addSpace(Space space) {
        // Validar nombre no vacío
        if (space.getName() == null || space.getName().trim().isEmpty()) {
            return "space.error.name.empty";
        }
        // Validar capacidad
        if (space.getCapacity() == null || space.getCapacity() < 1) {
            return "space.error.capacity.invalid";
        }
        // Validar nombre duplicado entre espacios activos
        Optional<Space> existing = spaceRepository.findActiveByNameExcluding(
                space.getName().trim(), -1L
        );
        if (existing.isPresent()) {

            return "space.error.name.duplicate";

        }

        spaceRepository.save(space);
        return null;
    }


    public String editSpace(Long id, Space updated) {
        Optional<Space> opt = spaceRepository.findById(id);
        if (opt.isEmpty()) {
            return "space.error.notfound";
        }
        if (updated.getName() == null || updated.getName().trim().isEmpty()) {
            return "space.error.name.empty";
        }
        if (updated.getCapacity() < 1) {

            return "space.error.capacity.invalid";

        }
        if (spaceRepository.findActiveByNameExcluding(updated.getName().trim(), id).isPresent()) {
            return "space.error.name.duplicate";
        }
        Space space = opt.get();
        space.setName(updated.getName().trim());
        space.setType(updated.getType());
        space.setLocation(updated.getLocation());
        space.setCapacity(updated.getCapacity());
        space.setDescription(updated.getDescription());
        spaceRepository.save(space);
        log.info("Space edited: id={}, name={}", id, space.getName());
        return null;
    }


    public boolean toggleActive(Long id) {
        Optional<Space> opt = spaceRepository.findById(id);
        if (opt.isEmpty()) return false;
        Space space = opt.get();
        space.setActive(!space.isActive());
        spaceRepository.save(space);
        log.info("Space toggled: id={}, active={}", id, space.isActive());
        return true;
    }

    //Obtiene las fechas ocupadas para un espacio para un rango de fechas
    //tambien funciona como logiva para eviatr solapamientos (REQUISITO 14)
    //      si la lista que devuelve esta vacia no hay solapamientos
    //      si existe al menos un slot ocupado existe solapamiento
    public List<OccupiedSlot> getAvailabilityForSpace(Long spaceId, LocalDateTime startDate,
                                                      LocalDateTime endDate){
        List<OccupiedSlot> occupiedSlots = new ArrayList<>();
        //se añden los "choques" con reservas tras mapear las a OccupiedSlot
        reservationRepository.findActiveReservationsInRange(spaceId, startDate, endDate)
                .stream()
                .map(r -> new OccupiedSlot("RESERVA", r.getStartDate(), r.getEndDate(), r.getReason()))
                .forEach(occupiedSlots::add);

        //se añden los "choques" con bloqueos tras mapear las a OccupiedSlot
        maintenanceBlockRepository.findOverlappingActiveBlocks(spaceId, startDate, endDate, null)
        //blockRepository.findActiveBlocksInRange(spaceId, startDate, endDate)
                .stream()
                .map(b -> new OccupiedSlot("BLOQUEO", b.getStartDate(), b.getEndDate(), b.getReason()))
                .forEach(occupiedSlots::add);
        //se ordenan los "choques" cpor fecha de inicio
        occupiedSlots.sort(Comparator.comparing(OccupiedSlot::getStartDate));

        return occupiedSlots;
    }

    //sobrecarga de metodo que devuelve la lista sin paginacion
    public List<Space> getActiveSpaces(){
        return spaceRepository.findByActiveTrue();
    }

    public Space getSpace(Long id) {
        return spaceRepository.findById(id).isPresent()
                ? spaceRepository.findById(id).get() : new Space();
    }
}