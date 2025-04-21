package com.projinteg.GesEvents.controller;

import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestBody Company company) {
        try {
            Company savedCompany = companyService.register(company);
            return ResponseEntity.ok(savedCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id)
                .orElseThrow(() -> new RuntimeException("Non trouvé"));
        return ResponseEntity.ok(company);
    }

}
