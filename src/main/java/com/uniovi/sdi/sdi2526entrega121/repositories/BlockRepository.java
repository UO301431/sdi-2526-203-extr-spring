package com.uniovi.sdi.sdi2526entrega121.repositories;

import com.uniovi.sdi.sdi2526entrega121.entities.Block;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BlockRepository extends CrudRepository<Block, Long> {

    // BlockRepository
    @Query("SELECT b FROM Block b " +
            "WHERE b.space.id = :spaceId " +
            "AND b.status = 'ACTIVE' " +
            "AND b.startDate < :endDate " +
            "AND b.endDate > :startDate")
    List<Block> findActiveBlocksInRange(
            @Param("spaceId") Long spaceId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
