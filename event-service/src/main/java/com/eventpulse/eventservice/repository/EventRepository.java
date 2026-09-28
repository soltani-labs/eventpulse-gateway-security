package com.eventpulse.eventservice.repository;

import com.eventpulse.eventservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Query("update Event e set e.availableSeats = e.availableSeats - 1 " +
            "where e.id = :id and e.availableSeats > 0")
    int decrementAvailableSeats(@Param("id") Long id);
}