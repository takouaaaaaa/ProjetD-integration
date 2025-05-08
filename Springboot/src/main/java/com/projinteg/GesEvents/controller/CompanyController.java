package com.projinteg.GesEvents.controller;

import ch.qos.logback.classic.Logger;
import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Event;
import com.projinteg.GesEvents.service.CompanyService;
import com.projinteg.GesEvents.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private EventService EventService;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
        return ResponseEntity.ok(company);
    }

    @GetMapping("/getUnconfirmedCompanies")
    public List<Company> getUnconfirmedCompanies() {
        return companyService.getUnconfirmedCompanies();
    }

    @GetMapping("/getConfirmedCompanies")
    public List<Company> getConfirmedCompanies() {
        return companyService.getConfirmedCompanies();
    }

    @PutMapping("/confirmCompany/{id}")

    public ResponseEntity<Company> confirmCompanyAccount(@PathVariable Long id) {
        try {
            Company confirmedCompany = companyService.confirmCompany(id);
            return ResponseEntity.ok(confirmedCompany);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error confirming company", e);
        }

    }

    @PutMapping("/unconfirmCompany/{id}")

    public ResponseEntity<Company> unconfirmCompanyAccount(@PathVariable Long id) {
        try {
            Company unconfirmedCompany = companyService.unconfirmCompany(id);
            return ResponseEntity.ok(unconfirmedCompany);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error unconfirming company", e);
        }

    }

    @DeleteMapping("deleteComapny/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting company", e);
        }
    }

    @GetMapping("/myEvents")
    public ResponseEntity<List<Event>> getMyEvents() {
        try {
            String currentCompanyEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Fetching events for company with email: " + currentCompanyEmail);

            Company currentCompany = companyService.getCompanyByEmail(currentCompanyEmail);
            Long companyId = currentCompany.getId();

            List<Event> events = EventService.getEventsByCompanyId(companyId);
            System.out.println("Found " + events.size() + " events for company ID " + companyId);

            return ResponseEntity.ok(events);

        } catch (UsernameNotFoundException e) {
            System.err.println("Attempt to access /myEvents for non-existent company email: " +
                    SecurityContextHolder.getContext().getAuthentication().getName());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.err.println("Unexpected error while fetching events for email: " +
                    SecurityContextHolder.getContext().getAuthentication().getName());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
