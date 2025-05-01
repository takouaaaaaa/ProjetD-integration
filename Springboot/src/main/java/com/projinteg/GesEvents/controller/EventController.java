package com.projinteg.GesEvents.controller;

import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.entities.Event;
import com.projinteg.GesEvents.service.EventService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvent")
    public Event createEvent(@RequestBody Event event) {
        if (event.getEtat() == null){
            event.setEtat(Etat.EN_ATTENTE);
        }
        return eventService.saveEvent(event);
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
