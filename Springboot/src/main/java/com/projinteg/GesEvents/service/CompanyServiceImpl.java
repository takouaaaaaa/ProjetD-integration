package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.dao.CompanyRepository;
import com.projinteg.GesEvents.entities.Company;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(company -> {
                    company.setPassword(null);
                    return company;
                });
    }

    @Override

    public Company register(Company company) {
        if (companyRepository.findByEmail(company.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered: " + company.getEmail());
        }
        company.setPassword(passwordEncoder.encode(company.getPassword()));
        company.setConfirmed(false);
        return companyRepository.save(company);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll().stream()
                .peek(company -> company.setPassword(null))
                .collect(Collectors.toList());
    }

    @Override
    public List<Company> getUnconfirmedCompanies() {
        return companyRepository.findByConfirmedFalse().stream()
                .peek(company -> company.setPassword(null))
                .collect(Collectors.toList());
    }

    @Override
    public List<Company> getConfirmedCompanies() {
        return companyRepository.findByConfirmedTrue().stream()
                .peek(company -> company.setPassword(null))
                .collect(Collectors.toList());
    }

    @Override
    public Company confirmCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));

        company.setConfirmed(true);

        Company updatedCompany = companyRepository.save(company);

        return updatedCompany;
    }

    @Override
    public Company unconfirmCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));

        company.setConfirmed(false);

        Company updatedCompany = companyRepository.save(company);

        return updatedCompany;
    }

    @Override

    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }

    public Company getCompanyByEmail(String email) {
        return companyRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Company not found with email: " + email));
    }
}
