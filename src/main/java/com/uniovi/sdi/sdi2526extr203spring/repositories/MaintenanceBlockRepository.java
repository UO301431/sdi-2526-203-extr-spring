package com.uniovi.sdi.sdi2526extr203spring.repositories;

import com.uniovi.sdi.sdi2526extr203spring.entities.BlockStatus;
import com.uniovi.sdi.sdi2526extr203spring.entities.MaintenanceBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceBlockRepository extends JpaRepository<MaintenanceBlock, Long> {

    Page<MaintenanceBlock> findBySpaceId(Long spaceId, Pageable pageable);

    List<MaintenanceBlock> findBySpaceId(Long spaceId);

    List<MaintenanceBlock> findBySpaceIdAndStatus(Long spaceId, BlockStatus status);

    @Query("SELECT mb FROM MaintenanceBlock mb " +
            "WHERE mb.space.id = :spaceId " +
            "AND mb.status = 'ACTIVE' " +
            "AND (:excludeId IS NULL OR mb.id <> :excludeId) " +
            "AND mb.startDate < :endDate AND mb.endDate > :startDate")
    List<MaintenanceBlock> findOverlappingActiveBlocks(@Param("spaceId") Long spaceId,
                                                       @Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate,
                                                       @Param("excludeId") Long excludeId);
}