package com.eventpulse.eventservice.dto;

public record NotificationRequest(String username, Long eventId,
                                  String eventTitle, String message) {}