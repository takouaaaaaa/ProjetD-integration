package com.projinteg.GesEvents.controller;

import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.entities.Event;
import com.projinteg.GesEvents.service.EventService;

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
    public ResponseEntity<Event> createEvent(
            @RequestPart Event event,
            @RequestPart(required = false) MultipartFile imageFile) {
        try {
            Event savedEvent = eventService.saveEvent(event, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/getAll")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
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
            return ResponseEntity.ok(eventService.saveEventWithoutImage(event));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/rejeter")
    public ResponseEntity<Event> rejeterEvent(@PathVariable Long id) {
        Optional<Event> optionalEvent = eventService.getEventById(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setEtat(Etat.REJETE);
            return ResponseEntity.ok(eventService.saveEventWithoutImage(event));
        }
        return ResponseEntity.notFound().build();
    }


}
