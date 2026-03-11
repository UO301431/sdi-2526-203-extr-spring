package com.uniovi.sdi.sdi2526entrega121.services;

import org.springframework.stereotype.Service;

@Service
public class RolesService {
    String[] roles = {"ROLE_EMPLOYEE", "ROLE_ADMIN"};
    public String[] getRoles() {
        return roles;
    }
}
