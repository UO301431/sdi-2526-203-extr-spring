package com.uniovi.sdi.sdi2526extr203spring.services;

import com.uniovi.sdi.sdi2526extr203spring.dtos.ChangePasswordDto;
import com.uniovi.sdi.sdi2526extr203spring.entities.User;
import com.uniovi.sdi.sdi2526extr203spring.repositories.UsersRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService(UsersRepository usersRepository, BCryptPasswordEncoder
            bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init() {
    }

    public Page<User> getUsers(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    public User getUser(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    public User getUserByDni(String dni) {
        return usersRepository.findByDni(dni);
    }

    public void changeUserPassword(String dni, ChangePasswordDto dto){
        User user = usersRepository.findByDni(dni);

        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        usersRepository.save(user);
    }
}
