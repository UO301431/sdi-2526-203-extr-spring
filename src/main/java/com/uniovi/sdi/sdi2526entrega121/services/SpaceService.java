package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.repositories.SpaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SpaceService {
    private final SpaceRepository spaceRepository;

    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
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
}
