package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.entities.SpaceType;
import com.uniovi.sdi.sdi2526entrega121.repositories.SpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    private static final Logger log = LoggerFactory.getLogger(SpaceService.class);

    @Autowired
    private SpaceRepository spaceRepository;


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
        if (!existing.isEmpty()) {

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
}