package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.dao.CompanyRepository;
import com.projinteg.GesEvents.dao.EventRepository;
import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.entities.Event;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CompanyRepository companyRepository;


    @Override
    public Event saveEvent(Event event) {


        if (event.getEtat() == null) {
            event.setEtat(Etat.EN_ATTENTE);
        }

        if (event.getCompany() != null && event.getCompany().getId() != null) {
            Company company = companyRepository.findById(event.getCompany().getId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            event.setCompany(company);
        }

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

    @Override
    public List<Event> getEventsByCompanyId(Long companyId) {

        return eventRepository.findByCompanyId(companyId);
    }

    @Override
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (eventDetails.getDate() != null) {
            event.setDate(eventDetails.getDate());
        }
        if (eventDetails.getTime() != null) {
            event.setTime(eventDetails.getTime());
        }
        if (eventDetails.getLocalisation() != null) {
            event.setLocalisation(eventDetails.getLocalisation());
        }

        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}


