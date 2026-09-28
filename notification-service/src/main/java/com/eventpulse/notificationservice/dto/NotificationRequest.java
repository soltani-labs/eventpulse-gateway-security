package com.eventpulse.notificationservice.dto;

public record NotificationRequest(String username, Long eventId,
                                  String eventTitle, String message) {}