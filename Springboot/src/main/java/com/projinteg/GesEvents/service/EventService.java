package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.entities.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EventService {
    Event saveEventWithoutImage(Event event);

    Event saveEvent(Event event, MultipartFile imageFile) throws IOException;

    Optional<Event> getEventById(Long id);

    List<Event> getAllEvents();

}
