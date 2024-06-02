package com.eventapp.repository;

import com.eventapp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizer(String organizer);
}
