package com.projinteg.GesEvents.dao;

import com.projinteg.GesEvents.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}