package com.eventpulse.eventservice.config;

import com.eventpulse.eventservice.entity.Event;
import com.eventpulse.eventservice.repository.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;

    public DataSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        if (eventRepository.count() > 0) return;

        eventRepository.save(new Event("Java Workshop",
                "Intro to Spring Boot", "Casa Tech Hub",
                LocalDateTime.now().plusDays(10), 30));

        eventRepository.save(new Event("Startup Meetup",
                "Networking Meetup", "Central Cafe",
                LocalDateTime.now().plusDays(15), 50));

        eventRepository.save(new Event("Photography Walk",
                "Sunset photo", "Grand Soc",
                LocalDateTime.now().plusDays(20), 2)); 
    }
}