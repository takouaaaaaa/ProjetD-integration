package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.dao.EventRepository;
import com.projinteg.GesEvents.entities.Event;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

   
    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

}



