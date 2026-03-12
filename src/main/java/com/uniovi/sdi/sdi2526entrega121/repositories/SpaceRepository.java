package com.uniovi.sdi.sdi2526entrega121.repositories;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.entities.SpaceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    // All active spaces (for standard user listing)
    Page<Space> findByActiveTrue(Pageable pageable);

    // Active spaces with optional filters (type and/or min capacity)
    @Query("SELECT s FROM Space s WHERE s.active = true " +
            "AND (:type IS NULL OR s.type = :type) " +
            "AND (:minCapacity IS NULL OR s.capacity >= :minCapacity)")
    Page<Space> findActiveByFilters(@Param("type") SpaceType type,
                                    @Param("minCapacity") Integer minCapacity,
                                    Pageable pageable);

    // All spaces (for admin)
    Page<Space> findAll(Pageable pageable);

    // Check duplicate active name (excluding a given id for edits)
    @Query("SELECT s FROM Space s WHERE s.active = true AND s.name = :name AND (:excludeId IS NULL OR s.id <> :excludeId)")
    Optional<Space> findActiveByNameExcluding(@Param("name") String name,
                                              @Param("excludeId") Long excludeId);

    // All active spaces as list (for dropdowns)
    List<Space> findByActiveTrue();
}