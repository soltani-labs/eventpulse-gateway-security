package com.eventpulse.eventservice.repository;

import com.eventpulse.eventservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByEventIdAndUsername(Long eventId, String username);

    List<Booking> findByUsernameOrderByBookedAtDesc(String username);
}