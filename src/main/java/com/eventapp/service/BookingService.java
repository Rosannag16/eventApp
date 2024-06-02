package com.eventapp.service;

import com.eventapp.exception.CustomException;
import com.eventapp.exception.ResourceNotFoundException;
import com.eventapp.model.Booking;
import com.eventapp.model.Event;
import com.eventapp.model.User;
import com.eventapp.repository.BookingRepository;
import com.eventapp.repository.EventRepository;
import com.eventapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public void bookEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (event.getAvailableSeats() <= 0) {
            throw new CustomException("No available seats");
        }
        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setUser(user);
        bookingRepository.save(booking);
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);
    }

    public void cancelBooking(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findByUserAndEvent(user, event)
                .orElseGet(() -> {
                    throw new ResourceNotFoundException("Booking not found");
                });

        bookingRepository.delete(booking);
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);
    }




}
