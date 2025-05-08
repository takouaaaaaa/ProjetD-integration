package com.projinteg.GesEvents.controller;

import ch.qos.logback.classic.Logger;
import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Event;
import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.service.CompanyService;
import com.projinteg.GesEvents.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private EventService EventService;
    @Value("${app.images.dir}")
    private String imgDir;

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

    @PostMapping("/addEvent")
    public ResponseEntity<Event> createEvent(@RequestParam("image") MultipartFile file,
                                             @RequestParam String nom,
                                             @RequestParam String description,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                             @RequestParam String lieu,
                                             @RequestParam String localisation,
                                             @RequestParam(required = false) String animateur) {
        try {
            if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Get current company based on authenticated user
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Company currentCompany = companyService.getCompanyByEmail(email);

            // Save image
            String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path target = Paths.get(imgDir).resolve(filename);
            Files.createDirectories(target.getParent());
            file.transferTo(target);

            // Create and populate event
            Event event = new Event();
            event.setNom(nom);
            event.setDescription(description);
            event.setDate(date);
            event.setTime(time);
            event.setLocalisation(localisation);
            event.setLieu(lieu);
            event.setAnimateur(animateur);
            event.setEtat(Etat.EN_ATTENTE);
            event.setImage(filename);
            event.setCompany(currentCompany);

            Event saved = EventService.saveEvent(event);
            return ResponseEntity.ok(saved);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
