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

    @Query("SELECT r FROM Reservation r WHERE " +
            "(?1 IS NULL OR r.status = ?1) AND " +
            "(?2 IS NULL OR r.space.id = ?2) AND " +
            "(?3 IS NULL OR r.startDate >= ?3) AND " +
            "(?4 IS NULL OR r.endDate <= ?4)")
    Page<Reservation> findByFilters(@Param("status") ReservationStatus status,
                                    @Param("spaceId") Long spaceId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate,
                                    Pageable pageable);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
            "WHERE r.space.id = :spaceId " +
            "AND r.status = :status " +
            "AND r.startDate < :endDate AND r.endDate > :startDate")
    boolean existsActiveOverlap(@Param("spaceId") Long spaceId,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                @Param("status") ReservationStatus status);

    @Query("SELECT COUNT(b) > 0 FROM Block b " +
            "WHERE b.space.id = :spaceId " +
            "AND b.status = 'ACTIVE' " +
            "AND b.startDate < :endDate AND b.endDate > :startDate")
    boolean existsActiveBlockOverlap(@Param("spaceId") Long spaceId,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM Reservation r WHERE " +
            "r.user.id = ?1 AND " +
            "(?2 IS NULL OR r.status = ?2) AND " +
            "(?3 IS NULL OR r.space.id = ?3) AND " +
            "(?4 IS NULL OR r.startDate >= ?4) AND " +
            "(?5 IS NULL OR r.endDate <= ?5)")
    Page<Reservation> findByUserAndFilters(@Param("userId") Long userId,
                                           @Param("status") ReservationStatus status,
                                           @Param("spaceId") Long spaceId,
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

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.user.id = :userId " +
            "AND r.status = 'ACTIVE'")
    long countActiveByUser(
            @Param("userId") Long userId
    );
}