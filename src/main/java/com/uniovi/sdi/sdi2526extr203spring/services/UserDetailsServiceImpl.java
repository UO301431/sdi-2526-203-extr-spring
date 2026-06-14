package com.uniovi.sdi.sdi2526extr203spring.services;

import com.uniovi.sdi.sdi2526extr203spring.entities.User;
import com.uniovi.sdi.sdi2526extr203spring.repositories.UsersRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@NullMarked
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        User user = usersRepository.findByDni(dni);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user == null) {
            throw new UsernameNotFoundException(dni);
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(
                user.getDni(), user.getPassword(), grantedAuthorities);
    }
}
