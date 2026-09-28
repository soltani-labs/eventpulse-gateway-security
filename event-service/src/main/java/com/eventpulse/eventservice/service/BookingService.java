package com.eventpulse.eventservice.service;

import com.eventpulse.eventservice.client.NotificationClient;
import com.eventpulse.eventservice.dto.BookingResponse;
import com.eventpulse.eventservice.dto.NotificationRequest;
import com.eventpulse.eventservice.entity.Booking;
import com.eventpulse.eventservice.entity.Event;
import com.eventpulse.eventservice.repository.BookingRepository;
import com.eventpulse.eventservice.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final NotificationClient notificationClient;
    private final TransactionTemplate transactionTemplate;

    public BookingService(EventRepository eventRepository,
                          BookingRepository bookingRepository,
                          NotificationClient notificationClient,
                          TransactionTemplate transactionTemplate) {
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
        this.notificationClient = notificationClient;
        this.transactionTemplate = transactionTemplate;
    }

    public BookingResponse book(Long eventId, String username) {

        // 1) save the booking with seat
        Booking booking = transactionTemplate.execute(status -> {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "event not found: " + eventId));

            if (bookingRepository.existsByEventIdAndUsername(eventId, username)) {
                throw new IllegalStateException("event already booked");
            }

            if (eventRepository.decrementAvailableSeats(eventId) == 0) {
                throw new IllegalStateException("seats not available");
            }

            return bookingRepository.save(new Booking(eventId, event.getTitle(), username));
        });

        // 2) notify .if a failure here must not undo the booking.
        try {
            notificationClient.send(new NotificationRequest(
                    username,
                    booking.getEventId(),
                    booking.getEventTitle(),
                    "seat for '" + booking.getEventTitle() + "' is confirmed!"
            ));
        } catch (Exception e) {
            log.warn("booking {} saved, but notification failed: {}", booking.getId(), e.getMessage());
        }

        return toResponse(booking);
    }

    public List<BookingResponse> findByUsername(String username) {
        return bookingRepository.findByUsernameOrderByBookedAtDesc(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking b) {
        return new BookingResponse(b.getId(), b.getEventId(), b.getEventTitle(),
                b.getUsername(), b.getBookedAt());
    }
}