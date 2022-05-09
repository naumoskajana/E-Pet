package com.example.epet.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    OWNER,
    GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
