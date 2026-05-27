package org.example.hotelapiproject.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_TRAVELER, ROLE_HOTEL_OWNER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
