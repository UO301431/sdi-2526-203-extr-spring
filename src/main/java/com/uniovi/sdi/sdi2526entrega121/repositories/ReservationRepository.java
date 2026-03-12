package com.uniovi.sdi.sdi2526entrega121.repositories;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationRepository  extends CrudRepository<Reservation, Long>  {

    Page<Reservation> findAll(Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(cast(:spaceId as long) IS NULL OR r.space.id = :spaceId) AND " +
            "(cast(:startDate as timestamp) IS NULL OR r.startDate >= :startDate) AND " +
            "(cast(:endDate as timestamp) IS NULL OR r.endDate <= :endDate)")
    Page<Reservation> findByFilters(@Param("status") ReservationStatus status,
                                    @Param("spaceId") Long spaceId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE " +
            "(r.user.id = :userId) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(cast(:spaceId as long) IS NULL OR r.space.id = :spaceId) AND " +
            "(cast(:startDate as timestamp) IS NULL OR r.startDate >= :startDate) AND " +
            "(cast(:endDate as timestamp) IS NULL OR r.endDate <= :endDate)")
    Page<Reservation> findByUserAndFilters(@Param("userId") Long userId,
                                           @Param("status") ReservationStatus status,
                                           @Param("spaceId") Long spaceId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);
}
