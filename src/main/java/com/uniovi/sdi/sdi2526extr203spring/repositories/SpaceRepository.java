package com.uniovi.sdi.sdi2526extr203spring.repositories;

import com.uniovi.sdi.sdi2526extr203spring.entities.Space;
import com.uniovi.sdi.sdi2526extr203spring.entities.SpaceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    Page<Space> findByActiveTrue(Pageable pageable);

    @Query("SELECT s FROM Space s WHERE s.active = true " +
            "AND (:type IS NULL OR s.type = :type) " +
            "AND (:minCapacity IS NULL OR s.capacity >= :minCapacity)")
    Page<Space> findActiveByFilters(@Param("type") SpaceType type,
                                    @Param("minCapacity") Integer minCapacity,
                                    Pageable pageable);

    Page<Space> findAll(Pageable pageable);

    @Query("SELECT s FROM Space s WHERE s.active = true AND s.name = :name AND (:excludeId IS NULL OR s.id <> :excludeId)")
    Optional<Space> findActiveByNameExcluding(@Param("name") String name,
                                              @Param("excludeId") Long excludeId);

    List<Space> findByActiveTrue();

}