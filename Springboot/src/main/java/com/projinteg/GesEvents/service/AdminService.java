package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.dao.AdminRepository;
import com.projinteg.GesEvents.entities.Admin;
import com.projinteg.GesEvents.entities.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @PostConstruct
    public void initAdmin() {
        if (adminRepository.count() == 0) {
            logger.info("Initializing default admin user.");
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            adminRepository.save(admin);
            logger.info("Default admin user created.");
        } else {
            logger.debug("Admin user already exists. Skipping initialization.");
        }
    }
}