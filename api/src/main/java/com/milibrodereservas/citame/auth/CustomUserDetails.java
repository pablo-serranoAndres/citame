package com.milibrodereservas.citame.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final String username;
    private final String userphone;
    private final String name;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, String username, String userphone, String name, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.userphone = userphone;
        this.name = name;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserphone() {
        return userphone;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public String getPassword() { return null; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
