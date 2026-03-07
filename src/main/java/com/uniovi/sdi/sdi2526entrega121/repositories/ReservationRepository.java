package com.uniovi.sdi.sdi2526entrega121.repositories;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository  extends CrudRepository<Reservation, Long>  {

    Page<Reservation> findAll(Pageable pageable);

}
