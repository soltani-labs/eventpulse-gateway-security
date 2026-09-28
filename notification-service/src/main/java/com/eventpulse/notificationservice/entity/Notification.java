package com.eventpulse.notificationservice.entity;

import java.time.LocalDateTime;

public class Notification {

    private final String username;
    private final Long eventId;
    private final String eventTitle;
    private final String message;
    private final LocalDateTime sentAt;

    public Notification(String username, Long eventId, String eventTitle, String message) {
        this.username = username;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }

    public String getUsername() { return username; }
    public Long getEventId() { return eventId; }
    public String getEventTitle() { return eventTitle; }
    public String getMessage() { return message; }
    public LocalDateTime getSentAt() { return sentAt; }
}