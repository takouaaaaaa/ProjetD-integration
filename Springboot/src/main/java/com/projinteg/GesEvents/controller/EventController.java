package com.projinteg.GesEvents.controller;

import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.entities.Event;
import com.projinteg.GesEvents.service.EventService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvent")
    public Event createEvent(@RequestBody Event event) {
        if (event.getEtat() == null) {
            event.setEtat(Etat.EN_ATTENTE);
        }
        return eventService.saveEvent(event);
    }

    @GetMapping("/companies/{companyId}/events")
    public ResponseEntity<List<Event>> getEventsByCompany(@PathVariable Long companyId) {
        try {
            List<Event> events = eventService.getEventsByCompanyId(companyId);
            // The check for isEmpty() is still useful if you want to return 404
            // when no events are found, but often returning 200 OK with an empty list is preferred.
            // if (events.isEmpty()) {
            //     // return ResponseEntity.notFound().build(); // Option 1: Return 404
            // }

            // Return 200 OK with the list (which might be empty)
            return ResponseEntity.ok(events);

        } catch (Exception e) {
            // It's still good practice to handle exceptions, even without logging here.
            // Consider logging in a centralized exception handler instead.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Return empty list on error
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


}