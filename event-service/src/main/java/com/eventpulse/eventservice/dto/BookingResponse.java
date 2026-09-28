package com.eventpulse.eventservice.dto;

import java.time.LocalDateTime;

public record BookingResponse(Long bookingId, Long eventId, String eventTitle,
                              String username, LocalDateTime bookedAt) {}