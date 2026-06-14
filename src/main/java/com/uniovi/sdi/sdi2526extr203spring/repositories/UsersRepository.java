package com.uniovi.sdi.sdi2526extr203spring.repositories;

import com.uniovi.sdi.sdi2526extr203spring.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {

    User findByDni(String dni);
    Page<User> findAll(Pageable pageable);
}
