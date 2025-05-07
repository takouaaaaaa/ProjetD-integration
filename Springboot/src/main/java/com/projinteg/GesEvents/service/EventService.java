package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EventService {
    Event saveEvent(Event event);

    Optional<Event> getEventById(Long id);

    List<Event> getAllEvents();

    List<Event> getEventsByCompanyId(Long companyId);

}