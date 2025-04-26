package com.projinteg.GesEvents.controller;

import com.projinteg.GesEvents.entities.Event;
import com.projinteg.GesEvents.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvent")
    public Event createEvent(@RequestBody Event event) {
        return eventService.saveEvent(event);
    }


}
