package com.projinteg.GesEvents.controller;

import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.entities.Event;
import com.projinteg.GesEvents.service.EventService;
import com.projinteg.GesEvents.service.CompanyService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/events")
public class EventController {


    @Autowired
    private EventService eventService;

    @Autowired
    private CompanyService companyService;

    @Value("${app.images.dir}")
    private String imgDir;

    @GetMapping("/companies/{companyId}/events")
    public ResponseEntity<List<Event>> getEventsByCompany(@PathVariable Long companyId) {
        try {
            List<Event> events = eventService.getEventsByCompanyId(companyId);


            return ResponseEntity.ok(events);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Event>> getAllEvents() {
        try {
            List<Event> events = eventService.getAllEvents();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);

        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/etat")
    public ResponseEntity<Etat> getEtat(@PathVariable Long id) {
        Optional<Event> optionalEvent = eventService.getEventById(id);
        return optionalEvent
                .map(event -> ResponseEntity.ok(event.getEtat()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/accepter")
    public ResponseEntity<Event> accepterEvent(@PathVariable Long id) {
        Optional<Event> optionalEvent = eventService.getEventById(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setEtat(Etat.ACCEPTE);
            return ResponseEntity.ok(eventService.saveEvent(event));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/rejeter")
    public ResponseEntity<Event> rejeterEvent(@PathVariable Long id) {
        Optional<Event> optionalEvent = eventService.getEventById(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setEtat(Etat.REJETE);
            return ResponseEntity.ok(eventService.saveEvent(event));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("updateEvent/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}