package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.dao.AdminRepository;
import com.projinteg.GesEvents.dao.CompanyRepository;
import com.projinteg.GesEvents.entities.Admin;
import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        if (usernameOrEmail.contains("@")) {

            Company company = companyRepository.findByEmailIgnoreCase(usernameOrEmail);
            if (company != null) {
                return UserDetailsImpl.build(company);
            }
        } else {

            Admin admin = adminRepository.findByUsernameIgnoreCase(usernameOrEmail);
            if (admin != null) {
                return UserDetailsImpl.build(admin);
            }
        }

        throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
    }


}