package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.entities.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    Company register(Company company);

    List<Company> getAllCompanies();

    Optional<Company> getCompanyById(Long id);

    List<Company> getUnconfirmedCompanies();

    List<Company> getConfirmedCompanies();

    Company confirmCompany(Long id);

    void deleteCompany(Long id);
}
