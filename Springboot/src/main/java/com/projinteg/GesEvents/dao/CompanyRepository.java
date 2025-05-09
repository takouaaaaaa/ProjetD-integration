package com.projinteg.GesEvents.dao;

import com.projinteg.GesEvents.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {


    Optional<Company> findByEmail(String email);

    List<Company> findByConfirmedFalse();


    List<Company> findByConfirmedTrue();

    Company findByEmailIgnoreCase(String identifier);
}