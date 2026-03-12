package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.OccupiedSlot;
import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.repositories.BlockRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.ReservationRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.SpaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SpaceService {
    private final SpaceRepository spaceRepository;
    private final BlockRepository blockRepository;
    private final ReservationRepository reservationRepository;

    public SpaceService(SpaceRepository spaceRepository, BlockRepository blockRepository, ReservationRepository reservationRepository) {
        this.spaceRepository = spaceRepository;
        this.blockRepository = blockRepository;
        this.reservationRepository = reservationRepository;
    }


    public Page<Space> getSpaces(Pageable pageable) {
        return spaceRepository.findAll(pageable);
    }

    public Page<Space> getActiveSpaces(Pageable pageable){
        return spaceRepository.findActiveSpaces(pageable);
    }

    public Page<Space> getActiveSpacesByType(Pageable pageable, String searchType){
        searchType = "%" + searchType + "%";
        return spaceRepository.findActiveSpacesByType(pageable, searchType);
    }

    public Page<Space> getActiveSpacesByCapacity(Pageable pageable, Integer searchCapacity){
        return spaceRepository.findActiveSpacesByCapacity(pageable, searchCapacity);

    }

    public  Page<Space> getActivSpacesByTypeAndCapacity(Pageable pageable, String searchType, Integer searchCapacity){
        searchType = "%" + searchType + "%";
        return spaceRepository.findActiveSpacesByTypeAndCapacity(pageable, searchType, searchCapacity);
    }

    public Space getSpace(Long id) {
        return spaceRepository.findById(id).isPresent()
                ? spaceRepository.findById(id).get() : new Space();
    }

    public List<OccupiedSlot> getAvailabilityForSpace(Long spaceId, LocalDateTime startDate,
                                                      LocalDateTime endDate){
        List<OccupiedSlot> occupiedSlots = new ArrayList<>();
        //se añden los "choques" con reservas tras mapear las a OccupiedSlot
        reservationRepository.findActiveReservationsInRange(spaceId, startDate, endDate)
                .stream()
                .map(r -> new OccupiedSlot("RESERVA", r.getStartDate(), r.getEndDate(), r.getReason()))
                .forEach(occupiedSlots::add);

        //se añden los "choques" con bloqueos tras mapear las a OccupiedSlot
        blockRepository.findActiveBlocksInRange(spaceId, startDate, endDate)
                .stream()
                .map(b -> new OccupiedSlot("BLOQUEO", b.getStartDate(), b.getEndDate(), b.getReason()))
                .forEach(occupiedSlots::add);
        //se ordenan los "choques" cpor fecha de inicio
        occupiedSlots.sort(Comparator.comparing(OccupiedSlot::getStartDate));

        return occupiedSlots;
    }
}
