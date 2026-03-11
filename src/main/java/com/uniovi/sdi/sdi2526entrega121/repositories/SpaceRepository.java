package com.uniovi.sdi.sdi2526entrega121.repositories;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SpaceRepository extends CrudRepository<Space, Long> {

    Page<Space> findAll(Pageable pageable);

    @Query("SELECT sp from Space sp where sp.active = true")
    Page<Space> findActiveSpaces(Pageable pageable);

    @Query("SELECT sp from Space sp where sp.active = true and (lower(sp.type) like lower(?1))")
    Page<Space> findActiveSpacesByType(Pageable pageable, String searchType);

    @Query("SELECT sp from Space sp where sp.active = true and (sp.capacity >= ?1)")
    Page<Space> findActiveSpacesByCapacity(Pageable pageable, Integer searchCapacity);

    @Query("SELECT sp from Space sp where sp.active = true " +
            "and (lower(sp.type) like lower(?1) " +
            "and (sp.capacity >= ?2)")
    Page<Space> findActiveSpacesByTypeAndCapacity(Pageable pageable, String searchType, Integer searchCapacity);
}
