package com.uniovi.sdi.sdi2526entrega121.repositories;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface SpaceRepository extends CrudRepository<Space, Long> {

    Page<Space> findAll(Pageable pageable);

}
