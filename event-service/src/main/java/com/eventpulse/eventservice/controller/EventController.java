package com.eventpulse.eventservice.controller;

import com.eventpulse.eventservice.dto.BookingResponse;
import com.eventpulse.eventservice.dto.CreateEventRequest;
import com.eventpulse.eventservice.entity.Event;
import com.eventpulse.eventservice.service.BookingService;
import com.eventpulse.eventservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final BookingService bookingService;

    public EventController(EventService eventService, BookingService bookingService) {
        this.eventService = eventService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Event> listEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestHeader("X-User-Role") String role,
                                             @Valid @RequestBody CreateEventRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN can create events");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request));
    }

    @PostMapping("/book/{eventId}")
    public ResponseEntity<BookingResponse> book(@PathVariable Long eventId,
                                                @RequestHeader("X-User-Name") String username) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(eventId, username));
    }

    @GetMapping("/my-bookings")
    public List<BookingResponse> myBookings(@RequestHeader("X-User-Name") String username) {
        return bookingService.findByUsername(username);
    }
}