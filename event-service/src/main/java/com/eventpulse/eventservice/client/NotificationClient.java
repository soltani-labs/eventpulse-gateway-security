package com.eventpulse.eventservice.client;

import com.eventpulse.eventservice.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/notifications")
    void send(@RequestBody NotificationRequest request);
}