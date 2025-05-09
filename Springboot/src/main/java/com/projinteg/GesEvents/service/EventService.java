package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event saveEvent(Event event);

    Optional<Event> getEventById(Long id);

    List<Event> getAllEvents();

    List<Event> getEventsByCompanyId(Long companyId);

    Event updateEvent(Long id, Event eventDetails);

    void deleteEvent(Long id);

}