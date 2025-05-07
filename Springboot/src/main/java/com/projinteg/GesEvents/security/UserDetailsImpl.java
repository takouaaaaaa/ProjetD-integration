package com.projinteg.GesEvents.security;

import com.projinteg.GesEvents.entities.Admin;
import com.projinteg.GesEvents.entities.Company;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor to initialize fields
    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // Static method to build UserDetailsImpl from Admin entity
    public static UserDetailsImpl build(Admin admin) {
        return new UserDetailsImpl(
                admin.getId(),
                admin.getUsername(),
                admin.getUsername(),  // Admin username is used here (can be email if required)
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(admin.getRole().name()))  // Role enum is converted to String
        );
    }

    // Static method to build UserDetailsImpl from Company entity
    public static UserDetailsImpl build(Company company) {
        return new UserDetailsImpl(
                company.getId(),
                company.getEmail(),  // Email used as username
                company.getEmail(),  // Email for internal use (this is the login email)
                company.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(company.getRole().name()))  // Role enum is converted to String
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Getter for Email field (not part of UserDetails interface, but used for your internal logic)
    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getter for id field (can be useful if you need user id access)
    public Long getId() {
        return id;
    }
}
