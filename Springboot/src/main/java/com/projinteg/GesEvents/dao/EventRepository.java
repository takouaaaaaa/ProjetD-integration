package com.projinteg.GesEvents.dao;


import com.projinteg.GesEvents.entities.Event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long id);

    List<Event> findByCompanyId(Long companyId);
}


