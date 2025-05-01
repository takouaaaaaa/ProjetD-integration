package com.projinteg.GesEvents.service;

import com.projinteg.GesEvents.dao.CompanyRepository;
import com.projinteg.GesEvents.dao.EventRepository;
import com.projinteg.GesEvents.entities.Company;
import com.projinteg.GesEvents.entities.Etat;
import com.projinteg.GesEvents.entities.Event;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Event saveEventWithoutImage(Event event) {
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
    public Event saveEvent(Event event, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            event.setImageName(imageFile.getOriginalFilename());
            event.setImageType(imageFile.getContentType());
            event.setImageData(imageFile.getBytes());
        }

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

}



