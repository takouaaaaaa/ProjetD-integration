package com.projinteg.GesEvents.dao;

import com.projinteg.GesEvents.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUsernameIgnoreCase(String username);
}
