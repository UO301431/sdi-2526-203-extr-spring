package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.Space;
import com.uniovi.sdi.sdi2526entrega121.repositories.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public Page<Space> getSpaces(Pageable pageable) {
        return spaceRepository.findAll(pageable);
    }
}
