package com.eventpulse.authservice.dto;

public record AuthResponse(String token, String username, String role) {}