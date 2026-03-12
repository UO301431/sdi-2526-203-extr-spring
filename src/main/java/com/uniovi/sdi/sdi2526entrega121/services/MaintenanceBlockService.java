package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.BlockStatus;
import com.uniovi.sdi.sdi2526entrega121.entities.MaintenanceBlock;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.repositories.MaintenanceBlockRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.ReservationRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.SpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceBlockService {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceBlockService.class);

    @Autowired
    private MaintenanceBlockRepository blockRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // ── Queries ───────────────────────────────────────────────────────────────

    public Page<MaintenanceBlock> getBlocksBySpace(Long spaceId, Pageable pageable) {
        return blockRepository.findBySpaceId(spaceId, pageable);
    }

    public List<MaintenanceBlock> getActiveBlocksBySpace(Long spaceId) {
        return blockRepository.findBySpaceIdAndStatus(spaceId, BlockStatus.ACTIVE);
    }

    public Optional<MaintenanceBlock> findById(Long id) {
        return blockRepository.findById(id);
    }

    // ── Create ────────────────────────────────────────────────────────────────

    /**
     * Creates a maintenance block.
     * Returns null on success, or an i18n error key on failure.
     */
    public String createBlock(Long spaceId, LocalDateTime start, LocalDateTime end, String reason) {
        if (start == null || end == null) {
            return "block.error.dates.required";
        }
        if (!start.isBefore(end)) {
            return "block.error.dates.order";
        }
        if (reason == null || reason.trim().isEmpty()) {
            return "block.error.reason.empty";
        }

        Optional<Space> spaceOpt = spaceRepository.findById(spaceId);
        if (spaceOpt.isEmpty()) {
            return "block.error.space.notfound";
        }

        // Check overlap with other active blocks
        List<MaintenanceBlock> blockOverlaps =
                blockRepository.findOverlappingActiveBlocks(spaceId, start, end, null);
        if (!blockOverlaps.isEmpty()) {
            return "block.error.overlap.block";
        }

        // Check overlap with active reservations
        boolean reservationOverlap = reservationRepository
                .existsActiveOverlap(spaceId, start, end, ReservationStatus.ACTIVE);
        if (reservationOverlap) {
            return "block.error.overlap.reservation";
        }

        MaintenanceBlock block = new MaintenanceBlock(spaceOpt.get(), start, end, reason.trim());
        blockRepository.save(block);
        log.info("Maintenance block created for space id={} from {} to {}", spaceId, start, end);
        return null;
    }

    // ── Cancel ────────────────────────────────────────────────────────────────

    /**
     * Cancels a maintenance block (logical delete — sets status to CANCELLED).
     * Returns null on success, or an i18n error key on failure.
     */
    public String cancelBlock(Long blockId) {
        Optional<MaintenanceBlock> opt = blockRepository.findById(blockId);
        if (opt.isEmpty()) {
            return "block.error.notfound";
        }
        MaintenanceBlock block = opt.get();
        if (block.getStatus() == BlockStatus.CANCELLED) {
            return "block.error.already.cancelled";
        }
        block.setStatus(BlockStatus.CANCELLED);
        blockRepository.save(block);
        log.info("Maintenance block cancelled: id={}", blockId);
        return null;
    }
}