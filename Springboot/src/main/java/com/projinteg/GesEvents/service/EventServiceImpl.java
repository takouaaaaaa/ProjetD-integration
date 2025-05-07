package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.dao.CompanyRepository;
import com.projinteg.GesEvents.dao.EventRepository;
import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.entities.Event;

import java.util.List;
import java.util.Optional;

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
    // Read-only transaction is efficient here
    public List<Event> getEventsByCompanyId(Long companyId) {

        return eventRepository.findByCompanyId(companyId);
    }
    @Override
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    
        // Mise à jour des champs uniquement si les valeurs sont non nulles
        if (eventDetails.getDate() != null) {
            event.setDate(eventDetails.getDate());
        }
        if (eventDetails.getTime() != null) {
            event.setTime(eventDetails.getTime());
        }
        if (eventDetails.getLocalisation() != null) {
            event.setLocalisation(eventDetails.getLocalisation());
        }
    
        // Sauvegarde de l'événement mis à jour
        return eventRepository.save(event);
    }
}


