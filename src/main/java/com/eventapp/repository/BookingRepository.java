package com.eventapp.repository;

import com.eventapp.model.Booking;
import com.eventapp.model.Event;
import com.eventapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    public Optional <Booking> findByUserAndEvent(User user, Event event);
}


