package com.eventpulse.notificationservice.service;

import com.eventpulse.notificationservice.dto.NotificationRequest;
import com.eventpulse.notificationservice.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);


    private final List<Notification> sentNotifications = new CopyOnWriteArrayList<>();

    public void send(NotificationRequest request) {
        Notification notification = new Notification(
                request.username(),
                request.eventId(),
                request.eventTitle(),
                request.message()
        );


        log.info("notification sent to [{}]: {}", notification.getUsername(), notification.getMessage());

        sentNotifications.add(notification);
    }

    public List<Notification> findByUsername(String username) {
        return sentNotifications.stream()
                .filter(n -> n.getUsername().equals(username))
                .toList();
    }

    public List<Notification> findAll() {
        return Collections.unmodifiableList(sentNotifications);
    }
}