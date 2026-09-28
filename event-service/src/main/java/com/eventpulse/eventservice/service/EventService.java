package com.eventpulse.eventservice.service;

import com.eventpulse.eventservice.dto.CreateEventRequest;
import com.eventpulse.eventservice.entity.Event;
import com.eventpulse.eventservice.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found: " + id));
    }

    public Event create(CreateEventRequest request) {
        Event event = new Event(
                request.title(),
                request.description(),
                request.location(),
                request.eventDate(),
                request.totalSeats()
        );
        return eventRepository.save(event);
    }
}