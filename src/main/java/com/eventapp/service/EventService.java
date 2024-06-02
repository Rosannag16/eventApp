package com.eventapp.service;

import com.eventapp.dto.EventDTO;
import com.eventapp.exception.CustomException;
import com.eventapp.exception.ResourceNotFoundException;
import com.eventapp.model.Event;
import com.eventapp.model.User;
import com.eventapp.repository.EventRepository;
import com.eventapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public void createEvent(EventDTO eventDTO, String organizerUsername) {
        User organizer = userRepository.findByUsername(organizerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found"));
        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setAvailableSeats(eventDTO.getAvailableSeats());
        eventRepository.save(event);
    }

    public void updateEvent(Long id, EventDTO eventDTO, String organizerUsername) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        if (!event.getOrganizer().equals(organizerUsername)) {
            throw new CustomException("Unauthorized");
        }
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setAvailableSeats(eventDTO.getAvailableSeats());
        eventRepository.save(event);
    }

    public void deleteEvent(Long id, String organizerUsername) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        if (!event.getOrganizer().equals(organizerUsername)) {
            throw new CustomException("Unauthorized");
        }
        eventRepository.delete(event);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> {
            EventDTO eventDTO = new EventDTO();
            eventDTO.setId(event.getId());
            eventDTO.setTitle(event.getTitle());
            eventDTO.setDescription(event.getDescription());
            eventDTO.setDate(event.getDate());
            eventDTO.setLocation(event.getLocation());
            eventDTO.setAvailableSeats(event.getAvailableSeats());
            return eventDTO;
        }).collect(Collectors.toList());
    }

    public void bookEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        if (event.getAvailableSeats() <= 0) {
            throw new CustomException("No available seats");
        }
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);
    }

    public void cancelBooking(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);
    }
}
