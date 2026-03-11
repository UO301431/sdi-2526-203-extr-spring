package com.uniovi.sdi.sdi2526entrega121.repositories;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository  extends CrudRepository<Reservation, Long>  {

    Page<Reservation> findAll(Pageable pageable);

    Page<Reservation> findByUserDni(String dni, Pageable pageable);

    Page<Reservation> findByUserDniAndStatus(String dni, ReservationStatus status, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE " +
            "(cast(:spaceId as long) IS NULL OR r.space.id = :spaceId) AND " +
            "(cast(:startDate as timestamp) IS NULL OR r.startDate >= :startDate) AND " +
            "(cast(:endDate as timestamp) IS NULL OR r.endDate <= :endDate)")
    Page<Reservation> findByFilters(@Param("spaceId") Long spaceId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.space.id = :spaceId  " +
            "AND r.status = 'ACTIVE'  " +
            "AND r.startDate < :endDate  " +
            "AND r.endDate > :startDate")
    List<Reservation> findActiveReservationsInRange(
            @Param("spaceId") Long spaceId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
