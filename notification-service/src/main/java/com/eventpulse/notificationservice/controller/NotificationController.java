package com.eventpulse.notificationservice.controller;

import com.eventpulse.notificationservice.dto.NotificationRequest;
import com.eventpulse.notificationservice.entity.Notification;
import com.eventpulse.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody NotificationRequest request) {
        notificationService.send(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<Notification> all() {
        return notificationService.findAll();
    }

    @GetMapping("/user/{username}")
    public List<Notification> forUser(@PathVariable String username) {
        return notificationService.findByUsername(username);
    }
}